package net.katsstuff.snowwar.algebras

import scala.language.higherKinds

import net.katsstuff.snowwar.objects.Team

trait Teams[F[_]] {

  def allTeams: F[Seq[Team]]

  def getTeam(name: String): F[Option[Team]]

  def saveTeam(team: Team): F[Either[String, Unit]]

  def removeTeam(name: String): F[Either[String, Unit]]

}
