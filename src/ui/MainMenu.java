package ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainMenu extends JFrame
{
    private JFrame frame;
    private JPanel buttonPanel, titlePanel;
    private JLabel title;
    private JButton startButton, continueButton;

    public MainMenu() 
    {
        frame = new JFrame();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));

        title = new JLabel("Tower Defense Game");
        title.setForeground(Color.red);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 32));
        startButton = new JButton("Start");
        continueButton = new JButton("Continue");

        //This will open up the game's window
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                new GameWindow();
                frame.dispose();
            }
        });

        //This will open up the game window using a saved file
        //If we choose to do this option of course
        continueButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                //do nothing right now
            }
        });

        //This puts the 2 buttons we currently have in the middle
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Can adjust the size
        frame.setSize(500, 350);
        frame.setTitle("Main Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title, BorderLayout.PAGE_START);
        //This puts the title slightly down from the top of the JFrame
        titlePanel.setBorder(BorderFactory.createEmptyBorder(35, 10, 50, 10));
        titlePanel.setBackground(Color.green);

        buttonPanel.add(startButton);
        buttonPanel.add(continueButton, BorderLayout.CENTER);
        //This splits the buttons and the title
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        buttonPanel.setBackground(Color.green);

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
}
