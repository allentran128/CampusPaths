package hw5.test;

import org.junit.Test;

import hw5.Edge;
import hw5.Node;

public class TestEdge {
  
  @Test(expected=NullPointerException.class)
  public void ConstructNullTest() {
    Edge<Node<String>, String> first = new Edge<Node<String>, String>(null, null, "home");
  }
}
