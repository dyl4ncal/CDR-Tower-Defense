package ui;

import javax.swing.*;
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
        mapComponent.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(towerList.getSelectedValue().equals("Sell tower"))
                {
                    int x = e.getX() / 50;
                    int y = e.getY() / 50;
                    mapComponent.sellTower(y, x);
                }

                else if (towerList.getSelectedValue() != null)
                {
                    int x = e.getX() / 50;
                    int y = e.getY() / 50;
                    //Instead of Basic it will be towerList.getSelectedValue()
                    mapComponent.setTile(new Tower(y, x, (String) towerList.getSelectedValue()), y, x);
                }

                else
                {
                    //
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
        roundNumLabel = new JLabel(String.format("%d", currentRound + 1));
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

                if(playButton.getText().equals("Play"))
                {
                    mapComponent.createWave(currentRound);
                    roundNumLabel.setText(String.format("%d", currentRound + 1));
                    playButton.setText("Faster");
                    gameClock.setDelay(80);
                }
                
                //Slow the game clock down
                else if(playButton.getText().equals("Slower"))
                {
                    gameClock.setDelay(80);
                    playButton.setText("Faster");
                }

                //Speed the game clock back up
                else if(playButton.getText().equals("Faster"))
                {
                    gameClock.setDelay(40);
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
        listModel.addElement("Sell tower");
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
                int cost, attack, range, speed;
                String info, des;
                switch( (String) towerList.getSelectedValue())
                {
                    case "Sell tower":
                    {
                        des = "Sells a tower";
                        info = "Returns 1/4 of its cost";
                        break;
                    }

                    case "Basic":
                    {
                        cost = 150;
                        attack = 2;
                        range = 3;
                        speed = 3;
                        des = "Tower with all moderate stats";
                        info = "Cost: $"+cost+"   Attack: "+attack+"   Range: "+range+"   Speed: "+speed;
                        break;
                    }

                    case "Melee":
                    {
                        cost = 200;
                        range = 1;
                        attack = 5;
                        speed = 5;
                        des = "Tower with the highest attack, small range, and moderate fire rate";
                        info = "Cost: $"+cost+"   Attack: "+attack+"   Range: "+range+"   Speed: "+speed;
                        break;
                    }

                    case "Sniper":
                    {
                        cost = 250;
                        attack = 4;
                        range = 5;
                        speed = 10;
                        des = "Tower with a high attack, largest range, but slow fire rate";
                        info = "Cost: $"+cost+"   Attack: "+attack+"   Range: "+range+"   Speed: "+speed;
                        break;
                    }

                    case "Splash":
                    {
                        cost = 300;
                        attack = 3;
                        range = 1;
                        speed = 7;
                        des = "Tower that hits all enemies in its range with a medium attack, small range, but slow fire rate";
                        info = "Cost: $"+cost+"   Attack: "+attack+"   Range: "+range+"   Speed: "+speed;
                        break;
                    }

                    default:
                    {
                        des = "";
                        info = "";
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
        boolean setupComplete = false;

            public void actionPerformed(ActionEvent evt)
            {
                if (!setupComplete)
                {
                    setupComplete = true;
                    mapComponent.setSetupComplete();
                }

                mapComponent.repaint();

                if(mapComponent.isRoundOver())
                {
                    playButton.setText("Play");
                    currentRound++;
                    data.incrementMoney(50 + (currentRound * 2));
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

