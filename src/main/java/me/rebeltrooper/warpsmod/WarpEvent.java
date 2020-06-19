package me.rebeltrooper.warpsmod;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class WarpEvent
{
	private final WarpLocation destination;
	private final UUID player;
	private int remaining;
	
	public WarpEvent(WarpLocation destination, EntityPlayer player)
	{
		this.destination = destination;
		this.player = player.getUniqueID();
		remaining = 4 * 20;
	}
	
	public WarpLocation getDestination()
	{
		return destination;
	}
	
	public UUID getPlayerUUID()
	{
		return player;
	}
	
	public void decrement()
	{
		remaining--;
	}
	
	public int getRemaining()
	{
		return remaining;
	}
}
