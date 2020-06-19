package me.rebeltrooper.warpsmod;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class Warp
{
	private final String name;
	private final WarpLocation location;
	private final UUID owner;
	
	public Warp(EntityPlayer player, String warpName)
	{
		name = warpName;
		location = new WarpLocation(player);
		owner = player.getUniqueID();
	}
	
	public Warp(String name, WarpLocation location, UUID owner)
	{
		this.name = name;
		this.location = location;
		this.owner = owner;
	}
	
	public String toString()
	{
		return name + " "
				+ owner + " "
				+ location.getCoords().x + ","
				+ location.getCoords().y + ","
				+ location.getCoords().z + " "
				+ location.getPitch() + ","
				+ location.getYaw() + " "
				+ location.getWorld();
	}

	public String getName()
	{
		return name;
	}

	public WarpLocation getLocation()
	{
		return location;
	}

	public UUID getOwner()
	{
		return owner;
	}
}
