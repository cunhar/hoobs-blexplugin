package live.hoobs.spigot.blexplugin.Boss;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Boss {
    LivingEntity bossEntity = null; 
    String bossName = null;
    BossBar bossBar = null; 
    ArrayList<Door> bossDoors = new ArrayList<Door>();
    ArrayList<Minion> bossMinions = new ArrayList<Minion>();
    ArrayList<BukkitTask> boosTimers = new ArrayList<BukkitTask>();
    Location bossSpawn = null;
    int bossJail = 50;
    

    public Boss(LivingEntity entity, String name, ArrayList<Door> doors, ArrayList<Minion> minions, Location spawn, int distance){
        bossEntity = entity; 
        bossName = name; 
        bossDoors = doors; 
        bossMinions = minions;
        bossSpawn = spawn;
        bossJail = (int)Math.pow(distance, 2);
        bossBar = Bukkit.getServer().createBossBar(bossName, BarColor.PURPLE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);

        bossMinions.stream().forEach(m -> m.spawn());

        boosTimers.add(new BukkitRunnable() {   
            @Override
            public void run() {
                if (!bossEntity.isDead()) {
                    bossBar.setProgress(bossEntity.getHealth() / bossEntity.getMaxHealth());
                } else {
                    bossBar.getPlayers().stream().forEach(pl -> bossBar.removePlayer(pl));
                    bossBar.setVisible(false);
                    openDoors();
                    cancel();
                }    
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(Boss.class), 0, 20));
        
        boosTimers.add(new BukkitRunnable() {   
            @Override
            public void run() {
                if (!entity.isDead()) {
                    if (bossEntity.getLocation().distanceSquared(bossSpawn) > bossJail ){
                        bossEntity.teleport(bossSpawn);
                    }
                } else {
                    cancel();
                }    
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(Boss.class), 0, 100));
        
        boosTimers.add(new BukkitRunnable() {   
            @Override
            public void run() {
                if (!entity.isDead()) {
                    bossMinions.stream().forEach(m ->{
                        if (m.entity.isDead()){
                            m.spawn();
                        }
                    });
                } else {
                    cancel();
                }    
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(Boss.class), 0, 100));

        // random tp

        if (name.equals("Hooba's Dad")) {
            boosTimers.add(new BukkitRunnable() {   
                @Override
                public void run() {
                    if (!entity.isDead()) {
                        Predicate<Entity> amorStands = stand -> stand.getType().equals(EntityType.ARMOR_STAND);
                        List<Entity> stands = entity.getNearbyEntities(50, 50, 50).stream().filter(amorStands).collect(Collectors.toList());
                        entity.teleport(stands.get(new Random().nextInt(stands.size())).getLocation());
                        Bukkit.broadcastMessage(name + " has teleported!");
                    } else {
                        cancel();
                    }    
                }
            }.runTaskTimer(JavaPlugin.getProvidingPlugin(Boss.class), 600, 600));
        }
        
        Bukkit.getOnlinePlayers().stream().forEach(pl -> bossBar.addPlayer(pl));
    }
    
    public void openDoors(){
        bossDoors.stream().forEach(door -> door.open());
    }

    public void closeDoors(){
        bossDoors.stream().forEach(door -> door.close());
    }

    public boolean isDead(){
        return bossEntity.isDead();
    }

    public void addBar(Player player){
        bossBar.addPlayer(player);
    }
    
    static public ArrayList<Entity> findBoss(World world, EntityType type, String tag, int x, int z, int r){
   
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Boss.class);
        plugin.getLogger().info("Checking world for "+tag+"!");
        ArrayList<Entity> bosses = new ArrayList<Entity>();
        
        for (int tX = x-r; tX <= x + (r * 2); tX++) {
            for (int tZ = z-r; tZ <= z + (r * 2); tZ++) { 
                for(Entity e : world.getChunkAt(tX, tZ).getEntities()) {
                    if (isBoss(e, type, tag)) {
                        plugin.getLogger().info("Found "+tag+"!");
                        bosses.add(e);
                        return bosses;
                    }
                }
            }
        } 

        plugin.getLogger().info("Finished checking world for "+tag+"!");

        return bosses;
    }

    static public boolean isBoss(Entity entity, EntityType type, String tag){
        if (!entity.getType().equals(type)){
            return false;
        }
        if (!entity.getScoreboardTags().contains(tag)){
            return false;
        } 
        return true;
    } 

}
