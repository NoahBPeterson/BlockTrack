package com.nbpe.db;

import com.j256.ormlite.field.DatabaseField;

public class BlockPlayer {

	
    @DatabaseField(generatedId = true)
	int genID;
    
    @DatabaseField(canBeNull = true, columnName = "uuid")
    String uuid;
    
	public BlockPlayer(String UUID)
	{
		this.uuid = UUID;
	}
	
	public BlockPlayer() {}
	
	public void setuuid(String c)
	{
		uuid=c;
	}
	
	public String getuuid()
	{
		return uuid;
	}
	
}
