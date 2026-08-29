package dev.kurai.uhc.example.role.defaults.solitary.test.power;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.impl.RightClickItemPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.util.CC;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class AirBumpPower extends RightClickItemPower {

  public AirBumpPower(final UUID owner, final UltraHardcoreAPI ultraHardcore) {
    super("air_bump", "Air Bump", owner, ultraHardcore);
    this.addRestriction(new CooldownPowerRestriction(ultraHardcore.plugin(), 30));
  }

  @Override
  public List<String> lore() {
    return Lists.newArrayList(
        "§6" + CC.SQUARE + "§f Utilisation: §eClic-Droit",
        "§6" + CC.SQUARE + "§f Délai: §e30 secondes",
        "",
        "§e" + CC.BAR_2 + "§f Description",
        "Vous envoie à§b 15 blocs§f en hauteur.");
  }

  @Override
  public ItemStack provideIcon(final Player player) {
    return new ItemStack(Material.NETHER_STAR);
  }

  @Override
  public boolean onUse(final Player player) {
    player.setVelocity(new Vector(0, 5, 0));
    return true;
  }
}
