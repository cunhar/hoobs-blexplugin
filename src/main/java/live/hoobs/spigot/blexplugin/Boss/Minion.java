package live.hoobs.spigot.blexplugin.Boss;

import java.util.Map;
import static java.util.Map.entry;    

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Minion {
    Location location = null;
    EntityType type = null;
    String name = null;
    LivingEntity entity = null;
    ItemStack[] hands = null;
    String armor = null;

    Map<String,ItemStack[]> armors = Map.ofEntries(
        entry("none", new ItemStack[] {
            null,
            null,
            null,
            null
        }),
        entry("bros", new ItemStack[] {
            new ItemStack(Material.NETHERITE_BOOTS),
            new ItemStack(Material.NETHERITE_LEGGINGS),
            new ItemStack(Material.NETHERITE_CHESTPLATE),
            new ItemStack(Material.NETHERITE_HELMET)
        })
    );

    public Minion(World w, int x, int y, int z, EntityType t, String n, String a){
        name = n;
        location = new Location(w, x, y ,z);
        type = t;
        armor = a;
    }

    public void spawn(){
        entity = (LivingEntity) location.getWorld().spawnEntity(location, type);
        entity.setCustomName(name);
        entity.setMaxHealth(100);
        entity.setHealth(100);

        EntityEquipment eq = entity.getEquipment();
        eq.setArmorContents(armors.get(armor));
        
        // disable drops
        eq.setHelmetDropChance(0);
        eq.setChestplateDropChance(0);
        eq.setLeggingsDropChance(0);
        eq.setBootsDropChance(0);
    }
}
