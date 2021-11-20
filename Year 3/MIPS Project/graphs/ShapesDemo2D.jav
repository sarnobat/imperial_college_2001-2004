  ShapesDemo2D.java    

package graphics.carl;

/*
 * 1.2 version.
 */

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

/*
 * This is like the FontDemo applet in volume 1, except that it
 * uses the Java 2D APIs to define and render the graphics and text.
 */

public class ShapesDemo2D extends javax.swing.JApplet {
  final static int maxCharHeight = 15;
  final static int minFontSize = 6;

  final static java.awt.Color bg = java.awt.Color.white;
  final static java.awt.Color fg = java.awt.Color.black;
  final static java.awt.Color red = java.awt.Color.red;
  final static java.awt.Color white = java.awt.Color.white;

  final static java.awt.BasicStroke stroke = new java.awt.BasicStroke(2.0f);
  final static java.awt.BasicStroke wideStroke = new java.awt.BasicStroke(8.0f);

  final static float dash1[] = {10.0f};
  final static java.awt.BasicStroke dashed = new java.awt.BasicStroke(1.0f,
                                                                      BasicStroke.CAP_BUTT,
                                                                      BasicStroke.JOIN_MITER,
                                                                      10.0f, dash1, 0.0f);
  java.awt.Dimension totalSize;
  java.awt.FontMetrics fontMetrics;

  public void init() {
    //Initialize drawing colors
    setBackground(bg);
    setForeground(fg);
  }

  java.awt.FontMetrics pickFont(java.awt.Graphics2D g2,
                                String longString,
                                int xSpace) {
    boolean fontFits = false;
    java.awt.Font font = g2.getFont();
    java.awt.FontMetrics fontMetrics = g2.getFontMetrics();
    int size = font.getSize();
    String name = font.getName();
    int style = font.getStyle();

    while (!fontFits) {
      if ((fontMetrics.getHeight() <= maxCharHeight)
          && (fontMetrics.stringWidth(longString) <= xSpace)) {
        fontFits = true;
      } else {
        if (size <= minFontSize) {
          fontFits = true;
        } else {
          g2.setFont(font = new java.awt.Font(name,
                                              style,
                                              --size));
          fontMetrics = g2.getFontMetrics();
        }
      }
    }

    return fontMetrics;
  }

  public void paint(java.awt.Graphics g) {
    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    java.awt.Dimension d = getSize();
    int gridWidth = d.width / 6;
    int gridHeight = d.height / 2;

    fontMetrics = pickFont(g2, "Filled and Stroked GeneralPath",
                           gridWidth);

    java.awt.Color fg3D = java.awt.Color.lightGray;

    g2.setPaint(fg3D);
    g2.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
    g2.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
    g2.setPaint(fg);

    int x = 5;
    int y = 7;
    int rectWidth = gridWidth - 2 * x;
    int stringY = gridHeight - 3 - fontMetrics.getDescent();
    int rectHeight = stringY - fontMetrics.getMaxAscent() - y - 2;

    // draw Line2D.Double
    g2.draw(new java.awt.geom.Line2D.Double(x, y + rectHeight - 1, x + rectWidth, y));
    g2.drawString("Line2D", x, stringY);
    x += gridWidth;

    // draw Rectangle2D.Double
    g2.setStroke(stroke);
    g2.draw(new java.awt.geom.Rectangle2D.Double(x, y, rectWidth, rectHeight));
    g2.drawString("Rectangle2D", x, stringY);
    x += gridWidth;

    // draw  RoundRectangle2D.Double
    g2.setStroke(dashed);
    g2.draw(new java.awt.geom.RoundRectangle2D.Double(x, y, rectWidth,
                                                      rectHeight, 10, 10));
    g2.drawString("RoundRectangle2D", x, stringY);
    x += gridWidth;

    // draw Arc2D.Double
    g2.setStroke(wideStroke);
    g2.draw(new java.awt.geom.Arc2D.Double(x, y, rectWidth, rectHeight, 90,
                                           135, Arc2D.OPEN));
    g2.drawString("Arc2D", x, stringY);
    x += gridWidth;

    // draw Ellipse2D.Double
    g2.setStroke(stroke);
    g2.draw(new java.awt.geom.Ellipse2D.Double(x, y, rectWidth, rectHeight));
    g2.drawString("Ellipse2D", x, stringY);
    x += gridWidth;

    // draw GeneralPath (polygon)
    int x1Points[] = {x, x + rectWidth, x, x + rectWidth};
    int y1Points[] = {y, y + rectHeight, y + rectHeight, y};
    java.awt.geom.GeneralPath polygon = new java.awt.geom.GeneralPath(GeneralPath.WIND_EVEN_ODD,
                                                                      x1Points.length);
    polygon.moveTo(x1Points[0], y1Points[0]);
    for (int index = 1; index < x1Points.length; index++) {
      polygon.lineTo(x1Points[index], y1Points[index]);
    }
    ;
    polygon.closePath();

    g2.draw(polygon);
    g2.drawString("GeneralPath", x, stringY);

    // NEW ROW
    x = 5;
    y += gridHeight;
    stringY += gridHeight;

    // draw GeneralPath (polyline)

    int x2Points[] = {x, x + rectWidth, x, x + rectWidth};
    int y2Points[] = {y, y + rectHeight, y + rectHeight, y};
    java.awt.geom.GeneralPath polyline = new java.awt.geom.GeneralPath(GeneralPath.WIND_EVEN_ODD,
                                                                       x2Points.length);
    polyline.moveTo(x2Points[0], y2Points[0]);
    for (int index = 1; index < x2Points.length; index++) {
      polyline.lineTo(x2Points[index], y2Points[index]);
    }
    ;

    g2.draw(polyline);
    g2.drawString("GeneralPath (open)", x, stringY);
    x += gridWidth;

    // fill Rectangle2D.Double (red)
    g2.setPaint(red);
    g2.fill(new java.awt.geom.Rectangle2D.Double(x, y, rectWidth, rectHeight));
    g2.setPaint(fg);
    g2.drawString("Filled Rectangle2D", x, stringY);
    x += gridWidth;

    // fill RoundRectangle2D.Double
    java.awt.GradientPaint redtowhite = new java.awt.GradientPaint(x, y, red, x + rectWidth, y, white);
    g2.setPaint(redtowhite);
    g2.fill(new java.awt.geom.RoundRectangle2D.Double(x, y, rectWidth,
                                                      rectHeight, 10, 10));
    g2.setPaint(fg);
    g2.drawString("Filled RoundRectangle2D", x, stringY);
    x += gridWidth;

    // fill Arc2D
    g2.setPaint(red);
    g2.fill(new java.awt.geom.Arc2D.Double(x, y, rectWidth, rectHeight, 90,
                                           135, Arc2D.OPEN));
    g2.setPaint(fg);
    g2.drawString("Filled Arc2D", x, stringY);
    x += gridWidth;

    // fill Ellipse2D.Double
    redtowhite = new java.awt.GradientPaint(x, y, red, x + rectWidth, y, white);
    g2.setPaint(redtowhite);
    g2.fill(new java.awt.geom.Ellipse2D.Double(x, y, rectWidth, rectHeight));
    g2.setPaint(fg);
    g2.drawString("Filled Ellipse2D", x, stringY);
    x += gridWidth;



    // fill and stroke GeneralPath
    int x3Points[] = {x, x + rectWidth, x, x + rectWidth};
    int y3Points[] = {y, y + rectHeight, y + rectHeight, y};
    java.awt.geom.GeneralPath filledPolygon = new java.awt.geom.GeneralPath(GeneralPath.WIND_EVEN_ODD,
                                                                            x3Points.length);
    filledPolygon.moveTo(x3Points[0], y3Points[0]);
    for (int index = 1; index < x3Points.length; index++) {
      filledPolygon.lineTo(x3Points[index], y3Points[index]);
    }
    ;
    filledPolygon.closePath();
    g2.setPaint(red);
    g2.fill(filledPolygon);
    g2.setPaint(fg);
    g2.draw(filledPolygon);
    g2.drawString("Filled and Stroked GeneralPath", x, stringY);
  }

  public static void main(String s[]) {
    javax.swing.JFrame f = new javax.swing.JFrame("graphics.carl.ShapesDemo2D");
    f.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    javax.swing.JApplet applet = new ShapesDemo2D();
    f.getContentPane().add("Center", applet);
    applet.init();
    f.pack();
    f.setSize(new java.awt.Dimension(550, 100));
    f.show();
  }

}
