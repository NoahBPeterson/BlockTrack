package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockTrackStatistics")
public class BlockTable
{
	
	@DatabaseField(canBeNull = false, columnName = "uuidBlockType", id=true)
	String uuidBlockType;
	
	@DatabaseField(canBeNull = false, columnName = "uuid")
	String uuid;
	
	@DatabaseField(canBeNull = false, columnName = "blockType")
	int blockType;
	
	@DatabaseField(canBeNull = true, columnName = "placed")
	int placed;
	
	@DatabaseField(canBeNull = true, columnName = "destroyed")
	int destroyed;
	
	public BlockTable(String uuID, int blockType)
	{
		uuidBlockType=uuID+blockType;
		this.uuid=uuID;
		this.blockType=blockType;
	}
	
	public BlockTable() {}
	
	public String getuuidBlockType()
	{
		return this.uuidBlockType;
	}
	
	
	public String getUUID()
	{
		return this.uuid;
	}
	
	public int getBlockType()
	{
		return blockType;
	}
	
	public void setUUID(String uuID)
	{
		this.uuid = uuID;
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
