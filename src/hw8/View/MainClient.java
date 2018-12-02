package hw8.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw8.Model.CampusPaths;
import hw8.Model.CampusXY;
import hw8.Model.Coordinate;

/**
 * The executable interface to the program
 * The client of this program's interface
 * @author allentran
 *
 */
public class MainClient {

  // Maybe use a run() method instead
  // or startup()
  public static void main(String[] args) {
    // Preload the Model
    // Load the interface
    // Then start the REPL
    try {
      CampusPaths campus = new CampusPaths();
      Scanner in = new Scanner(System.in);
      repl(in, campus);
    } catch (MalformedDataException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void repl(Scanner in, CampusPaths campus) {
    printMenu();
    prompt();
    String userInput;
    
    while((userInput = in.nextLine()) != null) {
      // Grab the command (the first char of the string)
      if (userInput.equals("")) {
        System.out.println(); 
        continue;
      }
      
      char input = userInput.charAt(0);
      
      // Parse
      if (input == 'b') {
        printBuildings(campus.allBuildings());
      } else if (input == 'm') {
        printMenu();
      } else if (input == 'q') {
        return;
      } else if (input == 'r') {
        System.out.print("Abbreviated name of starting building: ");
        String start = in.next();
        System.out.print("Abbreviated name of ending building: ");
        String finish = in.next();
        in.nextLine(); // Clear end of this token
        
        if (start.equals(finish)) {
          // self ref case - no longer handled by findPath so manually do so
          Map<String, String> buildings = campus.allBuildings();
          String fullName = buildings.get(start);
          System.out.printf("Path from %s to %s:\n", fullName, fullName);
          System.out.printf("Total distance: %d feet", 0.0);
        } else {
          List<Edge<Node<CampusXY>, Double>> pathResult = new LinkedList<Edge<Node<CampusXY>, Double>>();
          List<String> pathDirections = new LinkedList<String>();
          campus.findPath(start, finish, pathResult, pathDirections);
          printPath(pathResult, pathDirections);
        }
      } else if (input == '#') {
        System.out.println(userInput); // Echo empty line
        continue; // avoid re-prompting
      }else {
        // Tell View that this is an unknown option
        System.out.println("Unknown Option");
      }
      
      // prompt next
      prompt();
    } // End while loop
  }
  
  
  // VIEW methods 
  // dictates how the information is displayed to clients
  
  public static void printPath(List<Edge<Node<CampusXY>, Double>> path
        , List<String> directions) {
    if (path.size() == 0) {
      System.out.println("No path found");
    } else {
      CampusXY beginning = path.get(0).getSrcNode().getData();
      CampusXY ending = path.get(path.size()-1).getDstNode().getData();
      System.out.printf("Path from %s to %s:\n", beginning.getFullName(), ending.getFullName());
      double totalDist = 0.0;
      
      for (int i = 0; i < path.size(); i++) {
        Edge<Node<CampusXY>, Double> seg = path.get(i);
        Coordinate endLoc = seg.getDstNode().getData().getCoordinate();
        totalDist += seg.getLabel().doubleValue();
        System.out.printf("\tWalk %d feet %s to (%d, %d)\n", Math.round(seg.getLabel().doubleValue())
            , directions.get(i), Math.round(endLoc.getX()), Math.round(endLoc.getY()));
      }
      
      System.out.printf("Total distance: %d feet\n", Math.round(totalDist));
    }
  }
  
  public static void printBuildings(Map<String, String> buildings) {
    for (String bld : buildings.keySet()) {
      System.out.printf("%s: %s\n", bld, buildings.get(bld));
    }
  }
  
  public static void printMenu() {
    System.out.println("Menu:");
    System.out.println("\tr to find a route");
    System.out.println("\tb to see a list of all buildings");
    System.out.println("\tq to quit");
  }
  
  public static void prompt() {
    System.out.println();
    System.out.print("Enter an option ('m' to see the menu): ");
  }
}
