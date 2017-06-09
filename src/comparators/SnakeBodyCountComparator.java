package comparators;

import game.objects.Snake;

import java.util.Comparator;

/**
 * Created by Michel on 9-6-2017.
 */
public class SnakeBodyCountComparator implements Comparator<Snake> {

    @Override
    public int compare(Snake snakeOrigin, Snake snakeOther) {
        if(snakeOrigin.getBodyCount() > snakeOther.getBodyCount())
            return 1;
        else if (snakeOrigin.getBodyCount() < snakeOther.getBodyCount())
            return -1;
        else
            return 0;
    }
}
