package com.nbpe.listeners;

import com.nbpe.blocktrack.BlockTrack;
import com.nbpe.listeners.async.AsyncBlockHistory;
import com.nbpe.listeners.async.AsyncBlockSend;
import com.nbpe.listeners.async.AsyncBuild;

import cn.nukkit.Player;
import cn.nukkit.Server;
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
    
    public static void buildHandler(Event event, Player player, Block block)
    {
    	if(block.getId() == 0) return;
    	
		if(BlockTrack.playersHistoryCheck.contains(player.getUniqueId()))
		{			
	    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBlockSend(player, block));
			event.setCancelled();
			return;
		} else {
	    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBlockHistory(player.getUniqueId(), block, true));
		}
	
    	Server.getInstance().getScheduler().scheduleAsyncTask(BlockTrack.plugin, new AsyncBuild(event, player, block));

    }
}