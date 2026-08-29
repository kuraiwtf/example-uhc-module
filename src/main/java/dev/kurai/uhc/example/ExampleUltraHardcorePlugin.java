package dev.kurai.uhc.example;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.example.command.ExampleCommand;
import dev.kurai.uhc.example.command.argument.RoleDataArgumentResolver;
import dev.kurai.uhc.example.death.ExampleDeathAnnounce;
import dev.kurai.uhc.example.death.ExampleDeathProcessor;
import dev.kurai.uhc.example.listener.ExampleWaitingListener;
import dev.kurai.uhc.example.role.ExampleRoleData;
import dev.kurai.uhc.example.slot.slot.ExampleSlotProvider;
import dev.kurai.uhc.example.timer.RoleAttributionTimer;
import dev.kurai.uhc.game.configuration.ore.OreConfiguration;
import dev.kurai.uhc.game.death.DeathService;
import dev.kurai.uhc.timer.TimerService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExampleUltraHardcorePlugin extends JavaPlugin {

  private final UltraHardcoreAPI ultraHardcore;
  private final ExampleModule module;

  public ExampleUltraHardcorePlugin() {
    this.ultraHardcore = UltraHardcoreAPI.getInstance();
    this.module = new ExampleModule(this.ultraHardcore, this);
  }

  @Override
  public void onEnable() {
    OreConfiguration.DIAMOND_LIMIT_OPTION.setValue(24);

    this.ultraHardcore
        .commandRegistrar()
        .getArgumentResolverRegistrar()
        .registerArgumentResolver(ExampleRoleData.class, new RoleDataArgumentResolver(this.module));

    this.ultraHardcore.commandRegistrar().registerCommand(new ExampleCommand(this.module));
    this.ultraHardcore.eventService().registerListener(new ExampleWaitingListener(this.module));

    final var gameService = this.ultraHardcore.gameService();
    gameService.cycleService().enabled(true);
    gameService.episodeService().setEnabled(true);

    final DeathService deathService = gameService.deathService();
    deathService.deathProcessor(new ExampleDeathProcessor(this.module));
    deathService.deathAnnounce(new ExampleDeathAnnounce(this.module));

    gameService.groupService().enabled(true);
    gameService.slotService().slotProvider(new ExampleSlotProvider(this.module));

    final TimerService timerService = gameService.timerService();
    timerService.registerTimers(new RoleAttributionTimer(this.module));

    this.ultraHardcore.moduleService().installModule(this.module);
  }
}
