package chess.ui;

import chess.game.Position;
import chess.game.Game;
import java.awt.*;
import javax.swing.JPanel;

@Deprecated
public class Square extends JPanel {

    Graphics g;
    int color, x, y, size;
    Position pos;
    
    Square(int color, int x, int y, int size, Position pos) {
        super();
        this.color = color;
        this.x = x;
        this.y = y;
        this.size = size;
        this.pos = pos;
        if (color == Game.BLACK) {
            setBackground(ChessFrame.darkColor);
        } else {
            setBackground(ChessFrame.lightColor);
        }
    }

    @Override
    public void paint(Graphics g) {
        if (color == Game.BLACK) {
            g.setColor(ChessFrame.darkColor);
        } else {
            g.setColor(ChessFrame.lightColor);
        }
        g.fillRect(x, y, size, size);
    }

    void drawBlack() {
        g.setColor(new Color(120, 73, 17));
        g.fillRect(x, y, size, size);
        g.drawRect(x, y, size, size);
    }

    void drawWhite() {
        g.setColor(Color.black);
        g.drawRect(x, y, size, size);
    }
}
