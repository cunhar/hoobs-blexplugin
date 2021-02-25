package live.hoobs.spigot.blexplugin.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.TextComponent;

import java.lang.System.Logger;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity; 
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarFlag;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import live.hoobs.spigot.blexplugin.App;


public class HoobaListerner implements Listener { 
    BossBar bossBar = null;
    LivingEntity Hooba = null; 

    public HoobaListerner(){
        checkHooba();
    }

    public boolean isHooba(Entity entity){
        if (!entity.getType().equals(EntityType.ENDER_DRAGON)){
            return false;
        }
        if (!entity.getScoreboardTags().contains("HOOBA")){
            return false;
        } 
        return true;
    }

    public void checkHooba(){     
        World world = Bukkit.getServer().getWorld("world");
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(App.class);
   
        plugin.getLogger().info("Checking world for Hooba!");
        
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) { 
                for(Entity e : world.getChunkAt(x , z).getEntities()) {
                    if (isHooba(e)) {
                        plugin.getLogger().info("Found existing Hooba!");
                        Hooba = (LivingEntity) e;
                        bossBar = addBossBar(Hooba);
                        jailHooba();
                        mazeDoor(Material.CRYING_OBSIDIAN);
                        return;
                    }
                }
            }
        }

        plugin.getLogger().info("Finished checking world for Hooba!");
    }

    public void mazeDoor(Material material) {
        World world = Bukkit.getServer().getWorld("world");

        for (int y=78; y<=79; y++){
            for(int z=-13; z<=-12; z++){
                world.getBlockAt(-10, y, z).setType(material);
            }
        }
    }
    
    public void jailHooba(){
        Integer max_distance = 160;
        
        new BukkitRunnable() {   
            @Override
            public void run() {
                if (Hooba!=null && !Hooba.isDead()) {
                    if (Math.abs(Hooba.getLocation().getX()) > max_distance || Math.abs(Hooba.getLocation().getY()) > max_distance ){
                        Hooba.teleport(new Location(Hooba.getWorld(), 0, 90, 0));
                    }
                } else {
                    cancel();
                }    
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(App.class), 0, 20);
    }

    public BossBar addBossBar(LivingEntity livingEntity){
        bossBar = Bukkit.getServer().createBossBar("HOOBASAURUS", BarColor.PURPLE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
        Bukkit.getOnlinePlayers().stream().forEach(pl -> bossBar.addPlayer(pl));

        new BukkitRunnable() {   
            @Override
            public void run() {
                if (!livingEntity.isDead()) {
                    bossBar.setProgress(livingEntity.getHealth() / livingEntity.getMaxHealth());
                } else {
                    bossBar.getPlayers().stream().forEach(pl -> bossBar.removePlayer(pl));
                    bossBar.setVisible(false);
                    mazeDoor(Material.AIR);
                    cancel();
                }    
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(App.class), 0, 20);

        return bossBar;
    }
        
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        if (Hooba != null && !Hooba.isDead()){
            bossBar.addPlayer(event.getPlayer());
        }
    }
    
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (!isHooba(entity)){
            return;
        }

        Bukkit.getServer().spigot().broadcast( new TextComponent("§cHOOBA SPAWNS!"));
        Hooba = (LivingEntity) entity;
        bossBar = addBossBar(Hooba);
        jailHooba();
        mazeDoor(Material.CRYING_OBSIDIAN);
    }
}
