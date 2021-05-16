package live.hoobs.spigot.blexplugin.listeners;
import live.hoobs.spigot.blexplugin.Boss.Boss;
import live.hoobs.spigot.blexplugin.Boss.Door;
import live.hoobs.spigot.blexplugin.Boss.Minion;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;

public class BossListener implements Listener { 

    //bosses
    ArrayList<Boss> bosses = new ArrayList<Boss>();
    // worlds
    World overworld = Bukkit.getServer().getWorld("world");
    World nether = Bukkit.getServer().getWorld("world_nether");


    public BossListener(){
        //find bosses
        Boss.findBoss(overworld, EntityType.ENDER_DRAGON, "HOOBA", 0, 0, 10).stream().forEach(h -> bindHooba((LivingEntity)h));
        Boss.findBoss(nether, EntityType.GHAST, "HOOBASMOM", -5, -1, 6).stream().forEach(h -> bindHoobasMom((LivingEntity)h));
        Boss.findBoss(overworld, EntityType.PIGLIN_BRUTE, "HOOBASDAD", 0, 312, 6).stream().forEach(h -> bindHoobasDad((LivingEntity)h));
    }

    // Hooba
    public void bindHooba(LivingEntity entity){        
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Boss.class);
   
        plugin.getLogger().info("building hooba!");
        ArrayList<Door> doors = new ArrayList<Door>();
        doors.add(new Door(overworld, -10, 78, -13, 0, 1, 1, Material.CRYING_OBSIDIAN));
        ArrayList<Minion> minions = new ArrayList<Minion>();
        Boss hooba = new Boss(entity, "Hoobasaurus", doors, minions, new Location(overworld, 0, 90, 0), 100);
        bosses.add(hooba);

        // clean up all dead bosses
        bosses.removeIf(boss -> (boss.isDead()));
    }

    // Hooba's Mom
    public void bindHoobasMom(LivingEntity entity){        
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Boss.class);
   
        plugin.getLogger().info("building hooba's mom!");
        ArrayList<Door> doors = new ArrayList<Door>();
        doors.add(new Door(nether, -98, 63, -25, 3, 0, 1, Material.CRYING_OBSIDIAN));
        ArrayList<Minion> minions = new ArrayList<Minion>();

        minions.add(new Minion(nether, -77, 64, -28, EntityType.SKELETON, "Hooba's Half Brother 1", "bros"));
        minions.add(new Minion(nether, -89, 64, -28, EntityType.SKELETON, "Hooba's Half Brother 2", "bros"));
        minions.add(new Minion(nether, -78, 100, -26, EntityType.SKELETON, "Hooba's Half Brother 3", "bros"));
        minions.add(new Minion(nether, -59, 76, -23, EntityType.EVOKER, "Hooba's Half Brother 4", "none"));

        Boss hoobasmom = new Boss(entity, "Hoobas's Mom", doors, minions, new Location(nether, -82, 76, -23), 100);
        bosses.add(hoobasmom);

        // clean up all dead bosses
        bosses.removeIf(boss -> (boss.isDead()));
    }

    // Hooba's Dad
    public void bindHoobasDad(LivingEntity entity){        
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Boss.class);
   
        plugin.getLogger().info("building hooba's dad!");
        ArrayList<Door> doors = new ArrayList<Door>();
        doors.add(new Door(overworld, 34, 29, 5041, 1, 1, 0, Material.CRYING_OBSIDIAN));
        ArrayList<Minion> minions = new ArrayList<Minion>();
        Boss hoobasdad = new Boss(entity, "Hooba's Dad", doors, minions, new Location(overworld, 40, 32, 5033), 50);
        bosses.add(hoobasdad);

        // clean up all dead bosses
        bosses.removeIf(boss -> (boss.isDead()));
    }
 

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        
        if (Boss.isBoss(entity, EntityType.ENDER_DRAGON, "HOOBA")) {
            bindHooba((LivingEntity)entity);
            return;
        }
        if (Boss.isBoss(entity, EntityType.GHAST, "HOOBASMOM")) {
            bindHoobasMom((LivingEntity)entity);
            return;
        }
        if (Boss.isBoss(entity, EntityType.PIGLIN_BRUTE, "HOOBASDAD")) {
            bindHoobasDad((LivingEntity)entity);
            return;
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        bosses.stream().forEach(b -> b.addBar(event.getPlayer()));
    }


}
