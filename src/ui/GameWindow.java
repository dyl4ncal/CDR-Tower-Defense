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
    private JButton playButton, mainMenuButton;
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
        frame.setTitle("CDR Tower Defense");
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
        towerPanel.add(mainMenuButton, BorderLayout.PAGE_END);

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
        mapComponent.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
            try{
                if(towerList.getSelectedValue().equals("Select"))
                {
                   String info = mapComponent.selectTower(towerList.getSelectedValue().toString(), e.getX() / 50, e.getY() / 50);
                   textBox.setText(info);
                }

                else if (towerList.getSelectedValue() != null)
                {
                    mapComponent.swapTile(towerList.getSelectedValue().toString(), e.getY() / 50, e.getX() / 50);
                }
            }catch(Exception exp){}
        }
        });
    }

    private JLabel createLabel(String s, Color c)
    {
        JLabel label = new JLabel(s);
        label.setForeground(c);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(95, 20));
        label.setMinimumSize(new Dimension(300, 20));
        label.setMaximumSize(new Dimension(300, 20));
        label.setAlignmentX(RIGHT_ALIGNMENT);
        return label;
    }

    private JLabel createNumLabel(String s, int i, Color c)
    {
        JLabel label = new JLabel(String.format(s, i));
        label.setForeground(c);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(95, 20));
        label.setMinimumSize(new Dimension(300, 20));
        label.setMaximumSize(new Dimension(300, 20));
        label.setAlignmentX(RIGHT_ALIGNMENT);
        return label;
    }

    private void createLabels()
    {
        healthLabel = createLabel("Health: ", Color.RED);
        healthNumLabel = createNumLabel("%d", data.getHealth(), Color.RED);
        moneyLabel = createLabel("Money: ", Color.GREEN);
        moneyNumLabel = createNumLabel("$%d", data.getMoney(), Color.GREEN);
        roundLabel = createLabel("Round: ", Color.ORANGE);
        roundNumLabel = createNumLabel("%d", data.getRound(), Color.ORANGE);

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

        playButton.addActionListener(new ActionListener(){
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

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setMinimumSize(new Dimension(300, 25));
        mainMenuButton.setMaximumSize(new Dimension(300, 25));
        mainMenuButton.setAlignmentX(CENTER_ALIGNMENT);

        mainMenuButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //Start a new round based on currentRound
                gameClock.stop(); // ??
                frame.dispose();
                new TitleWindow();
            }
        });

    }

    private void createList()
    {
        DefaultListModel listModel = new DefaultListModel();
        //This will check if theres a tower in the cell, if there is sell it
        //For half of it's original cost
        listModel.addElement("Select");
        listModel.addElement("Sell");
        listModel.addElement("Upgrade");
        listModel.addElement("Basic");
        listModel.addElement("Melee");
        listModel.addElement("Sniper");
        listModel.addElement("Speed");
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
                String option = (String) towerList.getSelectedValue();
                switch(option)
                {
                    case "Select":
                    {
                        des = "Select a tower to view it's attributes";
                        info = "Attributes appear here";
                        break;
                    }
                    case "Sell":
                    {
                        des = "Sells a tower";
                        info = "Returns 1/4 the money spent on the tower";
                        break;
                    }

                    case "Upgrade":
                    {
                        des = "Upgrades = tower's initial cost * upgrade level";
                        info = "Attack value increase  Basic: 4  Melee: 8  Sniper: 4  Speed: 2  Splash: 2";
                        break;
                    }

                    case "Basic":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
                        des = "Cost efficient tower with all moderate stats";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }

                    case "Melee":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
                        des = "Tower with a very high attack, small range, and moderate fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }

                    case "Sniper":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
                        des = "Tower with a high attack, large range, but slow fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }

                    case "Speed":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
                        des = "Tower with a low attack, low range, but fast fire rate";
                        info = String.format("Cost: $%d   Attack: %d   Range: %d   Speed: %d", cost, attack, range, speed);
                        break;
                    }

                    case "Splash":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
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
        gameClock = new Timer(60, taskPerformer);
    }
}

