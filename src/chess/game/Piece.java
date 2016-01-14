package chess.game;

import java.io.Serializable;

/**
 * This is the general abstract class which is extended by all pieces
 *
 * @author Abel MacNeil, Jurgen Aliaj
 */
public abstract class Piece implements Serializable {

    protected Position curPos;
    private int color;
    protected Game game;
    protected int nMoves;

    /**
     * Creates and instance of a Piece with position, color, and current game.
     * @param pos The piece's current position.
     * @param color The color (Game.BLACK or Game.WHITE)
     * @param game the current game in progress
     */
    public Piece(Position pos, int color, Game game) {
        this.curPos = pos;
        this.color = color;
        this.game = game;
        nMoves = 0;
    }

    /**
     * Check if the move does not result in the king being exposed.
     * All subclasses should call super.isValidMove(pos);
     *
     * @param newPos the position to move to
     * @return whether or not the new position is a valid move
     */
    public boolean isValidMove(Position newPos) {
        Position oldPos = this.getPosition(); // temporarily move the piece to the new position
        Piece tempPiece = this.move(newPos);
        //remove the piece if it exists and is not a King
        if (tempPiece != null && !(tempPiece instanceof King)) {
            game.pieces.remove(tempPiece);
        }
        boolean n = this.inCheck(getColor()); // check if the side is in check
        newPos.getPiece().move(oldPos); //move the pieces back to their original positions
        newPos.setPiece(tempPiece);
        if (tempPiece != null && !(tempPiece instanceof King)) {
            game.pieces.add(newPos.getPiece());
        }
        oldPos.getPiece().nMoves -= 2; // balance the number of moves again
        return !n;
    }

    /**
     * Whether or not the move to the new position results in a valid capture.
     * @param pos the new Position
     * @return boolean whether or not the move to the new position results in a valid capture.
     */
    public abstract boolean isValidCapture(Position pos);

    /**
     * Gets the current position.
     * @return the current position
     */
    public Position getPosition() {
        return this.curPos;
    }

    /**
     * Sets the current position
     * @param pos the new position
     */
    public void setPosition(Position pos) {
        this.curPos = pos;
    }

    /**
     * Move a piece to a new position
     *
     * @param newPos the new position
     * @return captured piece
     */
    public Piece move(Position newPos) {
        Piece result = newPos.getPiece();
        // if the en passant, remove the piece behind pawn
        if (curPos.getPiece() instanceof Pawn && ((Pawn) curPos.getPiece()).enPassant(newPos)) { 
            result = game.board[curPos.getRow()][newPos.getCol()].getPiece();
            game.pieces.remove(result);
            game.board[curPos.getRow()][newPos.getCol()].setPiece(null);
        }
        curPos.setPiece(null);//sets current postion to null (empty)
        curPos = newPos;//set the current position as the new position
        curPos.setPiece(this);
        nMoves++;//increment the number of moves this piece has taken
        return result;
    }

    /**
     * Return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Converts the Piece to a String
     * @return string of the piece object
     */
    @Override
    public String toString() {
        char col = 'W';
        if (getColor() == Game.BLACK) {
            col = 'B';
        }
        return this.getClass().getSimpleName() + "-" + col + "-" + curPos;
    }

    /**
     * Check if the king can be attacked
     *
     * @return whether or not our king is in check
     */
    public boolean inCheck(int color) {
        for (int i = 0; i < game.pieces.size(); i++) {
            if (game.pieces.get(i).getColor() != color && game.pieces.get(i).isValidCapture(getKingPosition(Math.abs(color)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the location of the king
     *
     * @return the location of our king
     */
    public Position getKingPosition(int color) {
        Position kingPos = null;
        for (int i = 0; i < game.pieces.size(); i++) {
            if (game.pieces.get(i).getColor() == color && game.pieces.get(i) instanceof King) {
                kingPos = game.pieces.get(i).getPosition();
            }
        }
        return kingPos;
    }

    /**
     * Check to see if two positions have nothing in between them to prevent
     * pieces jumping over each other (recursive algorithm)
     *
     * @param newPos the new position
     * @param current the current position
     * @param dirX the horizontal direction (magnitude 1)
     * @param dirY the vertical direction (magnitude 1)
     * @return whether or not there is nothing in between the two positions
     */
    public boolean nothingInBetween(Position newPos, Position current, int dirX, int dirY) {
        if (current == newPos || current.getCol() + dirX == -1 || current.getRow() + dirY == -1
                || current.getCol() + dirX == 8 || current.getRow() + dirY == 8) {
            return true;
        }
        return !current.isOccupied() //recursively check if the next position is also empty
                && nothingInBetween(newPos, game.board[current.getRow() + dirY][current.getCol() + dirX], dirX, dirY);
    }

    /**
     * Check if there are no more possible moves
     *
     * @return whether or not there are no more possible moves
     */
    public boolean isUnplayable() {
        for (int i = 0; i < game.pieces.size(); i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    game.pieces.get(i).isValidMove(game.board[j][k]);
                    if (game.pieces.get(i).getColor() != getColor() && game.pieces.get(i).isValidMove(game.board[j][k])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check for checkmate
     *
     * @return whether or not checkmate
     */
    public boolean isCheckMate() {
        return inCheck(Math.abs(getColor() - 1)) && isUnplayable();
    }

    /**
     * Check for stalemate
     *
     * @return
     */
    public boolean isStaleMate() {
        return !inCheck(Math.abs(getColor() - 1)) && isUnplayable();
    }

    /**
     * Check if there is a draw due to insufficient material to complete the
     * game
     *
     * @return a draw if there is insufficient material (boolean)
     */
    public boolean isDraw() {
        boolean insufficient = false;
        for (int i = 0; i < game.pieces.size(); i++) {
            insufficient = game.pieces.get(i) instanceof Knight || game.pieces.get(i) instanceof Bishop;
        }
        return game.pieces.size() == 2 || (game.pieces.size() == 3 && insufficient);
    }

    /**
     * Gets the path to the image for the current piece.
     * ex src/chess/images/w_rook70.png
     * @return the path to the image for the current piece
     */
    public String getImagePath() {
        String result = "src/chess/images/";
        if (getColor() == Game.WHITE) {
            result += "w";
        } else {
            result += "b";
        }
        result += "_" + this.getClass().getSimpleName().toLowerCase() + "70.png";
        return result;
    }
}
