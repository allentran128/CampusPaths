package hw8.Model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import hw5.Edge;
import hw5.Graph;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;

/**
 * CampusPaths is a mapping from campus buildings,
 *  contained in CampusXY, to other campus buildings with
 *  an associated distance between them. Each building in
 *  CampusPaths is unique.
 * 
 * @author allentran
 * 
 * AF(CampusPaths this) = Once initialized,
 *  this.nodes - each Node represents a campus building and
 *     each Edge (bld1, bld2, distance) represents that 'bld2' is
 *     'distance' away from 'bld1'. This graph maps Nodes to List of Edges.
 *    
 *   this.abrBldList holds an internal copy of the list of the abbreviated
 *   names of buildings in CampusPaths
 *   
 * RI
 *  For all nodes in this graph, n, n != null
 *  this.abrBldList != null
 *  For all edges in this graph, e, the distance of e, d, d >= 0.0
 *  For all self referencing edges in this graph, the distance == 0.0
 */
public class CampusPaths extends Graph<CampusXY, Double>{
  
  private Map<String, String> abrBldList;
  
  private boolean testing = false;
  
  // Set files to use
  private String buildingFile = "src/hw8/data/campus_buildings.dat";
  private String pathsFile = "src/hw8/data/campus_paths.dat";
  
  /**
   * Constructs a new CampusPaths instance from data
   * specified by filePath
   * 
   * @param filePath - path to the data file
   * 
   * @modifies this.nodes
   * @effects loads in data from filePath into this.nodes
   * @throws MalformedDataException {@link CampusPathsParser#parsePaths(String, Map)}
   */
  public CampusPaths() throws MalformedDataException {
    Set<CampusXY> allBuilding = CampusBuildingParser.parseBuildings(buildingFile);
    
    // Turn this set into a List of abrNames AND a Map<Coordinate, node campusXY>
    abrBldList = parseToList(allBuilding);
    Map<Coordinate, Node<CampusXY>> lookup = parseToLookupTable(allBuilding);
    
    // Make the graph
    this.nodes = CampusPathsParser.parsePaths(pathsFile, lookup);
    
    if (testing) {
      checkRep();
    }
  }
  
  /**
   * List of all the buildings in CampusPaths
   * Deep copy of the list
   * @return a List of all of the abbreviated building names
   */
  public Map<String, String> allBuildings() {
    return new TreeMap<String, String>(this.abrBldList);
  }
  
  /**
   * Finds a path from src to dst
   * 
   * @param src - the "starting" building name
   * @param dst - the "destination" building name
   * @return a list of the paths to get from src to dst
   */
  public void findPath(String src, String dst, 
      List<Edge<Node<CampusXY>, Double>> pathResult, List<String> pathDirections) {
    // Helper data structures rather than adding additional fields to Nodes
    Map<Node<CampusXY>, Double> distance = new HashMap<Node<CampusXY>, Double>();
    Map<Node<CampusXY>, Edge<Node<CampusXY>, Double>> prev = 
        new HashMap<Node<CampusXY>, Edge<Node<CampusXY>, Double>>();
    
    // Set to check if the node is a min cost node along the path
    Set<Node<CampusXY>> checked = new HashSet<Node<CampusXY>>();
    
    // Queue to process what to look at next
    Queue<Edge<Node<CampusXY>, Double>> unchecked = new PriorityQueue<Edge<Node<CampusXY>, Double>>(10, 
              new Comparator<Edge<Node<CampusXY>, Double>>() {
      @Override 
      public int compare(Edge<Node<CampusXY>, Double> e1, Edge<Node<CampusXY>, Double> e2) {
        Double costToE1 = distance.get(e1.getSrcNode()) + e1.getLabel().doubleValue();
        Double costToE2 = distance.get(e2.getSrcNode()) + e2.getLabel().doubleValue();
        return Double.compare(costToE1, costToE2);
      }
    });
    
    // Find the src Node
    Node<CampusXY> srcNode = abrNameToCampus(src);
    
    // Find the dst Node
    Node<CampusXY> dstNode = abrNameToCampus(dst);
    
    // Sets all nodes to inf value
    Iterator<Node<CampusXY>> it = getNodes();
    while (it.hasNext()) {
      Node<CampusXY> item = it.next();
      distance.put(item, Double.POSITIVE_INFINITY);
    }
    
    // Add in consideration for self-ref case
    Edge<Node<CampusXY>, Double> self = new Edge<Node<CampusXY>, Double>(srcNode, srcNode, 0.0);
    distance.put(srcNode, 0.0);
    unchecked.add(self);
    prev.put(srcNode, self);
    
    // Impl. of Dijkstra's alg
    while (!unchecked.isEmpty()) {
      // Get the next lowest costing path
      Edge<Node<CampusXY>, Double> curr = unchecked.remove();
      // Get it's destination node
      Node<CampusXY> minDest = curr.getDstNode();
      
      if (minDest.equals(dstNode)) {
        // Make the final chain
        prev.put(minDest, curr);
        
        // We found it, update the result and exit
        buildResult(srcNode, dstNode, prev, pathResult);
        buildPathDirections(pathResult, pathDirections);
        break;
      }
      
      if (checked.contains(minDest)) {
        continue;
      } else {
        // Need to eval all routes from minDest
        Iterator<Edge<Node<CampusXY>, Double>> adjChildren = this.getEdges(minDest);
        
        // Convert Iterator to list so sorting is easier
        List<Edge<Node<CampusXY>, Double>> adjList = new LinkedList<Edge<Node<CampusXY>, Double>>();
        while (adjChildren.hasNext()) {
          adjList.add(adjChildren.next());
        }
        
        // Cost to minDest
        Double costToMinDest = distance.get(minDest);
        
        // Add children nodes to unchecked
        // link to previous only if cost is less than current cost
        while (!adjList.isEmpty()) {
          // Edges of minDest to 'child'
          Edge<Node<CampusXY>, Double> childEdge = adjList.remove(0);
          Node<CampusXY> child = childEdge.getDstNode();
          
          if (!checked.contains(child)) {
            // Calculate the cost to get to child
            Double costToChild = costToMinDest + childEdge.getLabel().doubleValue();
            if (costToChild < distance.get(child)) {
              // Set up the cost
              distance.put(child, costToChild);
              // link paths
              prev.put(child, childEdge);
              // Add to the queue
              unchecked.add(childEdge);
            }
          }
        }
        
        // Establish that the path to minDest is a min path.
        checked.add(minDest);
        prev.put(minDest, curr);
      }
    }
  }
  
  /**
   * Given an abbreviated campus name returns the
   * associated node
   * @requires this contains a node that has an abbreviated name 
   *  equal to abrName
   * @param abrName - abr name of campus building
   * @return the node associated to abrName
   */
  private Node<CampusXY> abrNameToCampus(String abrName) {
    Iterator<Node<CampusXY>> it = this.getNodes();
    while (it.hasNext()) {
      Node<CampusXY> item = it.next();
      if (item.getData().getAbrName().equals(abrName)) {
        return item;
      }
    }
    return null;
  }
  
  /**
   * Builds the path after successfully finding a path
   * 
   * @requires src and dst are in this, prev is not null,
   *  result is initialized and empty
   * @param src - Starting Node
   * @param dst - Ending Node
   * @param prev - Map that links nodes
   * @param result - a list to build upon
   * @modifies result
   * @effects adds Edges to result
   * @return a List of edges in the order to get from src to dst
   */
  private void buildResult(Node<CampusXY> src, Node<CampusXY> dst
      , Map<Node<CampusXY>, Edge<Node<CampusXY>, Double>> prev
      , List<Edge<Node<CampusXY>, Double>> result) {
    
    Node<CampusXY> curr = dst;
    
    while (!curr.equals(src)) {
      Edge<Node<CampusXY>, Double> linkToCurr = prev.get(curr);
      // Add everything to the front in order to "reverse" list
      result.add(0, linkToCurr);
      curr = linkToCurr.getSrcNode();
    }
  }
  
  /**
   * Builds an associated list of compass directions given a path and
   * a list to build upon
   * @requires pathDirections is initialized and empty
   * @param pathDirections - list to build result in
   * @param path - path of edges
   * @modifies pathDirections
   * @effect a list of compass directions is added to pathDirections
   */
  private void buildPathDirections(List<Edge<Node<CampusXY>, Double>> path
      , List<String> pathDirections) {
    
    for(Edge<Node<CampusXY>, Double> segment : path) {
      CampusXY start = segment.getSrcNode().getData();
      CampusXY end = segment.getDstNode().getData();
      double deltaX = Math.round(end.getCoordinate().getX()) - Math.round(start.getCoordinate().getX());
      double deltaY = Math.round(end.getCoordinate().getY()) - Math.round(start.getCoordinate().getY());
      double theta = Math.atan2(-deltaY, deltaX);
      String compassDir = thetaToDir(theta);
      pathDirections.add(compassDir);
    }
  }
  
  /**
   * Determines the compass direction given an angle theta
   * @param theta - the angle
   * @return a string representing the compass direction
   */
  private String thetaToDir(double theta) {
    Double thetaC = new Double(theta);
    if (thetaC.equals(-Math.PI) || theta < (-7 * Math.PI / 8)) { 
      return "W";
    } else if (theta > (-7 * Math.PI / 8) && theta < (-5 * Math.PI / 8)) { 
      return "SW";
    } else if (theta >= (-5 * Math.PI / 8) && theta <= (-3 * Math.PI / 8)) { 
      return "S";
    } else if (theta > (-3 * Math.PI / 8) && theta < (-1 * Math.PI / 8)) { 
      return "SE";
    } else if (theta >= (-1 * Math.PI / 8) && theta <= (Math.PI / 8)) {
      return "E";
    } else if (theta > (Math.PI / 8) && theta < (3 * Math.PI / 8)) { 
      return "NE";
    } else if (theta >= (3 * Math.PI / 8) && theta <= (5 * Math.PI / 8)) {
      return "N";
    } else if (theta > (5 * Math.PI / 8) && theta < (7 * Math.PI / 8)) {
      return "NW";
    } else if (theta >= (7 * Math.PI / 8) && theta <= Math.PI) {
      return "W";
    } else {
      return "Error";
    }
  }
  
  /**
   * Produces a map of building abr names to full names 
   * given a set of all the buildings
   * @param allBuilding - set of campus buildings
   * @return a map of building abr names to full names
   */
  private Map<String, String> parseToList(Set<CampusXY> allBuilding) {
    Map<String, String> result = new TreeMap<String, String>();
    for (CampusXY bld : allBuilding) {
      result.put(bld.getAbrName(), bld.getFullName());
    }
    return result;
  }
  
  /**
   * Produces a map of Coordinates to CampusXY given a set of
   * all buildings
   * @param allBuilding - set of campus buildings
   * @return a map of Coordinates to CampusXY
   */
  private Map<Coordinate, Node<CampusXY>> parseToLookupTable(Set<CampusXY> allBuilding) {
    Map<Coordinate, Node<CampusXY>> result = new HashMap<Coordinate, Node<CampusXY>>();
    for (CampusXY bld : allBuilding) {
      Coordinate location = bld.getCoordinate();
      result.put(location, new Node<CampusXY>(bld));
    }
    return result;
  }
  
  /**
   * Check rep
   */
  private void checkRep() {
    Iterator<Node<CampusXY>> it = this.getNodes();
    while (it.hasNext()) {
      Node<CampusXY> item = it.next();
      assert item != null;
      assert item.getData() != null;
      assert item.getData().getAbrName() != null;
      assert item.getData().getCoordinate() != null;
      assert item.getData().getFullName() != null;
      
      Iterator<Edge<Node<CampusXY>, Double>> adjNodes = this.getEdges(item);
      while (adjNodes.hasNext()) {
        Edge<Node<CampusXY>, Double> adj = adjNodes.next();
        assert adj.getLabel() != null;
        assert adj.getLabel().doubleValue() >= 0.0;
        if (adj.getSrcNode().equals(adj.getDstNode())) {
          assert adj.getLabel().doubleValue() == 0.0;
        }
      }
    }
  }
}
