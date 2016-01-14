package chess.game;

/**
 * This class defines the knight piece and how it moves
 *
 * @author Jurgen Aliaj
 */
public class Knight extends Piece {

    /**
     *
     * @param pos
     * @param color
     * @param game
     */
    public Knight(Position pos, int color, Game game) {
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
     * Checks that the move does not violate knight rules
     */
    public boolean isValidCapture(Position newPos) {
        return curPos != newPos // must not click same position
                && ((Math.abs(curPos.getRow() - newPos.getRow()) == 2 // up/down 2 and left/right 1
                && Math.abs(curPos.getCol() - newPos.getCol()) == 1)
                || (Math.abs(curPos.getRow() - newPos.getRow()) == 1 // or up/down 1 and left/right 2
                && Math.abs(curPos.getCol() - newPos.getCol()) == 2))
                && (!newPos.isOccupied()
                || newPos.getPiece().getColor() != getColor()); // piece must not attack its own side
    }
}
