package dev.kurai.uhc.example.command.argument;

import static dev.kurai.uhc.util.CC.colorize;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.example.ExampleModule;
import java.util.Collection;

import dev.kurai.uhc.example.role.ExampleRoleData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class RoleDataArgumentResolver
    implements ArgumentResolver<@Nullable ExampleRoleData> {

  private final ExampleModule module;

  public RoleDataArgumentResolver(final ExampleModule module) {
    this.module = module;
  }

  @Override
  public @Nullable ExampleRoleData resolve(final CommandSender sender, final String argument) {
    final var found =
        this.module.getRoleRegistrar().getRepository().stream()
            .filter(
                data ->
                    data.getIdentifier().equalsIgnoreCase(argument)
                        || data.getName().equalsIgnoreCase(argument))
            .findFirst()
            .orElse(null);

    if (found == null && sender instanceof final Player player) {
      player.sendMessage(
          colorize(ExampleModule.PREFIX + " Le rôle &e" + argument + "&f est introuvable."));
    }

    return found;
  }

  @Override
  public Collection<String> complete(final CommandSender sender, final String argument) {
    return this.module.getRoleRegistrar().getRepository().stream()
        .map(ExampleRoleData::getIdentifier)
        .filter(id -> id.startsWith(argument.toLowerCase()))
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
  }
}
