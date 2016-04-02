package com.Ben12345rocks.VotingPlugin.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.Ben12345rocks.VotingPlugin.Main;

public class ConfigBungeeVoting {

	static ConfigBungeeVoting instance = new ConfigBungeeVoting();

	static Main plugin = Main.plugin;

	public static ConfigBungeeVoting getInstance() {
		return instance;
	}

	FileConfiguration data;

	File dFile;

	private ConfigBungeeVoting() {
	}

	public ConfigBungeeVoting(Main plugin) {
		ConfigBungeeVoting.plugin = plugin;
	}

	public FileConfiguration getData() {
		return data;
	}

	public int getRecievePort() {
		return getData().getInt("RecievePort");
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getSendPorts() {
		return (List<Integer>) getData().getList("SendPorts");
	}

	public boolean recieveBungeeVotes() {
		return getData().getBoolean("RecieveBungeeVotes");
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dFile);
	}

	public void saveData() {
		try {
			data.save(dFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
			.severe(ChatColor.RED + "Could not save BungeeVoting.yml!");
		}
	}

	public boolean sendBungeeVotes() {
		return getData().getBoolean("SendBungeeVotes");
	}

	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dFile = new File(p.getDataFolder(), "BungeeVoting.yml");

		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
				plugin.saveResource("BungeeVoting.yml", true);
			} catch (IOException e) {
				Bukkit.getServer()
				.getLogger()
				.severe(ChatColor.RED
						+ "Could not create BungeeVoting.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dFile);
	}
}