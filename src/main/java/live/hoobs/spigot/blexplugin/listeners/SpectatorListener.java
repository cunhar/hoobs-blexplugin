package live.hoobs.spigot.blexplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
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

    public void setInventory(Inventory inv, Player p){
        //Set BlexBot Inventory
        blexBot.getInventory().setContents(p.getInventory().getContents());

        //set inventory for showing
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (inv.getType() != InventoryType.SHULKER_BOX || inv.getViewers().size() >= 1){
                    blexBot.openInventory(inv);
                }               
            }
        }, 1);
    }

    // @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    // public void onPlayerInteract(PlayerInteractEvent e) {
    //     Player p = e.getPlayer();

    //     if (blexBot == null || !blexBot.isOnline()){
    //         return;
    //     }

    //     if (p != blexBot.getSpectatorTarget()){
    //         return;
    //     }

    //     if (!e.hasBlock() || e.getAction() != Action.RIGHT_CLICK_BLOCK) { 
    //         return; 
    //     }

    //     Block b = e.getClickedBlock();
    //     BlockState bs = b.getState();
    //     Inventory i = null;

    //     if(bs instanceof InventoryHolder){
    //         plugin.getLogger().info("Spectator opened Container!");

    //         //Get inventory for showing
    //         InventoryHolder ih = (InventoryHolder) b.getState();
    //         i = ih.getInventory();
    //     } 

    //     if (b.getType() == Material.ENDER_CHEST) {
    //         plugin.getLogger().info("Spectator opened Ender Chest!");
    //         i = p.getEnderChest();
    //     }
    
    //     if (b.getType() == Material.CRAFTING_TABLE) {
    //         plugin.getLogger().info("Spectator opened Crafting Table");
    //         i = Bukkit.createInventory(null, InventoryType.WORKBENCH, "Crafting");
    //     }
    
    //     if (i != null){
    //         setInventory(i,p);
    //     }
    // }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (blexBot == null || !blexBot.isOnline()){
            return;
        }
        

        if (p != blexBot.getSpectatorTarget()){
            return;
        }

        if (blexBot.getOpenInventory().getType() == InventoryType.CRAFTING){
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                blexBot.getInventory().setContents(p.getInventory().getContents());
            }
        }, 1);

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void InvOpen(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();

        if (blexBot == null || !blexBot.isOnline()){
            return;
        }

        if (p != blexBot.getSpectatorTarget()){
            return;
        }

        blexBot.openInventory(e.getInventory());

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void InvClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();

        if (blexBot == null || !blexBot.isOnline()){
            return;
        }

        if (p != blexBot.getSpectatorTarget()){
            return;
        }

        blexBot.closeInventory();
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = (Player) e.getPlayer();

        if (blexBot == null || !blexBot.isOnline()){
            return;
        }

        if (p != blexBot) {
            return;
        }

        if (blexBot.getSpectatorTarget() == null)
            blexBot.closeInventory();
    }
}
