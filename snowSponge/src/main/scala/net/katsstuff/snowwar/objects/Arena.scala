package net.katsstuff.snowwar.objects

import org.spongepowered.api.world.{Location, World}

case class Arena(
    name: String,
    spawnLocations: Seq[SpawnLocation]
)

case class SpawnLocation(
  teamName: String,
  location: Location[World]
)
