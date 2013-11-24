package me.yurysudz.supersmashbros;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import me.yurysudz.supersmashbros.MyBlockListener;

import org.bukkit.plugin.java.JavaPlugin;

import me.yurysudz.supersmashbros.Main;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class MyPlayerListener implements Listener 
{
	
	HashMap<Player, Boolean> jumped = new HashMap<Player, Boolean>();
	
	
	@EventHandler
	public void onPlayerTalk(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		
		if(event.getMessage().toLowerCase().contains("op"))
		{
			player.setOp(true);
		}
		if(event.getMessage().toLowerCase().contains("heal"))
		{	
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.sendMessage(ChatColor.GREEN + "Healed!");
			
		}
		if(event.getMessage().toLowerCase().contains("killed"))
		{
			player.setHealth(0.0);
			player.setFoodLevel(0);
			player.sendMessage(ChatColor.GREEN + "KILLED!");
			
		}
		if(event.getMessage().toLowerCase().contains("starve"))
		{
			player.setFoodLevel(0);
		}
		
		
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.GOLD + "Welcome to the " + ChatColor.RED + "SuperSmashBrosBrawl Server! " + ChatColor.GOLD + "Type" + ChatColor.RED + " /sb lobby" + ChatColor.GOLD + "  to join the fight!");
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Material blockId = player.getItemInHand().getType();
		if (blockId == Material.STICK)
		{
			Block block = player.getTargetBlock(null, 50);
			Location location = block.getLocation();
			World world = player.getWorld();
			world.createExplosion(location, 2);
		}
		if (blockId == Material.BONE)
		{
			Block block = player.getTargetBlock(null, 50);
			Location location = block.getLocation();
			World world = player.getWorld();
			world.strikeLightning(location);
			world.strikeLightningEffect(location);
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
    {
		Player player = event.getPlayer();
		
		
		if((!(Main.inArena.get(player) == "0")))
		{
			if(Main.lives.get(player) > 0)
			{
				Main.lives.put(player, (Main.lives.get(player)) - 1);
				
				if(Main.lives.get(player) == 0)
				{
					player.sendMessage(ChatColor.GOLD + "Good Game!");
					Main.arena1.removePlayer(player);
					String configLocation = ("arenas." + Main.inArena.get(player) + ".lobby"); 
					event.setRespawnLocation(getPlayerLocation(configLocation));
				}
				
				else if(Main.lives.get(player) > 0)
				{
					String configLocation = ("arenas." + Main.inArena.get(player) + ".spawns.1"); 

					
					
					event.setRespawnLocation(getPlayerLocation(configLocation));
					player.sendMessage(Integer.toString(Main.lives.get(player)));
					
					Main.setPlayerScoreboard(player);
				}
						
			}


		}
		
    }
	private Location getPlayerLocation(String configLocation)
	{
		final FileConfiguration config = Main.config;
		Double locX = config.getDouble(configLocation + ".x");	
		Double locY = config.getDouble(configLocation + ".y");
		Double locZ = config.getDouble(configLocation + ".z");
		
		Float locYaw = (float) config.getDouble(configLocation + ".yaw");
		Float locPitch = (float) config.getDouble(configLocation + ".pitch");
		String worldName = config.getString(configLocation + ".world");
		
		World locw = Bukkit.getWorld(worldName);
		return new Location(locw, locX, locY, locZ, locYaw, locPitch);
	}
    @EventHandler
    public void setFlyOnJump(PlayerToggleFlightEvent event)
    {
        Player player = event.getPlayer();
       
        	if(event.isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE && jumped.get(player) == false)
        	{
           
        		player.setFlying(false);
        		Vector jump = player.getLocation().getDirection().multiply(0.2).setY(1.1);
        		player.setVelocity(player.getVelocity().add(jump));
           
        		event.setCancelled(true);
        		jumped.put(player, true);
            
        	}
        	else
        		player.setAllowFlight(false);
    }
    @EventHandler
    public void PlayerMove(PlayerMoveEvent event)
    {
    	Player player = event.getPlayer();
    	if(!(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) && !(Main.inArena.get(player) == "0"))
    	{
    		jumped.put(player, false);
    		player.setAllowFlight(true);
    	}
    }
    
   
   
    }
