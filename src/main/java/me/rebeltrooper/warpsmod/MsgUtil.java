package me.rebeltrooper.warpsmod;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class MsgUtil
{
	public static void msg(ICommandSender sender, String msg)
	{
		sender.sendMessage(new TextComponentString(msg));
	}
}
