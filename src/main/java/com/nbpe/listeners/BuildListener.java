package com.nbpe.listeners;

import java.util.UUID;

import com.nbpe.blocktrack.BlockTrack;
import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {
	
	private DBAccess dbAccess;

    @EventHandler(priority = EventPriority.HIGH)
	public void buildHandler(BlockPlaceEvent e)
	{
		UUID player = e.getPlayer().getUniqueId();
		dbAccess = DBAccess.getDB();
		if(BlockTrack.playersHistoryCheck.contains(player))
		{			
			BlockTrack.bhc.blockHistorySend(e.getPlayer(), e.getBlock());
			e.setCancelled();
			return;
		} else {
			DBAccess.blockHistoryAddEntry(player, e.getBlock(), true); //BlockHistory Tracker
		}

		int blockType = e.getBlock().getId();
		if(blockType == 0) return;

				
		BlockTable entry = DBAccess.getByUUIDandBlockType(player, blockType);	//Get DAO from database with the above UUID and blocktype
		int placed = 1;
		if(entry != null)
		{
			placed += entry.getPlaced(); //Get number of {blockType} placed by player, increment
		}else
		{
			DBAccess.blockTrackAddEntry(player, blockType);
			entry = DBAccess.getByUUIDandBlockType(player, blockType);
			entry.setPlaced(placed);
			dbAccess.blockTrackUpdateEntry(entry);
			return;
		}
		entry.setPlaced(placed); //Set number of {blockType} placed by player
		dbAccess.blockTrackUpdateEntry(entry);
	}
}