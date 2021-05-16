package live.hoobs.spigot.blexplugin.Boss;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Door {
    Material closedMaterial = null;
    World world = null;
    int x = 0;
    int y = 0;
    int z = 0;
    int dx = 0;
    int dy = 0;
    int dz = 0;
    
    public Door(World _w, int _x, int _y, int _z, int _dx, int _dy, int _dz, Material _material ){
        world = _w; 
        x = _x;
        y = _y;
        z = _z;
        dx = _dx;
        dy = _dy;
        dz = _dz;
        closedMaterial = _material;

        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Door.class);
        plugin.getLogger().info("making new door!");

        close();
    }

    public void close(){
        fill(closedMaterial);
    }

    public void open(){
        fill(Material.AIR);
    }

    private void fill(Material material) {
        for (int tX=x; tX<=x+dx; tX++){
            for (int tY=y; tY<=y+dy; tY++){
                for (int tZ=z; tZ<=z+dz; tZ++){
                    world.getBlockAt(tX, tY, tZ).setType(material);
                }
            }
        }
    }
}
