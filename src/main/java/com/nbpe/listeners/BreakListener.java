package com.nbpe.listeners;

import java.util.UUID;

import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {
	
	DBAccess dbAccess;
	
	@EventHandler
	public void BreakHandler(BlockBreakEvent e)
	{
		UUID player = e.getPlayer().getUniqueId();
		int blockType = e.getBlock().getId();
		if(blockType == 0) return;
		dbAccess = DBAccess.getDB();
				
		BlockTable entry = DBAccess.getByUUIDandBlockType(player, blockType);	//Get DAO from database with the above UUID and blocktype
		int broken = 1;
		if(entry != null)
		{
			broken += entry.getDestroyed(); //Get number of {blockType} broken by player, increment
		}else
		{
			entry = new BlockTable(player.toString(), blockType);
			entry.setDestroyed(broken);
			DBAccess.addEntry(player, blockType);
			return;
		}
		entry.setDestroyed(broken); //Set number of {blockType} broken by player
		dbAccess.updateEntry(entry);
	}

}