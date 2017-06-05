package game.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Michel on 5-6-2017.
 */
public class Snake extends GameObject {
    private LinkedList<SnakeBodyElement> _snakeBody;
    private Point _snakePosition;
    private Direction _snakeDirection;

    public Snake(Point startPostion, int startTailSize) {
        _snakeBody = new LinkedList<>();
        _snakePosition = startPostion;
        _snakeDirection = Direction.NONE;

        for (int i = 0; i < startTailSize + 1; i++) {
            _snakeBody.add(new SnakeBodyElement(startPostion.x, startPostion.y + (i * GameConstants.SNAKE_ELEMENT_SIZE) ));
        }
    }

    public Snake(Point startPosition) {
        this(startPosition, 0);
    }

    @Override
    public void draw(Graphics2D g) {
        Iterator<SnakeBodyElement> it = _snakeBody.iterator();
        for(int i = 0; i < _snakeBody.size(); i++) {
            if(it.hasNext()) {
                if (i == 0) {
                    g.setColor(GameConstants.SNAKE_HEAD_COLOR);
                } else {
                    g.setColor(GameConstants.SNAKE_ELEMENT_COLOR);
                }

                SnakeBodyElement e = it.next();

                g.fillRect(e.x, e.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(e.x, e.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
            }
        }
    }

    @Override
    public void update() {
        _snakeBody.removeLast();

        SnakeBodyElement newBodyElement = new SnakeBodyElement(_snakePosition.x, _snakePosition.y);

        switch(_snakeDirection) {
            case LEFT:
                _snakePosition.x -= GameConstants.SNAKE_ELEMENT_SIZE;
                newBodyElement.x -= GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case DOWN:
                _snakePosition.y += GameConstants.SNAKE_ELEMENT_SIZE;
                newBodyElement.y += GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case RIGHT:
                _snakePosition.x += GameConstants.SNAKE_ELEMENT_SIZE;
                newBodyElement.x += GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case UP:
                _snakePosition.y -= GameConstants.SNAKE_ELEMENT_SIZE;
                newBodyElement.y -= GameConstants.SNAKE_ELEMENT_SIZE;
                break;
            case NONE:
                // DO NOTHING
                break;
        }


        _snakeBody.push(newBodyElement);
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
        if(_snakeDirection == Direction.LEFT && newDirection == Direction.RIGHT) {
            // Do nothing
        } else if (_snakeDirection == Direction.DOWN && newDirection == Direction.UP) {
            // Do nothing
        } else if (_snakeDirection == Direction.RIGHT && newDirection == Direction.LEFT) {
            // Do nothing
        } else if (_snakeDirection == Direction.UP && newDirection == Direction.DOWN) {
            // Do nothing
        } else {
            _snakeDirection = newDirection;
        }
    }
}
