package ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import io.MapData;

/**
 * This class is for creating the Game Over window
 * Allows the user to return to the starting window
 * Or exit out of the program
 * @author Raymond
 */

public class GameOverWindow extends JFrame
{
    private JFrame frame;
    private JPanel buttonPanel, titlePanel;
    private JLabel title;
    private JButton restartButton, exitButton;
    private final MapData mapData;

    public GameOverWindow(MapData data) 
    {
        frame = new JFrame();

        //Set the icon image
        try
        {
            ImageIcon img = new ImageIcon("icon.png");
            frame.setIconImage(img.getImage());
        }
        catch(Exception e){}

        mapData = data;

        createLabels();
        createButtons();
        createPanels();

        //Can adjust the size
        frame.setSize(400, 250);
        frame.setTitle("Game Over Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.add(titlePanel, BorderLayout.PAGE_START);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        frame.add(contentPane);

        //frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        //This makes it appear in the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }

    public void createButtons()
    {
        restartButton = new JButton("Restart");
        restartButton.setMinimumSize(new Dimension(80, 25));
        restartButton.setMaximumSize(new Dimension(80, 25));

        exitButton = new JButton("Exit");
        exitButton.setMinimumSize(new Dimension(80, 25));
        exitButton.setMaximumSize(new Dimension(80, 25));

        //Add listeners to the buttons
        //This will open up the game's window
        restartButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Restarts the game from the start menu
                //And allows the player to select a map
                new MainMenu(mapData);
                frame.dispose();
            }
        });

        //This will open up the game window using a saved file
        //If we choose to do this option of course
        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //do nothing right now
                frame.dispose();
            }
        });

        //This puts the 2 buttons we currently have in the middle
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createLabels()
    {
        //Creates the title label
        title = new JLabel("Game Over");
        title.setForeground(Color.red);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createPanels()
    {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
        
        //Add buttons and labels to the JPanels

        titlePanel.add(title, BorderLayout.PAGE_START);
        //This puts the title slightly down from the top of the JFrame
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        titlePanel.setBackground(Color.BLACK);

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton, BorderLayout.CENTER);
        //This splits the buttons and the title
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
    }
}