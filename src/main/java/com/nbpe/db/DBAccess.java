package com.nbpe.db;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nbpe.blocktrack.BlockTrack;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import ru.nukkit.dblib.DbLib;

public class DBAccess
{
	
	private static Dao<BlockTable, BlockTable> entriesDao;
	private static Dao<BlockPosition, BlockPosition> blockPosDao;
	private static Dao<BlockHistory, BlockHistory> blockHisDao;
	private static ConnectionSource connectionSource;
	private static DBAccess dbAccess;
	
	public DBAccess()
	{
		if(dbAccess == null)
		{
			dbAccess = this;
			if(!connectToDbLib())
			{
				dbAccess = null;
			}
		}
	}
	
	public static DBAccess getDB()
	{
		return dbAccess;
	}
	
    private boolean connectToDbLib()
    {
    	connectionSource = DbLib.getConnectionSource();
    	
    	if (connectionSource == null) 
		{
    		Server.getInstance().getLogger().alert("ConnectionSource is null!");
    		return false;
		}
    	try 
    	{
    		entriesDao =  DaoManager.createDao(connectionSource, BlockTable.class);
    		blockPosDao = DaoManager.createDao(connectionSource, BlockPosition.class);
    		blockHisDao = DaoManager.createDao(connectionSource, BlockHistory.class);
    		//if(!entriesDao.isTableExists())
	    	TableUtils.createTableIfNotExists(connectionSource, BlockTable.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockPosition.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockHistory.class);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}
    
	public static void BHaddEntry(UUID player, Block block, boolean placed)
	{
		
		BlockHistory record = new BlockHistory();
		record.setBlockPosition(DBAccess.getByBlock(block));
		record.setBlockType(block.getId());
		record.setPlaced(placed);
		record.setUUID(player.toString());
		if(record.getBlockPosition() == null) //Create BlockPosition record if it doesn't exist
		{
			BlockPosition bpEntry = new BlockPosition();
			bpEntry.setWorld(block.level.getName());
			bpEntry.setX(block.getFloorX());
			bpEntry.setY(block.getFloorY());
			bpEntry.setZ(block.getFloorZ());
			try {
				blockPosDao.create(bpEntry);
				record.setBlockPosition(bpEntry); //Now set record's foreign key to the new BlockPosition entry
			} catch (SQLException e) {
				BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
			}
		}
		
		try
		{
			blockHisDao.create(record);
			
		} catch (SQLException e)
		{
			BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
		}
	}
    
	public static void BTaddEntry(UUID player, int blockType)
	{
		BlockTable record = new BlockTable(player.toString(), blockType);
		try
		{
			entriesDao.create(record);
			
		} catch (SQLException e)
		{
			BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
		}
	}
	
	public void BTupdateEntry(BlockTable entryUpdate)
	{
		try {
			if(entryUpdate.getUUID() == null || entryUpdate.getBlockType() == 0)
			{
				Server.getInstance().getLogger().alert("Error: Tried to update BlockTable with either no UUID or BlockType!");
			}
			entriesDao.update(entryUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int totalBlocksBroken(UUID player)
	{
		int blocksDestroyed = 0;
		List<BlockTable> numDestroyed = getByUUID(player);
		if(numDestroyed!=null)
		{
			for(int i = 0; i < numDestroyed.size(); i++)
			{
				blocksDestroyed+= numDestroyed.get(0).getDestroyed();
			}
		}
        return blocksDestroyed;
	}
	
	public static int totalBlocksPlaced(UUID player)
	{
		int blocksPlaced = 0;
		List<BlockTable> numPlaced = getByUUID(player);
		if(numPlaced!=null)
		{
			for(int i = 0; i < numPlaced.size(); i++)
			{
				blocksPlaced+= numPlaced.get(0).getPlaced();
			}
		}
        return blocksPlaced;
	}
	
	public static BlockPosition getByBlock(Block block)
	{
		BlockPosition record = null;
		try
		{
			QueryBuilder<BlockPosition, BlockPosition> queryBuilder = blockPosDao.queryBuilder();
			queryBuilder.where().eq("x", block.getFloorX()).and().eq("y", block.getFloorY()).and().eq("z", block.getFloorZ()).and().eq("world", block.level.getName());
			
			PreparedQuery<BlockPosition> preparedQuery = queryBuilder.prepare();
			record = blockPosDao.queryForFirst(preparedQuery);
		} catch (SQLException e) {
            e.printStackTrace();
            return record;
        }
        return record;
	}
	
	public static BlockTable getByUUIDandBlockType(UUID player, int BlockType)
	{
		BlockTable record = null;
		try
		{
			QueryBuilder<BlockTable, BlockTable> queryBuilder = entriesDao.queryBuilder();
			queryBuilder.where().eq("uuid", player.toString()).and().eq("blockType", BlockType);
			
			PreparedQuery<BlockTable> preparedQuery = queryBuilder.prepare();
			record = entriesDao.queryForFirst(preparedQuery);

		} catch (SQLException e) {
            e.printStackTrace();
            return record;
        }
        return record;
	}
	
    public static List<BlockTable> getByUUID(UUID player)
    {
        List<BlockTable> records = null;
        try {
        	// "SELECT * from BlockTable WHERE uuid=player.toString() ORDER BY destroyed DESC";
        	records = entriesDao.queryBuilder().orderBy("destroyed", false).where().eq("uuid", player.toString()).query();
            //records = entriesDao.queryForEq("uuid", player.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return records;
        }
        return records;
    }
    
    public static List<BlockHistory> getListByBlock(Block block)
    {
        List<BlockHistory> records = null;
    	BlockPosition getHistory = getByBlock(block);
    	if(getHistory == null)
    	{
    		return records;
    	}
        try {
        	// "SELECT * from BlockHistory AND BlockPosition WHERE BlockHistory.BlockPosition = BlockPosition
        	records = blockHisDao.queryBuilder().orderBy("unixTime", false).where().eq("ID_BLOCK_POSITION", getHistory.genID)
        			.query();
            //records = entriesDao.queryForEq("uuid", player.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return records;
        }
        return records;
    }
}