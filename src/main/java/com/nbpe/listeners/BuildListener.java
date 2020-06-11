package com.nbpe.listeners;

import com.nbpe.blocktrack.BlockTrack;
import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockWater;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;

public class BuildListener implements Listener {
	
	private DBAccess dbAccess;

    @EventHandler(priority = EventPriority.HIGH)
	public void buildHandler(BlockPlaceEvent e)
	{
    	buildHandler(e, e.getPlayer(), e.getBlock());
	}
    
    @EventHandler(priority = EventPriority.HIGH)
    public void bucketEmpty(PlayerBucketEmptyEvent e)
    {
    	Block placedLiquid;
    	if(e.getBucket().getDamage() == 8) //getDamage() => Meta
    	{
    		placedLiquid = new BlockWater();
    		placedLiquid.x = e.getBlockClicked().x;
    		placedLiquid.y = e.getBlockClicked().y;
    		placedLiquid.z = e.getBlockClicked().z;
    	} else if(e.getBucket().getDamage() == 10) //getDamage() => Meta
    	{
    		placedLiquid = new BlockLava();
    		placedLiquid.x = e.getBlockClicked().x;
    		placedLiquid.y = e.getBlockClicked().y;
    		placedLiquid.z = e.getBlockClicked().z;
    	}else
    	{
    		return;
    	}
    	placedLiquid.level = e.getBlockClicked().level;
    	buildHandler(e, e.getPlayer(), placedLiquid);
    }
    
    void buildHandler(Event event, Player player, Block block)
    {
    	if(block.getId() == 0) return;
    	
		dbAccess = DBAccess.getDB();
		if(BlockTrack.playersHistoryCheck.contains(player.getUniqueId()))
		{			
			BlockTrack.bhc.blockHistorySend(player, block);
			event.setCancelled();
			return;
		} else {
			DBAccess.blockHistoryAddEntry(player.getUniqueId(), block, true); //BlockHistory Tracker
		}
	
		BlockTable entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId());	//Get DAO from database with the above UUID and blocktype
		int placed = 1;
		if(entry != null)
		{
			placed += entry.getPlaced(); //Get number of {blockType} placed by player, increment
		}else
		{
			DBAccess.blockTrackAddEntry(player.getUniqueId(), block.getId());
			entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId());
			entry.setPlaced(placed);
			dbAccess.blockTrackUpdateEntry(entry);
			return;
		}
		entry.setPlaced(placed); //Set number of {blockType} placed by player
		dbAccess.blockTrackUpdateEntry(entry);
    }
}