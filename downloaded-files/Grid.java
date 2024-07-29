import javax.swing.*;
import java.awt.*;

public class Grid {

    private static final int TILES = 30;
    private static final int SIZE = 30; 
    private static JButton[][] buttons = new JButton[TILES][TILES];

    public static void main(String[] args) {
        Game game = new Game(); 
        JFrame f = new JFrame("Conway's Game of Life");

        JPanel p = new JPanel(new GridLayout(TILES, TILES));
        JPanel p2 = new JPanel();
        JButton next = new JButton("Next");
        p2.add(next);

        //top panel
        JPanel topPanel = new JPanel(new BorderLayout()); 
        topPanel.setBackground(new Color(54,99,168));
        JLabel titleLabel = new JLabel("Conway's Game of Life");
        titleLabel.setForeground(new Color(169,203,255));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50)); 
         topPanel.add(titleLabel, BorderLayout.WEST);
    
         for (int x = 0; x < TILES; x++) {
             for (int y = 0; y < TILES; y++) {
                 buttons[x][y] = new JButton();
                 buttons[x][y].setBackground(Color.GRAY);
                 buttons[x][y].setOpaque(true);
                 buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Set light grey outline
 
                 int finalX = x;
                 int finalY = y;
                 buttons[x][y].addActionListener(e -> {
                     JButton button = (JButton) e.getSource();
                     if (button.getBackground() == Color.GRAY) {
                         button.setBackground(Color.YELLOW);
                         game.changeState(finalX, finalY);
                     } else {
                         button.setBackground(Color.GRAY);
                     }
                 });
 
                 p.add(buttons[x][y]);
             }
         }
 
         next.addActionListener(e -> {
             game.checkNeighbors();
             game.nextMove();
             boolean[][] gameGrid = game.getGameGrid();
             for (int i = 0; i < TILES; i++) {
                 for (int j = 0; j < TILES; j++) {
                     if (gameGrid[i][j]) {
                         buttons[i][j].setBackground(Color.YELLOW);
                     } else {
                         buttons[i][j].setBackground(Color.GRAY);
                     }
                 }
             }
         });
 
         f.setLayout(new BorderLayout());
 
         f.add(topPanel, BorderLayout.NORTH);
         f.add(p, BorderLayout.CENTER);
         f.add(p2, BorderLayout.SOUTH);
 
         f.setSize(TILES * SIZE, TILES * SIZE);
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         f.setVisible(true);
     }
 }