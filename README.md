# ClearLag
####  ***[LocaleAPI](https://ore.spongepowered.org/Semenkovsky_Ivan/LocaleAPI) - required.*** 
####  ***[CommandPack](https://ore.spongepowered.org/Semenkovsky_Ivan/CommandPack) - required.***

##### Features:
* Automatic removal of items lying on the ground by timer. 
* Customizable limit of monsters when reached in each individual world they will be killed.
* Blacklist of items that will not be removed by the plugin.
* Kill mobs by category using commands.
* Temporary disable basic world functions [mob spawning, mob griefing, fire tick, random tick speed = 0]
* Clearing RAM using the command.
* Dynamically adjust the view distances of each individual world, as well as the game rule `randomTickSpeed`.
* The commands to kill mobs ignore entities with a custom name.

##### Commands:
Command | Permission | Lore
-- | -- | --
/lagg | clearlag.help | Main command
/lagg clear | clearlag.clear | remove any items lying on the ground
/lagg halt | clearlag.halt | enable/disable the halt function in the world.
/lagg gc | clearlag.garbagecollector | clear ram
/lagg kill | clearlag.help | killing mobs by category
/lagg kill monsters | clearlag.kill.monsters | kill all hostile mobs
/lagg kill creature | clearlag.kill.creature | kill all the peaceful mobs
/lagg kill ambient | clearlag.kill.ambient | kill all the ambient mobs
/lagg kill waterсreature | clearlag.kill.waterсreature | kill all the peaceful water mobs
/lagg kill waterambient | clearlag.kill.waterambient | kill all the ambient water mobs
/lagg kill misc | clearlag.kill.misc | kill all mobs outside of the other categories
/lagg kill all | clearlag.kill.all | kill all the mobs
