package game.objects;

import game.interfaces.Drawable;
import game.interfaces.Updatable;

import java.awt.*;

/**
 * Created by Michel on 5-6-2017.
 */
public abstract class GameObject implements Drawable, Updatable {
    public abstract boolean hasCollision(GameObject otherGameObject);
    public abstract Point getPosition();
}
