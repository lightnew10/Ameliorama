package fr.lightnew.ameliorama.listeners;

import fr.lightnew.ameliorama.Ameliorama;
import fr.lightnew.ameliorama.entities.PlayerManager;
import fr.lightnew.ameliorama.entities.WellOfSoul;
import fr.lightnew.ameliorama.game.GameSettings;
import fr.lightnew.ameliorama.game.GameState;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class WorldManagerEvents implements Listener {

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();
        ItemMeta meta = itemStack.getItemMeta();
        Block block = event.getBlockPlaced();

        if (!GameState.getCurrentGameState().equals(GameState.GAME)) {
            if (player.isOp()) {
                if (meta.getPersistentDataContainer().has(new NamespacedKey(Ameliorama.instance, "wellofsoul"), PersistentDataType.BOOLEAN)) {
                    Ameliorama.gameSettings.getWellOfSoulList().add(new WellOfSoul(block.getLocation(), 30));
                    player.sendMessage(ChatColor.YELLOW + "Well of soul posed !");
                }
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!GameState.getCurrentGameState().equals(GameState.GAME)) {
            if (player.isOp()) {
                for (int i = 0; i < Ameliorama.gameSettings.getWellOfSoulList().size(); i++) {
                    WellOfSoul soul = Ameliorama.gameSettings.getWellOfSoulList().get(i);
                    if (soul.getLocation().equals(block.getLocation())) {
                        Ameliorama.gameSettings.getWellOfSoulList().remove(i);
                        player.sendMessage(ChatColor.RED + "Well of soul removed !");
                    }
                }
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void portal(EntityPortalEnterEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getLocation().getBlock().getType().name().equalsIgnoreCase(Material.NETHER_PORTAL.name()))
                if (Ameliorama.spawnSafeZone != null) {
                    player.teleport(Ameliorama.spawnSafeZone, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    //regen life in safe zone
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 9, true, false, false));
                }
            if (event.getLocation().getBlock().getType().name().equalsIgnoreCase(Material.END_PORTAL.name()) && GameState.getCurrentGameState().equals(GameState.GAME))
                if (Ameliorama.spawnGameZone != null)
                    player.teleport(Ameliorama.spawnGameZone, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    public void deathEntity(EntityDeathEvent event) {
        event.getDrops().clear();
        if (event.getEntity() instanceof Zombie) {
            Player killer = event.getEntity().getKiller();
            if (killer != null)
                Ameliorama.gameSettings.getPlayerManagerList().get(killer.getUniqueId()).giveResourcesKillZombie();
            Location location = event.getEntity().getLocation();

            Optional<WellOfSoul> optionalWellOfSoul = Ameliorama.gameSettings.getWellOfSoulList().stream().filter(wellOfSoul -> wellOfSoul.getLocation().distance(location) <= wellOfSoul.getNearBy()).findFirst();
            if (optionalWellOfSoul.isPresent()) {
                WellOfSoul soul = optionalWellOfSoul.get();
                soul.setTotalInspiredSoul(soul.getTotalInspiredSoul() + 1);
                Ameliorama.workerParticle.put(location, soul.getLocation().clone());
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Zombie) {
            event.getEntity().setCustomName(healthBar((int)((Zombie) event.getEntity()).getHealth(), (int) ((Zombie) event.getEntity()).getAttribute(Attribute. GENERIC_MAX_HEALTH).getValue(), 10));
            event.getEntity().setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Zombie) {
            event.getEntity().setCustomName(healthBar((int)((Zombie) event.getEntity()).getHealth(), (int) ((Zombie) event.getEntity()).getAttribute(Attribute. GENERIC_MAX_HEALTH).getValue(), 10));
            event.getEntity().setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageEvent event) {
        if (event.getEntity() instanceof Zombie && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onZombieTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof Zombie zombie && event.getTarget() instanceof Player player) {
        }
    }


    private String healthBar(int health, int maxHealth, int maxLine) {
        String placeholder = "§m ";
        StringBuilder healthBar = new StringBuilder();
        int segments = (int) Math.round(((double) health / maxHealth) * maxLine);

        Bukkit.broadcastMessage(String.valueOf(health));
        for (int i = 0; i < maxLine; i++)
            if (i < segments)
                healthBar.append(ChatColor.GREEN + placeholder);
            else
                healthBar.append(ChatColor.RESET + placeholder);

        return healthBar.toString();
    }

    public void changeHealth(CraftLivingEntity entity, double maxHealth) {
        entity.getAttribute(Attribute. GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        entity.setHealth(maxHealth);
    }
}
