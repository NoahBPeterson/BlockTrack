package com.nbpe.listeners.async;

import java.util.UUID;

import com.nbpe.db.DBAccess;

import cn.nukkit.block.Block;
import cn.nukkit.scheduler.AsyncTask;

public class AsyncBlockHistory extends AsyncTask {
	
	UUID player;
	Block block;
	boolean placed;
	
	public AsyncBlockHistory(UUID uuid, Block block, boolean placed)
	{
		this.placed = placed;
		this.player = uuid;
		this.block = block;
	}

	@Override
	public void onRun() {
		DBAccess.blockHistoryAddEntry(player, block, placed);
	}

}
