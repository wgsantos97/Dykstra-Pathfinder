// 2017 version
public class astar 
{
    public static final int OPEN = 0;   // don't confuse with cost
    public static final int DESERT = 1;  
    public static final int FIRE = 2;
    public static final int WATER = 3;
    public static final int ROAD = 4; // MOD FOR MEGA CHALLENGE
    public static final int PORT = 5; // MOD FOR GIGA CHALLENGE

    public static final int W = Hexagon.West;
    public static final int E = Hexagon.East;
    public static final int NW = Hexagon.NorthWest;
    public static final int SW = Hexagon.SouthWest;
    public static final int NE = Hexagon.NorthEast;
    public static final int SE = Hexagon.SouthEast;
    public static int[] DY = Hexagon.DY;
    public static int[][] DX = Hexagon.DX;

    public int M[][];  // the map itself, value=terrain type
    public int ROWS, COLS; // size of map in array coords

    // constructor creates M, generates a random map
    public astar(int r0, int c0)  // typically 32x44
    {
	M = new int[r0][c0];
	ROWS=r0;  COLS=c0;
	if (!pathfinder.genmap) return;

	// generate random map  (initially all OPEN)
	int GENS = 10;  // number of generations
	double NFACTOR = 0.1;  //0.12;   tweak for best results
	double p, r;  // for random probability calculation
	int generation;  int i, j;
	for(generation=0;generation<GENS;generation++)
	    {
	     for(i=0;i<ROWS;i++) 
		 for(j=0;j<COLS;j++)
	     {
	       p = 0.004; // base probability factor
	       // calculate probability of water based on surrounding cells
	       for(int k=0;k<6;k++)
		   {
		       int ni = i + DY[k], nj = j + DX[i%2][k];
		       if (ni>=0 && ni<ROWS && nj>=0 && nj<COLS && M[ni][nj]==WATER) p+= NFACTOR;
		   }
	       r = Math.random();
	       if (r<=p) M[i][j] = WATER;
	     } // for each cell i, j
	    } // for each generation

	for(generation=0;generation<GENS-2;generation++)
	    {
	     for(i=0;i<ROWS;i++) 
		 for(j=0;j<COLS;j++)
	     {
	       p = 0.004; // base probability factor
	       // calculate probability of fire based on surrounding cells
	       for(int k=0;k<6;k++)
		   {
		       int ni = i + DY[k], nj = j + DX[i%2][k];
		       if (ni>=0 && ni<ROWS && nj>=0 && nj<COLS && M[ni][nj]==FIRE) p+= NFACTOR;
		   }
	       r = Math.random();
	       if (r<=p && M[i][j]==0) M[i][j] = FIRE;
	     } // for each cell i, j
	    } // for each generation
/* ==================== MEGA & GIGA CHALLENGE ========================== */
	//GIGA CHALLENGE: create port
	for(generation=0;generation<1;generation++){
		for(i=0;i<ROWS;i++){
			for(j=0;j<COLS;j++){
	       			p = 0.0004; // base probability factor
	       			// calculate probability of ports based on surrounding cells
	       			for(int k=0;k<6;k++){
					int ni = i + DY[k], nj = j + DX[i%2][k];
					if (ni>=0 && ni<ROWS && nj>=0 && nj<COLS){
						if(M[ni][nj]==WATER && M[i][j]==OPEN) p+= NFACTOR;
		   			}
				}
				r = Math.random();
				if (r<=p && M[i][j]==0) M[i][j] = PORT;
			}
		} // for each cell i, j
	} // for each generation
	
	//MEGA CHALLENGE: create road
	for(int x=0; x<ROWS; x++){
		int y = COLS/3;
		M[y][x] = ROAD;
	}

/* ==================== MEGA & GIGA CHALLENGE END ====================== */
    } //constructor

    // Determines distance properly in hex coordinates:
    //   distance is max of |dx|, |dy|, and |dx-dy|.
    // Note: this does not give exact distance but does not overestimate
    // it, so it is admissible for algorithm A*
    public static int hexdist(int y1, int x1, int y2, int x2)
    {
        int dx = x1-x2, dy = y1-y2;
	int dd = Math.abs(dx - dy);
	dy = Math.abs(dy);
	int max = Math.abs(dx);
	if (dy>max) max = dy;
	if (dd>max) max = dd;
	return max;
    }

    // you need to override the search method in astar.java

	//OPEN; DESERT; FIRE; WATER; ROAD; PORT
    int[] costof = {3,5,-1,8,0,1};  // cost vector

    // USE THIS FUNCTION IF YOU LIKE:
    // makeneighbor attempts to create a coord object for coordinates y,x
    // adjacent to coord p (which will become its parent in the search tree)
    // if y or x is out of bounds, it returns null.  otherwise it creates
    // a coord object with parent p and where dist and cost are calculated
    // based on distance back to source, and estimated distance to ty,tx
    public coord makeneighbor(coord p, int y, int x, int ty, int tx)
    {
	if (y<0 || y>=ROWS || x<0 || x>=COLS) return null;
	int coordcost = costof[M[y][x]]; //potential new position
	int currentpos = costof[M[p.y][p.x]]; //current position
	int estimate = hexdist(y,x,ty,tx);
	coord node = new coord(y,x);
	
	/*================= MEGA & GIGA CHALLENGE =================*/
	//MEGA
	if(coordcost==-1){//if fire
		return null; // cannot pass
	}
	//GIGA
	if(((currentpos>1 || currentpos<1) && coordcost==8) || (currentpos==8 && (coordcost>1 || coordcost<1))){
		//if land-water or water-land
		if(currentpos<8 || coordcost<8){
			return null; // cannot pass;
		}
	}
	/*================= MEGA & GIGA CHALLENGE =================*/
	
	node.knowndist = p.knowndist + coordcost;
	node.estcost = node.knowndist + estimate;
	node.prev = p;
	return node;	
    }// calccost


    // incorrect version: NEED TO OVERRIDE IN myastar.java:
    public coord search(int sy, int sx, int ty, int tx)

    {
	return null; // this means no path was found.
	/*
	// following code tries to find target randomly, not
	// likely to work (won't return until solution found).
        coord current = new coord(sy,sx);
	while (current.y!=ty || current.x!=tx)
	    {
		//pick random direction
		coord next = null;
                while (next==null)
		  {
		int d = (int)(Math.random()*6);
		int cy = current.y, cx = current.x;
		int ny = cy + DY[d];
		int nx = cx + DX[cy%2][d];
		if (nx>=0 && nx<COLS && ny>=0 && ny<ROWS)
		    {
			next = new coord(ny,nx);
			next.prev = current;
			current = next; 
		    }
		  } // while next==null
	    }// main while
	return current;
	*/
    }// incorrect search

}//astar
