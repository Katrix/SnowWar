package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import shapeless.{Witness => W}
import org.spongepowered.api.entity.living.player.Player

import net.katsstuff.snowwar.Perms

class UserCommands[G[_], F[_], Page](val bundle: SnowCommandBundle[G, F, Page]) {
  import bundle._

  lazy val guiCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("gui"), mkCommandInfo(Permission(Perms.UserGuiCommand), "gui"))

  lazy val joinCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("join"), mkCommandInfo(Permission(Perms.JoinCommand), "join"))

  lazy val leaveCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("leave"), mkCommandInfo(Permission(Perms.LeaveCommand), "leave"))

  lazy val statsCommand: ChildCommand = Command
    .withSender[Player, NotUsed] { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        ???
      }
    }
    .toChild(Seq("stats"), mkCommandInfo(Permission(Perms.StatsCommand), "stats"))

  lazy val leaderboardCommand: ChildCommand = Command
    .simple[Option[Named[W.`"stat"`.T, String]]] { (src, _, optNamedStat) =>
      GtoFLocalized(src) { implicit locale =>
        val stat = optNamedStat.fold("score")(_.value)
        ???
      }
    }
    .toChild(Seq("leaderboard", "top"), mkCommandInfo(Permission(Perms.LeaderboardCommand), "leaderboard"))
}
