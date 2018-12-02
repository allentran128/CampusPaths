package hw7.test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw7.MarvelPaths2;

public class TestMarvelPaths2 {

  @Test
  public void emptyConstruct() {
    MarvelPaths2 path = new MarvelPaths2();
    assertTrue(path.isEmpty());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void zeroWeight() {
    MarvelPaths2 path = new MarvelPaths2();
    
    Node<String> meng = new Node<String>("Meng");
    Node<String> xu = new Node<String>("Xu");
    
    path.addNode(meng);
    path.addNode(xu);
    
    path.addEdge(new Edge<Node<String>, Double>(meng, xu, 0.0));
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void negativeWeight() {
    MarvelPaths2 path = new MarvelPaths2();
    
    Node<String> meng = new Node<String>("Meng");
    Node<String> xu = new Node<String>("Xu");
    
    path.addNode(meng);
    path.addNode(xu);
    
    path.addEdge(new Edge<Node<String>, Double>(meng, xu, -1.0));
  }
  
  @Test
  public void emptyFile() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/emptyData.tsv");
    assertTrue(path.isEmpty());
  }
  
  @Test(expected=MalformedDataException.class)
  public void badFormatFile() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/badData.tsv");
  }
  
  @Test
  public void manualConstructFromEmpty() {
    MarvelPaths2 path = new MarvelPaths2();
    path.addNode(new Node<String>("Meng"));
    path.addNode(new Node<String>("Xu"));
    path.addNode(new Node<String>("Chu"));
    assertTrue(path.isInGraph("Meng"));
    assertTrue(path.isInGraph("Xu"));
    assertTrue(path.isInGraph("Chu"));
  }
  
  @Test
  public void findPathsFromManual() {
    MarvelPaths2 path = new MarvelPaths2();
    Node<String> meng = new Node<String>("Meng");
    Node<String> xu = new Node<String>("Xu");
    Node<String> chu = new Node<String>("Chu");
    
    path.addNode(meng);
    path.addNode(xu);
    path.addNode(chu);
    
    path.addEdge(new Edge<Node<String>, Double>(meng, xu, 1.1));
    path.addEdge(new Edge<Node<String>, Double>(meng, chu, 1.0));
    path.addEdge(new Edge<Node<String>, Double>(chu, xu, 0.05));
    
    List<Edge<Node<String>, Double>> result = path.findPath("Meng", "Xu");
    double cost = 0.0;
    for (Edge<Node<String>, Double> e : result) {
      System.out.println(e.toString());
      cost += e.getLabel().doubleValue();
    }
    assertTrue(cost == 1.05);
  }
  
  @Test
  public void findPathsFromManual2() {
    MarvelPaths2 path = new MarvelPaths2();
    Node<String> meng = new Node<String>("Meng");
    Node<String> xu = new Node<String>("Xu");
    Node<String> chu = new Node<String>("Chu");
    Node<String> li = new Node<String>("Li");
    
    path.addNode(meng);
    path.addNode(xu);
    path.addNode(chu);
    path.addNode(li);
    
    path.addEdge(new Edge<Node<String>, Double>(meng, li, 2.0));
    path.addEdge(new Edge<Node<String>, Double>(meng, chu, 1.0));
    path.addEdge(new Edge<Node<String>, Double>(li, xu, 1.0));
    path.addEdge(new Edge<Node<String>, Double>(chu, xu, 5.0));
    
    List<Edge<Node<String>, Double>> result = path.findPath("Meng", "Xu");
    double cost = 0.0;
    for (Edge<Node<String>, Double> e : result) {
      System.out.println(e.toString());
      cost += e.getLabel().doubleValue();
    }
    assertTrue(cost == 3.0);
  }
  
  @Test
  public void findPathsFromHybrid() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/staffSuperheroes.tsv");
    Node<String> meng = new Node<String>("Meng");
    Node<String> xu = new Node<String>("Xu");
    Node<String> chu = new Node<String>("Chu");
    
    path.addNode(meng);
    path.addNode(xu);
    path.addNode(chu);
    
    path.addEdge(new Edge<Node<String>, Double>(meng, xu, 1.1));
    path.addEdge(new Edge<Node<String>, Double>(meng, chu, 1.0));
    path.addEdge(new Edge<Node<String>, Double>(chu, xu, 0.05));
    path.addEdge(new Edge<Node<String>, Double>(meng, new Node<String>("Ernst-the-Bicycling-Wizard"), 1.5));
    path.addEdge(new Edge<Node<String>, Double>(xu, new Node<String>("Ernst-the-Bicycling-Wizard"), 0.3));
    
    List<Edge<Node<String>, Double>> result = path.findPath("Meng", "Ernst-the-Bicycling-Wizard");
    double cost = 0.0;
    for (Edge<Node<String>, Double> e : result) {
      cost += e.getLabel().doubleValue();
    }
    assertTrue(cost == 1.35);
  }
  
  @Test
  public void smallFileConstruct() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/staffSuperheroes.tsv");
    assertFalse(path.isEmpty());
  }
  
  @Test
  public void allNodesAdded() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/staffSuperheroes.tsv");
    assertTrue(path.isInGraph("Ernst-the-Bicycling-Wizard"));
    assertTrue(path.isInGraph("Notkin-of-the-Superhuman-Beard"));
    assertTrue(path.isInGraph("Perkins-the-Magical-Singing-Instructor"));
    assertTrue(path.isInGraph("Grossman-the-Youngest-of-them-all"));
  }
  
  @Test
  public void smallFileInvertedWeights() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/staffSuperheroes.tsv");
    Iterator<Node<String>> from = path.getNodes();
    while (from.hasNext()) {
      Node<String> currSrc = from.next();
      Iterator<Edge<Node<String>, Double>> to = path.getEdges(currSrc);
      while (to.hasNext()) {
        Edge<Node<String>, Double> destination = to.next();
        Double weight = destination.getLabel().doubleValue();
        assertTrue(weight > 0.0);
        // Since construction via file, no weight should be greater than 1
        assertTrue(weight <= 1.0);
      }
    }
  }

  @Test
  public void staffHeroesPaths() throws MalformedDataException {
    MarvelPaths2 path = new MarvelPaths2("src/hw7/data/staffSuperheroes.tsv");
    List<Edge<Node<String>, Double>> result = path.findPath("Ernst-the-Bicycling-Wizard", "Notkin-of-the-Superhuman-Beard");
    double cost = 0.0;
    for (Edge<Node<String>, Double> e : result) {
      cost += e.getLabel().doubleValue();
    }
    assertTrue(cost == 0.5);

    cost = 0.0;
    result = path.findPath("Ernst-the-Bicycling-Wizard", "Perkins-the-Magical-Singing-Instructor");
    for (Edge<Node<String>, Double> e : result) {
      cost += e.getLabel().doubleValue();
    }
    assertTrue(cost == 1.0);
  }
}
