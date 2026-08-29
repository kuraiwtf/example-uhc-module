package dev.kurai.uhc.example.menu;

import static org.bukkit.Material.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.camp.defaults.*;
import dev.kurai.uhc.example.menu.composition.CompositionEditMenu;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Collection;
import java.util.function.Predicate;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ExampleConfigurationMenu extends Menu {

  private static final Collection<Class<? extends ExampleCamp>> SOLITARIES =
      Lists.newArrayList(SolitaryCamp.class, DuoCamp.class);

  private final ExampleModule module;

  public ExampleConfigurationMenu(final Player player, final ExampleModule module) {
    super(module.getName(), MenuSize.SIX, player);
    this.module = module;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));

    front.set(21, new CompositionEditButton(this.module, ProtagonistCamp.class));
    front.set(23, new CompositionEditButton(this.module, PrimaryAntagonistCamp.class));

    front.set(30, new CompositionEditButton(this.module, SecondaryAntagonistCamp.class));
    front.set(
        32,
        new CompositionEditButton(
            this.module,
            SolitaryCamp.class,
            roleData -> SOLITARIES.contains(roleData.getCampClass())));
  }

  private static final class CompositionEditButton extends Button {

    private final ExampleModule module;
    private final ExampleCampData campData;
    private final Predicate<ExampleRoleData> filter;

    public CompositionEditButton(
        final ExampleModule module, final Class<? extends ExampleCamp> campClass) {
      this.module = module;
      this.campData = module.getCampRegistrar().getTeam(campClass).orElseThrow();
      this.filter = roleData -> roleData.getCampClass() == campClass;
    }

    public CompositionEditButton(
        final ExampleModule module,
        final Class<? extends ExampleCamp> campClass,
        final Predicate<ExampleRoleData> filter) {
      this.module = module;
      this.campData = module.getCampRegistrar().getTeam(campClass).orElseThrow();
      this.filter = filter;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(INK_SACK)
          .amount(this.module.getEnabledRoles().stream().filter(this.filter).toList().size())
          .name(
              "%s%s%s"
                  .formatted(this.campData.getChatColor(), ChatColor.BOLD, this.campData.getName()))
          .data(this.campData.getDyeColor().getDyeData())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var compositionEditMenu =
          new CompositionEditMenu(
              menu.getPlayer(), this.module, this.filter, this.campData.getDyeColor());
      compositionEditMenu.setPreviousMenu(menu);
      compositionEditMenu.open();
    }
  }
}
