package dev.kurai.uhc.example.command;

import static dev.kurai.uhc.example.ExampleModule.PREFIX;
import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.camp.defaults.*;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.example.event.RoleAttributeEvent;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.profile.Profile;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@Command(@CommandMeta(name = "ex"))
@NullMarked
public final class ExampleCommand {

  private final ExampleModule module;
  private final UltraHardcoreAPI ultraHardcore;

  public ExampleCommand(final ExampleModule module) {
    this.module = module;
    this.ultraHardcore = module.getUltraHardcore();
  }

  @SubCommand(@CommandMeta(name = "me", aliases = "role", description = "Voir votre rôle"))
  public void role(final Player player) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    final RoleComponent component = profile.getComponent(RoleComponent.class);
    if (component == null) {
      profile.sendPrefixedMessage("Vous n'avez pas de rôle.");
      return;
    }

    final ExampleRole role = component.role();
    if (role == null) {
      profile.sendPrefixedMessage("Vous n'avez pas de rôle.");
      return;
    }

    profile.sendMessage(this.module.getDescriptionService().provideDescription(role));
    role.onDescription();
    role.sendAdditionalInformations();
  }

  @SubCommand(@CommandMeta(name = "roles", description = "Voir les rôles en vie"))
  public void roles(final Player player) {
    final var gameService = this.ultraHardcore.gameService();
    if (gameService.startTime() <= 0) {
      this.compo(player);
      return;
    }

    final var timer = gameService.timerService().getTimer("roles").orElse(null);
    if (timer == null) {
      this.compo(player);
      return;
    }

    if (timer.isRunning()) {
      this.compo(player);
      return;
    }

    final var playing = this.ultraHardcore.profileService().getPlayingProfiles();
    final var profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    profile.sendMessage(
        text()
            .append(
                text()
                    .append(line(GOLD, YELLOW))
                    .appendNewline()
                    .appendNewline()
                    .append(
                        text()
                            .appendSpace()
                            .append(text(BIG_SQUARE, GOLD))
                            .appendSpace()
                            .append(text("Rôles en vie").decorate(BOLD))
                            .appendSpace()
                            .append(text('(', DARK_GRAY))
                            .append(text(playing.size(), GOLD, BOLD))
                            .append(text(')', DARK_GRAY))
                            .build())
                    .appendNewline()
                    .appendNewline())
            .append(this.appendAliveRoles(ProtagonistCamp.class))
            .append(this.appendAliveRoles(PrimaryAntagonistCamp.class))
            .append(this.appendAliveRoles(SecondaryAntagonistCamp.class))
            .append(this.appendAliveRoles(SolitaryCamp.class))
            .append(this.appendAliveRoles(DuoCamp.class))
            .append(line(GOLD, YELLOW))
            .build());
  }

  @SubCommand(
      @CommandMeta(name = "meta", aliases = "setrole", description = "Définir le rôle d'un joueur"))
  public void meta(
      final Player player,
      final @Argument(name = "rôle") ExampleRoleData roleData,
      final @Argument(name = "joueur", defaultValue = "self") Player target) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(target.getUniqueId());
    final ExampleRole role;

    try {
      role =
          roleData
              .getId()
              .getConstructor(UUID.class, ExampleModule.class)
              .newInstance(profile.getId(), this.module);
    } catch (final InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    profile.addComponent(new RoleComponent(role));
    role.onDistribute();
    profile.sendMessage(this.module.getDescriptionService().provideDescription(role));
    role.onDescription();
    role.sendAdditionalInformations();

    this.ultraHardcore.eventService().dispatchEvent(new RoleAttributeEvent(profile, role));

    player.sendMessage(
        prefix(
            "&6%s&r possède désormais le rôle&6 %s&r."
                .formatted(target.getName(), roleData.getName()),
            PREFIX));
  }

  @SubCommand(@CommandMeta(name = "compo", description = "Voir la composition de la partie"))
  public void compo(final Player player) {
    final var profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    profile.sendMessage(
        text()
            .append(line(GOLD, YELLOW))
            .appendNewline()
            .appendNewline()
            .append(
                text()
                    .appendSpace()
                    .append(text(BIG_SQUARE, GOLD))
                    .appendSpace()
                    .append(text("Composition de la partie").decorate(BOLD))
                    .appendSpace()
                    .append(text('(', DARK_GRAY))
                    .append(text(this.module.getEnabledRoles().size(), GOLD, BOLD))
                    .append(text(')', DARK_GRAY))
                    .build())
            .appendNewline()
            .appendNewline()
            .append(this.appendComposition(ProtagonistCamp.class))
            .append(this.appendComposition(PrimaryAntagonistCamp.class))
            .append(this.appendComposition(SecondaryAntagonistCamp.class))
            .append(this.appendComposition(SolitaryCamp.class))
            .append(this.appendComposition(DuoCamp.class))
            .append(line(GOLD, YELLOW))
            .build());
  }

  private Component appendComposition(final Class<? extends ExampleCamp> campClass) {
    final var campData = this.module.getCampRegistrar().getTeam(campClass).orElse(null);
    if (campData == null) {
      return text().append(text(campClass.getSimpleName() + " n'existe pas")).build();
    }

    final var roles =
        this.module.getEnabledRoles().stream()
            .filter(roleData -> roleData.getCampClass().equals(campClass))
            .sorted(Comparator.comparing(ExampleRoleData::getName))
            .toList();

    if (roles.isEmpty()) {
      return empty();
    }

    final var message =
        text()
            .appendSpace()
            .append(text(BAR, campData.getTextColor()))
            .appendSpace()
            .append(text(campData.getName()).decorate(BOLD))
            .appendSpace()
            .append(text('(', DARK_GRAY))
            .append(text(roles.size(), campData.getTextColor(), BOLD))
            .append(text(')', DARK_GRAY))
            .appendNewline();

    for (final var data : roles) {
      message.append(
          text()
              .appendSpace()
              .appendSpace()
              .append(text(SQUARE, campData.getTextColor()))
              .appendSpace()
              .append(text(data.getName()))
              .appendNewline()
              .build());
    }

    return message.appendNewline().build();
  }

  private Component appendAliveRoles(final Class<? extends ExampleCamp> campClass) {
    final var campData = this.module.getCampRegistrar().getTeam(campClass).orElse(null);
    if (campData == null) {
      return empty();
    }

    final var aliveProfiles =
        this.ultraHardcore
            .profileService()
            .getProfiles(target -> target.getState().getId().equals("playing"))
            .stream()
            .filter(
                profile -> {
                  final var component = profile.getComponent(RoleComponent.class);
                  if (component == null) {
                    return false;
                  }

                  final var role = component.role();
                  if (role == null) {
                    return false;
                  }

                  return this.module
                      .getRoleRegistrar()
                      .getRoleData(role.getClass())
                      .map(data -> data.getCampClass().equals(campClass))
                      .orElse(false);
                })
            .toList();

    if (aliveProfiles.isEmpty()) {
      return empty();
    }

    final var message =
        text()
            .appendSpace()
            .append(text(BAR, campData.getTextColor()))
            .appendSpace()
            .append(text(campData.getName()).decorate(BOLD))
            .appendSpace()
            .append(text('(', DARK_GRAY))
            .append(text(aliveProfiles.size(), campData.getTextColor(), BOLD))
            .append(text(')', DARK_GRAY))
            .appendNewline();

    for (final var profile : aliveProfiles) {
      final var component = profile.getComponent(RoleComponent.class);
      if (component == null) {
        continue;
      }

      final var role = component.role();
      if (role == null) {
        continue;
      }
      this.module
          .getRoleRegistrar()
          .getRoleData(role.getClass())
          .ifPresent(
              roleData ->
                  message.append(
                      text()
                          .appendSpace()
                          .appendSpace()
                          .append(text(SQUARE, campData.getTextColor()))
                          .appendSpace()
                          .append(text(roleData.getName()))
                          .appendNewline()
                          .build()));
    }

    return message.appendNewline().build();
  }
}
