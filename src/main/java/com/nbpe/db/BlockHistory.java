package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockHistory")
public class BlockHistory {
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_BLOCK_POSITION")
	BlockPosition blockPos;
	
    @DatabaseField(canBeNull = false, columnName = "uuid")
	String uuid;
	
    @DatabaseField(canBeNull = false, columnName = "unixTime")
	int unixTime;
	
    @DatabaseField(canBeNull = false, columnName = "placed")
	boolean placed;
	
    @DatabaseField(canBeNull = true, columnName = "blockType")
	int blockType;
	
    BlockHistory(BlockPosition blockPosition, String UUID, boolean placed, int BlockType)
    {
    	blockPos = blockPosition;
    	uuid=UUID;
    	placed=true;
    	blockType=BlockType;
		unixTime = (int) System.currentTimeMillis()/1000;
    }
    
    BlockHistory(BlockPosition blockPosition, String UUID)
    {
    	blockPos = blockPosition;
    	uuid=UUID;
    	placed=false;
		unixTime = (int) System.currentTimeMillis()/1000;
    }
    
	BlockHistory()
	{
		unixTime = (int) System.currentTimeMillis()/1000;
	}
	
	//LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(getTime()),TimeZone.getDefault().toZoneId());
	//2017-07-03T10:25
	public int getTime()
	{
		return unixTime;
	}
	
	public void setTime(int time)
	{
		unixTime = time;
	}
	
	public BlockPosition getBlockPosition()
	{
		return blockPos;
	}
	
	public void setBlockPosition(BlockPosition d)
	{
		blockPos = d;
	}
	
	public String getUUID()
	{
		return uuid;
	}
	
	public void setUUID(String c)
	{
		uuid = c;
	}
	
	public boolean placed()
	{
		return placed;
	}
	
	public void setPlaced(boolean b)
	{
		placed = b;
	}
	
	public int getBlockType()
	{
		return blockType;
	}
	
	public void setBlockType(int a)
	{
		blockType = a;
	}

}
