package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockTrackStatistics")
public class BlockTable
{
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_PLAYER", foreignAutoRefresh = true)
	BlockPlayer bPlayer;
	
    //https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#index-byte-array
    //Short?
	@DatabaseField(canBeNull = false, columnName = "blockType")
	int blockType;
	
	@DatabaseField(canBeNull = true, columnName = "placed")
	int placed;
	
	@DatabaseField(canBeNull = true, columnName = "destroyed")
	int destroyed;
	
    @DatabaseField(generatedId = true)
	int genID;
	
	public BlockTable(BlockPlayer blockPlayer, int blockType)
	{
		this.bPlayer = blockPlayer;
		this.blockType=blockType;
	}
	
	public BlockTable() {}
	
	
	public BlockPlayer getPlayer()
	{
		return bPlayer;
	}
	
	public int getBlockType()
	{
		return blockType;
	}
	
	public void setPlayer(BlockPlayer blockPlayer)
	{
		this.bPlayer = blockPlayer;
	}
	
	public void setBlockType(int BlockType)
	{
		this.blockType = BlockType;
	}
	
	public int getPlaced()
	{
		return placed;
	}
	
	public void setPlaced(int numberPlaced)
	{
		this.placed=numberPlaced;
	}
	
	public int getDestroyed()
	{
		return destroyed;
	}
	
	public void setDestroyed(int numberDestroyed)
	{
		this.destroyed=numberDestroyed;
	}
}
