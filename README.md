# Dykstra-Pathfinder
This program randomly generates a hex grid and maps out an optimized path from point A to point B.

To execute this program, you need to run the file, 'pathfinder.java'

This program has a few unique features.

For starters, it has a save system to save a randomly generated map. It always saves under the filename, 'myastar.run'
To save that file, rename 'myastar.run'. Otherwise, it will be overwritten the next time the program is run.

To run a specific .run file, type 'java pathfinder YOURFILENAME.run' and it will rerun that map.

Right now this program has four '.run' files which each highlight a unique scenario:
 
 A road tile has the lowest possible value (see line 114: 'astar.java'). These maps simulate how the pathfinder will always
 prioritize roads in its pathing due to the value of the terrain.
 - roadcross.run  
 - roadcross2.run 
 
 A port tile has the second lowest possible value (see line 114: 'astar.java'). These maps simulate how the pathfinder will
 access the water or land tiles through ports. It will NEVER cross between land and water tiles directly.
 Only through ports tiles.
 - portcross.run   
 - portcross2.run  
