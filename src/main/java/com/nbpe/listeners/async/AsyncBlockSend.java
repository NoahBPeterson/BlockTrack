package com.nbpe.listeners.async;

import com.nbpe.blocktrack.BlockHistoryCommand;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.scheduler.AsyncTask;

public class AsyncBlockSend extends AsyncTask {
	
	Player player;
	Block block;
	
	public AsyncBlockSend(Player player, Block block)
	{
		this.player = player;
		this.block = block;
	}

	@Override
	public void onRun() {
		BlockHistoryCommand.blockHistorySend(player, block);
	}

}
