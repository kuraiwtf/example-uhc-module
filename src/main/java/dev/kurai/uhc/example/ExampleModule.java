package dev.kurai.uhc.example;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Sets;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.example.camp.ExampleCamp;
import dev.kurai.uhc.example.camp.ExampleCampData;
import dev.kurai.uhc.example.camp.ExampleCampRegistrar;
import dev.kurai.uhc.example.description.DescriptionService;
import dev.kurai.uhc.example.menu.ExampleConfigurationMenu;
import dev.kurai.uhc.example.role.ExampleRole;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.example.role.ExampleRoleRegistrar;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.camp.module.CampModule;
import dev.kurai.uhc.module.component.ModuleDocumentationComponent;
import dev.kurai.uhc.module.component.ModuleShortNameComponent;
import dev.kurai.uhc.module.role.module.RoleModule;
import dev.kurai.uhc.util.CC;
import java.util.Set;
import net.j4c0b3y.api.menu.Menu;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ExampleModule extends AbstractModule
    implements CampModule<ExampleCamp, ExampleCampData, ExampleCampRegistrar>,
        RoleModule<ExampleRole, ExampleRoleData, ExampleRoleRegistrar> {

  private static final Component SEPARATOR =
      text().append(text(CC.BAR, DARK_GRAY)).appendSpace().build();

  public static final String PREFIX = "&e&lEX&7 &l" + CC.BAR + "&f";

  private final Plugin plugin;

  private final ExampleCampRegistrar campRegistrar;
  private final ExampleRoleRegistrar roleRegistrar;

  private final DescriptionService descriptionService;

  private final Set<ExampleRoleData> enabledRoles;

  public ExampleModule(final UltraHardcoreAPI ultraHardcore, final Plugin plugin) {
    super("example", "Exemple UHC", "ex", ultraHardcore);
    this.plugin = plugin;

    this.enabledRoles = Sets.newConcurrentHashSet();

    this.campRegistrar = new ExampleCampRegistrar();
    this.roleRegistrar = new ExampleRoleRegistrar(this);

    this.descriptionService = new DescriptionService(plugin, this);

    this.addComponents(
        new ModuleDocumentationComponent("https://kurai.gitbook.io"),
        new ModuleShortNameComponent("EX UHC"));
  }

  @Override
  public Menu provideModuleMenu(final Player player) {
    return new ExampleConfigurationMenu(player, this);
  }

  @Override
  public ItemStack provideModuleIcon(final Player player) {
    return new ItemStack(Material.INK_SACK, 1, DyeColor.YELLOW.getDyeData());
  }

  public Plugin plugin() {
    return this.plugin;
  }

  public String documentation() {
    final ModuleDocumentationComponent component =
        this.getComponent(ModuleDocumentationComponent.class);
    if (component == null) {
      return "";
    }

    return component.documentation();
  }

  public DescriptionService getDescriptionService() {
    return this.descriptionService;
  }

  @Override
  public ExampleCampRegistrar getCampRegistrar() {
    return this.campRegistrar;
  }

  @Override
  public ExampleRoleRegistrar getRoleRegistrar() {
    return this.roleRegistrar;
  }

  public Set<ExampleRoleData> getEnabledRoles() {
    return this.enabledRoles;
  }
}
