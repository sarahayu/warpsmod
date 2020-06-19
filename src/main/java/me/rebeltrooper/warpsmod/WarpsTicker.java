package me.rebeltrooper.warpsmod;

import java.util.Iterator;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class WarpsTicker
{
	private final MinecraftServer server;
	private final Warps warps;
	private final WarpQueue queue;
	private int ticks;
	private static final int REFRESH_INTERVAL_TICKS = 10 * 20 * 60;		// 10 minutes
	
	public WarpsTicker(Warps warps, WarpQueue queue)
	{
		this.warps = warps;
		this.queue = queue;
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
	}	

    @SubscribeEvent
    public void onTick(ServerTickEvent event) 
    {
    	if (event.phase == Phase.END)
    		return;
    	
    	ticks++;
    	
    	if (ticks > REFRESH_INTERVAL_TICKS)
    	{
    		ticks -= REFRESH_INTERVAL_TICKS;
    		
    		WarpsMod.LOGGER.info("Periodically saving warps...");
    		warps.dumpWarpsToFile();
    	}    	
    	
    	Iterator<WarpEvent> evntItr = queue.getWarpQueue().iterator();
    	
    	while (evntItr.hasNext())
    	{
    		WarpEvent evnt = evntItr.next();
    		
        	evnt.decrement();
        	if (evnt.getRemaining() <= 0)
        	{
        		EntityPlayerMP player = getPlayer(evnt.getPlayerUUID());
        		
        		if (player != null && !player.isDead)
        		{
        			WarpLocation destination = evnt.getDestination();
        			Vec3d loc = destination.getCoords();
        			if (player.dimension != destination.getWorld())
        			{
            			MsgUtil.msg(player, TextFormatting.GRAY + "Uh oh, it looks like you went through a portal before you could finish your warp");        			
        			}
        			else
        			{
            			player.setPositionAndRotation(loc.x, loc.y, loc.z, destination.getYaw(), destination.getPitch());
            			player.setPositionAndUpdate(loc.x, loc.y, loc.z);
            			MsgUtil.msg(player, TextFormatting.GREEN + "Teleported!");
        			}
        		}
        		else
        		{
        			WarpsMod.LOGGER.info("[DEBUG] WarpsMod: WarpsTicker detected player " + evnt.getPlayerUUID() + " has logged out before they finished teleporting.");        			
        		}
        		evntItr.remove();
        	}
        	else if (evnt.getRemaining() % 20 == 0)
        	{
        		EntityPlayerMP player = getPlayer(evnt.getPlayerUUID());

        		if (player != null && !player.isDead)
        		{
        			MsgUtil.msg(player, TextFormatting.GREEN + "Teleporting in " + evnt.getRemaining() / 20 + "...");
        		}
        		else
        		{
        			WarpsMod.LOGGER.info("[DEBUG] WarpsMod: WarpsTicker detected player " + evnt.getPlayerUUID() + " has logged out before they finished teleporting.");
            		evntItr.remove();
        		}
        	}
    	}        
    }
    
    private EntityPlayerMP getPlayer(UUID uuid)
    {
    	return server.getPlayerList().getPlayerByUUID(uuid);
    }
}
