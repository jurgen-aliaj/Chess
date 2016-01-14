package chess.game;

/**
 * This class defines the queen piece and how it moves
 *
 * @author Jurgen Aliaj
 */
public class Queen extends Piece {

    /**
     *
     * @param pos
     * @param color
     * @param game
     */
    public Queen(Position pos, int color, Game game) {
        super(pos, color, game);
    }

    @Override
    /**
     * Checks that the move does not violate general rules
     */
    public boolean isValidMove(Position newPos) {
        return super.isValidMove(newPos) && isValidCapture(newPos);
    }

    @Override
    /**
     * Checks that the move does not violate queen rules
     */
    public boolean isValidCapture(Position newPos) {
        int deltaX = (newPos.getCol() - curPos.getCol()), dirX = 0;
        int deltaY = (newPos.getRow() - curPos.getRow()), dirY = 0;
        if (deltaX != 0) {
            dirX = deltaX / Math.abs(deltaX); // convert to unit vector to obtain direction
        }
        if (deltaY != 0) {
            dirY = deltaY / Math.abs(deltaY); // {...}
        }
        return newPos != curPos // must not click the same position
                && ((deltaX == 0 || deltaY == 0) || Math.abs(deltaX) == Math.abs(deltaY)) // move up/down or left/right or diagonally
                && nothingInBetween(newPos, game.board[curPos.getRow() + dirY][curPos.getCol() + dirX], dirX, dirY) // no jumping
                && (!newPos.isOccupied()
                || newPos.getPiece().getColor() != getColor()); // must not attack its own side
    }
}
