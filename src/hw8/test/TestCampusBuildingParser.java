package hw8.test;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import hw8.Model.CampusXY;
import hw8.Model.CampusBuildingParser;

public class TestCampusBuildingParser {

  @Test
  public void parsingSmallDataSetTest() {
    CampusXY cse = new CampusXY("Paul G. Allen Center for Computer Science & Engineering", "CSE", 2259.7112, 1715.5273);
    CampusXY den = new CampusXY("Denny Hall", "DEN", 1890.0, 892.57144);
    CampusXY eeb = new CampusXY("Electrical Engineering Building (North Entrance)", "EEB", 2159.9587, 1694.8192);
    
    Set<CampusXY> allBuildings = new TreeSet<CampusXY>();
    allBuildings.add(cse);
    allBuildings.add(den);
    allBuildings.add(eeb);
    
    Set<CampusXY> parseSet = CampusBuildingParser.parseBuildings("src/hw8/data/small_building_set.dat");
    assertEquals(allBuildings, parseSet);
  }

}
