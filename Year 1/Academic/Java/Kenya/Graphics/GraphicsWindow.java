package kenya;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import java.awt.image.*;

class GraphicsArea extends Canvas {

    BufferedImage b;
    Graphics2D g2d;
    boolean fill;
   
    public GraphicsArea() {

	fill = false;
        b = new BufferedImage( 500 , 500 , BufferedImage.TYPE_INT_RGB );
	g2d = (Graphics2D)b.getGraphics();
	setSize( new Dimension ( 500, 500 ));
    }

    public void paint( Graphics g ) {

	Graphics2D g2 = (Graphics2D)g;
	g2.drawImage( b, 0, 0, this );
    }

    public void drawBox( int x1, int y1, int x2, int y2 ) {

	if ( fill ) {
	    g2d.fill( new Rectangle( x1, y1, x2, y2 ) );
	} else {
	    g2d.draw( new Rectangle( x1, y1, x2, y2 ) );  
	}      	
	repaint();
    }

    public void drawTriangle( int x1 ,int y1 , int x2, int y2 , int x3 , int y3 ) {

	int[] xpoints = { x1 , x2 , x3 };
	int[] ypoints = { y1 , y2 , y3 };

	Polygon tri = new Polygon( xpoints , ypoints , 3 );

	if ( fill ) {
	    g2d.fill( tri );
	} else {
	    g2d.draw( tri );
	}
	repaint();
    }

    public void drawEllipse( int x, int y, int w, int h ) {

	if ( fill ) {
	    g2d.fill( new Ellipse2D.Float( x, y , w, h) );
	} else {
	    g2d.draw( new Ellipse2D.Float( x, y , w, h) );
	}
	repaint();
    }

    public void drawLine( int x1, int y1, int x2, int y2 ) {

	Polygon line = new Polygon();

	line.addPoint( x1, y1 );
	line.addPoint( x2, y2 );	
	g2d.draw( line );
	repaint();
    }

    public void drawString( String s , int x, int y ) {

	g2d.drawString( s , x , y );
	repaint();
    }

    public void clear() {

	g2d.clearRect( 0, 0, 500, 500 );
	repaint();
    }

    public void setFGColour( Color c ) {
	
	g2d.setColor( c );
    }

    public void setBGColour( Color c ) {
	
	g2d.setBackground( c );
    }
}


public class GraphicsWindow extends JFrame {

    GraphicsArea g;

    public GraphicsWindow() {

	super("Kenya Graphics Window");
	g = new GraphicsArea();
	setLocation( 150,200 );
	this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	this.getContentPane().add("Center" , g);
	this.pack();
	this.show();
    }

    public void fill( boolean b ) {

	g.fill = b;
    }

    public void drawBox( int x1, int y1, int x2, int y2 ) {

	 g.drawBox( x1, y1, x2, y2 );
    } 

    public void drawTriangle( int x1, int y1 , int x2 , int y2 , int x3 , int y3 ) {

	g.drawTriangle( x1, y1, x2, y2, x3, y3 );
    }

    public void drawEllipse( int x, int y, int w, int h ) {
	
	g.drawEllipse( x, y, w, h );
    } 

    public void drawLine( int x1, int y1, int x2, int y2 ) {

	g.drawLine( x1, y1, x2, y2 );
    }

    public void drawString( String s , int x, int y ) {

	g.drawString( s , x , y );
	repaint();
    }

    public void clear() {
	
	g.clear();
    }

    public void setBGColour( int r, int gr, int b ) {

	g.setBGColour( new Color( r, gr, b ) );
    }

    public void setFGColour( int r, int gr, int b ) {

	g.setFGColour( new Color( r, gr, b ) );
    }
}

