package game.objects;

import game.GameConstants;

import java.awt.*;

/**
 * Created by Michel on 5-6-2017.
 */
public class Player {
    private Snake _snake;

    public Player() {
        _snake = new Snake(this, new Point(40 * GameConstants.SNAKE_ELEMENT_SIZE, 20 * GameConstants.SNAKE_ELEMENT_SIZE), 5);
    }

    public Snake getSnake() {
        return _snake;
    }
}
