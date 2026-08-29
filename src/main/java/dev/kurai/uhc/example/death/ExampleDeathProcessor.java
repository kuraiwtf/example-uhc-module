package dev.kurai.uhc.example.death;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.example.event.RoleDeathEvent;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.game.death.DeathContext;
import dev.kurai.uhc.game.death.DeathProcessor;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ExampleDeathProcessor implements DeathProcessor {

  private final ExampleModule module;
  private final UltraHardcoreAPI ultraHardcore;

  public ExampleDeathProcessor(final ExampleModule module) {
    this.module = module;
    this.ultraHardcore = module.getUltraHardcore();
  }

  @Override
  public void processDeath(final DeathContext context) {
    final PlayerDeathEvent event = context.event();
    final Player player = event.getEntity();
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    final Location deathLocation = player.getLocation().clone();
    final var killer = player.getKiller();

    player.spigot().respawn();
    player.setGameMode(GameMode.ADVENTURE);
    player.teleport(new Location(this.ultraHardcore.worldService().getWorld(), 0.5, 200.5, 0.5));

    Bukkit.getScheduler()
        .runTaskLater(
            this.module.plugin(),
            () -> {
              if (killer != null) {
                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                killer.giveExpLevels(3);
              }

              final RoleComponent component = profile.getComponent(RoleComponent.class);
              if (component == null) {
                return;
              }

              final ExampleRole role = component.role();
              Bukkit.getPluginManager().callEvent(new RoleDeathEvent(profile, role, killer));

              player.setGameMode(GameMode.SPECTATOR);
              player.teleport(deathLocation);

              this.ultraHardcore
                  .gameService()
                  .deathService()
                  .eliminate(
                      profile,
                      (killer == null
                          ? null
                          : this.ultraHardcore.profileService().getOrCreateProfile(killer)),
                      false);
            },
            10 * 20L);
  }
}
