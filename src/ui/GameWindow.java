package ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import graphics.*;
import io.MapData;

/**
 * This class is for creating the game window and putting the mapComponent and side bars
 * Basically, this is the class that has all the GUI components
 *
 * @author Raymond
 */

public class GameWindow extends JFrame
{

    private JFrame frame;
    private JPanel mapPanel, towerPanel, textPanel, buttonPanel;
    private JLabel healthLabel, moneyLabel;
    private JButton playButton, fastForwardButton;
    private MapComponent mapComponent;
    private MapData data;
    private Timer gameClock;

    public GameWindow(MapData md)
    {
        data = md;
        frame = new JFrame();

        //Set the specs of the game menu frame
        frame.setSize(710, 590);
        frame.setTitle("Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createLabels();
        createButtons();
        createPanels();

        //Uses the real mapComponent data that I'm given through MainMenu
        drawMap();

        Container contentPane = getContentPane();
        contentPane.add(mapPanel, BorderLayout.CENTER);
        contentPane.add(textPanel, BorderLayout.PAGE_END);
        contentPane.add(towerPanel, BorderLayout.LINE_END);
        frame.add(contentPane);

        frame.setResizable(false);
        frame.setVisible(true);

        //This makes it appear in the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        //Create gameClock and associated listener
        buildClock();
    }

    public void createLabels() 
    {
        healthLabel = new JLabel(String.format("Health: %d", data.getHealth()));
        healthLabel.setForeground(Color.black);
        healthLabel.setFont(new Font(healthLabel.getFont().getName(), Font.PLAIN, 14));
        moneyLabel = new JLabel(String.format("Money: %d", data.getMoney()));
        moneyLabel.setForeground(Color.black);
        moneyLabel.setFont(new Font(moneyLabel.getFont().getName(), Font.PLAIN, 14));

        //Will likely be placed on the left hand side (use RIGHT_ALIGNMENT)
        healthLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        moneyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    }

    public void createButtons() 
    {
        playButton = new JButton("Play");
        fastForwardButton = new JButton("Fast");

        //This puts the buttons at the bottom of the tower panel
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fastForwardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Play or pause game
                if (gameClock.isRunning())
                {
                    gameClock.stop();
                    playButton.setText("Play");
                }
                else
                {
                    gameClock.start();
                    playButton.setText("Pause");
                }
            }
        });

        fastForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Changes the speed at which the enemies move
                if(gameClock.getDelay() == 10)
                {
                    gameClock.setDelay(50);
                }
                else
                {
                    gameClock.setDelay(10);
                }
            }
        });

    }

    public void createPanels() 
    {
        mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());

        towerPanel = new JPanel();
        towerPanel.setLayout(new BoxLayout(towerPanel, BoxLayout.PAGE_AXIS));

        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));

        towerPanel.add(healthLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyLabel, BorderLayout.PAGE_START);
        //This puts the labels slightly below the top
        towerPanel.setBorder(BorderFactory.createEmptyBorder(20, 7, 10, 25));

        //Will put the buttons at the bottom using a new panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(playButton);
        buttonPanel.add(fastForwardButton);
        buttonPanel.setBackground(Color.blue);

        towerPanel.add(buttonPanel, BorderLayout.PAGE_END);
        towerPanel.setBackground(Color.blue);

        textPanel.setBorder(BorderFactory.createEmptyBorder(31, 10, 30, 10));
        textPanel.setBackground(Color.red);
    }

    /*This will take in the data to read the text file and draw the mapComponent
	* onto the mapComponent panel
     */
    private void drawMap()
    {
        mapComponent = new MapComponent(data);
        mapPanel.add(mapComponent);
    }

    private void buildClock()
    {
        ActionListener taskPerformer = new ActionListener() {
            boolean setupComplete = false;

            public void actionPerformed(ActionEvent evt)
            {
                if (!setupComplete)
                {
                    setupComplete = true;
                    mapComponent.setSetupComplete();
                }

                mapComponent.repaint();

                if (data.getHealthChanged())
                {
                    healthLabel.setText(String.format("Health: %d", data.getHealth()));
                }
            }
        };
        gameClock = new Timer(50, taskPerformer);
    }
}

