package dev.kurai.uhc.example.role;

import static dev.kurai.uhc.example.ExampleModule.PREFIX;
import static java.util.Collections.emptyList;

import com.google.common.collect.Lists;
import dev.kurai.uhc.example.ExampleModule;
import dev.kurai.uhc.example.component.RoleComponent;
import dev.kurai.uhc.example.description.PowerDescription;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.holder.PowerHolder;
import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.profile.Profile;
import java.util.*;

import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class ExampleRole extends AbstractRole<ExampleModule> implements PowerHolder {

  protected final Collection<AbstractPower> powers;
  protected final Collection<Class<? extends ExampleRole>> knownRoles;

  protected final Collection<PotionEffect> permanentEffects;

  protected final Profile profile;

  public ExampleRole(final UUID owner, final ExampleModule module, final AbstractPower... powers) {
    super(owner, module);
    this.knownRoles = Lists.newArrayList();
    this.powers = Lists.newArrayList(powers);

    this.permanentEffects = Lists.newArrayList();

    this.profile = module.getUltraHardcore().profileService().getOrCreateProfile(owner);
  }

  public Collection<PowerDescription> getPassiveAbilities() {
    return emptyList();
  }

  public Collection<PowerDescription> getActiveAbilities() {
    return emptyList();
  }

  @Contract(pure = true)
  @Override
  public final @Unmodifiable Collection<AbstractPower> getPowers() {
    return List.copyOf(this.powers);
  }

  @Override
  public final <T extends AbstractPower> @Nullable T getPower(final Class<T> clazz) {
    return this.powers.stream()
        .filter(power -> clazz.isAssignableFrom(power.getClass()))
        .map(clazz::cast)
        .findFirst()
        .orElse(null);
  }

  @Override
  public final void registerPower(final AbstractPower power) {
    this.powers.add(power);
  }

  @Override
  public final void unregisterPower(final String id) {
    this.powers.removeIf(power -> power.getId().equals(id));
  }

  public final void addPermanentEffect(final PotionEffectType effect) {
    this.addPermanentEffect(effect, 0);
  }

  public final void addPermanentEffect(final PotionEffectType effect, final int level) {
    this.permanentEffects.add(new PotionEffect(effect, Integer.MAX_VALUE, level, false, false));
  }

  public final void removePermanentEffect(final PotionEffectType type) {
    this.permanentEffects.removeIf(effect -> effect.getType().equals(type));
  }

  public final @Unmodifiable Collection<PotionEffect> permanentEffects() {
    return List.copyOf(this.permanentEffects);
  }

  public void onDistribute() {}

  public void onDescription() {}

  public void onRemove() {}

  public void sendAdditionalInformations() {
    for (final Class<? extends ExampleRole> roleClass : this.knownRoles) {
      this.module
          .getRoleRegistrar()
          .getRoleData(roleClass)
          .ifPresent(
              roleData ->
                  this.module
                      .getCampRegistrar()
                      .getTeam(roleData.getCampClass())
                      .ifPresent(
                          team ->
                              this.module
                                  .getUltraHardcore()
                                  .profileService()
                                  .getProfile(
                                      target -> {
                                        final RoleComponent component =
                                            target.getComponent(RoleComponent.class);
                                        if (component == null) {
                                          return false;
                                        }

                                        return component.role() != null
                                            && component.role().getClass().equals(roleClass);
                                      })
                                  .ifPresentOrElse(
                                      targetProfile ->
                                          this.profile.sendPrefixedMessage(
                                              "Le "
                                                  + team.getChatColor()
                                                  + roleData.getName()
                                                  + "&r de la partie est "
                                                  + team.getChatColor()
                                                  + targetProfile.getName()
                                                  + "&r.",
                                              PREFIX),
                                      () ->
                                          this.profile.sendPrefixedMessage(
                                              "Il n'y a pas de "
                                                  + team.getChatColor()
                                                  + roleData.getName()
                                                  + "&r dans la partie.",
                                              PREFIX))));
    }
  }

  public Profile profile() {
    return this.profile;
  }

  public ExampleRoleData getRoleData() {
    return this.module.getRoleRegistrar().getRoleData(this.getClass()).orElseThrow();
  }
}
