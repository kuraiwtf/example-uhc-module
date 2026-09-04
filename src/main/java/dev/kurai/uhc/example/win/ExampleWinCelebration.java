package dev.kurai.uhc.example.win;

import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.component.CampComponent;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.win.WinCelebration;
import dev.kurai.uhc.win.WinInformation;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;

@RequiredArgsConstructor
public final class ExampleWinCelebration implements WinCelebration {

  private final ExampleModule module;
  private final UltraHardcoreAPI ultraHardcore;

  @Override
  public void celebrate(final Location location, final WinInformation information) {
    final var players =
        this.ultraHardcore.profileService().getProfiles().stream()
            .filter(profile -> profile.hasComponent(RoleComponent.class))
            .toList();

    final TextComponent.Builder message = text();
    message
        .append(line(GOLD, YELLOW))
        .appendNewline()
        .appendNewline()
        .append(text(colorize(" §e" + BAR_2 + " &f&lRésumé de la partie")))
        .appendNewline();

    for (final ExampleCampData campData : this.module.getCampRegistrar().getRegistry()) {
      message
          .appendNewline()
          .append(
              text(colorize(" " + campData.getChatColor() + BAR_2 + " §l" + campData.getName())));

      final var teamPlayers =
          players.stream()
              .filter(
                  profile ->
                      profile.hasComponent(CampComponent.class)
                          && profile.hasComponent(RoleComponent.class)
                          && campData
                              .getId()
                              .isAssignableFrom(
                                  profile.getComponent(CampComponent.class).camp().getClass()))
              .toList();
      for (final var profile : teamPlayers) {
        message
            .appendNewline()
            .append(
                text(
                    colorize(
                        "  "
                            + campData.getChatColor()
                            + SQUARE
                            + " "
                            + profile.getName()
                            + " &7("
                            + campData.getChatColor()
                            + profile.kills()
                            + "&7): "
                            + campData.getChatColor()
                            + profile.getComponent(RoleComponent.class).role().getName())));
      }

      message.appendNewline();
    }

    message.appendNewline().append(line(GOLD, YELLOW));
    for (final Profile profile : this.ultraHardcore.profileService().getProfiles()) {
      profile.sendMessage(message);
    }
  }
}
