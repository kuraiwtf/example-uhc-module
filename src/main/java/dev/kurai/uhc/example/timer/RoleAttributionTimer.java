package dev.kurai.uhc.example.timer;

import static com.google.common.collect.Lists.newArrayList;
import static dev.kurai.uhc.util.CC.*;
import static java.util.Collections.shuffle;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.component.CampComponent;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.example.event.RoleAttributeEvent;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DeadComponent;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

@Identifier("roles")
@Name("Rôles")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 25 * 60)
public final class RoleAttributionTimer extends AbstractTimer {

  private final ExampleModule module;
  private final EventService eventService;

  public RoleAttributionTimer(final ExampleModule module) {
    this.module = module;
    this.eventService = module.getUltraHardcore().eventService();
  }

  @Override
  public void onEnd() {
    final var roles = Lists.newArrayList(this.module.getEnabledRoles());
    final var profiles =
        Lists.newArrayList(
            this.module
                .getUltraHardcore()
                .profileService()
                .getProfiles(
                    profile ->
                        !profile.hasComponent(SpectatorComponent.class)
                            && !profile.hasComponent(DeadComponent.class)));

    for (final var profile : profiles) {
      if (profile.getPlayer() != null) {
        continue;
      }

      this.module.getUltraHardcore().gameService().deathService().eliminate(profile, null, true);
      profiles.remove(profile);
    }

    shuffle(roles);
    shuffle(profiles);

    if (roles.size() != profiles.size()) {
      this.module
          .getUltraHardcore()
          .gameService()
          .sendMessage(
              prefix()
                  .append(
                      text(
                          "Impossible d'attribuer les rôles, cette dernière vient d'être décalée de "))
                  .append(text("15 secondes", AQUA))
                  .append(text('.'))
                  .build());
      this.setTimeLeft(15);
      this.start(this.module.plugin());
      return;
    }

    this.module
        .getUltraHardcore()
        .gameService()
        .sendMessage(prefix().append(text("Attribution des rôles...")).build());

    this.handleRoleAttribution(newArrayList(profiles), newArrayList(roles));
  }

  private void handleRoleAttribution(
      final List<Profile> profiles, final List<ExampleRoleData> roles) {
    for (final var profile : profiles) {
      final RoleComponent component = profile.getComponent(RoleComponent.class);
      if (component != null) {
        continue;
      }

      this.attributeRole(profile, roles.removeFirst());
    }
  }

  private void attributeRole(final Profile profile, final ExampleRoleData roleData) {
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
    final ExampleCamp camp;

    try {
      camp = roleData.getCampClass().getConstructor(ExampleModule.class).newInstance(this.module);
    } catch (final InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    profile.addComponent(new CampComponent(camp));
    this.eventService.dispatchEvent(new RoleAttributeEvent(profile, role));

    final ExampleCampData campData =
        this.module.getCampRegistrar().getTeam(camp.getClass()).orElseThrow();

    Bukkit.getScheduler()
        .runTaskLater(
            this.module.plugin(),
            () -> {
              profile.playSound(
                  Sound.sound(
                      Key.key("example_uhc." + roleData.getIdentifier()),
                      Sound.Source.PLAYER,
                      1,
                      1));

              profile.showTitle(
                  Title.title(
                      text()
                          .append(text('»', DARK_GRAY, BOLD))
                          .appendSpace()
                          .append(text(role.getName(), GOLD, BOLD))
                          .appendSpace()
                          .append(text('«', DARK_GRAY, BOLD))
                          .build(),
                      text(campData.getName(), campData.getTextColor()),
                      Title.Times.times(
                          java.time.Duration.ZERO,
                          java.time.Duration.ofSeconds(3),
                          java.time.Duration.ZERO)));

              role.onDistribute();
              profile.sendMessage(this.module.getDescriptionService().provideDescription(role));
              role.sendAdditionalInformations();
              role.onDescription();
            },
            10);
  }
}
