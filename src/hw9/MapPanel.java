package hw9;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JPanel;

import hw5.Edge;
import hw5.Node;
import hw8.Model.CampusXY;
import hw8.Model.Coordinate;

/**
 * MapPanel spans the image found in the imgPath.
 * It is responsible for drawing the paths on to the
 * image and for drawing circles around the selected buildings.
 * @author allentran
 *
 */
@SuppressWarnings("serial")
public class MapPanel extends JPanel {
  // Image info
  private double originalHeight = 2964.0;
  private double originalWidth = 4330.0;
  private String imgPath = "src/hw8/data/campus_map.jpg";
  private Image pic = Toolkit.getDefaultToolkit().getImage(imgPath);
  
  // Blds to be highlighted if not null
  private Coordinate fromBld = null;
  private Coordinate toBld = null;
  
  // Path to draw if not null
  private List<Edge<Node<CampusXY>, Double>> pathResult = null;
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    Graphics2D g2 = (Graphics2D) g;
    
    // Set drawing properties
    g2.setStroke(new BasicStroke(3));
    g2.setColor(Color.yellow);
    
    // Find scaling factor
    double scaleWidth = getWidth() / this.originalWidth;
    double scaleHeight = getHeight() / this.originalHeight;
    
    g2.drawImage(this.pic, 0, 0, getWidth(), getHeight(), this);
    
    // Checks if there is a from bld to be highlighted
    if (this.fromBld != null) {
      highlightBld(g2, fromBld, scaleWidth, scaleHeight);
    }
    
    // Checks if there is a to bld to be highlighted
    if (this.toBld != null) {
      highlightBld(g2, toBld, scaleWidth, scaleHeight);
    }
    
    // Checks if there is a path to be drawn
    if (this.pathResult != null) {
      drawPath(g2, scaleWidth, scaleHeight);
    }
  }
  
  /**
   * Sets the path to draw
   * @param pathResult - the path
   */
  public void setPathToPaint(List<Edge<Node<CampusXY>, Double>> pathResult) {
    this.pathResult = pathResult;
  }
  
  /**
   * Sets the fromBld
   * @param bld - the coordinates of the bld
   */
  public void setFromBld(Coordinate bld) {
    this.fromBld = bld;
  }
  
  /**
   * Sets the toBld
   * @param bld - the coordinates of the bld
   */
  public void setToBld(Coordinate bld) {
    this.toBld = bld;
  }
  
  /**
   * Draws a small circle around the coordinate
   * specified by bld
   * @param g2 - the Graphics used to draw
   * @param bld - coordinate of bld
   * @param scaleWidth - scaling factor in x direction
   * @param scaleHeight - scaling factor in the y direction
   */
  public void highlightBld(Graphics2D g2, Coordinate bld, double scaleWidth, double scaleHeight) {
    int x = (int) Math.round(bld.getX() * scaleWidth);
    int y = (int) Math.round(bld.getY() * scaleHeight);
    
    Ellipse2D.Double circle = new Ellipse2D.Double(x-25, y-25, 50, 50);
    g2.draw(circle);
  }
   
  /**
   * Draws the path on the map
   * @param g2 - the Graphics used to draw
   * @param pic - the image of the map
   * @param scaleWidth - scaling factor in x direction
   * @param scaleHeight - scaling factor in the y direction
   */
  public void drawPath(Graphics2D g2, double scaleWidth, double scaleHeight) {
    for(Edge<Node<CampusXY>, Double> seg : this.pathResult) {
      // Draw a yellow line from seg.src to seg.dst
      // using the Coordinates after scaling
      
      Coordinate begin = seg.getSrcNode().getData().getCoordinate();
      int beginX = (int) Math.round(begin.getX().doubleValue() * scaleWidth);
      int beginY = (int) Math.round(begin.getY().doubleValue() * scaleHeight);
      
      Coordinate end = seg.getDstNode().getData().getCoordinate();
      int endX = (int) Math.round(end.getX().doubleValue() * scaleWidth);
      int endY = (int) Math.round(end.getY().doubleValue() * scaleHeight);
      
      // Draw the line
      g2.drawLine(beginX, beginY, endX, endY);
    }
  }
}
