package hw8.Model;

/**
 * Wrapper Class to hold the Campus Building's
 *  full name
 *  abbreviated name
 *  X coordinate
 *  Y coordinate
 * @author allentran
 *
 * AF(CampusXY) = { a street, where only the location field
 *  is set to a coordinate point and fullName and abrName are
 *  set to the empty string | a building, where each building
 *  has a unique abrName and unique fullName and a location set
 *  to a coordinate point
 * 
 * RI
 *  this.location != null
 *  this.fullName != null
 *  this.abrName != null
 */
public class CampusXY implements Comparable<CampusXY>{

  private String fullName;
  
  private String abrName;
  
  private Coordinate location;
  
  public CampusXY(Coordinate street) {
    this.location = street;
    this.fullName = "";
    this.abrName = "";
    checkRep();
  }
  
  /**
   * Constructor
   * 
   * @param fullName - full name of the building
   * @param abrName - abbreviated name of the building
   * @param x - x coordinate of the building
   * @param y - y coordinate of the building
   */
  public CampusXY(String fullName, String abrName, Double x, Double y) {
    this.fullName = fullName;
    this.abrName = abrName;
    this.location = new Coordinate(x,y);
    checkRep();
  }
  
  /**
   * Accessor to full name of building
   * @return the full name of the building
   */
  public String getFullName() {
    return this.fullName;
  }
  
  /**
   * Accessor to abbreviated name of building
   * @return the abbreviated name of tbe building
   */
  public String getAbrName() {
    return this.abrName;
  }
  
  /**
   * Accessor to the coordinate of the building
   * @return the coordinate of the building
   */
  public Coordinate getCoordinate() {
    return this.location;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CampusXY)) {
      return false;
    }
    CampusXY casted = (CampusXY) o;
    return casted.location.equals(this.location);
  }
  
  @Override
  public int hashCode() {
    return this.location.hashCode();
  }

  @Override
  public int compareTo(CampusXY o) {
    return this.fullName.compareTo(o.fullName);
  }
  
  @Override
  public String toString() {
    return this.abrName;
  }
  
  /**
   * Check Rep
   */
  public void checkRep() {
    assert this.location != null;
    assert this.fullName != null;
    assert this.abrName != null;
  }
}
