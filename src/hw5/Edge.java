package hw5;

/**
 * Implementation of a generic Edge
 * @author Allen Tran
 *
 *
 * AF(StringEdge this) = { (src, dst, label) where 
 *  src denotes the "from" node, and
 *  dst denotes the "to" node, and
 *  label denotes the name of the edge }
 *
 * Rep Invariant
 * src != null && dst != null && label != null
 */
public final class Edge<N,V> {

  private final N src;
  private final N dst;
  private final V label;
  
  /**
   * Constructor
   * 
   * @param dst - destination Node
   * @param label - label associated with the edge
   * @throws NullPointerException if src == null || dst == null || label == null
   */
  public Edge(N src, N dst, V label) {
    if (src == null || dst == null || label == null) {
      throw new NullPointerException();
    }
    
    this.src = src;
    this.dst = dst;
    this.label = label;
    checkRep();
  }
  
  /**
   * Src Node
   * 
   * @returns the source node
   */
  public N getSrcNode() {
    return this.src;
  }
  
  /**
   * Dst Node
   * 
   * @returns the destination node
   */
  public N getDstNode() {
    return this.dst;
  }
  
  /**
   * Label
   * 
   * @return the string label
   */
  public V getLabel() {
    return this.label;
  }
  
  /**
   * String Representation
   * @returns the string format of this StringNode
   * (dst, label)
   */
  public String toString() {
    return "(" + this.src.toString() + ", " 
        + this.dst.toString() + ", " + this.label + ")";
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Edge<?,?>)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Edge<N,V> casted = (Edge<N, V>) o;
    return casted.src.equals(this.src) && casted.dst.equals(this.dst)
          && casted.label.equals(this.label);
  }
  
  @Override
  public int hashCode() {
    return 11 * this.src.hashCode() 
         + 29 * this.dst.hashCode() 
         + 31 * this.label.hashCode();
  }
  
  /**
   * Check Rep
   */
  private void checkRep() {
    assert (this.src != null && this.dst != null && this.label != null);
  }
}
