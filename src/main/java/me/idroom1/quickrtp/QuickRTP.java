package me.idroom1.quickrtp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class QuickRTP extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("rtp").setExecutor(this);
        getLogger().info(getConfig().getString("plugin-name") + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(getConfig().getString("plugin-name") + " has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rtp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String worldName = getConfig().getString("world-name");
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    player.sendMessage("Invalid world specified in the config!");
                    return true;
                }

                Random random = new Random();
                int minX = getConfig().getInt("min-x");
                int maxX = getConfig().getInt("max-x");
                int minZ = getConfig().getInt("min-z");
                int maxZ = getConfig().getInt("max-z");

                int x = random.nextInt(maxX - minX + 1) + minX;
                int z = random.nextInt(maxZ - minZ + 1) + minZ;
                int y = world.getHighestBlockYAt(x, z);

                Location randomLocation = new Location(world, x, y, z);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Material blockType = world.getBlockAt(randomLocation).getType();
                        if (blockType != Material.WATER && blockType != Material.LAVA && blockType != Material.AIR) {
                            player.teleport(randomLocation);
                            player.sendMessage("QuickRTP ➤ You have been teleported to a random location!");
                        } else {
                            player.sendMessage("QuickRTP ➤ Failed to find a safe location. Try again.");
                        }
                    }
                }.runTask(this);

                return true;
            } else {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
        }
        return false;
    }
}