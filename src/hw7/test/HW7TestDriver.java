package hw7.test;

import java.io.*;
import java.util.*;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw7.MarvelPaths2;


/**
 * This class implements a testing driver which reads test scripts
 * from files for your graph ADT and improved MarvelPaths2application
 * using Dijkstra's algorithm.
 **/
public class HW7TestDriver {

    public static void main(String args[]) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            HW7TestDriver td;

            if (args.length == 0) {
                td = new HW7TestDriver(new InputStreamReader(System.in),
                                       new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File (fileName);

                if (tests.exists() || tests.canRead()) {
                    td = new HW7TestDriver(new FileReader(tests),
                                           new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java hw6.test.HW7TestDriver <name of input script>");
        System.err.println("to read from standard in: java hw6.test.HW7TestDriver");
    }

    private final Map<String, MarvelPaths2> graphs = new HashMap<String, MarvelPaths2>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new HW5TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public HW7TestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @effects Executes the commands read from the input and writes results to the output
     * @throws IOException if the input or output sources encounter an IOException
     **/
    public void runTests()
        throws IOException
    {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            if (command.equals("LoadGraph")) {
                loadGraph(arguments);
            } else if (command.equals("FindPath")) {
                findPath(arguments);
            } else if (command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void findPath(List<String> arguments) {
      if (arguments.size() != 3) {
        throw new CommandException("Bad arguments to findPath: " + arguments);
      }
      
      String graph = arguments.get(0);
      String src = arguments.get(1).replace((char) 95, (char) 32);
      String dst = arguments.get(2).replace((char) 95, (char) 32);
      
      findPath(graph, src, dst);
    }

    private void findPath(String graph, String src, String dst) {
      MarvelPaths2 path = graphs.get(graph);
      
      if (!path.isInGraph(src)) {
        output.println("unknown character " + src);
        if (!path.isInGraph(dst)) {
          output.println("unknown character " + dst);
        }
      } else {
        List<Edge<Node<String>, Double>> result = path.findPath(src, dst);
        output.println("path from " + src + " to " + dst + ":");
        
        if (result == null) {
          output.println("no path found");
        } else {
          double cost = 0.0;
          for (Edge<Node<String>, Double> e : result) {
            double weight = e.getLabel().doubleValue();
            cost += e.getLabel().doubleValue();
            String buildOutput = String.format("%s to %s with weight %.3f", e.getSrcNode(), e.getDstNode(), weight);
            output.println(buildOutput);
          }
          output.printf("total cost: %.3f", cost);
        }
      }
    }

    private void loadGraph(List<String> arguments) {
      if (arguments.size() != 2) {
        throw new CommandException("Bad arguments to loadGraph: " + arguments);
      }
      
      String filename = arguments.get(0);
      String dir = arguments.get(1);
      
      loadGraph(filename, dir);
    }
    
    private void loadGraph(String filename, String dir) {
      try {
        MarvelPaths2 path = new MarvelPaths2("src/hw6/data/" + dir);
        graphs.put(filename, path);
        output.println("loaded graph " + filename);
      } catch (MalformedDataException e) {
        System.out.println("Malformed Data in " + "src/hw6/data/" + dir);
        System.exit(0);
      }
    }
    
    private void createGraph(List<String> arguments) {
      if (arguments.size() != 1) {
          throw new CommandException("Bad arguments to CreateGraph: " + arguments);
      }

      String graphName = arguments.get(0);
      createGraph(graphName);
  }

    private void createGraph(String graphName) {
      graphs.put(graphName, new MarvelPaths2());
      
      String result = "created graph " + graphName;
      
      output.println(result);
    }

    private void addNode(List<String> arguments) {
      if (arguments.size() != 2) {
          throw new CommandException("Bad arguments to addNode: " + arguments);
      }

      String graphName = arguments.get(0);
      String nodeName = arguments.get(1);

      addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
      // Insert your code here.

      MarvelPaths2 thisGraph = graphs.get(graphName);
      Node<String> thisNode = new Node<String>(nodeName);
      thisGraph.addNode(thisNode);

      String result = "added node " + nodeName + " to " + graphName;
      
      output.println(result);
    }

    private void addEdge(List<String> arguments) {
      if (arguments.size() != 4) {
          throw new CommandException("Bad arguments to addEdge: " + arguments);
      }

      String graphName = arguments.get(0);
      String parentName = arguments.get(1);
      String childName = arguments.get(2);
      Double edgeLabel = Double.parseDouble(arguments.get(3)); // might need to check and let client know
      
      addEdge(graphName, parentName, childName, edgeLabel);  
    }

    private void addEdge(String graphName, String parentName, String childName,
          Double edgeLabel) {
    
      MarvelPaths2 thisGraph = graphs.get(graphName);
      
      Node<String> srcNode = new Node<String>(parentName);
      Node<String> dstNode = new Node<String>(childName);
      Edge<Node<String>, Double> thisEdge = new Edge<Node<String>, Double>(srcNode, dstNode, edgeLabel);
      thisGraph.addEdge(thisEdge);
      
      String buildOutput = String.format("added edge %.3f from %s to %s in %s", 
              thisEdge.getLabel().doubleValue(), parentName, childName, graphName);
      output.println(buildOutput);
    }

    private void listNodes(List<String> arguments) {
      if (arguments.size() != 1) {
          throw new CommandException("Bad arguments to listNodes: " + arguments);
      }

      String graphName = arguments.get(0);
      listNodes(graphName);
    }

    private void listNodes(String graphName) {
      MarvelPaths2 thisGraph = graphs.get(graphName);
      
      // Build list of nodes in string format
      Iterator<Node<String>> it = thisGraph.getNodes();
      String result = graphName + " contains: ";
      while (it.hasNext()) {
        result += it.next().toString() + " ";
      }
      result = result.trim(); // get rid of extra whitespace
      
      output.println(result);
    }

    private void listChildren(List<String> arguments) {
      if (arguments.size() != 2) {
          throw new CommandException("Bad arguments to listChildren: " + arguments);
      }

      String graphName = arguments.get(0);
      String parentName = arguments.get(1);
      listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
      // Insert your code here.

      MarvelPaths2 thisGraph = graphs.get(graphName);

      // Construct a list of children for parentName
      Node<String> parentNode = new Node<String>(parentName);
      Iterator<Edge<Node<String>, Double>> it = thisGraph.getEdges (parentNode);
      
      // Sort the list of children
      List<Edge<Node<String>, Double>> tempSort = new ArrayList<Edge<Node<String>, Double>>();
      while (it.hasNext()) {
        tempSort.add(it.next());
      }
      
      Collections.sort(tempSort, new Comparator<Edge<Node<String>, Double>>() {
        @Override
        public int compare(Edge<Node<String>, Double> b1, Edge<Node<String>, Double> b2) {
          int firstCheck = Double.compare(b1.getLabel().doubleValue(), b2.getLabel().doubleValue());
          if (firstCheck == 0) {
            return b1.getDstNode().toString().compareTo(b2.getDstNode().toString());
          }else {
            return firstCheck;
          }
        }
      });

      // Build a string representation
      String result = "the children of " + parentName + " in " + graphName + " are: ";
      for(Edge<Node<String>, Double> e : tempSort) {
        String buildOutput = String.format("%s(%.3f) ", e.getDstNode(), e.getLabel().doubleValue());
        result += buildOutput;
      }
      result = result.trim(); // get rid of extra whitespace

      output.println(result);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}

