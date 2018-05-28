// Name:  Will Santos 
import java.util.PriorityQueue;
import java.util.*;

public class myastar extends astar
{
	public myastar(int r, int c) { super(r,c); }
	// you need to override the search function in superclass astar
	public coord search(int sy, int sx, int ty, int tx){
		coord current = new coord(sy,sx);
		PriorityQueue<coord> Frontier = new PriorityQueue<coord>();
		coord[][] Status = new coord[ROWS][COLS]; //Interior
		Frontier.add(current);
		Status[sy][sx] = current;

		while(current.y!=ty || current.x!=tx){ //while we haven't reached the destination
			for(int i=0; i<6; i++){//check all 6 directions
				int ny = current.y + DY[i];
				int nx = current.x + DX[current.y%2][i];
				if(ny>=0 && ny<ROWS && nx>=0 && nx<COLS){//out of bounds?
					if(Status[ny][nx]==null || Status[ny][nx].interior==false){//if you haven't been here before?
						coord n = makeneighbor(current, ny, nx, ty, tx);
						if(n!=null){//check if neighbor is valid
							Frontier.add(n);
							Status[n.y][n.x]=n;
						}
					}
					
				}
			}
			current.interior = true;//record as part of interior
			Status[current.y][current.x] = current;
			current = Frontier.poll(); //retrieves & removes head of queue
		}
		return current;	
	}
}//myastar class

/*
Where do I start?

Study the coord.java file and the coord class.  These are objects that
we are going to build a search tree. 

Study the astar class.  There's an array int[][] M with ROWS rows and
COLS columns.  The value of each M[i][j] is its terrain type (0=OPEN,
3=WATER, etc).

Study the DX and DY vectors in the Hexagon class.  These tell you how to
calculate the array coordinates of each of your six neighbors.

The search function takes a starting position sy,sx and a target position 
ty,tx.  Your tree should have as root a coord object with y=sy, x=sx
(and null for prev, 0 for knowndist and estcost).  You are to build a 
spanning tree until you find ty,tx.  You need to return a coord object
with y=ty and x=tx, and with prev pointer set so we can follow it back to
sy,sx.

   more help: I wrote this version of search that searches the map 
   randomly in class: just look at, do not copy (it won't work).

        coord current = new coord(sy,sx);
	while (current.y!=ty || current.x!=tx)
	    {
		//pick random direction
		int dir = (int)(Math.random()*6);
		int cy = current.y, cx = current.x;
		int ny = cy + DY[dir];
		int nx = cx + DX[cy%2][dir];
		if (nx>=0 && nx<COLS && ny>=0 && ny<ROWS)
		    {
			coord next = new coord(ny,nx);
			next.prev = current;
			current = next; 
		    }
		// else, loop back and pick another direction
	    }// main while
	return current;

*/
