package net.katsstuff.snowwar.algebras

import scala.language.higherKinds

import net.katsstuff.snowwar.objects.Arena

trait Arenas[F[_]] {

  def allArenas: F[Seq[Arena]]

  def getArena(name: String): F[Option[Arena]]

  def newArena(name: String): F[Either[String, Unit]]

  def removeArena(name: String): F[Either[String, Unit]]
}
