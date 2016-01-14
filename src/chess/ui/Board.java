package chess.ui;

import chess.game.Position;
import chess.game.Game;
import chess.game.Piece;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the main Board that holds all of the pieces and squares. 
 * All of the {@code Game} components are accessed here.
 *
 * @author Abel MacNeil, Jurgen Aliaj
 */
public final class Board extends JPanel implements ActionListener {

    private int size;
    private Game game;
    private JButton[][] squares;
    private ImagePanel[][] imgs;
    private boolean isFirstClick;
    private Piece pieceToMove;
    private Position positionToMoveTo;
    private int initI = 0, initJ = 0;
    private Color lightColor;
    private Color darkColor;
    private ChessFrame cframe;

    /**
     * Main Constructor, creates a new Board to be added to another JCompnonent.
     *
     * @param size the size of each square on the board.
     * @param game the current game in play
     * @param cframe the frame that the board will be added to.
     */
    public Board(int size, Game game, ChessFrame cframe) {
        super();
        setLayout(null);
        lightColor = ChessFrame.lightColor;
        darkColor = ChessFrame.darkColor;
        this.size = size; //initialize
        this.game = game;
        this.cframe = cframe;
        squares = new JButton[8][8];
        imgs = new ImagePanel[8][8];
        isFirstClick = true;

        init();
    }

    /**
     * Initializes the frame's components
     */
    public void init() {
        //loop through all of the squares
        for (int i = squares.length - 1; i > -1; i--) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new JButton();//create new button
                //determines whether or not to place the background the square as light or dark
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    squares[i][j].setBackground(ChessFrame.lightColor);
                } else {
                    squares[i][j].setBackground(ChessFrame.darkColor);
                }
                //sets an etched border for the button
                squares[i][j].setBorder(BorderFactory.createEtchedBorder());
                squares[i][j].setLayout(null);//sets the layout manage to null
                imgs[i][j] = new ImagePanel("");//creates an empty image panel
                //if the square is occupied place an image of the piece occupying the place
                if (game.board[i][j].isOccupied()) {
                    imgs[i][j] = new ImagePanel(game.board[i][j].getPiece().getImagePath());
                    squares[i][j].add(imgs[i][j]);//adds the image
                    imgs[i][j].setBounds(0, 0, size, size);//sets the bounds of the image

                }

                add(squares[i][j]);//adds the buttons
                //setst he bonds of the button with respect to the board
                squares[i][j].setBounds((j) * size, (7 - i) * size, size, size);
                squares[i][j].addActionListener(this);//adds an actionlistener for actions
            }
        }
    }

    /**
     * Sets the light color for the board
     *
     * @param light the light color
     */
    public void setLightColor(Color light) {
        setColors(light, darkColor);
    }

    /**
     * Sets the dark color for the board
     *
     * @param dark the dark color
     */
    public void setDarkColor(Color dark) {
        setColors(lightColor, dark);
    }

    /**
     * Sets the colors for the board
     *
     * @param light the light color
     * @param dark the dark color
     */
    public void setColors(Color light, Color dark) {
        //copies our variables
        this.lightColor = light;
        this.darkColor = dark;
        ChessFrame.lightColor = light;
        ChessFrame.darkColor = dark;
        //loops and sets the colors for all of the squares
        for (int i = squares.length - 1; i > -1; i--) {
            for (int j = 0; j < squares.length; j++) {
                //determines whether or not to make the square light or dark
                if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                    squares[i][j].setBackground(lightColor);
                } else {
                    squares[i][j].setBackground(darkColor);
                }
            }
        }
        //sets the colors for number and letter labels on the left an bottom
        for (int i = 0; i < cframe.letterLbls.length; i++) {
            //every other label is dark
            if (i % 2 != 0) {
                cframe.letterLbls[i].setBackground(lightColor);
                cframe.numLbls[i].setBackground(lightColor);
            } else {
                cframe.letterLbls[i].setBackground(darkColor);
                cframe.numLbls[i].setBackground(darkColor);
            }
            cframe.letterLbls[i].setForeground(lightColor);
            cframe.numLbls[i].setForeground(lightColor);
        }
    }

    /**
     * Updates the board, setting the piece's images.
     */
    public void updateBoard() {
        //loops through all of the buttons
        for (int i = squares.length - 1; i > -1; i--) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j].remove(imgs[i][j]);//gets rid of the old image
                //if the pieces is occupied set the image
                if (game.board[i][j].getPiece() != null) {
                    imgs[i][j] = new ImagePanel(game.board[i][j].getPiece().getImagePath());
                    squares[i][j].add(imgs[i][j]);
                    imgs[i][j].setBounds(0, 0, size, size);
                } else { //sets the image to empty otherwise
                    imgs[i][j] = new ImagePanel("");
                    squares[i][j].add(imgs[i][j]);
                    imgs[i][j].setBounds(0, 0, size, size);
                }
            }
        }
    }

    /**
     * This method is called when a button is pressed
     *
     * @param e the ActionEvent for the button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //loops through all the buttons
        for (int i = squares.length - 1; i > -1; i--) {
            for (int j = 0; j < squares.length; j++) {
                //if a square is pressed
                if (squares[i][j] == e.getSource()) {
                    //If the square is occupied and it is the first click
                    if (!game.board[i][j].isEmpty() && isFirstClick) {
                        //saves the current piece
                        this.pieceToMove = game.board[i][j].getPiece();
                        //if the pieces is ours, set the background green
                        if (game.board[i][j].getPiece().getColor() == game.getCurrentTurn()) {
                            squares[i][j].setBackground(Color.GREEN);
                        } else {//otherwise set it red
                            squares[i][j].setBackground(Color.RED);
                        }
                        isFirstClick = !isFirstClick;//not first click anymore
                        
                        //if is not the first click
                    } else if (!isFirstClick) {

                        isFirstClick = !isFirstClick;//set to opposite
                        //if we click our piece on the second click
                        if (game.board[i][j].isOccupied() && game.board[i][j].getPiece().getColor() == game.getCurrentTurn()) {
                            //resets the colors
                            setColors(lightColor, darkColor);
                            //recursively calls the method again, this time as a first click
                            actionPerformed(e);
                            return;
                        }
                        //save the position clicked
                        this.positionToMoveTo = game.board[i][j];
                        //if the piece can move to the new postion and it is our color...
                        if (this.pieceToMove.isValidMove(positionToMoveTo)
                                && pieceToMove.getColor() == game.getCurrentTurn()) {
                            System.out.println(pieceToMove + " to " + positionToMoveTo);
                            //save any piece that might be removed
                            Piece removed = game.nextTurn(pieceToMove, positionToMoveTo);

                            //if a piece has been captured
                            if (removed != null) {
                                //add the piece to the side
                                cframe.addRemovedPiece(removed);
                                //remove the image from the square
                                squares[i][j].remove(imgs[i][j]);
                            }
                            this.updateBoard();//update the board
                            cframe.setTurnText(game.getCurrentTurn());//let the user know who's turn it is 
                        }
                        //if the game is in checkmate
                        if (pieceToMove.isCheckMate()) {
                            //let the user know who won the game
                            if (game.getCurrentTurn() == Game.BLACK) {
                                JOptionPane.showMessageDialog(cframe, "Checkmate, white wins!");
                            } else {
                                JOptionPane.showMessageDialog(cframe, "Checkmate, black wins!");
                            }
                            cframe.reset();//resets the game
                        } else if (pieceToMove.isStaleMate()) {//if the game is in stalemate
                            JOptionPane.showMessageDialog(cframe, "Stalemate, it's a draw!");
                            cframe.reset();
                        } else if (pieceToMove.isDraw()) {//if the game is a draw
                            JOptionPane.showMessageDialog(cframe, "Draw by insufficient material.");
                            cframe.reset();
                        }
                        setColors(lightColor, darkColor);//sets the colors

                    }
                }
            }
        }
    }

    /**
     * sets the current game
     * @param game the new game to be set
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
