package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockHistory")
public class BlockHistory {
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_BLOCK_POSITION")
	BlockPosition blockPos;
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_PLAYER", foreignAutoRefresh=true)
	BlockPlayer bPlayer;
	
    @DatabaseField(canBeNull = false, columnName = "unixTime")
	int unixTime;
	
    @DatabaseField(canBeNull = false, columnName = "placed")
	boolean placed;
	
    @DatabaseField(canBeNull = false, columnName = "blockType")
	int blockType;
    
    @DatabaseField(canBeNull = true, columnName = "blockSubType")
    int blockSubType;
	
    BlockHistory(BlockPosition blockPosition, BlockPlayer blockPlayer, boolean placed, int BlockType)
    {
    	blockPos = blockPosition;
    	bPlayer=blockPlayer;
    	this.placed=placed;
    	blockType=BlockType;
		unixTime = (int) (System.currentTimeMillis() / 1000L);

    }
    
    BlockHistory(BlockPosition blockPosition, BlockPlayer player)
    {
    	blockPos = blockPosition;
    	bPlayer=player;
    	placed=false;
		unixTime = (int) (System.currentTimeMillis() / 1000L);
    }
    
	BlockHistory()
	{
		unixTime = (int) (System.currentTimeMillis() / 1000L);
	}
	
	public int getTime()
	{
		return unixTime;
	}
	
	public int getSubType()
	{
		return blockSubType;
	}
	
	public BlockPosition getBlockPosition()
	{
		return blockPos;
	}
	
	public void setBlockPosition(BlockPosition d)
	{
		blockPos = d;
	}
	
	public BlockPlayer getPlayer()
	{
		return bPlayer;
	}
	
	public void setPlayer(BlockPlayer c)
	{
		bPlayer = c;
	}
	
	public boolean placed()
	{
		return placed;
	}
	
	public void setPlaced(boolean b)
	{
		placed = b;
	}
	
	public void setSubType(int n)
	{
		blockSubType = n;
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
