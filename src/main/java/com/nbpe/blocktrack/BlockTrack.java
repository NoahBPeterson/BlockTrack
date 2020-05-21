package com.nbpe.blocktrack;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import com.nbpe.db.BlockHistory;
import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;
import com.nbpe.listeners.BreakListener;
import com.nbpe.listeners.BuildListener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class BlockTrack extends PluginBase
{
	
	public static BreakListener breakListener;
    public static BuildListener buildListener;
    public static BlockTrack plugin;
    @SuppressWarnings("unused")
	private static DBAccess a; //Initialized here but not used in this class.
    public static ArrayList<UUID> playersHistoryCheck;
    
    public static BlockTrackCommand btc;
    public static BlockHistoryCommand bhc;


    @Override
    public void onLoad()
    {
        this.getLogger().info(TextFormat.WHITE + "BlockTrack has been loaded!");
    }

    @Override
    public void onEnable()
    {
    	playersHistoryCheck = new ArrayList<UUID>();
    	plugin=this;
        breakListener = new BreakListener();
        buildListener = new BuildListener();
        a = new DBAccess();
        btc = new BlockTrackCommand("blockTrack", this);
        bhc = new BlockHistoryCommand("blockHistory", this);
        this.getServer().getPluginManager().registerEvents(breakListener, plugin);
        this.getServer().getPluginManager().registerEvents(buildListener, plugin);
        this.getServer().getCommandMap().register("BlockTrack", (Command) btc);
        this.getServer().getCommandMap().register("BlockHistory", (Command) bhc);
        this.getLogger().info(TextFormat.DARK_GREEN + "BlockTrack has been enabled!");
    }

    @Override
    public void onDisable()
    {
    	playersHistoryCheck = null;
        breakListener = null;
        buildListener = null;
        plugin = null;
        this.getServer().getCommandMap().getCommands().remove("BlockTrack");
        this.getServer().getCommandMap().getCommands().remove("BlockHistory");
        this.getLogger().info(TextFormat.DARK_RED + "BlockTrack has been disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {
        if (!sender.isOp() && !(sender instanceof ConsoleCommandSender))
        {
        	sender.sendMessage("You do not have permission to use this command.");
            return false;
        }
        
        boolean blockTrack = cmd.getName().equalsIgnoreCase("blocktrack");
        Player p = args.length != 0 ? Server.getInstance().getPlayer(args[0]) : null;
        if(p == null && blockTrack)
        {
        	sender.sendMessage("Cannot find player "+p+".");
        	return false;
        }
        int pageNumber = 0;
        
        try
        {
        	pageNumber = Integer.parseInt(args[args.length-1]);
        }catch (Exception e)
        {
        	pageNumber = 0;
        }

        if(blockTrack)
        {

	        switch(args.length)
	        {
		        case 1: // /blocktrack [playerName]
					return btc.playerOverviewExecute(sender, p, pageNumber);
		        case 2: // /blocktrack [playerName] [blockType]
		        	Item blockType = Item.fromString(args[1]);
		        	if(blockType == null)
		        		return btc.playerOverviewExecute(sender, p, pageNumber);		        	
					return btc.playerBlockType(sender, p, pageNumber, blockType);
		        case 3: // /blockTrack [playerName] [total] [broken | placed]
					return btc.playerBlockSum(sender, p, pageNumber, args);
		        default:
		        	sender.sendMessage(getUsage());
		        	return false;
	        }
        }else if(cmd.getName().equalsIgnoreCase("blockhistory"))
        {
        	Player bHPlayer;
        	if(sender instanceof Player) {
        		bHPlayer = (Player) sender;
            	bhc.blockHistory(bHPlayer);
	        	sender.sendMessage(TextFormat.AQUA+"Place or destroy a block to get the history at that location.");
        	}
        }


        return true;
    }
    
    public static void sendStringArray(CommandSender sender, String[] array)
    {
    	for(int i = 0; i < array.length; i++)
    	{
    		sender.sendMessage(array[i]);
    	}
    }
    
    private String getUsage()
    {
    	return ("/blocktrack <player> [blockName]/[broken/placed]");
    }
    
	//LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(getTime()),TimeZone.getDefault().toZoneId());
	//2017-07-03T10:25
    public static String[] blockHistoryFormat(List<BlockHistory> tableToFormat, int page)
    {
    	int max = (10 < tableToFormat.size()) ? 10 : tableToFormat.size()+1;
    	String[] formatted = new String[max];
    	String formatOfText = TextFormat.GRAY+"BlockHistory:";
    	formatted[0] = formatOfText;
        int i = 1;
        
        //Removing pagination for the moment.
    	/*int totalPages = (int) Math.ceil(((double) tableToFormat.size()/8.0));
    	Server.getInstance().getLogger().alert("Max: "+max+" Size: "+tableToFormat.size()); //******** 10, 34

        if(totalPages > 1 && tableToFormat.size() > 9) //Pages are of size 8 if there are multiple, 9 if there is only one.
        {
        	max--; //Decrease number of entries displayed to 8 so that we the page number takes the place
            formatted[max]=TextFormat.GREEN+"Page "+ page+" of "+totalPages;
            totalPages--;
            i+=(totalPages*8); //If you modify this, you MUST also modify the referenced index in the for-loop below. 
            max+=(totalPages*8); 
        }
    	Server.getInstance().getLogger().alert("Max: "+max+" Size: "+tableToFormat.size()); //******** 41, 34*/
    	for(; i < max; i++)
    	{
        	LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(tableToFormat.get(i-1).getTime()),TimeZone.getDefault().toZoneId());
        	UUID playerUUID = UUID.fromString(tableToFormat.get(i-1).getPlayer().getuuid());
        	String playerName = Server.getInstance().getPlayer(playerUUID).get().getDisplayName();
        	formatted[i]=TextFormat.GOLD+triggerTime.toString().replace("T", " ")+"--"+TextFormat.BLUE+playerName;
        	if(tableToFormat.get(i-1).placed())
        	{
        		formatted[i]+=TextFormat.GREEN+" placed ";
        	}else
        	{
        		formatted[i]+=TextFormat.RED+" broke ";
        	}
        	formatted[i]+=TextFormat.GRAY+Block.get(tableToFormat.get(i-1).getBlockType()).getName();
    	}

    	
    	return formatted;
    }
    
    public static String[] dbList(List<BlockTable> tableToFormat, int page)
    {
    	int max = (10 < tableToFormat.size()) ? 10 : tableToFormat.size()+1;
    	String[] formatted = new String[max];
    	String formatOfText = TextFormat.GRAY+"Destroyed"+TextFormat.WHITE+"--"+TextFormat.GREEN+"Placed"+TextFormat.WHITE+"--"+TextFormat.GOLD+"Block";
    	formatted[0] = formatOfText;
        int i = 1;
        
    	int totalPages = (int) Math.ceil(((double) tableToFormat.size()/8.0));
    	
        if(totalPages > 1 && tableToFormat.size() > 9) //Pages are of size 8 if there are multiple, 9 if there is only one.
        {
        	max--;
            formatted[max]=TextFormat.GREEN+"Page "+ page+" of "+totalPages;
            formatted[1] = formatOfText;
            totalPages--;
            i+=(totalPages*8);
            max+=(totalPages*8);
        }
        
    	for(; i < max; i++)
    	{
    		BlockTable entry = tableToFormat.get((i-1));
    		String type = Block.get(entry.getBlockType()).getName();
    		int destroyedLength = String.valueOf(entry.getDestroyed()).length();
    		int placedLength = String.valueOf(entry.getPlaced()).length();
    		
    		formatted[i]=TextFormat.GRAY+""+entry.getDestroyed();
    		formatted[i]+="               ".substring(0,15-destroyedLength);
    		formatted[i]+=TextFormat.GREEN+""+entry.getPlaced();
    		formatted[i]+="            ".substring(0, (12-placedLength)-destroyedLength);
    		formatted[i]+=TextFormat.GOLD+""+type;

    	}
    	return formatted;
    } 
}