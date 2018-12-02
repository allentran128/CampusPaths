package hw8.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw8.Model.CampusPaths;
import hw8.Model.CampusXY;

public class CampusPathsTest {

  @Test
  public void OnlyPathTest() throws MalformedDataException {
    CampusPaths campus = new CampusPaths();
    
    List<Edge<Node<CampusXY>, Double>> path = new LinkedList<Edge<Node<CampusXY>, Double>>();
    List<String> pathDirections = new LinkedList<String>();
    campus.findPath("CSE", "EEB", path, pathDirections);
    assertTrue(path != null);
    assertTrue(pathDirections != null);
    assertEquals(path.size(), pathDirections.size());
    
    double cost = 0.0;
    for (Edge<Node<CampusXY>, Double> seg : path) {
      cost += seg.getLabel().doubleValue();
    }
    
    assertTrue(cost == 33.33);
  }

}
