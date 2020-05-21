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
	private static Dao<BlockPlayer, BlockPlayer> blockPlayDao;
	private static Dao<BlockWorld, BlockWorld> blockWorldDao;
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
    		blockPlayDao = DaoManager.createDao(connectionSource, BlockPlayer.class);
    		blockWorldDao = DaoManager.createDao(connectionSource, BlockWorld.class);
    		//if(!entriesDao.isTableExists())
	    	TableUtils.createTableIfNotExists(connectionSource, BlockTable.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockPosition.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockHistory.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockPlayer.class);
	    	TableUtils.createTableIfNotExists(connectionSource, BlockWorld.class);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}
    
    public static BlockPlayer getPlayer(UUID player)
    {
    	BlockPlayer record = null;
		try
		{
			QueryBuilder<BlockPlayer, BlockPlayer> queryBuilder = blockPlayDao.queryBuilder();
			queryBuilder.where().eq("uuid", player.toString());
			
			PreparedQuery<BlockPlayer> preparedQuery = queryBuilder.prepare();
			record = blockPlayDao.queryForFirst(preparedQuery);
		} catch (SQLException e) {
            e.printStackTrace();
            return record;
        }
        return record;
    }
    
    public static BlockWorld getWorld(String levelName)
    {
    	BlockWorld record = null;
		try
		{
			QueryBuilder<BlockWorld, BlockWorld> queryBuilder = blockWorldDao.queryBuilder();
			queryBuilder.where().eq("world", levelName);
			
			PreparedQuery<BlockWorld> preparedQuery = queryBuilder.prepare();
			record = blockWorldDao.queryForFirst(preparedQuery);
		} catch (SQLException e) {
            e.printStackTrace();
            return record;
        }
        return record;
    }
    
    public static void playerAddEntry(UUID player)
    {
    	BlockPlayer record = new BlockPlayer();
    	if(getPlayer(player)==null && player != null)
    	{
    		try {
    			record.setuuid(player.toString());
				blockPlayDao.create(record);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}else if(player == null){
    		Server.getInstance().getLogger().alert("Tried to insert null UUID!");
    	}
    }
    
    public static void worldNameAddEntry(String levelName)
    {
    	BlockWorld record = new BlockWorld();
    	if(getWorld(levelName)==null)
    	{
    		record.setWorld(levelName);
    		try {
				blockWorldDao.create(record);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public static void blockPositionAddEntry(Block block)
    {
    	BlockPosition record = new BlockPosition();
    	if(getByBlock(block)==null)
    	{
    		record.setWorld(getWorld(block.level.getName()));
    		record.setX(block.getFloorX());
    		record.setY(block.getFloorY());
    		record.setZ(block.getFloorZ());
    		if(record.getWorld() == null) //Create BlockWorld record if it doesn't exist
    		{
    			worldNameAddEntry(block.level.getName());
        		record.setWorld(getWorld(block.level.getName()));
    		}
			try {
				blockPosDao.create(record);
			} catch (SQLException e) {
				BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
			}
    	}
    }
    
	public static void blockHistoryAddEntry(UUID player, Block block, boolean placed)
	{
		
		BlockHistory record = new BlockHistory();
		record.setBlockPosition(getByBlock(block));
		record.setBlockType(block.getId());
		record.setPlaced(placed);
		record.setPlayer(getPlayer(player));
		if(record.getBlockPosition() == null) //Create BlockPosition record if it doesn't exist
		{
			blockPositionAddEntry(block);
			record.setBlockPosition(getByBlock(block));
		}
		
		if(record.getPlayer() == null) //Create BlockPlayer record if it doesn't exist
		{
			playerAddEntry(player);
			record.setPlayer(getPlayer(player));
		}
		
		try
		{
			blockHisDao.create(record);
			
		} catch (SQLException e)
		{
			BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
		}
	}
    
	public static void blockTrackAddEntry(UUID player, int blockType)
	{
		BlockPlayer bPlayer = DBAccess.getPlayer(player);
		if(bPlayer == null)
		{
			playerAddEntry(player);
			bPlayer = DBAccess.getPlayer(player);
		}
		BlockTable record = new BlockTable(bPlayer, blockType);
		try
		{
			entriesDao.create(record);
			
		} catch (SQLException e)
		{
			BlockTrack.plugin.getLogger().info("Failed to save record! "+e.getSQLState());
		}
	}
	
	public void blockTrackUpdateEntry(BlockTable entryUpdate)
	{
		try {
			if(entryUpdate.getPlayer() == null || entryUpdate.getBlockType() == 0)
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
				blocksDestroyed+= numDestroyed.get(i).getDestroyed();
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
				blocksPlaced+= numPlaced.get(i).getPlaced();
			}
		}
        return blocksPlaced;
	}
	
	public static BlockPosition getByBlock(Block block)
	{
		BlockPosition record = null;
		BlockWorld worldRecord = DBAccess.getWorld(block.level.getName());
		if(worldRecord == null)
		{
			return record;
		}
		try
		{
			QueryBuilder<BlockPosition, BlockPosition> queryBuilder = blockPosDao.queryBuilder();
			queryBuilder.where().eq("x", block.getFloorX()).and().eq("y", block.getFloorY()).and().eq("z", block.getFloorZ()).and().eq("ID_WORLD", worldRecord);
			
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
		BlockPlayer recordPlayer = DBAccess.getPlayer(player);
		if(recordPlayer == null)
		{
			DBAccess.playerAddEntry(player);
			recordPlayer = DBAccess.getPlayer(player);
    		try {
				blockPlayDao.refresh(recordPlayer);
			} catch (SQLException e) {
				e.printStackTrace();
			} //Without this, BlockPlayer only has the ID but not the UUID.
		}
		try
		{
			QueryBuilder<BlockTable, BlockTable> queryBuilder = entriesDao.queryBuilder();
			queryBuilder.where().eq("ID_PLAYER", recordPlayer.genID).and().eq("blockType", BlockType).query();
			
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
		BlockPlayer recordPlayer = DBAccess.getPlayer(player);
		if(recordPlayer == null)
		{
			return records;
		}
        try {
        	// "SELECT * from BlockTable WHERE uuid=player.toString() ORDER BY destroyed DESC";
        	records = entriesDao.queryBuilder().orderByRaw("(destroyed+placed) DESC").where().eq("ID_PLAYER", recordPlayer.genID).query();
        	//        	records = entriesDao.queryBuilder().orderBy("destroyed", false).where().eq("ID_PLAYER", recordPlayer.genID).query();
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
        	records = blockHisDao.queryBuilder().orderBy("unixTime", false).where().eq("ID_BLOCK_POSITION", getHistory.genID).query();
        	for(int i = 0; i < records.size(); i++)
    		{
        		blockPlayDao.refresh(records.get(i).getPlayer()); //Without this, BlockPlayer only has the ID but not the UUID.
    		}
        } catch (SQLException e) {
            e.printStackTrace();
            return records;
        }
        return records;
    }
}