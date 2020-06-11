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
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;

public class BreakListener implements Listener {
	
	private DBAccess dbAccess;
	
    @EventHandler(priority = EventPriority.HIGH)
	public void breakHandler(BlockBreakEvent e)
	{
    	breakHandler(e, e.getPlayer(), e.getBlock());
	}
    

    @EventHandler(priority = EventPriority.HIGH)
    public void bucketFill(PlayerBucketFillEvent e)
    {
    	Block takenLiquid;
    	
    	if(e.getItem().getDamage() == 8) //getDamage() => Meta
    	{
    		takenLiquid = new BlockWater();
    		takenLiquid.x = e.getBlockClicked().x - e.getBlockFace().getUnitVector().x;
    		takenLiquid.y = e.getBlockClicked().y - e.getBlockFace().getUnitVector().y;
    		takenLiquid.z = e.getBlockClicked().z - e.getBlockFace().getUnitVector().z;
    		takenLiquid.level = e.getBlockClicked().level;

    	} else if(e.getItem().getDamage() == 10) //getDamage() => Meta
    	{
    		takenLiquid = new BlockLava();
    		takenLiquid.x = e.getBlockClicked().x - e.getBlockFace().getUnitVector().x;
    		takenLiquid.y = e.getBlockClicked().y - e.getBlockFace().getUnitVector().y;
    		takenLiquid.z = e.getBlockClicked().z - e.getBlockFace().getUnitVector().z;
    		takenLiquid.level = e.getBlockClicked().level;
    	}else
    	{
    		return;
    	}
    	breakHandler(e, e.getPlayer(), takenLiquid);
    }
    
    void breakHandler(Event event, Player player, Block block)
    {
    	if(block.getId() == 0) return;

		dbAccess = DBAccess.getDB();
		if(BlockTrack.playersHistoryCheck.contains(player.getUniqueId()))
		{
			BlockTrack.bhc.blockHistorySend(player, block);
			event.setCancelled();
			return;
		} else {
			DBAccess.blockHistoryAddEntry(player.getUniqueId(), block, false); //BlockHistory Tracker
		}		
				
		BlockTable entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId());	//Get DAO from database with the above UUID and blocktype
		int broken = 1;
		if(entry != null)
		{
			broken += entry.getDestroyed(); //Get number of {blockType} broken by player, increment
		}else
		{
			DBAccess.blockTrackAddEntry(player.getUniqueId(), block.getId());
			entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId());
			entry.setDestroyed(broken);
			dbAccess.blockTrackUpdateEntry(entry);
			return;
		}
		entry.setDestroyed(broken); //Set number of {blockType} broken by player
		dbAccess.blockTrackUpdateEntry(entry);
    }
}