package me.rebeltrooper.warpsmod;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = WarpsMod.MODID, name = WarpsMod.NAME, version = WarpsMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class WarpsMod
{
    public static final String MODID = "rebeltrooper_warpsmod";
    public static final String NAME = "Warps Mod";
    public static final String VERSION = "1.0";
    
    public Warps warps;
    public WarpQueue warpQueue;

	public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLServerStartingEvent event)
    {
    	warps = new Warps();
    	warpQueue = new WarpQueue();
    	
    	event.registerServerCommand(new WarpCommand(warps, warpQueue));
    	MinecraftForge.EVENT_BUS.register(new WarpsTicker(warps, warpQueue));
    }

    @EventHandler
    public void shutDown(FMLServerStoppingEvent event)
    {
    	warps.dumpWarpsToFile();
    	LOGGER.info("Dumping warps to file...");
    }
}
