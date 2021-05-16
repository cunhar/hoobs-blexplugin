package live.hoobs.spigot.blexplugin.listeners;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;


public class KitListener implements Listener {
    final World world = Bukkit.getServer().getWorld("world");

    final Location resetLocation = new Location( world, 176, 68, 82);

    final Location armorLocation = new Location( world, 178, 36, 55);     
    final ItemStack[] armorItems = {
        new ItemStack(Material.LEATHER_HELMET),
        new ItemStack(Material.LEATHER_CHESTPLATE),
        new ItemStack(Material.LEATHER_LEGGINGS),
        new ItemStack(Material.LEATHER_BOOTS)
    };

    final Location claimLocation = new Location( world, 157, 23, 77);     
    final ItemStack[] claimItems = {
        new ItemStack(Material.STICK),
        new ItemStack(Material.GOLDEN_SHOVEL)
    };
    
    final Location toolsLocation = new Location( world, 162, 8, 44);   
    final ItemStack[] toolsItems = {
        new ItemStack(Material.WOODEN_AXE),
        new ItemStack(Material.WOODEN_HOE),
        new ItemStack(Material.WOODEN_PICKAXE),
        new ItemStack(Material.WOODEN_SHOVEL),
        new ItemStack(Material.WOODEN_SWORD)
    };

    final Location foodLocation = new Location( world, 171, 3, 54);   
    final ItemStack[] foodItems = {
        new ItemStack(Material.BAKED_POTATO, 16)
    };

    final Location torchesLocation = new Location( world, 172, 28, 76);   
    final ItemStack[] torchesItems = {
        new ItemStack(Material.TORCH, 16)
    };

    final Location saplingsLocation = new Location( world, 150, 49, 58);   
    final ItemStack[] saplingsItems = {
        new ItemStack(Material.OAK_SAPLING, 16)
    };

    public KitListener(){
        // color armor
        Color armorColor = Color.fromRGB(13500186);
        LeatherArmorMeta armorMeta  = (LeatherArmorMeta) armorItems[0].getItemMeta();
        armorMeta.setColor(armorColor);
        Arrays.stream(armorItems).forEach(ar -> ar.setItemMeta(armorMeta));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Location fromLocation = event.getFrom();
        final Location location = event.getTo();
        final Player player = event.getPlayer();
        
        //only survival
        if(player.getGameMode() != GameMode.SURVIVAL){
            return;
        }
        
        final int intX = (int)fromLocation.getX();
        final int intY = (int)fromLocation.getY();
        final int intZ = (int)fromLocation.getZ();
        final int intToX = (int)location.getX();
        final int intToY = (int)location.getY();
        final int intToZ = (int)location.getZ();

        // not enough
        if (intX == intToX && intY == intToY && intZ == intToZ){
            return;
        }

        //nowhere near
        if (intX > 179 || intToX < 149 || intY > 69 || intToY < 2 || intZ > 83 || intToZ < 43) {
            return;
        }
        
        final Set<String> tags = player.getScoreboardTags();

        // armor
        checkKit(player, location, armorLocation, "kit-armor", tags, armorItems);

        // claims tools
        checkKit(player, location, claimLocation, "kit-claim", tags, claimItems);

        // wood tools
        checkKit(player, location, toolsLocation, "kit-tools", tags, toolsItems);

        // food
        checkKit(player, location, foodLocation, "kit-food", tags, foodItems);

        // torches
        checkKit(player, location, torchesLocation, "kit-torches", tags, torchesItems);

        // saplings
        checkKit(player, location, saplingsLocation, "kit-saplings", tags, saplingsItems);

        // reset kit
        resetKit(player, location, resetLocation, "kit-", tags);
    }

    public void checkKit(Player player, Location playerLocation, Location location, String tag, Set<String> tags, ItemStack[] items){
        if (playerLocation.distanceSquared(location) < 2 && !tags.contains(tag)) {
            player.addScoreboardTag(tag);
            player.getInventory().addItem(items);
        }
    }

    public void resetKit(Player player, Location playerLocation, Location location, String tagSeed, Set<String> tags ) {
        if (tags != null && playerLocation.distanceSquared(location) < 2){
            tags.removeIf(tag -> (tag.startsWith(tagSeed)));
        }
    }
}
