package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.nukkit.block.Block;

@DatabaseTable(tableName = "BlockTrackStatistics")
public class BlockTable
{
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_PLAYER", foreignAutoRefresh = true)
	BlockPlayer bPlayer;
	
    //https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#index-byte-array
    //Short?
	@DatabaseField(canBeNull = false, columnName = "blockType")
	int blockType;
	
    @DatabaseField(canBeNull = true, columnName = "blockSubType")
    int blockSubType;
	
	@DatabaseField(canBeNull = true, columnName = "placed")
	int placed;
	
	@DatabaseField(canBeNull = true, columnName = "destroyed")
	int destroyed;
	
    @DatabaseField(generatedId = true)
	int genID;
	
	public BlockTable(BlockPlayer blockPlayer, Block block)
	{
		this.bPlayer = blockPlayer;
		this.blockType=block.getId();
		if(Block.hasMeta[block.getId()])
		{
			this.setSubType(block.getFullId() & 0xF); //Get lowest 4 bits of the fullId
		} else
		{
			this.setSubType(0);
		}

	}
	
	public BlockTable() {}
	
	public int getSubType()
	{
		return blockSubType;
	}
	
	public void setSubType(int n)
	{
		blockSubType = n;
	}
	
	
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
