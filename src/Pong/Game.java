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
	
    public Game() {
        super("Pong");
    }
    
    /**
     * Initializes data before starting the actual game loop.
     */
    @Override
    public void init(GameContainer container) throws SlickException {
    	testBackground = new Image("data/backgrounds/woodenbackground.png");
    	paddleImage = new Image("data/paddles/paddle1.gif");
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
    	paddleImage.draw(20, 20);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}