package com.nbpe.blocktrack;

import java.util.List;

import com.nbpe.db.BlockHistory;
import com.nbpe.db.DBAccess;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;

public class BlockHistoryCommand extends PluginCommand<BlockTrack>
{

	public BlockHistoryCommand(String name, Plugin owner) {
		super(name, BlockTrack.plugin);
        this.setPermission(Permission.DEFAULT_OP);
        this.commandParameters.clear();
        this.setUsage("/blockHistory");
        this.setDescription("After using the command, break or place a block to get a history of block change at that position.");
	}
	
	public void blockHistory(Player p)
	{

    	if(!BlockTrack.playersHistoryCheck.contains(p.getUniqueId()))
    	{
    		BlockTrack.playersHistoryCheck.add(p.getUniqueId());
    	}
	}
	
	public boolean blockHistorySend(Player p, Block block)
	{
		//Boolean blockPos = BlockTrack.playersHistoryCheck.get(p.getUniqueId());
		List<BlockHistory> blockHistory = DBAccess.getListByBlock(block);
		if(blockHistory != null && blockHistory.size() > 0)
		{
			BlockTrack.sendStringArray(p, BlockTrack.blockHistoryFormat(blockHistory, 0));
		}
		
		BlockTrack.playersHistoryCheck.remove(p.getUniqueId());
		return true;
	}
	
	
}