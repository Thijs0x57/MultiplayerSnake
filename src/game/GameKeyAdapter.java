package game;

import game.objects.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Michel on 5-6-2017.
 */
public class GameKeyAdapter extends KeyAdapter {
    private Player _player;
    private EnemyPlayer _enemyPlayer;

    public GameKeyAdapter(Player player, EnemyPlayer enemyPlayer) {
        _player = player;
        _enemyPlayer = enemyPlayer;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Snake snake = _player.getSnake();
        EnemySnake enemySnake = _enemyPlayer.getSnake();

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

        if(enemySnake != null)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_W:
                    enemySnake.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_A:
                    enemySnake.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_S:
                    enemySnake.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_D:
                    enemySnake.setDirection(Direction.RIGHT);
                    break;
            }
        }
    }
}
