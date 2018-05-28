//package Dasearch;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

//fun mods
import sun.audio.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel; 

public class pathfinder extends JFrame
{

    public static boolean showtrace = true; // does not erase image
    public static boolean genmap = true;
    protected BufferedImage diamondgif, mangif;  // animated gif images
    protected Graphics display;
    protected Image dbuf; // double buffer
    protected int gap = 16; // 20;  // side/radius of hexagon
    protected int yoff =16; // 40;
    protected astar PG;
    int rows, cols;
    int XDIM, YDIM; // window dimensions
    int gobx, goby, profx, profy;
    protected BufferedImage[] imageof; // image vector for terrain.
    protected BufferedImage[] imagechar; //image vector for character based on terrain.
	
	//MEGA & GIGA CHALLENGE
    Color[] colorof ={Color.green,Color.yellow,Color.white,Color.blue,Color.black,Color.red};
 // color corresponding to each terrain type.

    public static String fpath = "";  // file path prefix
    public static String savefile = "myastar.run";

    // graphical representation
    Hexagon[][] HX;
    int hpdist = Hexagon.calchpdist(gap);
    
    // access center graphical coordinates at cell i,j
    int getx(int i, int j) { return HX[i][j].x; }
    int gety(int i, int j) { return HX[i][j].y; }

    Graphics Gg;

    public void paint(Graphics g) {} // override automatic repaint

    public pathfinder(int r, int c, String[] argv) // constructor
    {
	rows = r; cols = c;
	int[][] M = null; // local map
	if(argv.length>0) // load map from file
	    {
		genmap = false;
		M = loadrun(argv[0]);
	    }// load from file
	hpdist = Hexagon.calchpdist(gap);	
	HX = new Hexagon[rows][cols];  // graphical map

	for(int i=0;i<rows;i++)
	    { 
	      int odd = i%2;
	      for(int j=0;j<cols;j++)
		{
		    HX[i][j] = new Hexagon(yoff/2+(j*2+odd)*hpdist,yoff+gap+(3*gap/2*i),gap);
		    //HX[i][j] = new Hexagon(yoff/2+(j*2+odd)*hpdist,yoff+(2*gap*i)+gap,gap);			    
		}
	    }//for i,j

	PG = new myastar(rows,cols);  // note it's myastar, not astar
	// constructor of myastar will generate map if genmap==true.

	if (!genmap) PG.M=M; // set to loaded file instead

	XDIM = cols*hpdist*2+ yoff/2; 
	YDIM = ((rows+1)*gap*3)/2 - yoff/4;
	this.setBounds(0,0,XDIM+5,YDIM+yoff+5);
	this.setVisible(true); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
try {
	dbuf = createImage(XDIM,YDIM+yoff);
	display = dbuf.getGraphics();
	display.fillRect(0,0,XDIM,YDIM+yoff);  // fill background	

	diamondgif = ImageIO.read(new File(fpath+"gem1.gif"));
	prepareImage(diamondgif,this);
	mangif = ImageIO.read(new File(fpath+"man15.gif"));
	prepareImage(mangif,this);
	
	//MOD SIZE
	imagechar = new BufferedImage[6]; // image of character on terrain type
	
	imagechar[astar.OPEN] = mangif;

/*================ MEGA & GIGA CHALLENGE ===================*/
	imagechar[astar.ROAD] = mangif; // MEGA CHALLENGE
	imagechar[astar.PORT] = mangif; // GIGA CHALLENGE
/*================ MEGA & GIGA CHALLENGE ===================*/

	imagechar[astar.WATER] = ImageIO.read(new File(fpath+"boat.gif"));
	imagechar[astar.DESERT] = imagechar[astar.FIRE] = mangif;
	prepareImage(imagechar[astar.WATER],this);

	//MOD SIZE
	imageof = new BufferedImage[6];  // background image for terrain type

	BufferedImage im = ImageIO.read(new File(fpath+"Water.gif"));
	int iw = im.getWidth(this), ih = im.getHeight(this);
	imageof[astar.WATER]= new BufferedImage(gap*2,gap*2,BufferedImage.TYPE_INT_RGB);
	Hexagon Hi = new Hexagon(hpdist,gap,gap);	
	Gg = imageof[astar.WATER].getGraphics();
	//	Gg.setClip(Hi);
	Gg.drawImage(im,0,0,null);
	prepareImage(imageof[astar.WATER],this);

	BufferedImage imm = ImageIO.read(new File(fpath+"grass1.gif"));	
	iw = imm.getWidth(null); ih = imm.getHeight(null);
	imageof[astar.OPEN] = new BufferedImage(gap*2,gap*2,BufferedImage.TYPE_INT_RGB);
	Gg = imageof[astar.OPEN].getGraphics();
	//	Gg.setClip(Hi);
	Gg.drawImage(imm,0,0,null);
	prepareImage(imageof[astar.OPEN],this);

	//	BufferedImage imf = ImageIO.read(new File(fpath+"volcano.gif"));
	BufferedImage imf = ImageIO.read(new File(fpath+"flames.jpeg"));	
	iw = imf.getWidth(null); ih = imf.getHeight(null);
	imageof[astar.FIRE] = new BufferedImage(gap*2,gap*2,BufferedImage.TYPE_INT_RGB);
	Gg = imageof[astar.FIRE].getGraphics();
	//	Gg.setClip(Hi);
	Gg.drawImage(imf,0,0,null);
	prepareImage(imageof[astar.FIRE],this);	

/*============= MEGA & GIGA CHALLENGE ============================================*/
	//MEGA CHALLENGE
	BufferedImage imr = ImageIO.read(new File(fpath+"road.jpg"));	
	iw = imr.getWidth(null); ih = imr.getHeight(null);
	imageof[astar.ROAD] = new BufferedImage(gap*2,gap*2,BufferedImage.TYPE_INT_RGB);
	Gg = imageof[astar.ROAD].getGraphics();
	//	Gg.setClip(Hi);
	Gg.drawImage(imr,0,0,null);
	prepareImage(imageof[astar.ROAD],this);	

	//GIGA CHALLENGE
	BufferedImage imp = ImageIO.read(new File(fpath+"port.jpg"));	
	iw = imp.getWidth(null); ih = imp.getHeight(null);
	imageof[astar.PORT] = new BufferedImage(gap*2,gap*2,BufferedImage.TYPE_INT_RGB);
	Gg = imageof[astar.PORT].getGraphics();
	//	Gg.setClip(Hi);
	Gg.drawImage(imp,0,0,null);
	prepareImage(imageof[astar.PORT],this);	
/*============= MEGA & GIGA CHALLENGE END ========================================*/


	
	try{Thread.sleep(500);} catch(Exception e) {} // Synch with system
	// draw static background as a green rectangle
	display.setColor(Color.green);
	display.fillRect(0,0,XDIM,YDIM+yoff);  // fill background

	// generate random starting positions.
	  // generate initial positions of professor and diamond
	if (genmap)
	{
	  do 
	    {
		gobx = (int)(Math.random() * PG.COLS);  // diamond
		goby = (int)(Math.random() * PG.ROWS);
	    }
	  while (PG.M[goby][gobx]!=PG.OPEN);
	  do 
	    {
		profx = (int)(Math.random() * PG.COLS);  
                profy = (int)(Math.random() * PG.ROWS);
	    }
	  while (PG.M[profy][profx]!=PG.OPEN ||
	       astar.hexdist(goby,gobx,profy,profx)<20);
	}// if need to generate new starting positions

	// draw map
	drawmap();
	// draw professor and diamond, initial position

	// draw static map
	display = (Graphics)this.getGraphics(); // change display
	display.drawImage(dbuf,0,0,this);
	
	int px = getx(profy,profx), py = gety(profy,profx); // center hx coords
	display.drawImage(imagechar[PG.M[profy][profx]],
			  (px-gap/2),(py-gap/2),gap,gap,null);
	px = getx(goby,gobx); py = gety(goby,gobx);
	display.drawImage(diamondgif,px-gap/2,py-gap/2,gap,gap,null);
	/*	
	display.drawImage(imagechar[PG.M[profy][profx]],
			  (profx*gap),(profy*gap)+yoff,gap,gap,null);
	display.drawImage(diamondgif,gobx*gap,goby*gap+yoff,gap,gap,null);
	*/
	animate();
	if (genmap)
	    { saverun(); // save the run
	      System.out.println("run saved in "+(fpath+savefile));
	    }
}//try
catch (Exception eee) {}
    } // constructor

    // save map and positions to file:
    void saverun()
    {
	try {
	    DataOutputStream dout = new DataOutputStream(new FileOutputStream(fpath+savefile));
	    dout.writeInt(gobx); dout.writeInt(goby); // position of diamond
	    dout.writeInt(profx); dout.writeInt(profy); // position of man
	    dout.writeInt(rows); dout.writeInt(cols);
	    dout.writeInt(gap); dout.writeInt(yoff);
	    for(int i=0;i<rows;i++)
		for(int j=0;j<cols;j++)
		    dout.writeInt(PG.M[i][j]);
	    dout.close();
	}
	catch(IOException ie) {System.out.println(ie);}
    }//saverun

    int[][] loadrun(String filen)
    {
	int[][] M=null;
	try {
	    DataInputStream din = new DataInputStream(new FileInputStream(fpath+filen));
	    gobx=din.readInt();  goby=din.readInt();
	    profx=din.readInt();  profy=din.readInt();
	    rows=din.readInt();  cols=din.readInt();
	    gap=din.readInt();   yoff=din.readInt();
	    M = new int[rows][cols];
	    for(int i=0;i<rows;i++)
		for(int j=0;j<cols;j++)
		    M[i][j] = din.readInt();
	    din.close();
	}
	catch(IOException ie) {System.out.println(ie);}
	return M;
    }//loadrun
    
    
    public void animate()
    {
	// invert path.
	coord path = PG.search(goby,gobx, profy,profx); // call SEARCH here
	if (path==null) 
	    {   display.setColor(Color.red);
		display.drawString("NO PATH TO TARGET!",50,100); 
		System.out.println("no path"); return;
	    }
	int px=0, py=0; // for calculating graphical coords
	while (path!=null)
	    {
		px = getx(path.y,path.x); py = gety(path.y,path.x);
		display.drawImage(imagechar[PG.M[path.y][path.x]],
				(px-gap/2),(py-gap/2),gap,gap,null);
		//	      display.drawImage(imagechar[PG.M[path.y][path.x]],
		//				(path.x*gap),(path.y*gap)+yoff,gap,gap,null);

		//System.out.printf("%d,%d: %d\n",path.y,path.x,PG.M[path.y][path.x]);

	      try{Thread.sleep(250);} catch(Exception se) {}
	      //	      display.drawImage(imageof[PG.M[path.y][path.x]],
	      //				(path.x*gap),(path.y*gap)+yoff,gap,gap,null);
	      //	      display.setColor(Color.red);
	      //      	      display.fillOval((path.x*gap)+8,(path.y*gap)+yoff+8,4,4);
	      // for animation:
	      //	      display.drawImage(diamondgif,gobx*gap,goby*gap+yoff,gap,gap,null);	      

	      if (!showtrace) // erase trail - redraw entire map
		  display.drawImage(dbuf,0,0,this);

	      path = path.prev;
	    }//with path!=null
	px = getx(goby,gobx); py = gety(goby,gobx);
	display.drawImage(diamondgif,px-gap/2,py-gap/2,gap,gap,null);
	display.drawImage(imagechar[PG.M[goby][gobx]],px-gap/2,py-gap/2,gap,gap,null);
    }//animate

    public void drawmap()
    {   
	int i, j;
	for(i=0;i<PG.ROWS;i++)
	    for(j=0;j<PG.COLS;j++)
		{

		    display.setColor(colorof[ PG.M[i][j] ]);
		    //		    display.fillPolygon(HX[i][j]);
		    //		    Gg = imageof[PG.M[i][j]].getGraphics();
		    //		    Gg.setClip(HX[i][j]);
		    //		    Gg.drawImage(imageof[PG.M[i][j]],0,0,null);
		    //		    display.drawImage(imageof[PG.M[i][j]],j*gap,(i*gap)+yoff,gap,gap,null);
		    display.setClip(HX[i][j]);
		    display.drawImage(imageof[PG.M[i][j]],yoff/2 + (j*2+(i%2)-1)*hpdist,(3*gap/2*i)+yoff,null);		    
		}
	//try{Thread.sleep(1000);} catch(Exception e) {} 
    } // drawmap

    public static void main(String[] args)
    {
	int r = 32; int c = 44;
	//new mods
	final JFXPanel fxPanel = new JFXPanel();
        String bip = "FE.wav";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();	
	
	//new mods END
	pathfinder pf = new pathfinder(r,c,args);
	
	mediaPlayer.stop();
	String pip = "FF7.mp3";
        Media end = new Media(new File(pip).toURI().toString());
        MediaPlayer mediaPlayer2 = new MediaPlayer(end);
        mediaPlayer2.play();	
    }

} // class pathfinder
