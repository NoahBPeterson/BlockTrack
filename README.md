# BlockTrack

A block tracking plugin for NukkitX which uses the DbLib plugin. Gives operators the ability to check how many blocks and of what kind particular players have broken, as well as the ability to check individual blocks for information on when they were broken, and by whom.

# Commands

/blocktrack \<player\> - Shows the number and type of blocks placed and destroyed by a given player, sorted by number of blocks destroyed.
  
/blocktrack \<player\> \<blockType\> - Shows the entry for the given blocktype.
  
/blocktrack \<player\> total \<placed/destroyed\> - Shows the total number of blocks created or destroyed by the given player.

/blockhistory - Shows the history of the blocks, including who altered them, when they did so, and what they altered. You must place or break a block after using the command to input the position to get the history.
  
## Todo

Allow operators to rollback the changes made in a region or made by a player.