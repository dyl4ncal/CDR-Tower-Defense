package ui;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for creating the Game Over window.
 * Allows the user to return to the starting window
 * or exit out of the program.
 * @author Raymond
 */

public class GameGuideWindow extends JFrame
{
    private JFrame frame;
    private JPanel guidePanel;
    private JLabel title;

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

    public void createButtons()
    {
        
    }

    public void createLabels()
    {
        //Creates the title label
        title = new JLabel("How to Play");
        title.setForeground(Color.BLACK);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void createPanels()
    {
        guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.PAGE_AXIS));

        //Add buttons and labels to the JPanels

        guidePanel.add(title, BorderLayout.PAGE_START);

        //Adds the title to the frame
        guidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guidePanel.setBackground(Color.WHITE);
    }
}