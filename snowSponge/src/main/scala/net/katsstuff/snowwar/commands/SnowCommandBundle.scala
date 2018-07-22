package net.katsstuff.snowwar.commands

import scala.language.higherKinds

import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.{Text => SpongeText}

import cats.data.NonEmptyList
import cats.effect.Sync
import cats.kernel.Monoid
import cats.syntax.all._
import cats.{MonadError, ~>}
import net.katsstuff.katlib.algebras.{Localized, Pagination, Resource, TextConversion}
import net.katsstuff.katlib.commands.SpongeKatLibCommands
import net.katsstuff.minejson.text._
import net.katsstuff.scammander.sponge.components.{CommandInfo, SpongeCommandWrapper}
import net.katsstuff.scammander.{CommandFailure, ScammanderHelper}
import net.katsstuff.snowwar.Perms

abstract class SnowCommandBundle[G[_], F[_], Page: Monoid](GtoF: G ~> F)(
    implicit pagination: Pagination.Aux[G, CommandSource, Page],
    R: Resource[G],
    localized: Localized[G, CommandSource],
    T: TextConversion[F],
    TG: TextConversion[G],
    val G: Sync[G],
    val F: MonadError[F, NonEmptyList[CommandFailure]] //TODO: Make a val in KatLib
) extends SpongeKatLibCommands[G, F, Page](GtoF) {

  private val userCommands = new UserCommands(this)
  private val modCommands  = new ModCommands(this)

  def localizedCmdInfo(key: String): CommandSource => F[Option[SpongeText]] = GtoFLocalized(_) { implicit locale =>
    R.getText(key).flatMap(TG.ourToSponge).map(Some.apply)
  }

  def localizedHelp(key: String): CommandSource => F[Option[SpongeText]] = localizedCmdInfo(key)

  def localizedDescription(key: String): CommandSource => F[Option[SpongeText]] = localizedCmdInfo(key)

  def mkCommandInfo(permission: Option[String], commandName: String) = CommandInfo(
    permission,
    localizedHelp(s"snowwar.command.$commandName.help"),
    localizedDescription(s"snowwar.command.$commandName.description")
  )

  lazy val snowHelpCommand: ChildCommand =
    helpCommand(t"SnowWar help", Set(ChildCommand(Set("snowwar"), userCommand)))
      .toChild(Seq("help"), mkCommandInfo(Permission.none, "help"))

  lazy val rootChildCommands = Set(
    snowHelpCommand,
    modCommand,
    userCommands.guiCommand,
    userCommands.joinCommand,
    userCommands.leaveCommand,
    userCommands.statsCommand,
    userCommands.leaderboardCommand
  )

  lazy val userCommand: SpongeCommandWrapper[F] = Command
    .withSenderAndChildren[Player, NotUsed](rootChildCommands) { (player, _, _) =>
      snowHelpCommand.command.command.runRaw(player, (), Nil)
    }
    .toSponge(mkCommandInfo(Permission.none, "snowwar"))

  lazy val modChildCommands = Set(
    modCommands.teamCommand,
    modCommands.arenaCommand
  )

  lazy val modCommand: ChildCommand = Command
    .withSenderAndChildren[Player, NotUsed](modChildCommands) { (player, _, _) =>
      snowHelpCommand.command.command.runRaw(player, (), ScammanderHelper.stringToRawArgsQuoted("mod"))
    }
    .toChild(Seq("mod"), mkCommandInfo(Permission(Perms.ModCommandTop), "mod"))
}
