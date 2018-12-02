package hw8.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw8.Model.CampusBuildingParser;
import hw8.Model.CampusPathsParser;
import hw8.Model.CampusXY;
import hw8.Model.Coordinate;

public class TestCampusPathsParser {

  @Test
  public void simpleParseTest() throws MalformedDataException {
    Set<CampusXY> parseSet = CampusBuildingParser.parseBuildings("src/hw8/data/small_building_set.dat");
    Map<Coordinate, Node<CampusXY>> lookup = new HashMap<Coordinate, Node<CampusXY>>();
    for(CampusXY bld : parseSet) {
      Coordinate geo = bld.getCoordinate();
      lookup.put(geo, new Node<CampusXY>(bld));
    }
    
    Map<Node<CampusXY>, List<Edge<Node<CampusXY>, Double>>> result = CampusPathsParser.parsePaths("src/hw8/data/small_paths_set.dat", lookup);
    
    Coordinate cse = new Coordinate(2259.7112, 1715.5273);
    Node<CampusXY> cseBld = lookup.get(cse);
    
    Coordinate dst = new Coordinate(1890.0, 892.57144);
    Node<CampusXY> dstBld = lookup.get(dst);
    
    assertTrue(result.containsKey(lookup.get(cse)));
    Edge<Node<CampusXY>, Double> item = new Edge<Node<CampusXY>, Double>(cseBld, dstBld, 11.11);
    assertTrue(result.get(cseBld).contains(item));
    
    Coordinate eeb = new Coordinate(2159.9587, 1694.8192);
    Node<CampusXY> eebBld = lookup.get(eeb);
    assertTrue(result.get(dstBld).contains(new Edge<Node<CampusXY>, Double>(dstBld, eebBld, 22.22)));
  }

}
