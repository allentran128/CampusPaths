package hw9;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hw5.Edge;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
import hw8.Model.CampusPaths;
import hw8.Model.CampusXY;

/**
 * The Executable GUI for Campus Maps
 * Features highlighting buildings on the map
 * and drawing paths on the map between two buildings.
 * @author allentran
 *
 */
public class CampusPathsMain {

  // Min size
  public static int width = 1024;
  public static int height = 768;
  
  // Path to img
  public static String imgPath = "src/hw8/data/campus_map.jpg";
  
  // Path finding model
  public CampusPaths paths;
  
  // GUI 
  public JFrame frame;
  public JPanel mapDisplay;
  public JButton reset;
  public JTextField src;
  public JTextField dst;
  public JButton find;
  
  /**
   * Constructor
   * Constructs and runs the application GUI
   * @throws MalformedDataException
   */
  public CampusPathsMain() throws MalformedDataException {
    // Initialize Path Finder
    paths = new CampusPaths();
    
    // Top level window
    frame = new JFrame("Campus Map");
    frame.setPreferredSize(new Dimension(width, height));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Map Display
    mapDisplay = new MapPanel();
    frame.add(mapDisplay, BorderLayout.CENTER);

    // User input
    JPanel inputArea = new JPanel();
    
    // Reset Btn
    reset = new JButton("Reset");
    reset.addActionListener(new ResetListener());
    
    // From text input
    JLabel from = new JLabel("From");
    src = new JTextField(7);
    JButton selectFrom = new JButton("Select");
    selectFrom.addActionListener(new ShowBuildingListener(src, "FROM"));
    
    // To text input
    JLabel to = new JLabel("To");
    dst = new JTextField(7);
    JButton selectTo = new JButton("Select");
    selectTo.addActionListener(new ShowBuildingListener(dst, "TO"));
    
    // Find Path btn
    find = new JButton("Find Path");
    find.addActionListener(new FindPathListener());
    
    // Pack inside of UI panel
    inputArea.add(reset);
    inputArea.add(from);
    inputArea.add(src);
    inputArea.add(selectFrom);
    inputArea.add(to);
    inputArea.add(dst);
    inputArea.add(selectTo);
    inputArea.add(find);
    frame.add(inputArea, BorderLayout.SOUTH);
    
    // Finalize window
    frame.pack();
    frame.setVisible(true);
  }
  
  public static void main(String[] args) throws MalformedDataException {
    new CampusPathsMain();
  }
  
  /**
   * Action Listener for Reset button
   * @author allentran
   * Resets the GUI to the initial state
   */
  class ResetListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println("RESET");
      ((MapPanel)mapDisplay).setPathToPaint(null);
      src.setText("");
      ((MapPanel)mapDisplay).setFromBld(null);
      dst.setText("");
      ((MapPanel)mapDisplay).setToBld(null);
      frame.setSize(new Dimension(width, height));
      frame.repaint();
    }
  }
  
  /**
   * Action Listener for find paths button
   * @author allentran
   * Finds the path given the two inputs
   * Cap insensitive input
   */
  class FindPathListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // Reformat input to all uppercase
      String srcBld = src.getText().toUpperCase();
      String dstBld = dst.getText().toUpperCase();
      
      if(!(srcBld.isEmpty() || dstBld.isEmpty())) {
        // Both fields have to be set before anything is run
        // Find Path
        List<Edge<Node<CampusXY>, Double>> pathResult = new LinkedList<Edge<Node<CampusXY>, Double>>();
        paths.findPath(srcBld, dstBld, pathResult, new LinkedList<String>());
        
        // Now that we have the result, pass it to mapDisplay
        ((MapPanel)mapDisplay).setPathToPaint(pathResult);
        System.out.println("Calling a frame repaint after finding path");
        frame.repaint();
      }
    } 
  }
  
  /**
   * Highlight the building on the map
   * @author allentran
   * If the input is valid, then the building on the
   * map will be highlighted
   */
  class ShowBuildingListener implements ActionListener {
    private JTextField box;
    private String name;
    
    public ShowBuildingListener(JTextField box, String name) {
      this.box = box;
      this.name = name;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!box.getText().isEmpty()) {
        String input = box.getText().toUpperCase();
        Iterator<Node<CampusXY>> it = paths.getNodes();
        while (it.hasNext()) {
          CampusXY bld = it.next().getData();
          if (bld.getAbrName().equals(input)) {
            // we found the building to highlight
            if (name.equals("FROM")) {
              ((MapPanel)mapDisplay).setFromBld(bld.getCoordinate());
            } else { //if (name.equals("TO")) {
              ((MapPanel)mapDisplay).setToBld(bld.getCoordinate());
            }
            break;
          }
        }
        frame.repaint();
      }
    }
  }
}
