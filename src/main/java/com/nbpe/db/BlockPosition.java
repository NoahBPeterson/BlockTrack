package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BlockPosition")
public class BlockPosition {
	
    @DatabaseField(canBeNull = false, columnName = "world")
	String world;
    
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
	
	public String getWorld()
	{
		return world;
	}
	
	public void setWorld(String d)
	{
		world=d;
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
