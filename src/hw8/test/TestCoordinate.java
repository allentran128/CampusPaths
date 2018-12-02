package hw8.test;

import static org.junit.Assert.*;

import org.junit.Test;

import hw8.Model.Coordinate;

public class TestCoordinate {

  @Test
  public void equivalanceTest() {
    Coordinate first = new Coordinate (11.11, 11.11);
    Coordinate second = new Coordinate (11.11, 11.11);
    assertEquals(first, second);
  }
  
  @Test
  public void associativeTest() {
    Coordinate first = new Coordinate (11.11, 11.11);
    Coordinate second = new Coordinate (11.11, 11.11);
    assertEquals(first, second);
    
    Coordinate third = new Coordinate(11.11, 11.11);
    assertEquals(second, third);
    
    assertEquals(first, second);
  }
  
  @Test
  public void nullTest() {
    Coordinate first = new Coordinate (11.11, 11.11);
    assertNotEquals(first, null);
  }
  
  @Test
  public void reflectiveTest() {
    Coordinate first = new Coordinate (11.11, 11.11);
    Coordinate second = new Coordinate (11.11, 11.11);
    assertEquals(first, second);
    assertEquals(second, first);
  }

}
