package dev.kurai.uhc.example.event;

import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class RoleAttributeEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile profile;
  private final ExampleRole role;

  public RoleAttributeEvent(final Profile profile, final ExampleRole role) {
    this.profile = profile;
    this.role = role;
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

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
