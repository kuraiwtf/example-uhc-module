package dev.kurai.uhc.example.event;

import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public final class RoleDeathEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile profile;
  private final ExampleRole role;
  private final @Nullable Player killer;

  public RoleDeathEvent(
      final Profile profile, final ExampleRole role, final @Nullable Player killer) {
    this.profile = profile;
    this.role = role;
    this.killer = killer;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public Profile getProfile() {
    return this.profile;
  }

  public ExampleRole getRole() {
    return this.role;
  }

  public @Nullable Player getKiller() {
    return this.killer;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
