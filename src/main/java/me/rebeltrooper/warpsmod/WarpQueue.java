package me.rebeltrooper.warpsmod;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class WarpQueue
{
	private ArrayList<WarpEvent> queue;
	
	public WarpQueue()
	{
		queue = new ArrayList<WarpEvent>();
	}

	public void pushWarpEvent(WarpLocation destination, EntityPlayer player)
	{
		queue.add(new WarpEvent(destination, player));
	}
	
	public ArrayList<WarpEvent> getWarpQueue()
	{
		return queue;
	}
}
