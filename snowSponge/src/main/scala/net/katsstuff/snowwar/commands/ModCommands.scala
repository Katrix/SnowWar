package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import org.spongepowered.api.entity.living.player.Player

import cats.syntax.all._
import net.katsstuff.minejson.text._
import net.katsstuff.snowwar.Perms
import net.katsstuff.snowwar.objects.{Arena, SpawnLocation}

class ModCommands[G[_], F[_], Page](val bundle: SnowCommandBundle[G, F, Page]) {
  import bundle._

  private val teamCommands   = new TeamCommands(bundle)
  private val arenasCommands = new ArenaCommands(bundle)

  lazy val teamChildCommands = Set(
    teamCommands.viewCommand,
    teamCommands.saveCommand,
    teamCommands.removeCommand,
    arenasCommands.addCommand,
    arenasCommands.removeCommand,
    arenasCommands.addSpawnCommand,
    arenasCommands.removeSpawnCommand
  )

  lazy val teamCommand: ChildCommand = Command
    .withSenderAndChildren[Player, NotUsed](teamChildCommands) { (player, _, _) =>
      GtoFLocalized(player) { implicit locale =>
        import pagination.pageOperations._
        for {
          teams <- T.allTeams
          content = button(t"Toolbox", "/snowwar mod team toolbox") +: teams.map { team =>
            val teamName     = s""""${team.name}""""
            val viewButton   = button(t"${Yellow}View", s"snowwar mod team view $teamName")
            val saveButton   = shiftButton(t"${Yellow}Save", s"snowwar mod team save $teamName")
            val removeButton = shiftButton(t"${Red}Remove", s"snowwar mod team remove $teamName")
            t"$teamName $viewButton$saveButton$removeButton"
          }
          pages = setTitle(t"SnowWar teams") |+| setContent(content)
          _ <- pagination.sendPage(pages, player)
        } yield Command.success()
      }
    }
    .toChild(Seq("team"), mkCommandInfo(Permission(Perms.TeamCommand), "mod.team"))

  lazy val arenaCommand: ChildCommand = Command
    .withSender[Player, Option[Arena]] { (player, _, optArena) =>
      GtoFLocalized(player) { implicit locale =>
        import pagination.pageOperations._

        optArena match {
          case Some(arena) =>
            for {
              teams <- T.allTeams
              arenaName = s""""${arena.name}""""
              setSpawnLine = teams
                .map { team =>
                  val teamName = s""""${team.name}""""
                  button(team.displayName, s"snowwar mod arena addspawn $arenaName $teamName")
                }
                .foldLeft(t"Add spawn location: ")((t1, t2) => t"$t1$t2")
              content = setSpawnLine +: arena.spawnLocations.zipWithIndex.map {
                case (SpawnLocation(rawTeamName, location), idx) =>
                  val teamDisplayName = teams.find(_.name == rawTeamName).fold(t"$rawTeamName")(_.displayName)
                  val teleportButton  = actionButton(t"${Yellow}Teleport")(src => player.setLocation(location))
                  val removeButton    = shiftButton(t"${Red}Remove", s"snowwar mod arena removespawn $arenaName $idx")
                  t"Spawn $idx for $teamDisplayName $teleportButton$removeButton"
              }
              pages = setTitle(t"SnowWar arena ${}") |+| setContent(content)
              _ <- pagination.sendPage(pages, player)
            } yield Command.success()
          case None =>
            for {
              arenas <- A.allArenas
              content = shiftButton(t"New", "snowwar mod arena add <name>") +: arenas.map { arena =>
                val arenaName    = s""""${arena.name}""""
                val infoButton   = button(t"${Yellow}Info", s"snowwar mod arena $arenaName")
                val removeButton = shiftButton(t"${Red}Remove", s"snowwar mod arena remove $arenaName")
                t"$arenaName $infoButton$removeButton"
              }
              pages = setTitle(t"SnowWar arenas") |+| setContent(content)
              _ <- pagination.sendPage(pages, player)
            } yield Command.success()
        }
      }
    }
    .toChild(Seq("arena"), mkCommandInfo(Permission(Perms.ArenaCommand), "mod.arena"))
}
