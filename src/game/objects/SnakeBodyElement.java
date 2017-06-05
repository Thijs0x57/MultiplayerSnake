package game.objects;

import game.GameConstants;

import java.awt.*;

/**
 * Created by Michel on 5-6-2017.
 */
public class SnakeBodyElement extends GameObject {
    public int x;
    public int y;

    public SnakeBodyElement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public boolean hasCollision(GameObject otherGameObject) {
        return false;
    }

    @Override
    public void setPosition(Point newPosition) {

    }

    @Override
    public Point getPosition() {
        return new Point(x, y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, GameConstants.SNAKE_ELEMENT_SIZE, GameConstants.SNAKE_ELEMENT_SIZE);
    }
}
