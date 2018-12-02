package hw5.test;

import static org.junit.Assert.*;

import org.junit.Test;

import hw5.Node;

public class TestNode {

  @Test
  public void EqualityReflectiveTest() {
    Node<String> first = new Node<String>("test");
    assertTrue(first.equals(first));
  }
  
  @Test
  public void EqualitySymmetricTest() {
    Node<String> first = new Node<String>("test");
    Node<String> second = new Node<String>("test");
    assertTrue(first.equals(second));
    assertTrue(second.equals(first));
  }

  @Test
  public void EqualityTransitiveTest() {
    Node<String> first = new Node<String>("test");
    Node<String> second = new Node<String>("test");
    Node<String> third = new Node<String>("test");
    assertTrue(first.equals(second));
    assertTrue(second.equals(third));
    assertTrue(first.equals(third));
  }
  
  @Test
  public void EqualityConsistentTest() {
    Node<String> first = new Node<String>("test");
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    Node<String> second = new Node<String>("Hello World");
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
    assertTrue(first.equals(first));
  }
  
  @Test
  public void EqualityNonNullTest() {
    Node<String> first = new Node<String>("test");
    assertFalse(first.equals(null));
  }
  
  @Test
  public void EqualityObjectTest() {
    Node<String> first = new Node<String>("test");
    Object firstObj = first;
    Object emptyObj = new Object();
    
    assertTrue(first.equals(firstObj));
    assertTrue(firstObj.equals(first));
    assertFalse(first.equals(emptyObj));
    assertFalse(emptyObj.equals(firstObj));
  }
}
