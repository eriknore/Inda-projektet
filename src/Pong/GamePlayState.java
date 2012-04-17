package Pong;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class GamePlayState extends BasicGame {

	private Image background, ballImage;
	private static int width = 800, height = 600; // ska inte vara i den här klassen
	// start-position, width between paddle and frame
	private final int paddle1XPosition = 20, speedFactor = 3;
	private int paddle2XPosition;
	private Paddle paddle1, paddle2;
	private Ball ball;
		
    public GamePlayState() {
        super("Pong");
    }
    
    /**
     * Initializes data before starting the actual game loop.
     */
    @Override
    public void init(GameContainer container) throws SlickException {
    	background = new Image("data/backgrounds/default.png");
    	Image paddleImage = new Image("data/paddles/paddle.png"); // bör sättas i paddle, men senare med meny?
    	ballImage = new Image("data/ball/default.png");
    	int yStart = (height-paddleImage.getHeight())/2;
    	paddle1 = new Paddle(yStart, height, paddleImage); // both paddles start at same y-position
    	paddle2 = new Paddle(yStart, height, paddleImage);
    	// mirror the startposition of paddle1
    	paddle2XPosition = width-paddle1XPosition-paddleImage.getWidth();
       	// Nedan är start-koordinater för en boll, dock skulle vi ju serva bollen så måste ändras till att följa paddlarna
    	ball = new Ball((width-ballImage.getWidth())/2, (height-ballImage.getHeight())/2, ballImage);
    }

    /**
     * Updates game logic during the gameplay.
     * Receives user input and prepares an output.
     */
    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
    	Input input = container.getInput();
    	
    	int speed = delta/speedFactor;
   	 
        if(input.isKeyDown(Input.KEY_W))
        	paddle1.paddleUp(speed);
 
        if(input.isKeyDown(Input.KEY_S))
        	paddle1.paddleDown(speed);
        
        if(input.isKeyDown(Input.KEY_UP))
            paddle2.paddleUp(speed);
 
        if(input.isKeyDown(Input.KEY_DOWN))
        	paddle2.paddleDown(speed);
    }
    
    /**
     * Renders the prepared output to visual output.
     */
    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
    	background.draw(0, 0);
    	paddle1.getImage().draw(paddle1XPosition, paddle1.getY());
    	paddle2.getImage().draw(paddle2XPosition, paddle2.getY());
    	ball.getImage().draw(ball.getXPosition(), ball.getYPosition());
    }
    
    public Image getBackground() {
    	return background;
    }
}