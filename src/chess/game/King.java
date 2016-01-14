package chess.game;

/**
 * This class defines the king piece and how it moves
 *
 * @author Jurgen Aliaj, Abel MacNeil
 */
public class King extends Piece {

    /**
     * Inherit behavior from parent class
     *
     * @param pos
     * @param color
     * @param game
     */
    public King(Position pos, int color, Game game) {
        super(pos, color, game);
    }

    /**
     * Checks that the move does not violate general rules
     * 
     * @param newPos
     * @return if the move 
     */
    @Override
    public boolean isValidMove(Position newPos) {
        return super.isValidMove(newPos) && isValidCapture(newPos);
    }

    /**
     * Checks that the move does not violate king moves
     *
     * @param newPos
     * @return if the move is valid (boolean)
     */
    @Override
    public boolean isValidCapture(Position newPos) {
        int deltaX = (newPos.getCol() - curPos.getCol());
        int deltaY = (newPos.getRow() - curPos.getRow());
        return curPos != newPos
                && (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 0) // left or right by 1
                | (Math.abs(deltaX) == 0 && Math.abs(deltaY) == 1) // up or down by 1
                | (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 1) // diagonally by 1
                | isCastlingLeft(newPos) // check for castling
                | isCastlingRight(newPos)
                && (!newPos.isOccupied()
                || newPos.getPiece().getColor() != getColor()); // cannot attack its own side
    }

    /**
     * Check if the king is castling right
     *
     * @param newPos
     * @return if the king is castling right (boolean)
     */
    public boolean isCastlingRight(Position newPos) {
        return curPos.getPiece().nMoves == 0 // both king and rook must have zero moves
                && game.board[curPos.getRow()][7].isOccupied()
                && game.board[curPos.getRow()][7].getPiece().nMoves == 0
                && newPos.getRow() == curPos.getRow() && newPos.getCol() == 6
                && nothingInBetween(game.board[curPos.getRow()][7], game.board[curPos.getRow()][curPos.getCol() + 1], 1, 0)
                && noInterference(curPos, game.board[curPos.getRow()][6]); // there must be no threat in the king's path
    }

    /**
     * Check if the king is castling to the left
     *
     * @param newPos
     * @return if the king is castling left (boolean)
     */
    public boolean isCastlingLeft(Position newPos) {
        return curPos.getPiece().nMoves == 0 // {...}
                && game.board[curPos.getRow()][0].isOccupied()
                && game.board[curPos.getRow()][0].getPiece().nMoves == 0
                && newPos.getRow() == curPos.getRow() && newPos.getCol() == 2
                && nothingInBetween(game.board[curPos.getRow()][0], game.board[curPos.getRow()][curPos.getCol() - 1], -1, 0)
                && noInterference(game.board[curPos.getRow()][2], curPos);
    }

    /**
     * Check for threat at a single position
     *
     * @param pos
     * @return no interference at a position (boolean)
     */
    public boolean interference(Position pos) {
        for (int i = 0; i < game.pieces.size(); i++) {
            if (game.pieces.get(i).getColor() != getColor() && game.pieces.get(i).isValidCapture(pos)) {
                return true; // if any piece can attack, there is a threat
            }
        }
        return false; // no pieces can attack, therefore no threat
    }

    /**
     * Check for no threats in between two positions (recursive algorithm)
     *
     * @param posA
     * @param posB
     * @return whether or not there is an interference (boolean)
     */
    public boolean noInterference(Position posA, Position posB) {
        if (posA == posB) {
            return !interference(posB); // must be no interference
        }
        return !interference(posB) // recursively call the next position until both positions are equal
                && noInterference(posA, game.board[posB.getRow()][posB.getCol() - 1]);
    }
}
