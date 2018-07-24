package net.katsstuff.snowwar.objects

import org.spongepowered.api.item.inventory.ItemStack

import net.katsstuff.minejson.text.Text

case class Team(
    name: String,
    displayName: Text,
    inventory: Map[Int, ItemStack]
)
