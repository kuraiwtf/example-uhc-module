package dev.kurai.uhc.example.listener;

import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.example.ExampleModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ExampleWaitingListener implements Listener {

  private final ExampleModule module;

  public ExampleWaitingListener(final ExampleModule module) {
    this.module = module;
  }

  @EventHandler
  public void onGameStart(final GameStartEvent event) {
    final var eventService = this.module.getUltraHardcore().eventService();
    eventService.unregisterListener(this);
    eventService.registerListener(new ExamplePlayingListener(this.module));
  }
}
