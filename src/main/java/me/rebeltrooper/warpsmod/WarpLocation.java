package me.rebeltrooper.warpsmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class WarpLocation
{
	private final Vec3d coords;
	private final float pitch, yaw;
	private final int world;

	public WarpLocation(Vec3d coords, float pitch, float yaw, int world)
	{
		this.coords = coords;
		this.pitch = pitch;
		this.yaw = yaw;
		this.world = world;
	}
	
	public WarpLocation(double x, double y, double z, float pitch, float yaw, int world)
	{
		this(new Vec3d(x, y, z), pitch, yaw, world);
	}
	
	public WarpLocation(EntityPlayer player)
	{
		this(player.getPositionVector(), player.rotationPitch, player.rotationYaw, player.dimension);
	}
	
	public Vec3d getCoords()
	{
		return coords;
	}
	
	public float getPitch()
	{
		return pitch;
	}
	
	public float getYaw()
	{
		return yaw;
	}
	
	public int getWorld()
	{
		return world;
	}
}
