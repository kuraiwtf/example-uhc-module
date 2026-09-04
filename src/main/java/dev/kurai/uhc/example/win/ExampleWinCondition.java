package dev.kurai.uhc.example.win;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.example.component.CampComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.win.WinCondition;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public final class ExampleWinCondition implements WinCondition {

  private final UltraHardcoreAPI ultraHardcore;

  @Override
  public @Nullable Collection<Profile> validateWin() {
    final var candidates = this.ultraHardcore.profileService().getPlayingProfiles();
    candidates.removeIf(profile -> !profile.hasComponent(CampComponent.class));

    final int differentTeams =
        candidates.stream()
            .map(profile -> profile.getComponent(CampComponent.class))
            .filter(Objects::nonNull)
            .map(CampComponent::camp)
            .collect(Collectors.toSet())
            .size();

    if (differentTeams > 1) {
      return null;
    }

    return candidates;
  }
}
