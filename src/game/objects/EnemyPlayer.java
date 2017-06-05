package game.objects;

import java.awt.*;

/**
 * Created by Thijs on 5-6-2017.
 */
public class EnemyPlayer {
    private EnemySnake _snake;

    public EnemyPlayer() {
        _snake = new EnemySnake(new Point(40 * GameConstants.SNAKE_ELEMENT_SIZE, 20 * GameConstants.SNAKE_ELEMENT_SIZE));
    }

    public EnemySnake getSnake() {
        return _snake;
    }
}
