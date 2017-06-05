package game.objects;

import game.GameConstants;

import java.awt.*;

/**
 * Created by Michel on 5-6-2017.
 */
public class Apple extends GameObject {
    private Point _applePosition;

    public Apple(Point startPosition) {
        _applePosition = startPosition;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(GameConstants.APPLE_COLOR);
        g.fillRect(_applePosition.x, _applePosition.y, GameConstants.GRID_ELEMENT_SIZE, GameConstants.GRID_ELEMENT_SIZE);
    }

    public Point getPosition() {
        return _applePosition;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(_applePosition.x, _applePosition.y, GameConstants.GRID_ELEMENT_SIZE, GameConstants.GRID_ELEMENT_SIZE);
    }

    @Override
    public boolean hasCollision(GameObject otherGameObject) {
        if(otherGameObject instanceof Snake) {

        } else if (otherGameObject instanceof Apple) {

        }

        return false;
    }

    @Override
    public void setPosition(Point newPosition) {

    }
}
