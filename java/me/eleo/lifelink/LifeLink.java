package me.eleo.lifelink;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class LifeLink extends JavaPlugin {

    // Barre de vie liée
    public static double sharedLife = 20.0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Chaque tick, la vie de chaque joueur s'update sur la barre de vie liée
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getHealth() > 0)
                        p.setHealth(sharedLife);
                }
            }
        }.runTaskTimer(this, 0L, 1L);

        // Boucle dans chaque monde et désactive la régénération naturelle (réglage du bug #1 dans la vidéo)
        Bukkit.getServer().getWorlds().forEach(world -> world.setGameRule(GameRule.NATURAL_REGENERATION, false));

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "LifeLink enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LifeLink disabled!");
    }
}
