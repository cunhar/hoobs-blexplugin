package live.hoobs.spigot.blexplugin;
import org.bukkit.plugin.java.JavaPlugin;

import live.hoobs.spigot.blexplugin.commands.CommandBlexBot;
import live.hoobs.spigot.blexplugin.listeners.BossListener;
import live.hoobs.spigot.blexplugin.listeners.SpectatorListener;

/**
 * BlexBot
 *
 */
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, I'm BlexBot!");
        
        //bosses
        // getServer().getPluginManager().registerEvents(new BossListener(), this);

        //spectator
        SpectatorListener specListener = new SpectatorListener();
        getServer().getPluginManager().registerEvents(specListener, this);
        //this.getCommand("blexbot").setExecutor(new CommandBlexBot(specListener));
        
        // respectate
        // new BukkitRunnable() {   
        //     @Override
        //     public void run() {

        //         JavaPlugin plugin = JavaPlugin.getProvidingPlugin(App.class);
        //         plugin.getLogger().info("magic!");
        //         Player p = Bukkit.getPlayerExact("RealBaconEater");
        //         Entity t = p.getSpectatorTarget();
        //         if (p.getGameMode() == GameMode.SPECTATOR && t != null){
        //             p.setSpectatorTarget(null);
        //             // p.teleport(t, PlayerTeleportEvent.TeleportCause.PLUGIN);
        //             p.setSpectatorTarget(t);
                    
        //             plugin.getLogger().info("magic done!");
        //         }
        //     }
        // }.runTaskTimer(JavaPlugin.getProvidingPlugin(App.class), 0, 300);


        //getServer().getPluginManager().registerEvents(new KitListener(), this);
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again! Love, BlexBot!");
    }
}
