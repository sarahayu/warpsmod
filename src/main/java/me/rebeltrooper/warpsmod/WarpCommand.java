package me.rebeltrooper.warpsmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class WarpCommand implements ICommand
{
	private final Warps warps;
	private final WarpQueue warpQueue;
	private static final String USAGE = TextFormatting.DARK_AQUA + "/warp <set|delete> <warpName>"
    		+ TextFormatting.GRAY + " to set/delete a warp\n"
    		+ TextFormatting.DARK_AQUA + "/warp list <pageNumber>"
    	    + TextFormatting.GRAY + " to list warp points\n"
    	    		+ TextFormatting.DARK_AQUA + "/warp <warpName>"
    	    	    + TextFormatting.GRAY + " to warp";

	public WarpCommand(Warps warps, WarpQueue warpQueue)
	{
		this.warps = warps;
		this.warpQueue = warpQueue;
	}

	@Override
	public int compareTo(ICommand arg0)
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "warp";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
	    return USAGE;
	}

	@Override
	public List<String> getAliases()
	{
		return new ArrayList<String>(Arrays.asList("warp"));
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 2 && args[0].equalsIgnoreCase("set"))
		{
			if (!checkSender(sender))
				return;
			
			if (warps.addWarp((EntityPlayer)sender, args[1]))
				MsgUtil.msg(sender, TextFormatting.GREEN + "Warp '" + args[1] + "' set!");
			else
				MsgUtil.msg(sender, TextFormatting.RED + "A warp with that name already exists!");
			return;
		}
		if (args.length == 2 && args[0].equalsIgnoreCase("delete"))
		{
			if (!checkSender(sender))
				return;
			
			String response = warps.delWarp((EntityPlayer)sender, args[1]);
			if (response.length() == 0)
				MsgUtil.msg(sender, TextFormatting.GREEN + "Warp '" + args[1] + "' deleted!");
			else
				MsgUtil.msg(sender, TextFormatting.RED + "Warp delete failed! " + response);
			return;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("list"))
		{
			MsgUtil.msg(sender, TextFormatting.GRAY + "Type "
					+ TextFormatting.DARK_AQUA + "/warp list <pageNumber> "
					+ TextFormatting.GRAY + "to get a list of warps based on page number");
			return;
		}
		if (args.length == 2 && args[0].equalsIgnoreCase("list"))
		{
			if (!isInteger(args[1]))
			{
				MsgUtil.msg(sender, TextFormatting.RED + "'" + args[1] + "' is not a number!");
				return;
			}

			int pageNum = Integer.parseInt(args[1]) - 1;
			int start = pageNum * 7;
			ArrayList<String> names = warps.getWarpNames();
			
			if (start >= names.size() || start < 0)
			{
				MsgUtil.msg(sender, TextFormatting.RED + "Page " + args[1] + " doesn't exist!");
				return;
			}
			
			String page = TextFormatting.GRAY + "========= " 
					+ TextFormatting.DARK_AQUA + "Warps" 
					+ TextFormatting.GRAY + " =========";
			
			for (int i = start; i < start + 7 && i < names.size(); i++)
			{
				page += "\n " + names.get(i);
			}
			
			MsgUtil.msg(sender, page);
			return;
		}
		if (args.length == 1)
		{
			if (!checkSender(sender))
				return;
			
			Warp warp = warps.getWarp(args[0]);
			
			if (warp == null)
			{
				MsgUtil.msg(sender, TextFormatting.RED + "No warp is found with that name, type "
						+ TextFormatting.DARK_AQUA + "/warp list"
						+ TextFormatting.RED + " to list available warps");
				return;
			}
			
			if (!pushTeleportEvent((EntityPlayer)sender, warp.getLocation()))
				MsgUtil.msg(sender, TextFormatting.RED + "Player world and warp world are not the same!");
			
			return;
		}
		if (args.length == 0)
		{
			MsgUtil.msg(sender, USAGE);
			return;
		}
		
		MsgUtil.msg(sender, TextFormatting.RED + "Command not recognized! Type " 
		+ TextFormatting.DARK_AQUA + "/warp"
		+ TextFormatting.RED + " for help");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}

	private boolean pushTeleportEvent(EntityPlayer player, WarpLocation location)
	{
		if (player.dimension != location.getWorld())
			return false;

		warpQueue.pushWarpEvent(location, player);
		return true;
	}

	private boolean checkSender(ICommandSender sender)
	{
		if (!(sender instanceof EntityPlayer))
		{
			MsgUtil.msg(sender, TextFormatting.RED + "Only players may do execute this command!");
			return false;
		}
		
		return true;
	}
	
	// via https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
	// because i'm lazy
	private boolean isInteger(String intStr)
	{
	    if (intStr == null) {
	        return false;
	    }
	    int length = intStr.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (intStr.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = intStr.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}
