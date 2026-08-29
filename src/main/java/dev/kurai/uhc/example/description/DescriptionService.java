package dev.kurai.uhc.example.description;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.plugin.Plugin;

public final class DescriptionService {

  private final Plugin plugin;
  private final ExampleModule module;

  public DescriptionService(final Plugin plugin, final ExampleModule module) {
    this.plugin = plugin;
    this.module = module;
  }

  public Component provideDescription(final ExampleRole role) {
    final ExampleRoleData roleData = role.getRoleData();
    final ExampleCampData campData =
        this.module.getCampRegistrar().getTeam(roleData.getCampClass()).orElseThrow();
    final var msg = text();
    msg.append(CC.line(GOLD, YELLOW))
        .appendNewline()
        .appendNewline()
        .append(text(CC.center("§e§lEXEMPLE UHC")))
        .appendNewline();
    msg.appendSpace()
        .appendSpace()
        .append(text(CC.SQUARE, GOLD))
        .appendSpace()
        .append(text("Rôle: "))
        .append(text(roleData.getName(), campData.getTextColor()))
        .appendSpace()
        .append(
            text()
                .append(text('[', DARK_GRAY))
                .append(text(CC.BURGER, GOLD))
                .appendSpace()
                .append(text("Redirection", YELLOW))
                .append(text(']', DARK_GRAY))
                .clickEvent(
                    ClickEvent.openUrl(
                        this.module.documentation()
                            + "/roles/"
                            + campData.getDocumentReference()
                            + "/"
                            + roleData.getIdentifier()))
                .build())
        .appendNewline()
        .appendSpace()
        .appendSpace()
        .append(text(CC.SQUARE, GOLD))
        .appendSpace()
        .append(text("Objectif: "))
        .append(text(campData.getObjective()))
        .appendNewline()
        .appendNewline();

    final var passiveAbilities = role.getPassiveAbilities();
    if (!passiveAbilities.isEmpty()) {
      this.appendSectionHeader(msg, "Passifs");
      for (final PowerDescription ability : passiveAbilities) {
        this.appendPassiveAbility(msg, ability, 0);
      }
    }

    final var activeAbilities = role.getActiveAbilities();
    if (!activeAbilities.isEmpty()) {
      this.appendSectionHeader(msg, "Activables");
      for (final PowerDescription ability : activeAbilities) {
        this.appendActiveAbility(msg, ability, 0);
      }
      msg.appendNewline();
    }

    msg.append(CC.line(GOLD, YELLOW));
    return msg.build();
  }

  private void appendSectionHeader(final TextComponent.Builder msg, final String title) {
    msg.appendSpace()
        .appendSpace()
        .append(text(CC.BAR_2, GOLD))
        .appendSpace()
        .append(text(title, WHITE, TextDecoration.BOLD))
        .appendNewline();
  }

  private void appendPassiveAbility(
      final TextComponent.Builder msg, final PowerDescription ability, final int depth) {
    msg.appendSpace().appendSpace().appendSpace().appendSpace();
    for (int i = 0; i < depth * 2; i++) {
      msg.appendSpace();
    }
    msg.append(text(CC.SQUARE, GOLD))
        .appendSpace()
        .append(text(ability.name()))
        .append(text(':'))
        .appendSpace()
        .append(ability.description())
        .appendNewline()
        .appendNewline();

    for (final PowerDescription child : ability.children()) {
      this.appendPassiveAbility(msg, child, depth + 1);
    }
  }

  private void appendActiveAbility(
      final TextComponent.Builder msg, final PowerDescription ability, final int depth) {
    msg.appendSpace().appendSpace().appendSpace().appendSpace();
    for (int i = 0; i < depth * 2; i++) {
      msg.appendSpace();
    }
    msg.append(
            text()
                .append(text('[', DARK_GRAY))
                .append(text(CC.BURGER, YELLOW))
                .appendSpace()
                .append(text(ability.name(), GOLD))
                .append(text(']', DARK_GRAY))
                .hoverEvent(showText(ability.description()))
                .build())
        .appendNewline();

    for (final PowerDescription child : ability.children()) {
      this.appendActiveAbility(msg, child, depth + 1);
    }
  }
}
