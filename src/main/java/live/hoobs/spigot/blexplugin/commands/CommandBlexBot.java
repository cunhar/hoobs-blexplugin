package live.hoobs.spigot.blexplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import live.hoobs.spigot.blexplugin.listeners.SpectatorListener;

public class CommandBlexBot implements CommandExecutor {
    JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CommandBlexBot.class);
    SpectatorListener specListener = null;

    public CommandBlexBot(SpectatorListener s){
        specListener = s;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //only console can do it
        if (!(sender instanceof ConsoleCommandSender)){
            plugin.getLogger().info("BlexBot command can only be run from Console!");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("blexbot")) { // If the player typed /basic then do the following...
            // do something...
            plugin.getLogger().info("BlexBot command!");
            
            //get player
            if (args.length > 0){
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    plugin.getLogger().warning("Can't find player by that name!");
                    return false;
                }

                //set spec
                specListener.setSpectator(player);
                return true;
            }
        }
        return false;
    }
}