package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import shapeless._
import shapeless.{Witness => W}
import org.spongepowered.api.entity.living.player.Player

import net.katsstuff.snowwar.Perms
import net.katsstuff.snowwar.objects.{Arena, Team}

class ArenaCommands[G[_], F[_], Page](val bundle: SnowCommandBundle[G, F, Page]) {
  import bundle._

  lazy val addCommand: ChildCommand = Command
    .withSender[Player, Named[W.`"name"`.T, String]] {
      case (player, _, Named(name)) =>
        GtoFLocalized(player) { implicit locale =>
          ???
        }
    }
    .toChild(Seq("add"), mkCommandInfo(Permission(Perms.ArenaAddCommand), "mod.arenas.add"))

  lazy val removeCommand: ChildCommand = Command
    .withSender[Player, Named[W.`"arena"`.T, String]] {
      case (player, _, Named(name)) =>
        GtoFLocalized(player) { implicit locale =>
          ???
        }
    }
    .toChild(Seq("remove"), mkCommandInfo(Permission(Perms.ArenaRemoveCommand), "mod.arenas.remove"))

  lazy val addSpawnCommand: ChildCommand = Command
    .withSender[Player, Arena :: Team :: HNil] {
      case (player, _, arena :: team :: HNil) =>
        GtoFLocalized(player) { implicit locale =>
          ???
        }
    }
    .toChild(Seq("addspawn"), mkCommandInfo(Permission(Perms.ArenaAddSpawnCommand), "mod.arenas.addSpawn"))

  lazy val removeSpawnCommand: ChildCommand = Command
    .withSender[Player, Arena :: Named[W.`"spawnIndex"`.T, Int] :: HNil] {
      case (player, _, arena :: Named(spawnIndex) :: HNil) =>
        GtoFLocalized(player) { implicit locale =>
          ???
        }
    }
    .toChild(Seq("removespawn"), mkCommandInfo(Permission(Perms.ArenaRemoveSpawnCommand), "mod.arenas.removeSpawn"))

}
