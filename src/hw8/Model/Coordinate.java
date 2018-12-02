package hw8.Model;

/**
 * Represents a geographical coordinate point
 * @author allentran
 *
 * AF(this) = { (x, y) where x and y are doubles }
 * 
 * RI
 *  this.x != null
 *  this.x >= 0.0
 *  this.y != null
 *  this.y >= 0.0
 */
public class Coordinate {
  
  private Double x;
  
  private Double y;
  
  /**
   * Constructs a Coordinate
   * 
   * @param x - x value
   * @param y - y value
   * @modifies this
   * @effects populates the Coordinate fields
   */
  public Coordinate(Double x, Double y) {
    this.x = x;
    this.y = y;
    
    checkRep();
  }
  
  /**
   * Gets the x value
   * @return the x value
   */
  public Double getX() {
    return this.x;
  }
  
  /**
   * Gets the y value
   * @return the y value
   */
  public Double getY() {
    return this.y;
  }
  
  /**
   * Determines equality
   * @param o - Object to be compared to
   * @returns true if o equals this, else false
   */
  // TODO All EQ checks
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Coordinate)) {
      return false;
    }
    Coordinate castedArg = (Coordinate) o;
    return Double.compare(castedArg.x, this.x) == 0 
        && Double.compare(castedArg.y, this.y) == 0;
  }
  
  /**
   * HashCode
   * returns the hash code of this
   */
  @Override
  public int hashCode() {
    return 31 * Double.hashCode(this.x) +  37 * Double.hashCode(this.y);
  }
  
  /**
   * Check Rep
   */
  public void checkRep() {
    assert this.x != null;
    assert this.x >= 0;
    assert this.y != null;
    assert this.y >= 0;
  }
  
  /**
   * String Representation
   */
  @Override
  public String toString() {
    return String.format("(%f, %f)", this.x, this.y);
  }
}
