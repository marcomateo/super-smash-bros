package me.yurysudz.supersmashbros;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.yurysudz.supersmashbros.Main;
public class MyBlockListener extends JavaPlugin implements Listener
{
	public static Main plugin;
	public static Material[] blacklist = {Material.TNT, Material.BEDROCK};
		
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Material block = event.getBlock().getType();
		Player player = event.getPlayer();
		
		for(Material blocked : blacklist)
		{
			if(blocked == block && !player.isOp())
			{
				event.getBlock().setType(block);
				player.chat(ChatColor.RED + "I just placed " + ChatColor.DARK_RED + blocked);
			}
				
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		World playerWorld = player.getWorld();
		World ssbWorld = Bukkit.getWorld("world");
		Boolean blockbreak = Main.blockbreak;
		final String playerInArena = Main.inArena.get(player);
		if (!(Main.inArena.get(player) == "0") | !(Main.inArenaLobby.get(player) == "0") | Main.inLobby.get(player) == true)
		{
			event.setCancelled(true);
		}

			
	}

	private void ssbArena(BlockBreakEvent event)
	{

	}
	
}
