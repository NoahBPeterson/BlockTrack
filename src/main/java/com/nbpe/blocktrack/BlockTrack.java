package com.nbpe.blocktrack;

import java.util.ArrayList;
import java.util.List;

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
	
    private static BreakListener breakListener;
    private static BuildListener buildListener;
    public static BlockTrack plugin;
    private static DBAccess a;


    @Override
    public void onLoad()
    {
        this.getLogger().info(TextFormat.WHITE + "BlockTrack has been loaded!");
    }

    @Override
    public void onEnable()
    {
    	plugin=this;
        breakListener = new BreakListener();
        buildListener = new BuildListener();
        a = new DBAccess();
        this.getServer().getPluginManager().registerEvents(breakListener, plugin);
        this.getServer().getPluginManager().registerEvents(buildListener, plugin);
        this.getServer().getCommandMap().register("BlockTrack", (Command)new BlockTrackCommand("blockTrack", this));
        this.getLogger().info(TextFormat.DARK_GREEN + "BlockTrack has been enabled!");
    }

    @Override
    public void onDisable()
    {
        breakListener = null;
        buildListener = null;
        plugin = null;
        this.getServer().getCommandMap().getCommands().remove("BlockTrack");
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
        Player p = args.length != 0 ? Server.getInstance().getPlayer(args[0]) : null;
        if(p == null)
        {
        	sender.sendMessage("Cannot find player "+p+".");
        	return false;
        }
        switch(args.length)
        {
	        case 0:
	        	sender.sendMessage(getUsage());
	        	break;
	        case 1:
				List<BlockTable> values = DBAccess.getByUUID(p.getUniqueId());
				if(values == null || values.size() == 0)
				{
					sender.sendMessage("No data for player "+p.getDisplayName());
					return false;
				}
				sendStringArray(sender, dbList(values));
	        	return true;
	        case 2:
	        	Item blockType = Item.fromString(args[1]);
	        	if(blockType == null)
	        	{
	        		sender.sendMessage("Cannot find block "+args[1]+".");
	        		return false;
	        	}
				BlockTable value = DBAccess.getByUUIDandBlockType(p.getUniqueId(), blockType.getId());
				if(value == null)
				{
					sender.sendMessage("No "+blockType.toString()+" broken by player.");
					return false;
				}
				List<BlockTable> format = new ArrayList<BlockTable>();
				format.add(value);
				sendStringArray(sender, dbList(format));
				return true;
	        case 3:
				String total = p.getDisplayName()+" has ";
				if(args[2].toLowerCase().contains("broken"))
				{
					total+="broken "+DBAccess.totalBlocksBroken(p.getUniqueId());
				}else if(args[2].toLowerCase().contains("placed"))
				{
					total+="placed "+DBAccess.totalBlocksPlaced(p.getUniqueId());
				}else
				{
					sender.sendMessage(getUsage());
					return false;
				}
				total+=" blocks.";
				sender.sendMessage(total);
				return true;
	        default:
	        	sender.sendMessage(getUsage());
	        	return false;
        }


        return true;
    }
    
    void sendStringArray(CommandSender sender, String[] array)
    {
    	for(int i = 0; i < array.length; i++)
    	{
    		sender.sendMessage(array[i]);
    	}
    }
    
    String getUsage()
    {
    	return ("/blocktrack <player> [total broken/placed]/[blockName]");
    }
    
    static String[] dbList(List<BlockTable> tableToFormat)
    {
    	int max = (10 < tableToFormat.size()) ? 10 : tableToFormat.size()+1;
    	String[] formatted = new String[max];
        formatted[0]=TextFormat.GRAY+"Destroyed"+TextFormat.WHITE+"--"+TextFormat.GREEN+"Placed"+TextFormat.WHITE+"--"+TextFormat.GOLD+"Block";
    	for(int i = 1; i < max; i++)
    	{
    		BlockTable entry = tableToFormat.get((i-1));
    		String type = Block.get(entry.getBlockType()).getName();
    		int destroyedLength = String.valueOf(entry.getDestroyed()).length();
    		int placedLength = String.valueOf(entry.getPlaced()).length();
    		
    		formatted[i]=TextFormat.GRAY+""+entry.getDestroyed();
    		formatted[i]+="               ".substring(0,15-destroyedLength);
    		formatted[i]+=TextFormat.GREEN+""+entry.getPlaced();
    		formatted[i]+="            ".substring(0, (12-placedLength)-destroyedLength);;
    		formatted[i]+=TextFormat.GOLD+""+type;

    	}
    	return formatted;
    } 
}