package Pong;



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
	private Image background;
	private int height, width;
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;

	private int leftScore = 0;
	private int rightScore = 0;

	
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


	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		height = container.getHeight();
		width = container.getWidth();
		background = new Image("data/backgrounds/default.png");
		paddleLeft = new Paddle(height, paddleLeftXPosition, false); // both paddles start at same y-position
		paddleRightXPosition = width-paddleLeftXPosition-paddleLeft.getImage().getWidth();
		paddleRight = new Paddle(height, paddleRightXPosition, true);
		// mirror the startposition of paddle1
		ball = new Ball();
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		
		float timeDelta = delta/10;
		Transition t = new FadeOutTransition();
		
		Input input = container.getInput();

		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);

		if(input.isKeyDown(Input.KEY_H)) 
			sbg.enterState(PongGame.HELPSTATE, t, t);	

		if(input.isKeyDown(Input.KEY_W))
			paddleLeft.paddleUp(timeDelta);

		if(input.isKeyDown(Input.KEY_S))
			paddleLeft.paddleDown(timeDelta);

		if(input.isKeyDown(Input.KEY_D))
			ball.servedLeft();

		if(input.isKeyDown(Input.KEY_UP))
			paddleRight.paddleUp(timeDelta);

		if(input.isKeyDown(Input.KEY_DOWN))
			paddleRight.paddleDown(timeDelta);

		if(input.isKeyDown(Input.KEY_LEFT))
			ball.servedRight();
		
		if(!paddleLeft.isHuman())
			paddleLeft.getAIMovement(ball, timeDelta);
		
		if(!paddleRight.isHuman())
			paddleRight.getAIMovement(ball, timeDelta);
		
		ball.moveBall(paddleLeft, paddleRight, height, timeDelta);
		
		if(ball.checkOutOfBounds(width))
			playerScore();
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		paddleLeft.getImage().draw(paddleLeftXPosition, paddleLeft.getY());
		paddleRight.getImage().draw(paddleRightXPosition, paddleRight.getY());
		ball.getImage().draw(ball.getXPosition(), ball.getYPosition());

		g.drawString("" + rightScore, 500, 40);
		g.drawString("" + leftScore, 300, 40);
		g.drawString("Press 'H' for help", width-200, height-30);
		

	}
	
	/**
	 * 
	 */
	private void playerScore(){
		if(ball.getXPosition() > 0){
			leftScore++;
		}else{
			rightScore++;
		}

	}
}