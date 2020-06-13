package com.nbpe.listeners.async;

import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Event;
import cn.nukkit.scheduler.AsyncTask;

public class AsyncBuild extends AsyncTask {

	Event event;
	Player player;
	Block block;
	
	public AsyncBuild(Event event, Player player, Block block)
	{
		this.event = event;
		this.player = player;
		this.block = block;
	}
	
	@Override
	public void onRun() {
		DBAccess dbAccess = DBAccess.getDB();

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
