package chess.game;

import java.io.Serializable;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * This initializes the board and controls the flow of the game by switching
 * between player turns
 *
 * @author Abel MacNeil, Jurgen Aliaj
 */
public class Game implements Serializable {

    public static final int BLACK = 1;
    public static final int WHITE = 0;
    protected List<Piece> pieces;
    public List<Piece> removed;
    /**
     * the games's board positions in an 8x8 array
     */
    public Position[][] board;
    private int currentTurn;

    /**
     * Creates a new instance of the Game class
     */
    public Game() {
        //initializes the variabels
        pieces = new LinkedList<>();
        removed = new LinkedList<>();
        board = new Position[8][8];
        //loops through the board setting default values
        for (int i = board.length - 1; i > -1; i--) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Position(i, j);
            }
        }
        // initialize board pieces with their colors
        for (int color = 0; color < 2; color++) {
            //sets the pieces according to their order
            board[color * 7][0].setPiece(new Rook(board[color * 7][0], color, this));
            board[color * 7][1].setPiece(new Knight(board[color * 7][1], color, this));
            board[color * 7][2].setPiece(new Bishop(board[color * 7][2], color, this));
            board[color * 7][3].setPiece(new Queen(board[color * 7][3], color, this));
            board[color * 7][4].setPiece(new King(board[color * 7][4], color, this));
            board[color * 7][5].setPiece(new Bishop(board[color * 7][5], color, this));
            board[color * 7][6].setPiece(new Knight(board[color * 7][6], color, this));
            board[color * 7][7].setPiece(new Rook(board[color * 7][7], color, this));
        }
        //sets the pawns
        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(new Pawn(board[1][i], WHITE, this));
            board[6][i].setPiece(new Pawn(board[6][i], BLACK, this));
        }
        // initialize list of pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //if occupied add the piece to the list
                if (board[i][j].isOccupied()) {
                    pieces.add(board[i][j].getPiece());
                }
            }
        }
        currentTurn = Game.WHITE; // white starts
    }

    /**
     * Gets whose turn it currently is, either: {@code Game.BLACK} or
     * {@code Game.WHITE}
     *
     * @return the current turn
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * Moves to the next turn. {@code Piece.isValidMove()} must be called first.
     *
     * @param piece the piece to move
     * @param newPos the new position to move to
     * @return the piece capture (null otherwise)
     */
    public Piece nextTurn(Piece piece, Position newPos) {
        Piece result = null;
        //resets a pawn's ability for en passent, loops through all of the pieces
        for (int i = 0; i < pieces.size(); i++) {
            //if the piece is a pawn, of our color, and has moved only once
            if (pieces.get(i) instanceof Pawn
                    && pieces.get(i).getColor() == currentTurn
                    && pieces.get(i).nMoves == 1) {
                //sets the opportunity for en Passent false
                ((Pawn) pieces.get(i)).enPassantOpportunity = false;
            }
        }
        //if the piece is ours
        if (piece.getColor() == currentTurn) {
            Position oldPos = piece.getPosition();//save the old position
            boolean twoUp = piece instanceof Pawn && ((Pawn) piece).twoUp(newPos);//whether or not a pawn has moved up
            result = piece.move(newPos);//move the piece and save any captured piece
            //if we have taken a piece, remove it and add it to the list of removed pieces
            if (newPos.isOccupied() && result != null) {
                pieces.remove(result);
                removed.add(result);
            }
            //if the pawn has reached the other side of the board, promote it.
            if (piece instanceof Pawn && (newPos.getRow() == 7 || newPos.getRow() == 0)) {
                int tempColor = piece.getColor();
                //possible pieces to promote to
                Object[] possibilities = {"Queen", "Rook", "Bishop", "Knight"};
                //show an option dialogue of options and save the user's choice
                String s = (String) JOptionPane.showInputDialog(
                        null,
                        "Which piece would you like?",
                        "Pawn Promotion",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        "chess");
                pieces.remove(piece);//remove the pawn
                //based on user input set the piece to the selected piece
                if (s.equals(possibilities[0])) {
                    newPos.setPiece(new Queen(newPos, tempColor, this));
                } else if (s.equals(possibilities[1])) {
                    newPos.setPiece(new Rook(newPos, tempColor, this));
                } else if (s.equals(possibilities[2])) {
                    newPos.setPiece(new Bishop(newPos, tempColor, this));
                } else if (s.equals(possibilities[3])) {
                    newPos.setPiece(new Knight(newPos, tempColor, this));
                }
                pieces.add(newPos.getPiece());//add t to the list of new pieces

            } else if (twoUp) {//if the pawn has moved up two spots, the pawn can be taken via en passent
                ((Pawn) piece).enPassantOpportunity = true;
                //if the king moves two over to the right (castling)
            } else if (piece instanceof King && oldPos.getCol() + 2 == newPos.getCol()
                    && board[oldPos.getRow()][7].isOccupied()) {
                board[oldPos.getRow()][7].getPiece().move(board[oldPos.getRow()][5]);
                //if the king moves two over to the left (castling)
            } else if (piece instanceof King && oldPos.getCol() - 2 == newPos.getCol()
                    && board[oldPos.getRow()][0].isOccupied()) {
                board[oldPos.getRow()][0].getPiece().move(board[oldPos.getRow()][3]);
            }
            currentTurn = Math.abs(currentTurn - 1);//change the current turn
        }
        return result;//return the captured piece
    }
}
