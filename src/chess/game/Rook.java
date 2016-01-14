package chess.game;

/**
 * This class defines the rook piece and controls how it moves
 *
 * @author Jurgen Aliaj
 */
public class Rook extends Piece {

    /**
     *
     * @param pos
     * @param color
     * @param game
     */
    public Rook(Position pos, int color, Game game) {
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
     * Checks that the move does not violate rook rules
     */
    public boolean isValidCapture(Position newPos) {
        int deltaX = (newPos.getCol() - curPos.getCol());
        int deltaY = (newPos.getRow() - curPos.getRow());
        if (deltaX != 0) {
            deltaX = deltaX / Math.abs(deltaX); // convert to unit vector to obtain direction
        }
        if (deltaY != 0) {
            deltaY = deltaY / Math.abs(deltaY); // {...}
        }
        return curPos != newPos // must not click the same position
                && (deltaX == 0 || deltaY == 0) // can move up/down or left/right
                && nothingInBetween(newPos, game.board[curPos.getRow() + deltaY][curPos.getCol() + deltaX], deltaX, deltaY) // no jumping
                && (!newPos.isOccupied()
                || newPos.getPiece().getColor() != getColor()); // must not attak its own side
    }
}