package live.hoobs.spigot.blexplugin;
import org.bukkit.plugin.java.JavaPlugin;
import live.hoobs.spigot.blexplugin.listeners.HoobaListerner;

/**
 * BlexBot
 *
 */
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, I'm BlexBot!");
        getServer().getPluginManager().registerEvents(new HoobaListerner(), this);
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again! Love, BlexBot!");
    }
}
