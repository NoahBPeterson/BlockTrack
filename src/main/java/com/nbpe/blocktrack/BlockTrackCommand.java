package com.nbpe.blocktrack;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.Plugin;

public class BlockTrackCommand extends PluginCommand<BlockTrack>
{

	public BlockTrackCommand(String name, Plugin owner) {
		super("BlockTrack", BlockTrack.plugin);
        this.setPermission(Permission.DEFAULT_OP);
        this.commandParameters.clear();
        this.commandParameters.put("blockTrack", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false)

        });
        this.commandParameters.put("specificBlock", new CommandParameter[]
        		{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("blockName", false, CommandParameter.ENUM_TYPE_BLOCK_LIST)

        });
        this.commandParameters.put("total", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("total", CommandParamType.STRING, false),
                new CommandParameter("broken/placed", CommandParamType.STRING, false)
        });
        this.setUsage("/blocktrack <player> [total/blockName] [broken/placed]");
	}
}