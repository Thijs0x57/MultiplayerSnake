package game;

import Network.Client;
import Network.NetworkPanel;
import game.objects.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Michel on 5-6-2017.
 */
public class GameKeyAdapter extends KeyAdapter {
    private Player _player;
    private NetworkPanel networkPanel;

    public GameKeyAdapter(Player player) {
        _player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Snake snake = _player.getSnake();

        if(snake != null)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    snake.setDirection(Direction.UP);

                    break;
                case KeyEvent.VK_DOWN:
                    snake.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    snake.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    snake.setDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_SPACE:
                    snake.setDirection(Direction.NONE);
                    break;
            }
        }
    }
}
