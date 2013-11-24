package me.yurysudz.supersmashbros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class Main extends JavaPlugin implements Listener
{
	final static String fewArguments = "Too Little Arguments!";	
	final static String manyArguments = "Too Many Arguments!";
	final static String notOnline = "This Person Is Not Online!";
	final static String youHealed = "You Were Healed!";
	final static String someoneHealed = "Someone Healed You!";
	public final MyBlockListener blockListener = new MyBlockListener();
	public final MyPlayerListener playerListener = new MyPlayerListener();
	public final Logger logger = Logger.getLogger("Minecraft");
	public static String speakString;
	public static Boolean speakFinished;
	public static String MOTD;
	public static Main plugin;
	public static Boolean blockbreak;
	public static Objective obj;
	public static Location tempLoc;
	public static HashMap<Player, Integer> inArena = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> inArenaLobby = new HashMap<Player, Integer>();
	static FileConfiguration config;
	public static ScoreboardManager sbManager = Bukkit.getScoreboardManager();
	public static Scoreboard sBoard = sbManager.getNewScoreboard();
	Team gem = sBoard.registerNewTeam("Team gem");
	public static Team arena1 = sBoard.registerNewTeam("TeaminArena1");
	public static Objective livesInArena1 = sBoard.registerNewObjective("lives", "lives");
	
	public static HashMap<Player, Integer> lives = new HashMap<Player, Integer>();
	private static boolean Player;


	

	@Override
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Disabled!");
	}
	@Override
	public void onEnable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.playerListener, this);
		blockbreak = false;

		config = this.getConfig();

	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)

	{	
		Player player = (Player)sender;
		if(commandLabel.equalsIgnoreCase("flipcoin"))
			Main.flipcoin(sender, cmd, commandLabel, args);
		if(commandLabel.equalsIgnoreCase("heal"))
			Main.heal(sender, cmd, commandLabel, args);
		if(commandLabel.equalsIgnoreCase("teleport"))
			Main.teleport(sender,cmd,commandLabel, args);
		if(commandLabel.equalsIgnoreCase("speak"))
			Main.speak(sender, cmd, commandLabel, args);
		if(commandLabel.equalsIgnoreCase("ko"))
			Main.ko(sender, cmd, commandLabel, args);
		if(commandLabel.equalsIgnoreCase("MOTD"))
			motd(sender, cmd, commandLabel, args);
		if(commandLabel.equalsIgnoreCase("ssb"))
			ssb(sender, cmd, commandLabel, args);
		return false;
	}

	
	
	public static void heal(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player) sender;
		if(args.length == 0)
		{
			player.setHealth(20.0);
			player.setFireTicks(0);
			player.sendMessage(ChatColor.GREEN + youHealed);
		}
		if(args.length == 1)
		{
			if(player.getServer().getPlayer(args[0]) != null)
			{
				Player targetPlayer = player.getServer().getPlayer(args[0]);
				targetPlayer.setHealth(20.0);
				targetPlayer.setFireTicks(0);
				player.sendMessage(ChatColor.GREEN + someoneHealed);
			}
			else
			{
				player.sendMessage(ChatColor.RED + notOnline);
			}
		}
		if(args.length > 1)
		{
			player.sendMessage(ChatColor.RED + manyArguments);
		}
	}
	public static void teleport(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player) sender;
		if(args.length == 0)
		{
			player.sendMessage(ChatColor.RED + fewArguments);
		}
		if(args.length == 1)
		{
			Player locationPlayer = player.getServer().getPlayer(args[0]);
			Player teleportedPlayer = player;
			Location targetPlayerLocation = locationPlayer.getLocation();
			teleportedPlayer.teleport(targetPlayerLocation);
		}
		if(args.length == 2)
		{
			Player locationPlayer = player.getServer().getPlayer(args[1]);
			Player teleportedPlayer = player.getServer().getPlayer(args[0]);
			Location targetPlayerLocation = locationPlayer.getLocation();
			teleportedPlayer.teleport(targetPlayerLocation);
			teleportedPlayer.sendMessage("You were teleported to " + locationPlayer.getDisplayName());
		}
	}
	public static void flipcoin(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player)sender;
		Random object = new Random();
		int coin;
		
		for(int counter = 1; counter<=1; counter++)
		{
			coin = 	1+object.nextInt(2);
			
			if(coin == 1)
			{
				player.sendMessage(ChatColor.GOLD + "HEADS!");
			}
			if(coin == 2)
			{
				player.sendMessage(ChatColor.GRAY + "TAILS!");
			}
				
		}
	}
	public static void speak(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player) sender;
		if(args.length == 0)
			player.sendMessage(ChatColor.RED + "Type '/speak i am awesome' to broadcast i am awesome to the server.");
		else
		{
			String speakString = ("(Console): ");
			int totalWords = args.length;
			int possibleWords = (0);
			for(speakFinished=false; speakFinished == false;)
			{
				if(totalWords == possibleWords)
				{
					speakFinished = true;
					Bukkit.broadcastMessage(ChatColor.GOLD + speakString);
				}
				else
				{
				String currentWord = (args[possibleWords] + " ");
				speakString = (speakString + currentWord);
				possibleWords++;
				}
			}
		}
	}
	public static void ko(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		List<Player> onlinePlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
		Iterator<Player> iterator = onlinePlayers.iterator();
		while(iterator.hasNext())
		{
			Player onlinePlayer = iterator.next();
			onlinePlayer.setHealth(0.0);
		}
	}
	private void motd(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player)sender;
		player.sendMessage(getConfig().getString("motd"));
	}
	private void ssb(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player)sender;
		
		if(args[0].equalsIgnoreCase("lobby"))
			ssbLobby(sender, cmd, commandLabel, args);
		if(args[0].equalsIgnoreCase("arena"))
			ssbArena(sender, cmd, commandLabel, args);
		if(args[0].equalsIgnoreCase("kit"))
			ssbKit(sender, cmd, commandLabel, args);
		if(args[0].equalsIgnoreCase("setGem"))
			setGem(sender, cmd, commandLabel, args);
		if(args[0].equalsIgnoreCase("blockbreak"))
		{
			if(blockbreak == true)
			{
				blockbreak = false;
				player.sendMessage(ChatColor.DARK_RED + "Blockbreaking is now disabled for this world.");
			}
			else
			{
					blockbreak = true;
					player.sendMessage(ChatColor.GREEN + "Blockbreaking is now enabled for this world.");
			}
		}
			
		
		

	}
	private void ssbLobby(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player)sender;
		if(args.length == 1)
		{
			String configLocation = ("lobbyLocation");
			getPlayerLocation(configLocation);
			player.teleport(tempLoc);
			player.sendMessage(ChatColor.GREEN + "You have been teleported to the SSB lobby!");

		}
		if(args.length == 2)
		{
			if(args[1].equalsIgnoreCase("set"))
			{
				String configLocation = ("lobbyLocation");

				savePlayerLocation(sender, cmd, commandLabel, configLocation, args);
				
				player.sendMessage(ChatColor.GREEN + "Lobby Location set successfuly!");
			}

		}
	}
	private void ssbArena(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = (Player)sender;
		if(args.length == 1)
		{
			player.sendMessage(ChatColor.DARK_RED + "SSB Arena Commands:");
			player.sendMessage(ChatColor.GREEN + "'/ssb arena list'                 List all arenas");
			player.sendMessage(ChatColor.GREEN + "'/ssb arena join [number]'    Join the arena number that you specify");
			player.sendMessage(ChatColor.GREEN + "'/ssb arena leave'               Leave the arena you're in");
			
			
		}
		else if(args[1].equalsIgnoreCase("join"))
			{
				if(!(getConfig().getString("arenas." + args[2] + ".lobby") == null))
				{
					inArena.put(player, Integer.valueOf(args[2]));
					String configLocation = ("arenas." + args[2] + ".lobby");
					getPlayerLocation(configLocation);
					player.teleport(tempLoc);
					
					lives.put(player, Integer.valueOf("5"));
					
					
					setPlayerScoreboard(player);
					
					
				}

				
			}
		else if(args[1].equalsIgnoreCase("set"))
		{
			if(args[2].equalsIgnoreCase("lobby"))
			{
				if(args.length == 4)
				{
					String configLocation = ("arenas." + args[2] + ".lobby");
					savePlayerLocation(sender, cmd, commandLabel, configLocation, args);
					
					
				}
				else
					player.sendMessage(ChatColor.DARK_RED + "Incorrect syntax. '/ssb arena set lobby [arenaNumber]'");
			}
			if(args[2].equalsIgnoreCase("spawn"))
			{
				if(args.length == 5)
				{
					String configLocation = ("arenas." + args[3] + ".lobby");
					savePlayerLocation(sender, cmd, commandLabel, configLocation, args);
					
					
				}
				else
					player.sendMessage(ChatColor.DARK_RED + "Incorrect syntax. '/ssb arena set lobby [arenaNumber]'");
			}
		}
	}
	private void ssbKit(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		
	}
	private void setGem(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{

		Player player = (Player)sender;
		callPlayerScoreboard(player);

	}
	private void savePlayerLocation(CommandSender sender, Command cmd, String commandLabel, String configLocation, String[] args)
	{
		Player player = (Player)sender;
		
		Location playerLocation = player.getLocation();
		Double locx  = playerLocation.getX();
		Double locy  = playerLocation.getY();
		Double locz  = playerLocation.getZ();
		
		Double locpitch = (double) player.getLocation().getPitch();
		Double locyaw = (double) player.getLocation().getYaw();
		String locw = playerLocation.getWorld().getName();
		
		this.getConfig().set(configLocation + ".world", locw);
		this.getConfig().set(configLocation + ".x", locx);
		this.getConfig().set(configLocation + ".y", locy);
		this.getConfig().set(configLocation + ".z", locz);
		this.getConfig().set(configLocation + ".pitch", locpitch);
		this.getConfig().set(configLocation + ".yaw", locyaw);
		
		this.saveConfig();
	}
	final void getPlayerLocation(String configLocation)
	{
		
		Double locX = getConfig().getDouble(configLocation + ".x");	
		Double locY = getConfig().getDouble(configLocation + ".y");
		Double locZ = getConfig().getDouble(configLocation + ".z");
		
		Float locYaw = (float) getConfig().getDouble(configLocation + ".yaw");
		Float locPitch = (float) getConfig().getDouble(configLocation + ".pitch");
		String worldName = getConfig().getString(configLocation + ".world");
		
		World locw = Bukkit.getWorld(worldName);
		tempLoc = new Location(locw, locX, locY, locZ, locYaw, locPitch);
	}
	public static void setPlayerScoreboard(Player player)
		{

			livesInArena1.setDisplaySlot(DisplaySlot.SIDEBAR);
			livesInArena1.setDisplayName(ChatColor.GREEN + "Lives");
		
			player.sendMessage("" + lives.get(player));
			Score score = livesInArena1.getScore(player);
			score.setScore(lives.get(player));
			
			player.setScoreboard(sBoard);
		}
	public static void callPlayerScoreboard(Player player)
	{
		setPlayerScoreboard(player);
	}
}
