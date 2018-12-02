package hw5;

/**
 * Implementation of a generic Node
 * @author Allen
 *
 * AF(StringNode this) = { data is the value associated with this node }
 *
 * Rep Invariant
 * data != null 
 */
public final class Node<K> {

  private final K data;
  
  /**
   * Constructor
   * Creates a new Node
   * 
   * @param value
   * @throws NullPointerException if value == null 
   */
  public Node(K value) throws NullPointerException {
    if (value == null) {
      throw new NullPointerException();
    }
    
    this.data = value;
    checkRep();
  }

  /**
   * Equality
   * 
   * @param other - Object to compare with
   * @returns true if other is equal with this, else false
   */
  @Override
  public boolean equals(Object other) { 
    if (other == null) {
      return false;
    }
    
    // Type Check
    if (!(other instanceof Node<?>)) {
      return false;
    }
    
    // Cast to right type
    Node<?> comparedNode = (Node<?>) other;
    return this.data.equals(comparedNode.data);
  }
  
  /**
   * Hash Code
   * 
   * @return hashcode for this Node
   */
  @Override
  public int hashCode() {
    return this.data.hashCode();
  }
  
  /**
   * Immutable copy of the data
   * 
   * @requires K to be an immutable type
   * @returns this.data
   */
  public K getData() {
    return this.data;
  }
  
  /**
   * String Representation
   * 
   * @returns the string format of this StringNode
   */
  public String toString() {
    return this.data.toString();
  }
  
  /**
   * Check Rep
   */
  private void checkRep() {
    assert this.data != null;
  }
}
