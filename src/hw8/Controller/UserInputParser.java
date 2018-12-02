package hw8.Controller;

import java.util.Scanner;

/**
 * UserInputParser receives input from the user and parses
 * it into requests to the Model.
 *  
 * @author allentran
 * @deprecated instead the view/controller will be combined
 *             Reference MainClient.java
 */
@Deprecated
public class UserInputParser {
  
  public boolean parse(char input) {
    if (input == 'b') {
      // Request List of Buildings
      // Pass on the list to View
      return true;
    } else if (input == 'm') {
      // Tell View to print the menu of commands
      return true;
    } else if (input == 'q') {
      // Quit the REPL
      // return false to indicate the while loop to cease
      return false;
    } else if (input == 'r') {
      // Further Prompt the user for SRC and DST
      // Abbreviated building names
      // Make a request to Model
      // Pass on the result to View
      return true;
    } else {
      // Tell View that this is an unknown option
      return true;
    }
  }
  
  public void repl(Scanner in) {
    // Expected input style
    // <char> \n
    // So we read in nextLine
    // and check if length of the string is 1
    // then pass it to parse
    
    // Do something with Scanner to get input
    // While loop over parse( <preprocessed input>)
    // 
    
  }
}
