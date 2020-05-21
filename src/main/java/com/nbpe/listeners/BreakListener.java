package com.nbpe.listeners;

import java.util.UUID;

import com.nbpe.blocktrack.BlockTrack;
import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;

public class BreakListener implements Listener {
	
	DBAccess dbAccess;
	
    @EventHandler(priority = EventPriority.HIGH)
	public void BreakHandler(BlockBreakEvent e)
	{
		UUID player = e.getPlayer().getUniqueId();
		dbAccess = DBAccess.getDB();
		if(BlockTrack.playersHistoryCheck.contains(player))
		{
			BlockTrack.bhc.blockHistorySend(e.getPlayer(), e.getBlock());
			e.setCancelled();
			return;
		} else {
			DBAccess.BHaddEntry(player, e.getBlock(), false); //BlockHistory Tracker
		}
		
		int blockType = e.getBlock().getId();
		if(blockType == 0) return;
		
				
		BlockTable entry = DBAccess.getByUUIDandBlockType(player, blockType);	//Get DAO from database with the above UUID and blocktype
		int broken = 1;
		if(entry != null)
		{
			broken += entry.getDestroyed(); //Get number of {blockType} broken by player, increment
		}else
		{
			DBAccess.BTaddEntry(player, blockType);
			entry = DBAccess.getByUUIDandBlockType(player, blockType);
			entry.setDestroyed(broken);
			dbAccess.BTupdateEntry(entry);
			return;
		}
		entry.setDestroyed(broken); //Set number of {blockType} broken by player
		dbAccess.BTupdateEntry(entry);
	}

}