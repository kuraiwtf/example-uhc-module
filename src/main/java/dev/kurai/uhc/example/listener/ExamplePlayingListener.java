package dev.kurai.uhc.example.listener;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.event.RoleAttributeEvent;
import dev.kurai.uhc.module.power.AbstractPower;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ExamplePlayingListener implements Listener {

  private final ExampleModule module;
  private final UltraHardcoreAPI ultraHardcore;
  private final EventService eventService;

  public ExamplePlayingListener(final ExampleModule module) {
    this.module = module;
    this.eventService = (this.ultraHardcore = module.getUltraHardcore()).eventService();
  }

  @EventHandler
  public void onRoleAttribute(final RoleAttributeEvent event) {
    final var profile = event.getProfile();
    final var role = event.getRole();

    if (role instanceof final Listener listener) {
      this.eventService.registerListener(listener);
    }

    for (final AbstractPower power : role.getPowers()) {
      profile.registerPower(power, true);
    }

    for (final PotionEffect effect : role.permanentEffects()) {
      profile.addPotionEffect(effect);
    }
  }
}
