/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * This class creates a small menu bar
 *
 * @author Abel MacNeil, Jurgen Aliaj
 */
public final class ChessMenuBar extends JMenuBar implements ActionListener {

    //GUI components
    private JMenu colorMenu;
    private JButton lightColorBtn;
    private JButton darkColorBtn;
    private JButton resetBtn;
    private ChessFrame frame;

    /**
     * Creates a new menu bar, with the specified ChessFrame.
     *
     * @param frame
     */
    public ChessMenuBar(ChessFrame frame) {
        super();
        this.frame = frame;
        //initializes the new components
        colorMenu = new JMenu("Colors");
        lightColorBtn = new JButton("Choose light color");
        darkColorBtn = new JButton("Choose dark color");
        resetBtn = new JButton("Reset");
        //adds actions to the buttons
        resetBtn.addActionListener(this);
        lightColorBtn.addActionListener(this);
        darkColorBtn.addActionListener(this);
        //adds the color buttons to the color menu
        colorMenu.add(lightColorBtn);
        colorMenu.add(darkColorBtn);
        //adds the button and menu to the main bar
        add(resetBtn);
        add(colorMenu);
    }

    /**
     * Triggered whenever a button is pressed
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //if the light color button is pressed
        if (e.getSource() == lightColorBtn) {
            //displays a popup with an array of colors to choose from
            Color c = JColorChooser.showDialog(((Component) e.getSource()).getParent(), "Colours", ChessFrame.lightColor);
            //sets the board's light color
            frame.gameBoard.setLightColor(c);
            //sets current turn label
            frame.currentTurnLbl.setForeground(c);
        } else if (e.getSource() == darkColorBtn) {//if the dark button is pressed
            //displays a popup with an array of colors to choose from
            Color c = JColorChooser.showDialog(((Component) e.getSource()).getParent(), "Colours", ChessFrame.darkColor);
            //sets the board's light color
            frame.gameBoard.setDarkColor(c);
            //sets current turn label
            frame.getContentPane().setBackground(c);
        } else if (e.getSource() == resetBtn) {//if the reset button is pressed
            frame.reset();//reset the frame
        }
    }
}
