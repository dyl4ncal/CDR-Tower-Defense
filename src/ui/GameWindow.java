package ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GameWindow extends JFrame
{

    private JFrame frame;
    private JPanel mapPanel, towerPanel, textPanel, buttonPanel;
    private JLabel healthLabel, moneyLabel;
    private JButton playButton, fastForwardButton;

    public void createLabels()
    {
        healthLabel = new JLabel("Health");
        healthLabel.setForeground(Color.black);
        healthLabel.setFont(new Font(healthLabel.getFont().getName(), Font.PLAIN, 14));
        moneyLabel = new JLabel("Money");
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

        playButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if (playButton.getLabel().equals("Play")) 
                {
                    playButton.setLabel("Pause");
                } 
                else 
                {
                    playButton.setLabel("Play");
                }
            }
        });

    }

    public void createPanels()
    {
        mapPanel = new JPanel();
        mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.PAGE_AXIS));

        towerPanel = new JPanel();
        towerPanel.setLayout(new BoxLayout(towerPanel, BoxLayout.PAGE_AXIS));

        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));

        towerPanel.add(healthLabel, BorderLayout.PAGE_START);
        towerPanel.add(moneyLabel, BorderLayout.PAGE_START);
        //This puts the labels slightly below the top
        towerPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 25));

        //Will put the buttons at the bottom using a new panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(playButton);
        buttonPanel.add(fastForwardButton);
        buttonPanel.setBackground(Color.green);

        towerPanel.add(buttonPanel, BorderLayout.PAGE_END);
        towerPanel.setBackground(Color.green);

        //This splits the buttons and the title
        mapPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        mapPanel.setBackground(Color.blue);

        textPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        textPanel.setBackground(Color.red);

    }

    public GameWindow() 
    {
        frame = new JFrame();

        //Set the specs of the game menu frame
        frame.setSize(750, 525);
        frame.setTitle("Game Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createLabels();
        createButtons();
        createPanels();

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
    }
}
