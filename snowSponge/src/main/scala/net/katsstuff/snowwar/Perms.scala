package net.katsstuff.snowwar

object Perms {

  val Root    = "snowwar"
  val Command = s"$Root.command"

  val UserCommand        = s"$Command.user"
  val UserGuiCommand     = s"$UserCommand.gui"
  val JoinCommand        = s"$UserCommand.join"
  val LeaveCommand       = s"$UserCommand.leave"
  val StatsCommand       = s"$UserCommand.stats"
  val LeaderboardCommand = s"$UserCommand.leaderboard"

  val ModCommand    = s"$Command.mod"
  val ModCommandTop = s"$ModCommand.mod"

  val TeamCommand        = s"$ModCommand.team"
  val ViewTeamCommand    = s"$TeamCommand.view"
  val TeamToolboxCommand = s"$TeamCommand.toolbox"
  val SaveTeamCommand    = s"$TeamCommand.save"
  val RemoveTeamCommand  = s"$TeamCommand.remove"

  val ArenaCommand            = s"$ModCommand.arena"
  val ArenaGuiCommand         = s"$ArenaCommand.gui"
  val ArenaAddCommand         = s"$ArenaCommand.add"
  val ArenaRemoveCommand      = s"$ArenaCommand.remove"
  val ArenaAddSpawnCommand    = s"$ArenaCommand.addspawn"
  val ArenaRemoveSpawnCommand = s"$ArenaCommand.removespawn"

}
