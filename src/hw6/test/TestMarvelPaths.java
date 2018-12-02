package hw6.test;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import hw5.Edge;
import hw5.Node;

import hw6.MarvelParser.MalformedDataException;
import hw6.MarvelPaths;

public class TestMarvelPaths {

  @Test
  public void addNodeToEmptyConstructor() {
    MarvelPaths paths = new MarvelPaths();
    paths.addNode(new Node<String>("Adam"));
    paths.addNode(new Node<String>("Atlas"));
    assertFalse(paths.isEmpty());
  }
  
  @Test
  public void lexOrderPaths() {
    MarvelPaths paths = new MarvelPaths();
    Node<String> adam = new Node<String>("Adam");
    Node<String> atlas = new Node<String>("Atlas");
    paths.addNode(adam);
    paths.addNode(atlas);
    
    paths.addEdge(new Edge<Node<String>, String>(adam, atlas, "North"));
    paths.addEdge(new Edge<Node<String>, String>(adam, atlas, "East"));
    paths.addEdge(new Edge<Node<String>, String>(adam, atlas, "South"));
    
    List<Edge<Node<String>, String>> result = paths.findPath("Adam", "Atlas");
    System.out.println(result.get(0));
    assertTrue(result.get(0).toString().equals("(Adam, Atlas, East)"));
  }
  
  @Test
  public void loadFileThenAddMore() throws MalformedDataException {
    MarvelPaths paths = new MarvelPaths("src/hw6/data/staffSuperheroes.tsv");
    
    Node<String> adam = new Node<String>("Adam");
    paths.addNode(adam);
    paths.addEdge(new Edge<Node<String>, String>(adam, new Node<String>("Ernst-the-Bicycling-Wizard"), "Test"));
    List<Edge<Node<String>, String>> result = paths.findPath("Adam", "Ernst-the-Bicycling-Wizard");
    assertTrue(result.get(0).toString().equals("(Adam, Ernst-the-Bicycling-Wizard, Test)"));
  }
}
