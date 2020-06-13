package com.nbpe.listeners.async;

import com.nbpe.db.BlockTable;
import com.nbpe.db.DBAccess;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Event;
import cn.nukkit.scheduler.AsyncTask;

public class AsyncBreak extends AsyncTask {
	
	Event event;
	Player player;
	Block block;
	
	public AsyncBreak(Event event, Player player, Block block)
	{
		this.event = event;
		this.player = player;
		this.block = block;
	}

	@Override
	public void onRun() {
		DBAccess dbAccess = DBAccess.getDB();

		BlockTable entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId(), block.getFullId() & 0xF);	//Get DAO from database with the above UUID and blocktype
		int broken = 1;
		if(entry != null)
		{
			broken += entry.getDestroyed(); //Get number of {blockType} broken by player, increment
		}else
		{
			DBAccess.blockTrackAddEntry(player.getUniqueId(), block);
			entry = DBAccess.getByUUIDandBlockType(player.getUniqueId(), block.getId(), block.getFullId() & 0xF);
			entry.setDestroyed(broken);
			dbAccess.blockTrackUpdateEntry(entry);
			return;
		}
		entry.setDestroyed(broken); //Set number of {blockType} broken by player
		dbAccess.blockTrackUpdateEntry(entry);
	}

}