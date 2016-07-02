package com.Ben12345rocks.VotingPlugin.Data;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Files.Files;
import com.Ben12345rocks.VotingPlugin.Objects.SignHandler;

public class ServerData {

	static ServerData instance = new ServerData();

	static Main plugin = Main.plugin;

	public static ServerData getInstance() {
		return instance;
	}

	FileConfiguration data;

	File dFile;

	private ServerData() {
	}

	public ServerData(Main plugin) {
		ServerData.plugin = plugin;
	}

	public void addSign(Location location, String data, int position) {

		int count = nextSignNumber();

		getData().set("Signs." + count + ".World",
				location.getWorld().getName());
		getData().set("Signs." + count + ".X", location.getBlockX());
		getData().set("Signs." + count + ".Y", location.getBlockY());
		getData().set("Signs." + count + ".Z", location.getBlockZ());
		getData().set("Signs." + count + ".Data", data);
		getData().set("Signs." + count + ".Position", position);
		saveData();
		plugin.signs.add(new SignHandler("" + count,
				getSignLocation("" + count), getSignData("" + count),
				getSignPosition("" + count)));
	}

	public FileConfiguration getData() {
		return data;
	}

	public int getPrevMonth() {
		return getData().getInt("PrevMonth");
	}

	public String getSignData(String sign) {
		return getData().getString("Signs." + sign + ".Data");
	}

	public Location getSignLocation(String sign) {
		return new Location(Bukkit.getWorld(getData().getString(
				"Signs." + sign + ".World")), getData().getDouble(
				"Signs." + sign + ".X"), getData().getDouble(
				"Signs." + sign + ".Y"), getData().getDouble(
				"Signs." + sign + ".Z"));
	}

	public int getSignPosition(String sign) {
		return getData().getInt("Signs." + sign + ".Position");
	}

	public Set<String> getSigns() {
		try {
			return getData().getConfigurationSection("Signs").getKeys(false);
		} catch (Exception ex) {
			return new HashSet<String>();
		}
	}

	public int nextSignNumber() {
		Set<String> signs = getSigns();

		if (signs != null) {
			for (int i = 0; i < 100000; i++) {
				if (!signs.contains(Integer.toString(i))) {
					return i;
				}
			}
		}
		return 0;
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dFile);
	}

	public void removeSign(String sign) {
		getData().set("Signs." + sign + ".World", null);
		getData().set("Signs." + sign + ".X", null);
		getData().set("Signs." + sign + ".Y", null);
		getData().set("Signs." + sign + ".Z", null);
		getData().set("Signs." + sign + ".Data", null);
		getData().set("Signs." + sign + ".Position", null);
		getData().set("Signs." + sign, null);
		saveData();
	}

	public void saveData() {
		Files.getInstance().editFile(dFile, data);
	}

	public void setPluginVersion() {
		getData().set("PluginVersion", plugin.getDescription().getVersion());
		saveData();
	}

	public void setPrevMonth(int value) {
		getData().set("PrevMonth", value);
		saveData();
	}

	public void setSign(String count, Location location, String data,
			int position) {

		getData().set("Signs." + count + ".World",
				location.getWorld().getName());
		int x = (int) location.getX();
		int z = (int) location.getZ();
		getData().set("Signs." + count + ".X", x);
		getData().set("Signs." + count + ".Y", location.getY());
		getData().set("Signs." + count + ".Z", z);
		getData().set("Signs." + count + ".Data", data);
		getData().set("Signs." + count + ".Position", position);
		saveData();
	}

	@SuppressWarnings("deprecation")
	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dFile = new File(p.getDataFolder(), "ServerData.yml");

		boolean genFile = false;

		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
				genFile = true;
			} catch (IOException e) {
				Bukkit.getServer()
						.getLogger()
						.severe(ChatColor.RED
								+ "Could not create ServerData.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dFile);
		data.options().header("DO NOT EDIT THIS FILE MANUALLY");
		if (genFile) {
			setPrevMonth(new Date().getMonth());
		}
		saveData();
	}

	public void setVersion() {
		getData().set("Version", Bukkit.getVersion());
		saveData();
	}

	public void updateValues() {
		setVersion();
		setPluginVersion();
	}
}
