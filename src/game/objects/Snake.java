package game.objects;

import game.GameConstants;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Michel on 5-6-2017.
 */
public class Snake extends GameObject {
    private LinkedList<SnakeBodyElement> _snakeBody;
    private Point _snakePosition;
    private Direction _snakeDirection;

    private Player _ownerPlayer;

    private int _protectedCounter;

    public Snake(Player player, Point startPostion, int startTailSize) {
        _ownerPlayer = player;

        _snakeBody = new LinkedList<>();
        _snakePosition = startPostion;
        _snakeDirection = Direction.NONE;

        for (int i = 0; i < startTailSize + 1; i++) {
            _snakeBody.add(new SnakeBodyElement(startPostion.x, startPostion.y + (i * GameConstants.SNAKE_ELEMENT_SIZE) ));
        }

        _protectedCounter = GameConstants.PROTECTION_TIMER;
    }

    public Snake(Player player, Point startPosition) {
        this(player, startPosition, 5);
    }

    @Override
    public void draw(Graphics2D g) {
        Iterator<SnakeBodyElement> it = _snakeBody.iterator();
        for(int i = 0; i < _snakeBody.size(); i++) {
            if(it.hasNext()) {
                SnakeBodyElement e = it.next();

                g.setColor(Color.white);
                g.drawString(i + 1 + "", e.x, e.y);

                if (i == 0) {
                    g.setColor(GameConstants.SNAKE_HEAD_COLOR);
                } else {
                    if(isProtected() && (_protectedCounter % 2 == 0)) {
                        g.setColor(GameConstants.PROTECTION_COLOR);
                    } else {
                        g.setColor(GameConstants.SNAKE_ELEMENT_COLOR);
                    }

                }

                g.fillRect(e.x, e.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(e.x, e.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
            }
        }
    }

    @Override
    public void update() {
        if(_protectedCounter > 0) {
            _protectedCounter--;
        }

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

    public Direction getDirection() {
        return _snakeDirection;
    }

    @Override
    public void setPosition(Point newPosition) {
        _snakePosition = newPosition;
    }

    @Override
    public Point getPosition() {
        return _snakePosition;
    }

    /**
     *
     * @return Bounds of the head
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(_snakeBody.get(0).x, _snakeBody.get(0).y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
    }

    public void addBodyElement() {
        _snakeBody.push(new SnakeBodyElement(_snakePosition.x, _snakePosition.y));
    }

    public void removeBodyElement() {
        _snakeBody.removeLast();
    }

    @Override
    public boolean hasCollision(GameObject otherGameObject) {
        // Check collsion with other snake
        if(otherGameObject instanceof Snake && otherGameObject != this) {
            Rectangle headBounds = _snakeBody.get(0).getBounds();
            Rectangle otherSnakeHeadBounds = otherGameObject.getBounds();

            // Head-on-head collision
            if (headBounds.intersects(otherSnakeHeadBounds)) {
                return true;
            } else {
                // Head-on-body collision
                for(SnakeBodyElement sbe : ((Snake) otherGameObject)._snakeBody) {
                    if(headBounds.intersects(sbe.getBounds())) {
                        return true;
                    }
                }
            }
            // Check collision with body of self
        } else if (otherGameObject instanceof Snake && otherGameObject == this) {
            Rectangle headBounds = _snakeBody.get(0).getBounds();

            if(((Snake)otherGameObject).getDirection() != Direction.NONE) {
                for (int i = 1; i < _snakeBody.size(); i++) {
                    if (headBounds.intersects(_snakeBody.get(i).getBounds())) {
                        return true;
                    }
                }
            }
        } else if (otherGameObject instanceof Apple) {
            Rectangle headBounds = _snakeBody.get(0).getBounds();
            Rectangle appleBounds = otherGameObject.getBounds();

            if(headBounds.intersects(appleBounds)) {
                return true;
            }
        }

        return false;
    }

    public boolean isProtected() {
        return _protectedCounter > 0;
    }

    public void setIsProtected() {
        _protectedCounter = GameConstants.PROTECTION_TIMER;
    }

    public int getBodyCount() {
        return _snakeBody.size();
    }
}
