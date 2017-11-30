/**
 * This class is for creating the game window, mapComponent, and side bars
 * Basically, this is the class that has all the GUI components
 */

package ui;

import entities.MapData;
import entities.Tower;
import graphics.MapComponent;
import music.BackgroundMusic;
import graphics.EnemyComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JFrame
{
    private BackgroundMusic bgm;
    private JFrame frame;
    private JPanel mapPanel, towerPanel, textPanel;
    private JLabel healthLabel, healthNumLabel; 
    private JLabel moneyLabel, moneyNumLabel;
    private JLabel roundLabel, roundNumLabel;
    private JLabel textBox, description;
    private JButton playButton, mainMenuButton, musicButton, sfxButton;
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
            ImageIcon img = new ImageIcon("images/icon.png");
            frame.setIconImage(img.getImage());
        }
        catch(Exception e){}

        //Set the specs of the game menu frame based on map data
        frame.setSize(112 + (data.getNumCols() * 50), 90 + (data.getNumRows() * 50));
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

        try
        {
            bgm = new BackgroundMusic();
            bgm.play();
        }
        catch(Exception e){}
    }

    private void createPanels()
    {
        //Make mapPanel
        mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());

        //Create and add components to mapPanel
        createMapComponent();
        mapPanel.add(mapComponent);
        mapPanel.setBackground(new Color(16,16,16));

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
        towerPanel.add(sfxButton, BorderLayout.PAGE_END);
        towerPanel.add(musicButton, BorderLayout.PAGE_END);
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
            public void mouseClicked(MouseEvent e)
            {
                if(towerList.getSelectedValue().equals("Select"))
                {
                    String info = mapComponent.selectTower(e.getY() / 50, e.getX() / 50);
                    textBox.setText(info);
                }

                else if (towerList.getSelectedValue() != null)
                {
                    mapComponent.swapTile(towerList.getSelectedValue().toString(), e.getY() / 50, e.getX() / 50);
                }
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
        String info = "Tower attributes";
        textBox = new JLabel(info);
        textBox.setForeground(Color.ORANGE);
        textBox.setBackground(Color.DARK_GRAY);

        String des = "Description of option selected";
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
                if(playButton.getText().equals("Play") && data.getRoundOver())
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
                gameClock.stop();
                bgm.stop();
                frame.dispose();
                new TitleWindow();
            }
        });

        musicButton = new JButton("Music OFF");
        musicButton.setMinimumSize(new Dimension(300, 25));
        musicButton.setMaximumSize(new Dimension(300, 25));
        musicButton.setAlignmentX(CENTER_ALIGNMENT);

        musicButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(musicButton.getText().equals("Music OFF"))
                {
                    bgm.stop();
                    musicButton.setText("Music ON");
                }

                else
                {
                    try
                    {
                        bgm.play();
                        musicButton.setText("Music OFF");
                    }
                    catch(Exception e){}
                }
            }
        });


        sfxButton = new JButton("SFX OFF");
        sfxButton.setMinimumSize(new Dimension(300, 25));
        sfxButton.setMaximumSize(new Dimension(300, 25));
        sfxButton.setAlignmentX(CENTER_ALIGNMENT);

        sfxButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if(sfxButton.getText().equals("SFX OFF"))
                {
                    EnemyComponent.changeSFX();
                    sfxButton.setText("SFX ON");
                }

                else
                {
                    try
                    {
                        EnemyComponent.changeSFX();
                        sfxButton.setText("SFX OFF");
                    }
                    catch(Exception e){}
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
        listModel.addElement("Sell");
        listModel.addElement("Upgrade");
        listModel.addElement("Antivirus");
        listModel.addElement("Firewall");
        listModel.addElement("Quarantine");
        listModel.addElement("Encryption");
        listModel.addElement("Surge");
        towerList = new JList(listModel);

        towerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                //Nothing yet
                int cost, attack, range, speed;
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
                        info = "Attack value increase  Antivirus: 5  Firewall: 10  Quarantine: 5  Encryption: 3  Surge: 2";
                        break;
                    }

                    case "Antivirus":
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

                    case "Firewall":
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

                    case "Quarantine":
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

                    case "Encryption":
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

                    case "Surge":
                    {
                        Tower t = new Tower(option, 0, 0);
                        cost = t.getCost();
                        attack = t.getAttack();
                        range = t.getRange();
                        speed = t.getSpeed();
                        des = "Tower that hits all enemies in its range with a low attack, small range, but very slow fire rate";
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
                if(data.getRoundOver())
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
                if(data.getHealth() <= 0)
                {
                    new GameOverWindow(data.getRound());
                    gameClock.stop();
                    bgm.stop();
                    frame.dispose();
                }
            }
        };
        gameClock = new Timer(60, taskPerformer);
    }
}

