package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;

public class BlockWorld {
	
    @DatabaseField(generatedId = true)
	int genID;
    
    @DatabaseField(canBeNull = false, columnName = "world")
    String world;
    
	public BlockWorld(){}
	
	public void setWorld(String c)
	{
		world=c;
	}
	
	public String getWorld()
	{
		return world;
	}
}
