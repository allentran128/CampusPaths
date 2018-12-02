package hw6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import hw5.Graph;
import hw5.Node;
import hw5.Edge;
import hw6.MarvelParser.MalformedDataException;

/**
 * Description : 
 * MarvelPaths is a social network of marvel super heroes represented by a graph where
 * we map pairs of marvel super heroes to comic books where they co-appear in.
 * 
 * 
 * AF(MarvelPaths this) = {
 *  this.nodes - each Node represents a unique marvel super hero and
 *    each Edge (hero2, book) represents that 'hero2' co-appeared in the 'book'.
 *    For pairs of characters that co-appear in many books
 *    there is an Edge for each book. This graph maps Nodes to List of Edges.
 *  this.charToNode maps the hero's name to the node that they represent for all 
 *    nodes in this.nodes
 *  this.prev maps Nodes in this.nodes to the Edge that was used to access it.
 *  
 * 
 * RI :
 * No Null nodes
 * All nodes are valid
 * all edges exists in graph
 *  for each edge, dst is in the graph
 * @author Allen Tran
 *
 */
public class MarvelPaths extends Graph<String, String>{

  private Map<String, Node<String>> charToNode;
  
  private Map<Node<String>, Edge<Node<String>, String>> prev;
  
  private boolean testing = false;
  
  /**
   * Empty Marvel Graph
   * 
   * @modifies charToNode, nodes, prev
   * @effects charToNode is initialized
   * @effects nodes is initialized
   * @effects prev is initialized
   */
  public MarvelPaths() {
    this.charToNode = new HashMap<String, Node<String>>();
    this.nodes = new HashMap<Node<String>, List<Edge<Node<String>, String>>>();
    this.prev = new HashMap<Node<String>, Edge<Node<String>, String>>();
  }
  
  /**
   * Add Node
   * 
   * @requires node != null && !this.nodes.contains(node)
   * @param node - Node to be added to Graph
   * @modifies this.nodes
   * @effects adds a new key to this.nodes
   * @throws IllegalArgumentException if !(node instanceof StringNode) 
   */
  @Override
  public void addNode(Node<String> node) {
    // Cast to right type of node
    String name = node.toString();
    this.charToNode.put(name, node);
  
    // We need to add it into the Map, with an empty list
    this.nodes.put(node, new ArrayList<Edge<Node<String>, String>>());
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  /**
   * Add Edge to Graph
   * 
   * @requires r != null && this.nodes.contains(r.src) 
   * && this.nodes.contains(r.dst)
   * @param r - Edge to be added
   * @modifies this.nodes
   * @effect adds a edge into the this.nodes
   */
  @Override
  public void addEdge(Edge<Node<String>, String> r) {
    String src = r.getSrcNode().toString();
    String dst = r.getDstNode().toString();
    if (this.charToNode.containsKey(src) 
        && this.charToNode.containsKey(dst)) {
      Node<String> srcNode = this.charToNode.get(src);
      Node<String> dstNode = this.charToNode.get(dst);
      Edge<Node<String>, String> arg = new Edge<Node<String>, String>(srcNode, dstNode, r.getLabel());
      this.nodes.get(srcNode).add(arg);
    }
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  
  /**
   * File MarvelPaths Constructor
   * Constructs a new MarvelPaths object
   * @param filename - path to the data file that will be used to construct the graph
   * @modifies charToNode, nodes, prev
   * @effects charToNode is populated with mappings from String to StringNode of
   *          for each valid key in the graph
   * @effects nodes is constructed using the data from filename
   * @effects prev is initialized
   * @throws MalformedDataException if filename leads to a file with bad data
   */
  public MarvelPaths(String filename) throws MalformedDataException {
    // Init Globals
    this.charToNode = new HashMap<String, Node<String>>();
    this.nodes = new HashMap<Node<String>, List<Edge<Node<String>, String>>>();
    this.prev = new HashMap<Node<String>, Edge<Node<String>, String>>();
    
    // Keep these data structures hidden from our clients
    // by having the responsibility on this class rather than clients of this class
    Set<String> chars = new HashSet<String>();
    Map<String, List<String>> comicCast = new HashMap<String, List<String>>();
    
    // Try to load in file
    try {
      MarvelParser.parseData(filename, chars, comicCast);
    } catch (MalformedDataException e) {
      // Need to handle this throwing an exception (translation)
      throw e;
    }
    
    populateCharToNode(chars);
    populateGraph(comicCast);
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  /**
   * Checks if name is in this graph
   * 
   * @param name - name of node
   * @return true if is in graph, else false
   */
  public boolean isInGraph(String name) {
    return this.charToNode.containsKey(name);
  }
  
  /**
   * Populate the Graph with Edges
   * 
   * @requires no duplicates in each List of Strings in comicCast
   * @modifies this.nodes 
   * @effects every edge is added to this.nodes
   */
  private void populateGraph(Map<String, List<String>> comicCast) {
    for (String comic : comicCast.keySet()) {
      // We want a local shallow copy of the cast of characters
      List<String> castOfCharacters = comicCast.get(comic);
      int castLength = castOfCharacters.size();
      
      for (int i = 0; i < castLength; i++) {
        // We grab the first character
        String firstCharacter = castOfCharacters.get(i);
        Node<String> firstHero = this.charToNode.get(firstCharacter);
        
        for (int j = i + 1; j < castLength; j++) {
          // We grab the second character
          String secondCharacter = castOfCharacters.get(j);
          Node<String> secondHero = this.charToNode.get(secondCharacter);
          this.addEdge(new Edge<Node<String>, String>(firstHero, secondHero, comic));
          this.addEdge(new Edge<Node<String>, String>(secondHero, firstHero, comic));
        }
      }
    }
  }

  /**
   * Populates charToNode with mappings
   * 
   * @param chars - the keys to add
   * @modifies this.charToNode
   * @effects adds key-value mappings to this.charToNode
   */
  private void populateCharToNode(Set<String> chars) {
    for (String hero : chars) {
      // chars is guaranteed to have no duplicates b/c Set RI
      Node<String> heroNode = new Node<String>(hero);
      
      this.addNode(heroNode);
    }
  }
  
  /**
   * Find Paths
   * finds a path from src to dst
   * @requires src, dst in graph && src != dst
   * @param src - Starting point
   * @param dst - Ending point
   * @modifies this.prev
   * @effects key-value mappings are added to this.prev, then this.prev is cleared on a clean exit
   * @return if a path exists, a List of edges in the order to get from src to dst
   *         else, null
   */
  public List<Edge<Node<String>, String>> findPath(String src, String dst) {
    List<Edge<Node<String>, String>> result = null;
    
    // Set to check the "visited" nodes
    Set<Node<String>> checked = new HashSet<Node<String>>();
    
    // Queue to process what to look at next
    Queue<Node<String>> unchecked = new LinkedList<Node<String>>();
    
    // Find the src Node
    Node<String> srcNode = this.charToNode.get(src);
    
    // Find the dst Node
    Node<String> dstNode = this.charToNode.get(dst);
    
    // Add it to the queue and checked
    unchecked.add(srcNode);
    checked.add(srcNode);
    
    while (!unchecked.isEmpty()) {
      // Search adj nodes
      Node<String> curr = unchecked.remove();
      
      if (curr.equals(dstNode)) {
        // We found it, update the result and exit
        result = buildResult(srcNode, dstNode);
        break;
      } else {
        // look through the adjacent nodes
        Iterator<Edge<Node<String>, String>> adjChildren = this.getEdges(curr);
        
        // Convert Iterator to list so sorting is easier
        List<Edge<Node<String>, String>> adjList = new LinkedList<Edge<Node<String>, String>>();
        while (adjChildren.hasNext()) {
          adjList.add(adjChildren.next());
        }
        
        // Sort the List of Edges in alphabetical order
        Collections.sort(adjList, new Comparator<Edge<Node<String>, String>>() {
          @Override
          public int compare(Edge<Node<String>, String> e1, Edge<Node<String>, String> e2) {
            int compareDst = e1.getDstNode().toString().compareTo(e2.getDstNode().toString());
            if (compareDst == 0) {
              return e1.getLabel().toString().compareTo(e2.getLabel().toString());
            } else {
              return compareDst;
            }
          }
        });
        
        // Add children nodes to unchecked
        while (!adjList.isEmpty()) {
          Edge<Node<String>, String> childEdge = adjList.remove(0);
          Node<String> child = childEdge.getDstNode();
          
          if (!checked.contains(child)) {
            // Haven't checked this child yet
            // Add to the queue
            unchecked.add(child);
            
            // Add to checked (so does not get overridden
            checked.add(child);
            
            // Map what node lead to this one (backwards)
            this.prev.put(child, childEdge);
          }
        }   
      }
    }
    
    // Reset the mappings
    this.prev.clear();
    
    return result;
  }
  
  /**
   * Builds the path after successfully found a path
   * @param src - Starting Node
   * @param dst - Ending Node
   * @return a List of edges in the order to get from src to dst
   */
  private List<Edge<Node<String>, String>> buildResult(Node<String> src, Node<String> dst) {
    // Add everything to the front in order to "reverse" list
    List<Edge<Node<String>, String>> result = new LinkedList<Edge<Node<String>, String>>();
    Node<String> curr = dst;
    while (curr != src) {
      Edge<Node<String>, String> linkToCurr = this.prev.get(curr);
      result.add(0, linkToCurr);
      curr = linkToCurr.getSrcNode();
    }
    return result;
  }

 /**
  * Check Rep
  * Checks if the representation invariants hold.
  */
  private void marvelCheckRep() {
    // No null Strings
    for (String s : this.charToNode.keySet()) {
      assert s != null;
    }
    
    Set<Node<String>> workingNodes = new HashSet<Node<String>>();
    
    // No null Nodes
    Iterator<Node<String>> it = this.getNodes();
    while (it.hasNext()) {
      Node<String> n = it.next();
      workingNodes.add(n);
      assert n != null;
      assert n.toString() != null;
    }
    
    // No bad Edges
    it = this.getNodes();
    while (it.hasNext()) {
      Node<String> n = it.next();
      Iterator<Edge<Node<String>, String>> edges = this.getEdges(n);
      while (edges.hasNext()) {
        Edge<Node<String>, String> e = edges.next();
        assert workingNodes.contains(e.getDstNode());
      }
    }
  }
}
