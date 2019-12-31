package com.nbpe.listeners;

import java.util.UUID;

import com.nbpe.db.BlockPosition;
import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {
	
	DBAccess dbAccess;

	@EventHandler
	public void BuildHandler(BlockPlaceEvent e)
	{
		UUID player = e.getPlayer().getUniqueId();
		int blockType = e.getBlock().getId();
		if(blockType == 0) return;
		dbAccess = DBAccess.getDB();
		
		Block a = e.getBlock();
		String world = e.getBlock().level.getName();
		
		BlockPosition BPentry;
				
		BlockTable entry = DBAccess.getByUUIDandBlockType(player, blockType);	//Get DAO from database with the above UUID and blocktype
		int placed = 1;
		if(entry != null)
		{
			placed += entry.getPlaced(); //Get number of {blockType} placed by player, increment
		}else
		{
			entry = new BlockTable(player.toString(), blockType);
			entry.setPlaced(placed);
			DBAccess.addEntry(player, blockType);
			return;
		}
		entry.setPlaced(placed); //Set number of {blockType} placed by player
		dbAccess.updateEntry(entry);
	}
}