package hw7;

import java.util.ArrayList;
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

import hw5.Edge;
import hw5.Graph;
import hw5.Node;
import hw6.MarvelParser;
import hw6.MarvelParser.MalformedDataException;

/**
 * Description : 
 *   MarvelPaths2 is a social network of marvel super heroes represented by a graph where
 *   we map pairs of marvel super heroes to a real number that represents the inverse
 *   of the number of comics they co-appear in.
 * 
 * 
 * AF(MarvelPaths2 this) = {
 *   this.nodes - each Node represents a unique marvel super hero and
 *     each Edge (hero1, hero2, weight) represents that 'hero2' co-appeared 
 *     in (1 / 'weight') comic books with 'hero1'.
 *    
 *     If constructed from a file, then pairs of heroes that co-appear in at least one book
 *     there is at most one edge that represents the relationship.
 *     Otherwise if it was constructed by manually adding in data, then there can be more than one
 *     Edge to represent the relationship between two nodes (hero1 to hero2 in that direction)
 *     but the path finding algorithm will only use the Edge with the smallest 'weight'.
 *    
 *     This graph maps Nodes to List of Edges.
 *    
 *   this.charToNode maps the hero's name to the node that they represent for all 
 *     nodes in this.nodes
 *    
 *   this.prev maps Nodes in this.nodes to the Edge that was used to access it.
 *  
 *   this.distance maps Nodes to their 'cost'
 * @author allentran
 *
 * RI
 *   For all nodes in this graph, n, n != null
 *   charToNode != null
 *   nodes != null
 *   prev != null
 *   distance != null
 *   For all edges in this graph, e, the weight of e, w, w > 0.0
 */
public class MarvelPaths2 extends Graph<String, Double>{

  private Map<String, Node<String>> charToNode;
    
  private Map<Node<String>, Edge<Node<String>, Double>> prev;
  
  private Map<Node<String>, Double> distance;
  
  private boolean testing = false;
  
  /**
   * Empty Marvel Graph
   * 
   * @modifies charToNode, nodes, prev
   * @effects charToNode is initialized
   * @effects nodes is initialized
   * @effects prev is initialized
   */
  public MarvelPaths2() {
    this.charToNode = new HashMap<String, Node<String>>();
    this.nodes = new HashMap<Node<String>, List<Edge<Node<String>, Double>>>();
    this.prev = new HashMap<Node<String>, Edge<Node<String>, Double>>();
    this.distance = new HashMap<Node<String>, Double>();
    
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
  public MarvelPaths2(String filename) throws MalformedDataException {
    // Init Globals
    this.charToNode = new HashMap<String, Node<String>>();
    this.nodes = new HashMap<Node<String>, List<Edge<Node<String>, Double>>>();
    this.prev = new HashMap<Node<String>, Edge<Node<String>, Double>>();
    this.distance = new HashMap<Node<String>, Double>();
    
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
    invertWeights();
    
    if (testing) {
      marvelCheckRep();
    }
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
    this.nodes.put(node, new ArrayList<Edge<Node<String>, Double>>());
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  /**
   * Add Edge to graph manually with weights
   * 
   * @requires r's src and dst node to be in the graph and r's label to be
   *           greater than 0.0
   * @param r - the edge to be added
   * @modifies this
   * @effects adds this edge to the graph if it is a valid edge
   */
  public void addEdge(Edge<Node<String>, Double> r) {
    Node<String> srcNode = r.getSrcNode();
    Node<String> dstNode = r.getDstNode();
    Double weight = r.getLabel().doubleValue();
    checkWeight(weight);
    
    if (this.nodes.containsKey(srcNode) && this.nodes.containsKey(dstNode)) {
      // Valid Edge
      this.nodes.get(srcNode).add(r);
    }
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  /**
   * Add Edge to graph via construction
   * 
   * @requires src and dst in graph
   * @param src - "from" node
   * @param dst - "to" node
   * @modifies this
   * @effects if edge is not in the graph then add a new one otherwise
   *          update the count on the edge
   */
  public void addEdge(Node<String> src, Node<String> dst) {
    // Lookup the Edge
    if (this.nodes.containsKey(src)) {
      Edge<Node<String>, Double> edge = findEdge(src, dst);
      if (edge != null) {
        Double count = edge.getLabel().doubleValue();
        this.nodes.get(src).remove(edge);
        this.nodes.get(src).add(new Edge<Node<String>, Double> (src, dst, count + 1.0));
      } else {
        this.nodes.get(src).add(new Edge<Node<String>, Double>(src, dst, 1.0));
      }
    }
    
    if (testing) {
      marvelCheckRep();
    }
  }
  
  /**
   * Finds the edge specified by src and dst in graph
   * 
   * @requires src and dst in graph
   * @param src - "from" node
   * @param dst - "to" node
   * @return the edge from src to dst if such edge exists, else null
   */
  private Edge<Node<String>, Double> findEdge(Node<String> src, Node<String> dst) {
    Iterator<Edge<Node<String>, Double>> it = getEdges(src);
    while (it.hasNext()) {
      Edge<Node<String>, Double> e = it.next();
      if (e.getDstNode().equals(dst)) {
        return e;
      }
    }
    return null;
  }

  /**
   * Invert the weights of each edge in the graph
   * 
   * Usage : path finding
   * 
   * @modifies this
   * @effects for each edge, sets label = (1 / label)
   */
  private void invertWeights() {
    Iterator<Node<String>> from = getNodes();
    while (from.hasNext()) {
      Node<String> currSrc = from.next();
      
      List<Edge<Node<String>, Double>> inverted = new ArrayList<Edge<Node<String>, Double>>();
      
      Iterator<Edge<Node<String>, Double>> to = getEdges(currSrc);
      while (to.hasNext()) {
        Edge<Node<String>, Double> destination = to.next();
        Double weight = destination.getLabel().doubleValue();
        inverted.add(new Edge<Node<String>, Double>(currSrc, destination.getDstNode(), (1 / weight)));
      }
      
      // Replace the prev edge list with the new inverted edge list
      this.nodes.put(currSrc, inverted);
    }
    
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
          
          // Via construction is symmetric
          this.addEdge(firstHero, secondHero);
          this.addEdge(secondHero, firstHero);
        }
      }
    }
    
    if (testing) {
      marvelCheckRep();
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
  public List<Edge<Node<String>, Double>> findPath(String src, String dst) {
    List<Edge<Node<String>, Double>> result = null;
    
    // Set to check the "visited" nodes
    Set<Node<String>> checked = new HashSet<Node<String>>();
    
    // Queue to process what to look at next
    Queue<Edge<Node<String>, Double>> unchecked = new PriorityQueue<Edge<Node<String>, Double>>(20, 
        new Comparator<Edge<Node<String>, Double>>() {
      @Override // TODO distance uses Nodes now
      public int compare(Edge<Node<String>, Double> e1, Edge<Node<String>, Double> e2) {
        // Should get distance to SrcNode + edge label (weight)
        // Default inf value should be Double.POSITIVE_INFINITY
        Double costToE1 = distance.get(e1.getSrcNode()) + e1.getLabel().doubleValue();
        Double costToE2 = distance.get(e2.getSrcNode()) + e2.getLabel().doubleValue();
        return Double.compare(costToE1, costToE2);
      }
    });
    
    // Find the src Node
    Node<String> srcNode = this.charToNode.get(src);
    
    // Find the dst Node
    Node<String> dstNode = this.charToNode.get(dst);
    
    // Sets all nodes to inf value
    initCostValue();
    
    // Add in consideration for self-ref case
    Edge<Node<String>, Double> self = new Edge<Node<String>, Double>(srcNode, srcNode, 0.0);
    this.distance.put(srcNode, 0.0);
    unchecked.add(self);
    this.prev.put(srcNode, self);
    
    // No longer needed b/c of the self-rep case
    //checked.add(srcNode);
    
    while (!unchecked.isEmpty()) {
      // Search adj nodes
      Edge<Node<String>, Double> curr = unchecked.remove(); // GET LOWEST COST NODE
      Node<String> minDest = curr.getDstNode();
      
      if (minDest.equals(dstNode)) {
        // Make the final chain
        this.prev.put(minDest, curr);
        
        // We found it, update the result and exit
        result = buildResult(srcNode, dstNode);
        
        // Reset the mappings
        this.prev.clear();
        
        return result;
      }
      
      if (checked.contains(minDest)) {
        continue;
      } else {
        // Need to eval all routes from this node
        Iterator<Edge<Node<String>, Double>> adjChildren = this.getEdges(minDest);
        
        // Convert Iterator to list so sorting is easier
        List<Edge<Node<String>, Double>> adjList = new LinkedList<Edge<Node<String>, Double>>();
        while (adjChildren.hasNext()) {
          adjList.add(adjChildren.next());
        }
        
        // Cost to minDest
        Double costToMinDest = this.distance.get(minDest);
        
        // Add children nodes to unchecked
        // link to previous only if cost is less than current cost
        while (!adjList.isEmpty()) {
          Edge<Node<String>, Double> childEdge = adjList.remove(0);
          Node<String> child = childEdge.getDstNode();
          
          if (!checked.contains(child)) {
            // Calculate the cost to get to child
            Double costToChild = costToMinDest + childEdge.getLabel().doubleValue();
            if (costToChild < this.distance.get(child)) {
              // Set up the cost
              this.distance.put(child, costToChild);
              // link paths
              this.prev.put(child, childEdge);
              // Add to the queue
              unchecked.add(childEdge);
            }
          }
        }
        
        // Establish that the path to minDest is a min path.
        checked.add(minDest); // This node is KNOWN
        this.prev.put(minDest, curr);
      }
    }
    
    // Reset the mappings
    this.prev.clear();
    
    return result;
  }
  
  /**
   * Init cost of nodes to inf
   */
  private void initCostValue() {
   Iterator<Node<String>> it = getNodes();
   while (it.hasNext()) {
     Node<String> item = it.next();
     this.distance.put(item, Double.POSITIVE_INFINITY);
   }
  }
  
  
  /**
   * Builds the path after successfully found a path
   * @param src - Starting Node
   * @param dst - Ending Node
   * @return a List of edges in the order to get from src to dst
   */
  private List<Edge<Node<String>, Double>> buildResult(Node<String> src, Node<String> dst) {
    List<Edge<Node<String>, Double>> result = new ArrayList<Edge<Node<String>, Double>>();
    Node<String> curr = dst;
    
    while (!curr.equals(src)) {
      Edge<Node<String>, Double> linkToCurr = this.prev.get(curr);
      // Add everything to the front in order to "reverse" list
      result.add(0, linkToCurr);
      curr = linkToCurr.getSrcNode();
    }
    
    return result;
  }
  
  /**
   * Check if weight is valid
   * 
   * @param weight - value to be checked
   * @throws IllegalArgumentException if weight <= 0
   */
  private void checkWeight(Double weight) {
    if (weight <= 0) {
      throw new IllegalArgumentException();
    }
  }

 /**
  * Check Rep
  */
  private void marvelCheckRep() {
    // Fields initialized
    assert this.charToNode != null;
    assert this.nodes != null;
    assert this.prev != null;
    assert this.distance != null;
    
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
    //  No weight less than or equal 0.0
    it = this.getNodes();
    while (it.hasNext()) {
      Node<String> n = it.next();
      Iterator<Edge<Node<String>, Double>> edges = this.getEdges(n);
      while (edges.hasNext()) {
        Edge<Node<String>, Double> e = edges.next();
        Double weight = e.getLabel().doubleValue();
        assert weight > 0.0;
        assert workingNodes.contains(e.getDstNode());
      }
    }
  }
}

