package hakasenz.herteffect;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ChangeInventory extends JavaPlugin implements Listener {
    private int previousSlot;
    @Override
    public void onEnable() {
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        Random random = new Random();

        if (args[0].equalsIgnoreCase("/changeInventory")) {
            // 检查玩家手持物品栏是否为第一格
            if (player.getInventory().getHeldItemSlot() == 0) {
                // 切换到第二格物品栏
                player.getInventory().setHeldItemSlot(2);

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                player.getInventory().setHeldItemSlot(0);
            }
            // 检查玩家手持物品栏不为第一格
            if (player.getInventory().getHeldItemSlot() != 0) {
                //储存物品栏位置
                previousSlot = player.getInventory().getHeldItemSlot();
                // 切换到第二格物品栏
                player.getInventory().setHeldItemSlot(0);
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    player.getInventory().setHeldItemSlot(previousSlot);
                }, 2); // 2个tick
            }
        }

        if (args[0].equalsIgnoreCase("/changeInventoryBuff")) {
            // 储存当前物品栏位置
            previousSlot = player.getInventory().getHeldItemSlot();

            int randomTimes = random.nextInt(9) + 1;
            int randomSlot;

            for (int i = 0; i < randomTimes+2+randomTimes*2; i++) {
                randomSlot = random.nextInt(10);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                // 切换到其他物品栏
                player.getInventory().setHeldItemSlot(randomSlot);
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    event.setCancelled(true);
                }, 2); // 2个tick

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 休眠100ms后再循环
                }
            }
            // 返回之前的物品栏
            player.getInventory().setHeldItemSlot(previousSlot);
        }
    }
}



