package ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import io.MapData;

/**
 * This class is for creating the main menu screen
 * 
 * 
 * @author Raymond
 */
public class MainMenu extends JFrame
{
    private JFrame frame;
    private JPanel buttonPanel, titlePanel;
    private JLabel title;
    private JButton startButton, guideButton;
    private final MapData mapData;

    public MainMenu(MapData data) 
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
        frame.setSize(500, 350);
        frame.setTitle("Main Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        startButton = new JButton("Start");
        startButton.setMinimumSize(new Dimension(75, 25));
        startButton.setMaximumSize(new Dimension(75, 25));

        guideButton = new JButton("How to Play");
        guideButton.setMinimumSize(new Dimension(100, 25));
        guideButton.setMaximumSize(new Dimension(100, 25));

        //This will open up the game's window
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Sends the MapComponent Data over to the game menu
                new GameWindow(mapData);
                frame.dispose();
            }
        });

        //This will open up the game window using a saved file
        //If we choose to do this option of course
        guideButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Open the game over window
                //Will open a real guide window later
                new GameGuideWindow();
            }
        });

        //This puts the 2 buttons we currently have in the middle
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guideButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createLabels()
    {
        //Creates the title label
        title = new JLabel("CDR Tower Defense");
        title.setForeground(Color.CYAN);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createPanels()
    {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
    
        titlePanel.add(title, BorderLayout.PAGE_START);
        //This puts the title slightly down from the top of the JFrame
        titlePanel.setBorder(BorderFactory.createEmptyBorder(35, 10, 50, 10));
        titlePanel.setBackground(Color.DARK_GRAY);

        buttonPanel.add(startButton);
        buttonPanel.add(guideButton, BorderLayout.CENTER);
        //This splits the buttons and the title
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        buttonPanel.setBackground(Color.DARK_GRAY);
    }
}


