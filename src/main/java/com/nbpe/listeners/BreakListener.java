package com.nbpe.listeners;

import com.nbpe.blocktrack.BlockTrack;
import com.nbpe.listeners.async.AsyncBlockHistory;
import com.nbpe.listeners.async.AsyncBlockSend;
import com.nbpe.listeners.async.AsyncBreak;

import cn.nukkit.Player;
import cn.nukkit.Server;
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
    
    public static void breakHandler(Event event, Player player, Block block)
    {
    	if(block.getId() == 0) return;

		if(BlockTrack.playersHistoryCheck.contains(player.getUniqueId()))
		{
	    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBlockSend(player, block));
			event.setCancelled();
			return;
		} else {
	    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBlockHistory(player.getUniqueId(), block, false));
		}		
		
    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBreak(event, player, block));

    }
}