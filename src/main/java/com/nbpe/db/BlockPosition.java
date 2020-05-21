package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockPosition")
public class BlockPosition {
	
    @DatabaseField(canBeNull = false, foreign = true, columnName = "ID_WORLD", foreignAutoRefresh=true)
    BlockWorld bWorld;
    
    @DatabaseField(canBeNull = false, columnName = "x")
	int x;
    @DatabaseField(canBeNull = false, columnName = "y")
	int y;
    @DatabaseField(canBeNull = false, columnName = "z")
	int z;
	
    @DatabaseField(generatedId = true)
	int genID;
	
	public BlockPosition(){}
	
	public void setX(int c)
	{
		x=c;
	}
	
	public BlockWorld getWorld()
	{
		return bWorld;
	}
	
	public void setWorld(BlockWorld d)
	{
		bWorld = d;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setY(int b)
	{
		y=b;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public void setZ(int a)
	{
		z=a;
	}
	

}
