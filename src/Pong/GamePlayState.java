package Pong;



import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

/**
 * 
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class GamePlayState extends BasicGameState {

	private int stateID = -1;
	private Image background, ballImage;
	private int height, width;
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;


	public GamePlayState(int stateID) {
		this.stateID = stateID;
	}

	/**
	 * Returns the state ID
	 * @return Returns the state ID
	 */
	@Override
	public int getID() {
		return stateID;
	}

	/**
	 * Initializes data before starting the actual game loop.
	 */
	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		height = container.getHeight();
		width = container.getWidth();
		background = new Image("data/backgrounds/default.png");
		Image paddleImage = new Image("data/paddles/paddle.png"); // bör sättas i paddle, men senare med meny?
		ballImage = new Image("data/ball/default.png");
		int yStart = (height-paddleImage.getHeight())/2;
		paddleLeft = new Paddle(yStart, height, paddleImage); // both paddles start at same y-position
		paddleRight = new Paddle(yStart, height, paddleImage);
		// mirror the startposition of paddle1
		paddleRightXPosition = width-paddleLeftXPosition-paddleImage.getWidth();
		// Nedan är start-koordinater för en boll, dock skulle vi ju serva bollen så måste ändras till att följa paddlarna
		ball = new Ball((width-ballImage.getWidth())/2, (height-ballImage.getHeight())/2, ballImage);
	}

	/**
	 * Updates game logic during the gameplay.
	 * Receives user input and prepares an output.
	 */
	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		
		// Necessary coordinates for collision-control of ball
		HashMap<String, Integer> currentState = new HashMap<String, Integer>();
		currentState.put("pLeftX", paddleLeftXPosition);
		currentState.put("pLeftY", paddleLeft.getY());
		currentState.put("pRightX", paddleRightXPosition);
		currentState.put("pRightY", paddleRight.getY());
		currentState.put("pWidth", paddleLeft.getImage().getWidth());
		currentState.put("pHeight", paddleLeft.getImage().getHeight());
		currentState.put("frameHeight", height);
		currentState.put("frameWidth", width);
		currentState.put("angle", paddleLeft.getAngle());
		float speed = delta/4;
		
		ball.moveBall(currentState, speed);
		
		
		Input input = container.getInput();
		
		if(input.isKeyDown(Input.KEY_ESCAPE)) {
			Transition t = new FadeOutTransition();
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);
		}
		
		if(input.isKeyDown(Input.KEY_W))
			paddleLeft.paddleUp(speed);

		if(input.isKeyDown(Input.KEY_S))
			paddleLeft.paddleDown(speed);
		
		if(input.isKeyDown(Input.KEY_D))
			ball.servedLeft();

		if(input.isKeyDown(Input.KEY_UP))
			paddleRight.paddleUp(speed);

		if(input.isKeyDown(Input.KEY_DOWN))
			paddleRight.paddleDown(speed);
		
		if(input.isKeyDown(Input.KEY_LEFT))
			ball.servedRight();
	}

	/**
	 * Renders the prepared output to visual output.
	 */
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		paddleLeft.getImage().draw(paddleLeftXPosition, paddleLeft.getY());
		paddleRight.getImage().draw(paddleRightXPosition, paddleRight.getY());
		ball.getImage().draw(ball.getXPosition(), ball.getYPosition());
	}

	public Image getBackground() {
		return background;
	}

}