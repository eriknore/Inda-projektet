package Pong;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class Game extends BasicGame {

	private Image testBackground;
	private Image paddleImage;
	private Image ballImage;
	private static int width = 800;
	private static int height = 600;
	
    public Game() {
        super("Pong");
    }
    
    /**
     * Initializes data before starting the actual game loop.
     */
    @Override
    public void init(GameContainer container) throws SlickException {
    	testBackground = new Image("data/backgrounds/default.png");
    	paddleImage = new Image("data/paddles/paddle.png");
    	ballImage = new Image("data/ball/default.png");
    }

    /**
     * Updates game logic during the gameplay.
     * Receives user input and prepares an output.
     */
    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {}
    
    /**
     * Renders the prepared output to visual output.
     */
    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
    	testBackground.draw(0, 0);
    	int paddle1X = 20;
    	int paddle1Y = (height-paddleImage.getHeight())/2;
    	int paddle2X = width-20-paddleImage.getWidth();
    	int paddle2Y = (height-paddleImage.getHeight())/2;
    	paddleImage.draw(paddle1X, paddle1Y);
    	paddleImage.draw(paddle2X, paddle2Y);
    	ballImage.draw((width-ballImage.getWidth())/2, (height-ballImage.getHeight())/2);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(width, height, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}