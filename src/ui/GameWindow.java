package ui;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;

import entities.Tower;
import graphics.*;
import io.MapData;

/**
 * This class is for creating the game window and putting the mapComponent and side bars
 * Basically, this is the class that has all the GUI components
 *
 * @author Raymond and Colton
 */

public class GameWindow extends JFrame
{

    private JFrame frame;
    private JPanel mapPanel, towerPanel, textPanel;
    private JLabel healthLabel, healthNumLabel, moneyLabel, moneyNumLabel;
    private JButton playButton, fastForwardButton;
    private JList towerList;
    private JScrollPane towerScroller;
    private MapComponent mapComponent;
    private MapData data;
    private Timer gameClock;

    public GameWindow(MapData md)
    {
        data = md;
        frame = new JFrame();

        //Set the icon image
        try
        {
            ImageIcon img = new ImageIcon("icon.png");
            frame.setIconImage(img.getImage());
        }
        catch(Exception e){}

        //Set the specs of the game menu frame
        frame.setSize(712, 590);
        frame.setTitle("Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create contentPane
        Container contentPane = getContentPane();

        //Create and panels to contentPane
        createPanels();
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
        createClock();
    }

    private void createPanels()
    {
        //Make mapPanel
        mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());

        //Create and add components to mapPanel
        createMapComponent();
        mapPanel.add(mapComponent);

        //Make towerPanel
        towerPanel = new JPanel();
        towerPanel.setPreferredSize(new Dimension(105, 500));
        towerPanel.setLayout(new BoxLayout(towerPanel, BoxLayout.PAGE_AXIS));
        towerPanel.setBackground(Color.GRAY);
        towerPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        //Create and add components to towerPanel
        createLabels();
        towerPanel.add(healthLabel, BorderLayout.PAGE_START);
        towerPanel.add(healthNumLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyNumLabel, BorderLayout.PAGE_START);
        createList();
        towerPanel.add(towerScroller, BorderLayout.CENTER);
        createButtons();
        towerPanel.add(playButton, BorderLayout.PAGE_END);
        towerPanel.add(fastForwardButton, BorderLayout.PAGE_END);

        //Make textPanel
        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(31, 10, 30, 10));
        textPanel.setBackground(Color.GRAY);
    }

    private void createMapComponent()
    {
        mapComponent = new MapComponent(data);
        mapComponent.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(towerList.getSelectedValue().equals("Sell"))
                {
                    int x = e.getX() / 50;
                    int y = e.getY() / 50;
                    mapComponent.sellTower(y, x);

                    if (data.getMoneyChanged())
                    {
                        moneyNumLabel.setText(String.format("$%d", data.getMoney()));
                    }
                }

                else if (towerList.getSelectedValue() != null)
                {
                    int x = e.getX() / 50;
                    int y = e.getY() / 50;
                    //Instead of Basic it will be towerList.getSelectedValue()
                    mapComponent.setTile(new Tower(y, x, "Basic"), y, x);

                    if (data.getMoneyChanged())
                    {
                        moneyNumLabel.setText(String.format("$%d", data.getMoney()));
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void createLabels()
    {
        //Create healthLabel
        healthLabel = new JLabel("Health:");
        healthLabel.setForeground(Color.RED);
        healthLabel.setFont(new Font(healthLabel.getFont().getName(), Font.PLAIN, 16));
        healthLabel.setPreferredSize(new Dimension(95, 20));
        healthLabel.setMinimumSize(new Dimension(300, 20));
        healthLabel.setMaximumSize(new Dimension(300, 20));

        //Create healthNumLabel
        healthNumLabel = new JLabel(String.format("%d", data.getHealth()));
        healthNumLabel.setForeground(Color.RED);
        healthNumLabel.setFont(new Font(healthLabel.getFont().getName(), Font.PLAIN, 16));
        healthNumLabel.setMinimumSize(new Dimension(300, 20));
        healthNumLabel.setMaximumSize(new Dimension(300, 20));
        //This method creates the border around the label
        healthNumLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED), healthNumLabel.getBorder()));

        //Create moneyLabel
        moneyLabel = new JLabel("Money:");
        moneyLabel.setForeground(Color.GREEN);
        moneyLabel.setFont(new Font(moneyLabel.getFont().getName(), Font.PLAIN, 16));
        moneyLabel.setPreferredSize(new Dimension(95, 20));
        moneyLabel.setMinimumSize(new Dimension(300, 20));
        moneyLabel.setMaximumSize(new Dimension(300, 20));

        //Create moneyNumLabel
        moneyNumLabel = new JLabel(String.format("$%d", data.getMoney()));
        moneyNumLabel.setForeground(Color.GREEN);
        moneyNumLabel.setFont(new Font(moneyLabel.getFont().getName(), Font.PLAIN, 16));
        moneyNumLabel.setMinimumSize(new Dimension(600, 20));
        moneyNumLabel.setMaximumSize(new Dimension(600, 20));
        //This method creates the border around the label
        moneyNumLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN), moneyNumLabel.getBorder()));

        //Will likely be placed on the left hand side (use RIGHT_ALIGNMENT)
        healthLabel.setAlignmentX(RIGHT_ALIGNMENT);
        healthNumLabel.setAlignmentX(RIGHT_ALIGNMENT);
        moneyLabel.setAlignmentX(RIGHT_ALIGNMENT);
        moneyNumLabel.setAlignmentX(RIGHT_ALIGNMENT);
    }

    private void createButtons()
    {
        playButton = new JButton("Play");
        playButton.setMinimumSize(new Dimension(300, 25));
        playButton.setMaximumSize(new Dimension(300, 25));

        fastForwardButton = new JButton("Fast");
        fastForwardButton.setMinimumSize(new Dimension(300, 25));
        fastForwardButton.setMaximumSize(new Dimension(300, 25));

        //This puts the buttons at the bottom of the tower panel
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        fastForwardButton.setAlignmentX(CENTER_ALIGNMENT);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //If last enemy died
                //Set button to play
                if(gameClock.isRunning())
                {
                    gameClock.stop();
                    playButton.setText("Play");
                }

                //Start a new round based on currentRound
                //Current Round integer can be in Map Data
                /*else if(playbutton.getText().equals("Play"))
                {
                    createWave(currentRound);
                    playbutton.setText("Fast");
                }
                
                //Slow the game clock down
                else if(gameClock.getDelay() == 40)
                {
                    gameClock.setDelay(80);
                    fastForwardButton.setText("Fast");
                }

                //Speed the game clock back up
                else
                {
                    gameClock.setDelay(40);
                    fastForwardButton.setText("Slow");
                }*/

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
                //Also changed the speed of fast forwarding
                //Made it 2x the speed instead of 5x the speed
                if(gameClock.getDelay() == 40)
                {
                    gameClock.setDelay(80);
                    fastForwardButton.setText("Fast");
                }
                else
                {
                    gameClock.setDelay(40);
                    fastForwardButton.setText("Slow");
                }
            }
        });

    }

    private void createList()
    {
        DefaultListModel listModel = new DefaultListModel();
        //This will check if theres a tower in the cell, if there is sell it
        //For half of it's original cost
        listModel.addElement("Sell");
        listModel.addElement("Basic Tower");
        towerList = new JList(listModel);
        towerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                //Nothing yet
            }
        });
        towerList.setLayoutOrientation(JList.VERTICAL);
        towerScroller = new JScrollPane(towerList);
        towerScroller.setPreferredSize(new Dimension(95, 350));
    }

    private void createClock()
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

                if(data.getHealthChanged())
                {
                    healthNumLabel.setText(String.format("%d", data.getHealth()));
                }

                //Runs infinitely
                if(data.getHealth() == 0)
                {
                    new GameOverWindow(data);
                    gameClock.stop();
                    frame.dispose();
                    //data.decrementHealth();
                }
            }
        };
        gameClock = new Timer(80, taskPerformer);
    }
}

