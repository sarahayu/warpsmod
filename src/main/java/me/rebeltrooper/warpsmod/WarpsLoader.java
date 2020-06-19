package me.rebeltrooper.warpsmod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class WarpsLoader
{
	public static void dumpWarpsToFile(ArrayList<Warp> warps)
	{
		try
		{
			WarpsMod.LOGGER.info("Saving warps...");
			
			File source = new File("warps.json");
			
			Gson gson = new Gson();
			FileWriter writer = new FileWriter(source);
			gson.toJson(warps, writer);
			writer.flush();
			writer.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Warp> loadWarpsFromFile()
	{
		ArrayList<Warp> warps = new ArrayList<Warp>();
		
		try
		{
			File source = new File("warps.json");
			
			if (source.createNewFile())
			{
				WarpsMod.LOGGER.info("warps.json not located, creating new file...");

				FileWriter writer = new FileWriter(source);
				writer.write("[]");
				writer.close();
			}
			else
			{
				WarpsMod.LOGGER.info("Located warps.json! Loading data from it...");

				Gson gson = new Gson();
				warps = gson.fromJson(new FileReader(source), new TypeToken<ArrayList<Warp>>(){}.getType());
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return warps;
	}
}
