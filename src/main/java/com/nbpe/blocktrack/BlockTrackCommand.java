package com.nbpe.blocktrack;

import java.util.ArrayList;
import java.util.List;

import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;

public class BlockTrackCommand extends PluginCommand<BlockTrack>
{

	public BlockTrackCommand(String name, Plugin owner) {
		super("BlockTrack", BlockTrack.plugin);
        this.setPermission(Permission.DEFAULT_OP);
        this.commandParameters.clear();
        this.commandParameters.put("blockTrack", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false)

        });
        this.commandParameters.put("specificBlock", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("blockName", false, CommandParameter.ENUM_TYPE_BLOCK_LIST)

        });
        this.commandParameters.put("total", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("total", CommandParamType.STRING, false),
                new CommandParameter("broken/placed", CommandParamType.STRING, false)
        });
        this.setUsage("/blocktrack <player> [total/blockName] [broken/placed]");
	}
	
	public boolean playerOverviewExecute(CommandSender sender, Player p, int pageNumber)
	{
		List<BlockTable> values = DBAccess.getByUUID(p.getUniqueId());
		if(values == null || values.size() == 0)
		{
			sender.sendMessage("No data for player "+p.getDisplayName());
			return false;
		}
		BlockTrack.sendStringArray(sender, BlockTrack.dbList(values, pageNumber));
		return true;
	}
	
	public boolean playerBlockType(CommandSender sender, Player p, int pageNumber, Item blockType)
	{
		BlockTable value = DBAccess.getByUUIDandBlockType(p.getUniqueId(), blockType.getId());
		if(value == null)
		{
			sender.sendMessage("No "+blockType.toString()+" broken by player.");
			return false;
		}
		List<BlockTable> format = new ArrayList<BlockTable>();
		format.add(value);
		BlockTrack.sendStringArray(sender, BlockTrack.dbList(format, pageNumber));
		return true;
	}
	
	public boolean playerBlockSum(CommandSender sender, Player p, int pageNumber, String args[])
	{
		String total = p.getDisplayName()+" has ";
		if(args[1].toLowerCase().contains("total"))
		{
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
		}
		total+=" blocks.";
		sender.sendMessage(total);
		return true;
	}

	
}