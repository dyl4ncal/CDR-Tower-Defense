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
        frame.setSize(640, 480);
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
        title.setForeground(Color.CYAN);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //This defines the game guide's text instructions.
        String info = 
        "<html>" +  
        "1.) Select a map from the combo box and press 'Start' button on the Main Menu!" + "<br />" + 
        "<br />2.) After a game is started, you will find yourself inside a computer with a very "
                + "important task! The computer's Central Processing Unit (CPU) is under attack by "
                + "malicious software! YOUR JOB IS TO PROTECT IT." + "<br />" +
        "<br />3.) Press 'Play' when you are up for the task! Once you hit the play button, enemies will begin making "
                + "their way towards the computer's CPU by travelling along the electrical circuits of "
                + "the computer's motherboard. If enemies reach the CPU, it will take damage." + "<br />" +
        "<br />4.) Buy towers from the right panel and place them on the circuit board by clicking a "
                + "tower type and then clicking where you want it located."
                + "Defeat enemies to generate more $$$" + "<br />" +
        "<br />5.) If you are confident that the CPU is well protected and don't need to carefully plan "
                + "anymore defensive moves, there is a 'Faster' button at the bottom right which speeds up time." + "<br />" +
        "<br />6.) On the tower panel will be 3 option. 'Select', 'Sell', and 'Upgrade'" +
         "'Select' shows all the attributes of a tower"+
         "'Sell' will sell a tower for 1/4 the original cost + upgrades"+
         "'Upgrade' will upgrade a tower at a certain price. Upgrade price can be checked with 'Select'"+"<br />" +
        "<br />7.) Goodluck! This computer system is depending on you!" + "<br />" +        
        "</html>";
                
               
        guideInfo = new JLabel(info);
        guideInfo.setForeground(Color.ORANGE);
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
        guidePanel.setBackground(Color.DARK_GRAY);
    }
}