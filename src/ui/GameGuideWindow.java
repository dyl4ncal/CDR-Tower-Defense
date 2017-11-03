package ui;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for creating & displaying the Game Guide
 * window. after clicking this button, it allows the user 
 * to return to the starting window or exit out of the program.
 * 
 * @author Raymond & Dylan
 */

public class GameGuideWindow extends JFrame
{
    private JFrame frame;
    private JPanel guidePanel;
    private JLabel title;
    private JLabel guideInfo;

    public GameGuideWindow() 
    {
        frame = new JFrame();

        //Set the icon image
        try
        {
            ImageIcon img = new ImageIcon("icon.png");
            frame.setIconImage(img.getImage());
        }
        catch(Exception e){}

        createLabels();
        createPanels();

        //Can adjust the size
        frame.setSize(600, 450);
        frame.setTitle("Game Guide");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.add(guidePanel, BorderLayout.CENTER);
        frame.add(contentPane);

        //frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        //This makes it appear in the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }

    public void createLabels()
    {
        //Creates the title label
        title = new JLabel("How to Play CDR Tower Defense:");
        title.setForeground(Color.BLACK);
        title.setFont(new Font(title.getFont().getName(), Font.ITALIC, 26));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //This defines the game guide's text instructions.
        String info = 
        "<html>" +  
        "1.) To start playing CDR Tower Defense, press 'Start' on the Main Menu!" + "<br />" + 
        "<br />2.) After a game is started, you will find yourself inside a computer with a very" + "<br />" +
        "important task! The computer's Central Processing Unit (CPU) is under attack by" + "<br />" + 
        "malicious software! YOUR JOB IS TO PROTECT IT." + "<br />" + 
        "<br />3.) Press 'Play' when you are up for the task! Immediately, enemies will begin" + "<br />" +
        "making their way towards the computer's CPU by travelling along the electrical" + "<br />" + 
        "circuits of the computer's motherboard. If enemies reach the CPU, it takes damage." + "<br />" + 
        "<br />4.) Buy defenses from the right panel and place them on the circuit board" + "<br />" +
        "by clicking a defense type and then clicking where you want it located. Selecting" + "<br />" +
        "the 'Sell' option allows you to click a defense you own, and sell it for a fraction" + "<br />" + 
        "of the price you payed for it originally. Another way to make $ is by attacking enemies." + "<br />" +
        "<br />5.) If you are confident that the CPU is well protected and don't need to carefully plan" + "<br />" +
        "anymore defensive moves, there is a 'Faster' button at the bottom right which speeds up time." + "<br />" +
        "" + "<br />" +        
        "</html>";
                
               
        guideInfo = new JLabel(info);
        guideInfo.setForeground(Color.BLUE);
        guideInfo.setFont(new Font(title.getFont().getName(), Font.PLAIN, 14));
        guideInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void createPanels()
    {
        guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.PAGE_AXIS));

        //Add buttons and labels to the JPanels
        guidePanel.add(title, BorderLayout.PAGE_START);
        guidePanel.add(this.guideInfo);

        //Adds the title to the frame
        guidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guidePanel.setBackground(Color.WHITE);
    }
}