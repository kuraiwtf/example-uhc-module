package dev.kurai.uhc.example.death;

import static dev.kurai.uhc.util.CC.line;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.game.death.DeathAnnounce;
import dev.kurai.uhc.profile.Profile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.Nullable;

public final class ExampleDeathAnnounce implements DeathAnnounce {

  private final ExampleModule module;

  public ExampleDeathAnnounce(final ExampleModule module) {
    this.module = module;
  }

  @Override
  public Component provideDeathMessage(
      final Profile profile, @Nullable final Profile killer, final boolean offline) {
    final RoleComponent component = profile.getComponent(RoleComponent.class);

    final boolean noRole = component == null || component.role() == null;
    final TextColor roleColor = noRole ? RED : this.getRoleColor(component);
    return text()
        .append(line(GOLD, YELLOW))
        .appendNewline()
        .append(
            text()
                .appendSpace()
                .append(text('»', DARK_GRAY, BOLD))
                .appendSpace()
                .append(text(profile.getName(), GOLD))
                .appendSpace()
                .append(text("est"))
                .appendSpace()
                .append(text("mort", RED))
                .append(text('.'))
                .append(text(offline ? " (déconnexion)" : "", RED, ITALIC))
                .build())
        .appendNewline()
        .append(
            text()
                .appendSpace()
                .append(text('»', DARK_GRAY, BOLD))
                .appendSpace()
                .append(text("Son rôle était: "))
                .append(text(noRole ? "Inconnu" : component.role().getName(), roleColor))
                .append(text('.'))
                .build())
        .appendNewline()
        .append(line(GOLD, YELLOW))
        .build();
  }

  private TextColor getRoleColor(final RoleComponent component) {
    final ExampleRoleData roleData = component.role().getRoleData();
    return this.module
        .getCampRegistrar()
        .getTeam(roleData.getCampClass())
        .orElseThrow()
        .getTextColor();
  }
}
