package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import org.spongepowered.api.entity.living.player.Player

import net.katsstuff.snowwar.Perms

class ModCommands[G[_], F[_], Page](val bundle: SnowCommandBundle[G, F, Page]) {
  import bundle._

  private val teamCommands   = new TeamCommands(bundle)
  private val arenasCommands = new ArenaCommands(bundle)

  lazy val teamChildCommands = Set(
    teamCommands.viewCommand,
    teamCommands.saveCommand,
    teamCommands.removeCommand,
    arenasCommands.guiCommand,
    arenasCommands.addCommand,
    arenasCommands.removeCommand,
    arenasCommands.addSpawnCommand,
    arenasCommands.removeSpawnCommand
  )

  lazy val teamCommand: ChildCommand = Command
    .withSenderAndChildren[Player, NotUsed](teamChildCommands) { (src, _, _) =>
      GtoFLocalized(src) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("team"), mkCommandInfo(Permission(Perms.TeamCommand), "mod.team"))

  lazy val arenaCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (src, _, _) =>
      GtoFLocalized(src) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("arena"), mkCommandInfo(Permission(Perms.ArenaCommand), "mod.arena"))
}
