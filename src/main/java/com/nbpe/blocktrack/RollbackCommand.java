package com.nbpe.blocktrack;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.Plugin;

public class RollbackCommand extends PluginCommand<BlockTrack>
{
//https://github.com/Creeperface01/WorldFixer/blob/master/src/main/java/com/creeperface/nukkitx/worldfixer/LevelConverter.java
//https://github.com/Creeperface01/WorldFixer/blob/master/src/main/java/com/creeperface/nukkitx/worldfixer/WorldFixer.java
	
	public RollbackCommand(String name, Plugin owner) {
		super(name, BlockTrack.plugin);
		this.setPermission("blocktrack.rollback");
        this.setUsage("/blockRollBack");
        this.setDescription("Rollback a player's edits");
        
        this.commandParameters.clear();
        this.commandParameters.put("rollback", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("hours", CommandParamType.STRING, false)

        });
        this.commandParameters.put("specificWorld", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("world", CommandParamType.STRING, false)

        });

        
	}
	

	

	
	
}