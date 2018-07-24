package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import shapeless.{Witness => W}
import org.spongepowered.api.entity.living.player.Player

import net.katsstuff.snowwar.Perms
import net.katsstuff.snowwar.objects.Team

class TeamCommands[G[_], F[_], Page](val bundle: SnowCommandBundle[G, F, Page]) {
  import bundle._

  lazy val viewCommand: ChildCommand = Command
    .withSender[Player, Team] { (src, _, _) =>
      GtoFLocalized(src) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("view"), mkCommandInfo(Permission(Perms.ViewTeamCommand), "mod.teams.view"))

  lazy val toolboxCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("toolbox"), mkCommandInfo(Permission(Perms.TeamToolboxCommand), "mod.arenas.toolbox"))

  lazy val saveCommand: ChildCommand = Command
    .withSender[Player, Named[W.`"team"`.T, String]] { case (src, _, Named(name)) =>
      GtoFLocalized(src) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("add"), mkCommandInfo(Permission(Perms.SaveTeamCommand), "mod.teams.save"))

  lazy val removeCommand: ChildCommand = Command
    .withSender[Player, Named[W.`"team"`.T, String]] { case (src, _, Named(name)) =>
      GtoFLocalized(src) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("remove"), mkCommandInfo(Permission(Perms.RemoveTeamCommand), "mod.teams.remove"))
}
