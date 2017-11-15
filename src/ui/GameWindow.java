package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;

import entities.Tower;
import graphics.*;
import entities.MapData;

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
    private JLabel healthLabel, healthNumLabel; 
    private JLabel moneyLabel, moneyNumLabel;
    private JLabel roundLabel, roundNumLabel;
    private JLabel textBox, description;
    private JButton playButton;
    private JList towerList;
    private JScrollPane towerScroller;
    private MapComponent mapComponent;
    private MapData data;
    private Timer gameClock;
    private int currentRound = 0;

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

        //Set the specs of the game menu frame based on map data
        int rows = md.getNumCols() * 50;
        int cols = md.getNumRows() * 50;
        frame.setSize(112 + rows, 90 + cols);
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
        gameClock.start();
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
        towerPanel.setBackground(Color.DARK_GRAY);
        towerPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        //Create and add components to towerPanel
        createLabels();
        towerPanel.add(healthLabel, BorderLayout.PAGE_START);
        towerPanel.add(healthNumLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyNumLabel, BorderLayout.PAGE_START);
        towerPanel.add(roundLabel, BorderLayout.PAGE_START);
        towerPanel.add(roundNumLabel, BorderLayout.PAGE_START);
        createList();
        towerPanel.add(towerScroller, BorderLayout.CENTER);
        createButtons();
        towerPanel.add(playButton, BorderLayout.PAGE_END);

        //Make textPanel
        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.add(description, BorderLayout.LINE_START);
        textPanel.add(textBox, BorderLayout.LINE_START);
    }

    private void createMapComponent()
    {
        mapComponent = new MapComponent(data);
        mapComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(towerList.getSelectedValue().equals("Select"))
                {
                   String info = mapComponent.selectTower(towerList.getSelectedValue().toString(), e.getX() / 50, e.getY() / 50);
                   textBox.setText(info);
                }

                else if (towerList.getSelectedValue() != null)
                {
                    mapComponent.swapTile(towerList.getSelectedValue().toString(), e.getY() / 50, e.getX() / 50);
                }
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
        //healthNumLabel.setBorder(BorderFactory.createCompoundBorder(
            //BorderFactory.createLineBorder(Color.RED), healthNumLabel.getBorder()));

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
        //moneyNumLabel.setBorder(BorderFactory.createCompoundBorder(
            //BorderFactory.createLineBorder(Color.GREEN), moneyNumLabel.getBorder()));

        //Create healthLabel
        roundLabel = new JLabel("Round:");
        roundLabel.setForeground(Color.ORANGE);
        roundLabel.setFont(new Font(roundLabel.getFont().getName(), Font.PLAIN, 16));
        roundLabel.setPreferredSize(new Dimension(95, 20));
        roundLabel.setMinimumSize(new Dimension(300, 20));
        roundLabel.setMaximumSize(new Dimension(300, 20));

        //Create healthNumLabel
        roundNumLabel = new JLabel(String.format("%d", data.getRound()));
        roundNumLabel.setForeground(Color.ORANGE);
        roundNumLabel.setFont(new Font(roundNumLabel.getFont().getName(), Font.PLAIN, 16));
        roundNumLabel.setMinimumSize(new Dimension(300, 20));
        roundNumLabel.setMaximumSize(new Dimension(300, 20));

        //Will likely be placed on the left hand side (use RIGHT_ALIGNMENT)
        healthLabel.setAlignmentX(RIGHT_ALIGNMENT);
        healthNumLabel.setAlignmentX(RIGHT_ALIGNMENT);
        moneyLabel.setAlignmentX(RIGHT_ALIGNMENT);
        moneyNumLabel.setAlignmentX(RIGHT_ALIGNMENT);
        roundLabel.setAlignmentX(RIGHT_ALIGNMENT);
        roundNumLabel.setAlignmentX(RIGHT_ALIGNMENT);

        //Text panel label
        String info = "Description of option selected";
        textBox = new JLabel(info);
        textBox.setForeground(Color.ORANGE);
        textBox.setBackground(Color.DARK_GRAY);

        String des = "Tower attributes";
        description = new JLabel(des);
        description.setForeground(Color.ORANGE);
        description.setBackground(Color.DARK_GRAY);
    }

    private void createButtons()
    {
        playButton = new JButton("Play");
        playButton.setMinimumSize(new Dimension(300, 25));
        playButton.setMaximumSize(new Dimension(300, 25));

        //This puts the buttons at the bottom of the tower panel
        playButton.setAlignmentX(CENTER_ALIGNMENT);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Start a new round based on currentRound
                if(playButton.getText().equals("Play") && mapComponent.isRoundOver())
                {
                    data.incrementRound();
                    mapComponent.createWave();
                    roundNumLabel.setText(String.format("%d", data.getRound()));
                    playButton.setText("Faster");
                    gameClock.setDelay(60);
                }
                else if(playButton.getText().equals("Slower"))  //Slow the game clock down
                {
                    gameClock.setDelay(60);
                    playButton.setText("Faster");
                }
                else if(playButton.getText().equals("Faster"))  //Speed the game clock back up
                {
                    gameClock.setDelay(30);
                    playButton.setText("Slower");
                }
            }
        });
    }

    private void createList()
    {
        DefaultListModel listModel = new DefaultListModel();
        //This will check if theres a tower in the cell, if there is sell it
        //For half of it's original cost
        listModel.addElement("Select");
        listModel.addElement("Sell Tower");
        listModel.addElement("Upgrade Tower");
        listModel.addElement("Basic");
        listModel.addElement("Melee");
        listModel.addElement("Sniper");
        listModel.addElement("Splash");
        towerList = new JList(listModel);
        towerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                //Nothing yet
                int cost, attack, range, speed, upgrade, sell, level;
                String info = "";
                String des = "";
                switch( (String) towerList.getSelectedValue())
                {
                    case "Select":
                    {
                        des = "Select a tower to view it's attributes";
                        //6 things
                        info = "Attributes appear here";
                        break;
                    }
                    case "Sell Tower":
                    {
                        des = "Sells a tower";
                        info = "Returns 1/4 of its cost";
                        break;
                    }

                    case "Upgrade Tower":
                    {
                        des = "Upgrades = tower's cost * upgrade level";
                        info = "Attack value increase  Basic: 2  Melee: 4  Sniper: 3  Splash: 2";
                        break;
                    }

                    case "Basic":
                    {
                        cost = 150;
                        attack = 4;
                        range = 3;
                        speed = 5;
                        des = "Tower with all moderate stats";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }
                    case "Melee":
                    {
                        cost = 200;
                        attack = 8;
                        range = 1;
                        speed = 6;
                        des = "Tower with a high attack, small range, and moderate fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }
                    case "Sniper":
                    {
                        cost = 250;
                        attack = 6;
                        range = 5;
                        speed = 6;
                        des = "Tower with a moderate attack, large range, but slow fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }
                    case "Splash":
                    {
                        cost = 300;
                        attack = 3;
                        range = 1;
                        speed = 10;
                        des = "Tower that hits all enemies in its range with a low attack, small range, but slow fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }
                }
                description.setText(des);
                textBox.setText(info);
            }
        });
        towerList.setLayoutOrientation(JList.VERTICAL);
        towerScroller = new JScrollPane(towerList);
        towerScroller.setPreferredSize(new Dimension(95, 350));
    }

    private void createClock()
    {
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                if(mapComponent.isRoundOver())
                {
                    playButton.setText("Play");
                }
                else
                {
                    mapComponent.repaint();
                }

                if(data.getHealthChanged())
                {
                    healthNumLabel.setText(String.format("%d", data.getHealth()));
                }

                if (data.getMoneyChanged())
                {
                    moneyNumLabel.setText(String.format("$%d", data.getMoney()));
                }

                //Creates the game over window pop up
                if(data.getHealth() == 0)
                {
                    new GameOverWindow(data);
                    gameClock.stop();
                    frame.dispose();
                }
            }
        };
        gameClock = new Timer(80, taskPerformer);
    }
}

