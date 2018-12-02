package hw8.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;

/**
 * Parser utility to load the Paths in between Campus buildings
 * @author allentran
 *
 */
public class CampusPathsParser {

  /**
   * Reads the Campus Paths dataset.
   * 
   * Each block of input from filename contains a starting (x,y) coordinate
   * followed by an indented list of ending (x,y) with the distances from
   * starting to ending points
   * 
   * @requires filename is a valid file path and allBuildings is not null
   * @param filename - the file that will be read
   * @param allBuildings - a map of Coordinates to their buildings
   * @returns a map of buildings to buildings + distance between
   * @throws MalformedDataException if the file is not well-formed:
   *          each block starts with an un-tabbed (x,y) coordinate
   *          followed by a single tab indented (x,y) coordinate with
   *          the distance between them
   *          or else starting with a # symbol to indicate a comment line.
   */
  public static Map<Node<CampusXY>, List<Edge<Node<CampusXY>, Double>>> parsePaths(String filename, Map<Coordinate, Node<CampusXY>> allBuildings) 
                                                                 throws MalformedDataException {
    BufferedReader reader = null;
    Map<Node<CampusXY>, List<Edge<Node<CampusXY>, Double>>> paths = new HashMap<Node<CampusXY>, List<Edge<Node<CampusXY>, Double>>>();
    try {
        reader = new BufferedReader(new FileReader(filename));

        // Construct the collections of characters and books, one
        // <character, book> pair at a time.
        String inputLine;
        Node<CampusXY> currBld = null; // Temporary
        while ((inputLine = reader.readLine()) != null) {

            // Ignore comment lines.
            if (inputLine.startsWith("#")) {
                continue;
            }
            
            inputLine = inputLine.replaceAll("\t", " ");
            if (!inputLine.startsWith(" ")) {
              // Beginning of block
              Coordinate bld = parseBld(inputLine);
              currBld = allBuildings.get(bld);
              if (currBld == null) {
                // Outdoor Path, make an addition
                if(!allBuildings.containsKey(bld)) {
                  allBuildings.put(bld, new Node<CampusXY>(new CampusXY(bld)));
                }
                currBld = allBuildings.get(bld);
              }
            } else {
              // mid block
              if (currBld == null) { 
                // Hasnt went through prev block first or no such building exists
                throw new MalformedDataException();
              }
              
              // Else append to list of edges
              String[] pathTo = inputLine.split(": ");
              Coordinate dstCoordinate = parseBld(pathTo[0].trim()); // Need to check if this is right
              
              // Check if dstCoordiante is a street or not
              if(!allBuildings.containsKey(dstCoordinate)) {
                  allBuildings.put(dstCoordinate, new Node<CampusXY>(new CampusXY(dstCoordinate)));
              }
              
              Node<CampusXY> dstBld = allBuildings.get(dstCoordinate);
              Double distance = Double.parseDouble(pathTo[1]);
              
              // Make sure currBld is in keySet
              if (!paths.containsKey(currBld)) {
                paths.put(currBld, new LinkedList<Edge<Node<CampusXY>, Double>>());
              }
              
              // Make sure dstBld is in keySet
              if (!paths.containsKey(dstBld)) {
                paths.put(dstBld, new LinkedList<Edge<Node<CampusXY>, Double>>());
              }
              
              paths.get(currBld).add(new Edge<Node<CampusXY>, Double>(currBld, dstBld, distance));
            }
        }
    } catch (IOException e) {
        System.err.println(e.toString());
        e.printStackTrace(System.err);
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println(e.toString());
                e.printStackTrace(System.err);
            }
        }
    }
    return paths;
  }
  
  /**
   * Parse input into a Coordinate
   * @param inputLine - string to be parsed
   * @return a new Coordinate instance
   */
  public static Coordinate parseBld(String inputLine) {
    String[] coordinatePt = inputLine.split(","); // need to check that this splits it correctly
    Double x = Double.parseDouble(coordinatePt[0]);
    Double y = Double.parseDouble(coordinatePt[1]);
    return new Coordinate(x,y);
  }
}
