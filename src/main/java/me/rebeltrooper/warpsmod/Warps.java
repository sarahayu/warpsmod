package me.rebeltrooper.warpsmod;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class Warps
{
	private ArrayList<Warp> warps;
	
	public Warps()
	{
		warps = WarpsLoader.loadWarpsFromFile();

		WarpsMod.LOGGER.info("Loading warps...");
		WarpsMod.LOGGER.info("Found " + warps.size() + "!");
	}
	
	public void dumpWarpsToFile()
	{
		WarpsLoader.dumpWarpsToFile(warps);
	}
	
	public ArrayList<String> getWarpNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for (Warp warp : warps)
			names.add(warp.getName());
		return names;
	}
	
	public boolean addWarp(EntityPlayer player, String name)
	{
		if (getWarp(name) != null)
			return false;
		
		warps.add(new Warp(player, name));
		return true;
	}
	
	public String delWarp(EntityPlayer player, String name)
	{
		for (Warp warp : warps)
			if (warp.getName().equalsIgnoreCase(name))
			{
				if (warp.getOwner().equals(player.getUniqueID()))
				{
					warps.remove(warp);
					return "";
				}
				return "You are not the owner of this warp!";
			}
		
		return "Warp was not found.";
	}
	
	public Warp getWarp(String name)
	{
		for (Warp warp : warps)
			if (warp.getName().equalsIgnoreCase(name))
				return warp;
		return null;
	}
}
