package live.hoobs.spigot.blexplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.EnderChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class SpectatorListener implements Listener { 
    
    private Player blexBot = null;
    private Player spectator = null;

    JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SpectatorListener.class);
 
    public SpectatorListener(){

    }

    public void setSpectator(Player p){
        spectator = p;

        blexBot.setGameMode(GameMode.SPECTATOR);
        blexBot.setSpectatorTarget(spectator);
    }

    private boolean isSpectator(Player p){

        if (blexBot == null || !blexBot.isOnline()){
            return false;
        }

        if (p == blexBot){
            return false;
        }

        if (p != blexBot.getSpectatorTarget()){
            return false;
        }

        return true;
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (player.getName().toString().equals("BlexBot")){
            plugin.getLogger().info("BlexBot is back!");
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.chat("Hey Guys!  I'm Back!");              
                }
            }, 20);
            blexBot = player;
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if (player.getName().toString().equals("BlexBot")){
            plugin.getLogger().info("BlexBot left!");
            blexBot = null;
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void InvOpen(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();
        
        if (!isSpectator(p)){
            return;
        }

        InventoryType type = e.getInventory().getType();
        plugin.getLogger().info("Spectator (" + p.getDisplayName().toString() + ") opened " + type.toString());

        blexBot.getInventory().setContents(p.getInventory().getContents());

        if (type == InventoryType.WORKBENCH) {
            return;
        }

        if (type == InventoryType.ENDER_CHEST) {
            Inventory i = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Ender Chest");
            i.setStorageContents(e.getInventory().getContents());
            blexBot.openInventory(i);
            return;
        }

        blexBot.openInventory(e.getInventory());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        
        if (!isSpectator(p)){
            return;
        }

        Inventory inv = e.getInventory();
        if (inv.getType() == InventoryType.ENDER_CHEST) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Inventory i = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Ender Chest");
                    i.setStorageContents(e.getInventory().getContents());
                    blexBot.openInventory(i);       
                }
            }, 1);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void InvClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();

        if (!isSpectator(p)){
            return;
        }

        blexBot.closeInventory();
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = (Player) e.getPlayer();

        if (!isSpectator(p)){
            return;
        }

        if (blexBot.getSpectatorTarget() == null){
            blexBot.closeInventory();
        }
    }
}
