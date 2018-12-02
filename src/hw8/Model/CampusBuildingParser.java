package hw8.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import hw6.MarvelParser.MalformedDataException;

/**
 * Parser utility to load the Campus buildings
 * @author allentran
 *
 */
public class CampusBuildingParser {

  /**
   * Reads the Campus Buildings dataset.
   * 
   * Each line constitutes of abbreviated name,
   * full name, x coordinate and then y coordinate
   * all separated by space(s)
   * 
   * @requires filename is a valid file path
   * @param filename - the file that will be read
   * @returns a map of buildings to buildings + distance between
   * @throws MalformedDataException if the file is not well-formed:
   *          each line starts with abbreviated name, full name, x,
   *          then y all separated by space(s)
   *          or else starting with a # symbol to indicate a comment line.
   */
  public static Set<CampusXY> parseBuildings(String filename) {
    BufferedReader reader = null;
    Set<CampusXY> buildings = new TreeSet<CampusXY>();
    try {
        reader = new BufferedReader(new FileReader(filename));

        // Construct the collections of characters and books, one
        // <character, book> pair at a time.
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {

            // Ignore comment lines.
            if (inputLine.startsWith("#")) {
                continue;
            }
          
            String[] tokens = inputLine.replaceAll("\t", " ").split(" +");
            Double x = Double.parseDouble(tokens[tokens.length - 2]);
            Double y = Double.parseDouble(tokens[tokens.length - 1]);
            String abrName = tokens[0];
            int start = 1;
            if (tokens[start].startsWith("(")) {
              if (tokens[start].endsWith(")")) {
                abrName += " " + tokens[start];
                start++;
              } else {
                do {
                  abrName += " " + tokens[start];
                  start++;
                } while (!tokens[start].endsWith(")"));
                abrName += " " + tokens[start];
                start++;
              }
            }
            
            String fullName = "";
            for (int i = start; i < tokens.length - 2; i++) {
              fullName += tokens[i] + " ";
            }
            fullName = fullName.trim();
            
            CampusXY bld = new CampusXY(fullName, abrName, x, y);
            buildings.add(bld);
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
    return buildings;
  }
}
