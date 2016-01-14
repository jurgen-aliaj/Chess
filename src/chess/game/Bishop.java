package chess.game;

/**
 * This is the class which defines the bishop piece and how it moves
 *
 * @author Jurgen Aliaj
 */
public class Bishop extends Piece {

    /**
     *
     * @param pos
     * @param color
     * @param game
     */
    public Bishop(Position pos, int color, Game game) {
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
     * Checks that the move does not violate bishop rules
     */
    public boolean isValidCapture(Position newPos) {
        int deltaX = (newPos.getCol() - curPos.getCol()), dirX = 0;
        int deltaY = (newPos.getRow() - curPos.getRow()), dirY = 0;
        if (deltaX != 0) {
            dirX = deltaX / Math.abs(deltaX); // convert to unit vector to find direction of movement
        }
        if (deltaY != 0) {
            dirY = deltaY / Math.abs(deltaY); //{...}
        }
        return newPos != curPos // must not click the same position
                && Math.abs(deltaX) == Math.abs(deltaY) // must be in the same diagonal
                && nothingInBetween(newPos, game.board[curPos.getRow() + dirY][curPos.getCol() + dirX], dirX, dirY) // not jumping
                && (!newPos.isOccupied()
                || newPos.getPiece().getColor() != getColor()); // the piece must not attack its own side
    }
}
