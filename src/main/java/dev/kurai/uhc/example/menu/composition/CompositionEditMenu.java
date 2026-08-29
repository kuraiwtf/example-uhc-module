package dev.kurai.uhc.example.menu.composition;

import static org.bukkit.DyeColor.*;

import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class CompositionEditMenu extends PaginatedMenu {

  private final ExampleModule module;
  private final Predicate<ExampleRoleData> filter;
  private final DyeColor color;

  public CompositionEditMenu(
      final Player player,
      final ExampleModule module,
      final Predicate<ExampleRoleData> filter,
      final DyeColor color) {
    super("Composition", MenuSize.FIVE, player);
    this.module = module;
    this.filter = filter;
    this.color = color;
  }

  @Override
  public List<Button> getEntries() {
    return this.module.getRoleRegistrar().getRepository().stream()
        .filter(this.filter)
        .sorted(
            Comparator.comparing(
                    (ExampleRoleData data) ->
                        this.module
                            .getCampRegistrar()
                            .getTeam(data.getCampClass())
                            .orElseThrow()
                            .getIdentifier())
                .thenComparing(ExampleRoleData::getIdentifier))
        .map(
            roleData ->
                new RoleButton(
                    this.module,
                    this.module.getCampRegistrar().getTeam(roleData.getCampClass()).orElseThrow(),
                    roleData))
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(this.color.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  private static final class RoleButton extends Button {

    private final ExampleModule module;
    private final ExampleCampData campData;
    private final ExampleRoleData roleData;

    private RoleButton(
        final ExampleModule module,
        final ExampleCampData campData,
        final ExampleRoleData roleData) {
      this.module = module;
      this.campData = campData;
      this.roleData = roleData;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.INK_SACK)
          .data(
              (this.module.getEnabledRoles().contains(this.roleData)
                      ? this.campData.getDyeColor()
                      : GRAY)
                  .getDyeData())
          .name(
              "%s%s%s"
                  .formatted(this.campData.getChatColor(), ChatColor.BOLD, this.roleData.getName()))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var enabledRoles = this.module.getEnabledRoles();
      if (enabledRoles.contains(this.roleData)) {
        enabledRoles.remove(this.roleData);
      } else {
        enabledRoles.add(this.roleData);
      }

      final var menu = click.getMenu();
      final var player = menu.getPlayer();

      player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
      menu.update();
    }
  }
}
