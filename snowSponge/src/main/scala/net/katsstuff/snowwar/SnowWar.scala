package net.katsstuff.snowwar

import java.nio.file.Path

import javax.inject.Inject

import org.slf4j.Logger
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.plugin.{Plugin, PluginContainer}

import cats.effect.IO
import net.katsstuff.katlib.ImplKatPluginIO

object SnowWar {
  def init(event: GameInitializationEvent): IO[Unit] = ???
}

@Plugin(id = "snowwar", name = "SnowWar", version = "0.1")
class SnowWar @Inject()(
    logger: Logger,
    @ConfigDir(sharedRoot = false) cfgDir: Path,
    spongeContainer: PluginContainer
) extends ImplKatPluginIO(logger, cfgDir, spongeContainer) {

  @Listener
  def init(event: GameInitializationEvent): Unit = SnowWar.init(event).unsafeRunSync()
}
