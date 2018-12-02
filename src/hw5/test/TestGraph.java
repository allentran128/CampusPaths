package hw5.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hw5.Edge;
import hw5.Graph;
import hw5.Node;

import org.junit.After;

/**
 * String Graph Tests
 * 
 * @author allentran
 *
 */
public class TestGraph {
  
  List<Node<String>> pets;
  Graph<String, String> friends;
  
  @Before 
  public void init() {
    pets = new ArrayList<Node<String>>();
    pets.add(new Node<String>("dog"));
    pets.add(new Node<String>("cat"));
    
    friends = new Graph<String, String>();
  }

  @Test
  public void addNodeTest() {
    
    for(Node<String> pet : pets) {
      friends.addNode(pet);
    }
    
    assertFalse(friends.isEmpty());
    Iterator<Node<String>> it = friends.getNodes();
    for (int i = 0; i < 2; i++) {
      Node<String> thisPet = it.next();
      assertTrue(pets.contains(thisPet));
    }
    
  }

  @Test
  public void isEmptyTest() {
    
    assertTrue(friends.isEmpty());
    
    for(Node<String> pet : pets) {
      friends.addNode(pet);
      assertFalse(friends.isEmpty());
    }
  }
  
  @Test
  public void addEdgeTest() {
    
    for(Node<String> pet : pets) {
      friends.addNode(pet);
    }
    
    assertFalse(friends.isEmpty());
    Iterator<Node<String>> it = friends.getNodes();
    while(it.hasNext()) {
      assertTrue(pets.contains(it.next()));
    }
    
    friends.addEdge(new Edge<Node<String>, String>(pets.get(0), pets.get(1), "is older"));
    friends.addEdge(new Edge<Node<String>, String>(pets.get(1), pets.get(0), "is younger"));
    
    Iterator<Edge<Node<String>, String>> fromDog = friends.getEdges(pets.get(0));
    Edge<Node<String>, String> fromDogToCat = fromDog.next();
    
    assertTrue(fromDogToCat.getDstNode().equals(pets.get(1)));
    assertTrue(fromDogToCat.getLabel().equals("is older"));
    
    Iterator<Edge<Node<String>, String>> fromCat = friends.getEdges(pets.get(1));
    Edge<Node<String>, String> fromCatToDog = fromCat.next();
    assertTrue(fromCatToDog.getDstNode().equals(pets.get(0)));
    assertTrue(fromCatToDog.getLabel().equals("is younger"));
  }
  
  @After
  public void cleanup() {
    pets.clear();
  }
}
