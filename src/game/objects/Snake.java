package game.objects;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Michel on 5-6-2017.
 */
public class Snake extends GameObject {
    private int _tailSize;
    private ArrayList<SnakeTailElement> _snakeTail;
    private Point _snakePosition;
    private Direction _snakeDirection;

    public Snake(Point startPostion, int startTailSize) {
        _tailSize = startTailSize;
        _snakeTail = new ArrayList<>();
        _snakePosition = startPostion;
        _snakeDirection = Direction.LEFT;
    }

    public Snake(Point startPosition) {
        this(startPosition, 0);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(GameConstants.SNAKE_HEAD_COLOR);
        g.fillRect(_snakePosition.x, _snakePosition.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
    }

    @Override
    public void update() {
        switch(_snakeDirection) {
            case LEFT:
                _snakePosition.x -= GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case DOWN:
                _snakePosition.y += GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case RIGHT:
                _snakePosition.x += GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case UP:
                _snakePosition.y -= GameConstants.SNAKE_ELEMENT_SIZE;
                break;
        }
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public void onCollision() {
        if(canCollide()) {

        }
    }

    public void setDirection(Direction newDirection) {
        _snakeDirection = newDirection;
    }
}
