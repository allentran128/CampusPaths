package hw5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of generic Graph
 * @author Allen Tran
 *
 *
 * AF(Graph this) = { empty graph | map of nodes to list of their 
 * respective edges where the nodes in the map are unique and the lists
 * of edges may be empty or non-empty and may contain duplicate edges }
 * 
 * 
 * RI :
 * this.nodes != null
 */
public class Graph<K, V> {

  protected Map<Node<K>,List<Edge<Node<K>,V>>> nodes;
  
  /**
   * Constructor
   * Creates a new Graph
   * @modifies this.nodes
   * @effects initializes this.nodes
   */
  public Graph() {
    this.nodes = new HashMap<Node<K>, List<Edge<Node<K>,V>>>();
    checkRep();
  }
  
  /**
   * Add Node to graph
   * 
   * @requires node != null && !this.nodes.contains(node)
   * @param node - Node to be added to Graph
   * @modifies this.nodes
   * @effects adds a new key to this.nodes
   * @throws IllegalArgumentException if !(node instanceof Node<?>) 
   */
  public void addNode(Node<K> node) throws IllegalArgumentException {
    if (!(node instanceof Node<?>)) {
      throw new IllegalArgumentException("Wrong type of Node");
    }
    
    // Perform add
    if (!this.nodes.containsKey(node)) {
      // We need to add it into the Map, with an empty list
      this.nodes.put(node, new ArrayList<Edge<Node<K>,V>>());
    }
    checkRep();
  }

  /**
   * Add Edge to Graph
   * 
   * @requires r != null && r instance of Edge<?, ?>
   * && this.nodes.contains(r.dst)
   * @param r - Edge to be added
   * @modifies this.nodes
   * @effect adds a edge into the this.nodes
   */
  public void addEdge(Edge<Node<K>, V> r) {
    Node<K> src = r.getSrcNode();
    Node<K> dst = r.getDstNode();
    if (this.nodes.containsKey(src) && this.nodes.containsKey(dst)) {
      this.nodes.get(src).add(r);
    }
    checkRep();
  }

  /**
   * Empty Graph Check
   * 
   * @return true if this is empty, else false
   */
  public boolean isEmpty() {
    return this.nodes.isEmpty();
  }

  /**
   * Node Iterator
   * 
   * @return an iterator over the nodes of this graph
   */
  public Iterator<Node<K>> getNodes() {
    return this.nodes.keySet().iterator();
  }

  /**
   * Edge Iterator
   * 
   * @requires node != null && this.nodes.contains(node)
   * @return an iterator over the edge list associated with node
   */
  public Iterator<Edge<Node<K>,V>> getEdges(Node<K> node) {
    return this.nodes.get(node).iterator();
  }
  
  /**
   * String representation
   * 
   * @return the string representation of this graph
   */
  @Override
  public String toString() {
    String result = "";
    for (Node<K> n : this.nodes.keySet()) {
      result += n.toString() + "=[";
      for (Edge<Node<K>, V> e : this.nodes.get(n)) {
        String label = e.toString() + ", ";
        result += label;
      }
      result = result.substring(0, result.length() - 2); // get rid of last comma and space
      result += "] ";
    }
    return result.trim();
  }
  
  /**
   * Check Rep
   */
  private void checkRep() {
    assert this.nodes != null;
  }
}
