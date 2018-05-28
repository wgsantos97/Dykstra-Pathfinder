public class coord implements Comparable<coord>
{
    int y, x;
    int estcost;      // estimated cost, including heuristic
    int knowndist;    // distance (cost) from source node, excluding estimate
    coord prev; // pointer to previous coordinate on path.
    coord(int a, int b) {y=a; x=b;}

    public boolean equals(Object oc) // conforms to older java specs
    {
	if (oc==null || !(oc instanceof coord)) return false;
	coord c = (coord)oc;
	return (x==c.x && y==c.y);
    }

    public int compareTo(coord c) // compares cost
    {
	return estcost - c.estcost;
    }

    boolean interior = false; // false=frontier node, true=interior node
} // coord
