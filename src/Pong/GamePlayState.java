package Pong;

import java.util.Random;
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
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private int updateInterval = 0, stopwatch, limit;

	private int leftScore = 0, rightScore = 0;
	private String playerLeft, playerRight, effectDescription;
	private boolean drawEffect = false;

	private int delay = 0;
	private Random rand = new Random();
	private AI ai;
	private Effect effect;
	
	private final boolean developerMode = false;


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
		container.setShowFPS(false); 
		background = new Image("data/backgrounds/default.png");
		paddleLeft = new Paddle(paddleLeftXPosition, Settings.isLeftPaddleHuman()); // both paddles start at same y-position
		paddleRightXPosition = Settings.getFrameWidth()-paddleLeftXPosition-paddleLeft.getImage().getWidth();
		paddleRight = new Paddle(paddleRightXPosition, Settings.isRightPaddleHuman());
		// mirror the startposition of paddle1
		ball = new Ball();
		if(paddleRight.isHuman()) {
			playerRight = "Player 1";
		} else {
			playerRight = "PC";
		}
		if(paddleLeft.isHuman()) {
			playerLeft = "Player 2";
		} else {
			playerLeft = "PC";
		}
		limit = 1 + rand.nextInt(30000);
		if(!paddleLeft.isHuman() || !paddleRight.isHuman())
			ai = new AI();
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		if(!Settings.isGameRunning()){
			leftScore = 0;
			rightScore = 0;
			init(container, sbg);
			Settings.setGameIsRunning(true);
		}
		
		if(drawEffect) {
			delay += delta;
			if(delay > 1000) {
				drawEffect = false;
				delay = 0;
			}
		}
		
		stopwatch += delta;
		if(stopwatch > limit) {
			effect = new Effect();
			effectDescription = effect.getEffectType();
			limit = 1 + rand.nextInt(30000);
			stopwatch = 0;
		}
		
		if(stopwatch >= 30000) {
			limit = 1 + rand.nextInt(30000);
			stopwatch = 0;
		}
		
		updateInterval += delta;
		int updateLimit = 20;
		if(updateInterval < updateLimit)
			return;

		getInput(container, sbg);
		
		if(!paddleRight.isHuman())
			ai.getAIMovement(paddleRight, ball);
		if (!paddleLeft.isHuman())
			ai.getAIMovement(paddleLeft, ball);
		
		ball.moveBall(paddleLeft, paddleRight);
		if(effect != null) {
			if(effect.checkEffectCollision(ball, paddleLeft, paddleRight)) {
				effect = null;
				drawEffect = true;
			}
		}
		if(ball.checkOutOfBounds(paddleLeft, paddleRight))
			playerScore();

		updateInterval -= updateLimit;		
	}


	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		
		if(effect != null){
			Image effectImage = effect.getImage();
			int effectX = Settings.getFrameWidth()/2 - effectImage.getWidth()/2;
			int effectY = Settings.getFrameHeight()/2 - effectImage.getWidth()/2;
			effectImage.draw(effectX, effectY);
		}
		if(drawEffect) {
			g.drawString(effectDescription + "!", Settings.getFrameWidth()/2+10, Settings.getFrameHeight()/2);
		}
		
		paddleLeft.getImage().draw(paddleLeftXPosition, paddleLeft.getCoordinates()[1]);
		paddleRight.getImage().draw(paddleRightXPosition, paddleRight.getCoordinates()[1]);
		ball.getImage().draw(ball.getCoordinates()[0], ball.getCoordinates()[1]);

		g.drawString("Press 'H' for help", Settings.getFrameWidth()-200, Settings.getFrameHeight()-30);

		g.drawString(playerLeft + ":    " + leftScore, 200, 40);
		g.drawString(playerRight + ":    " + rightScore, 500, 40);
	}

	/**
	 * @param container
	 * @param sbg
	 * @throws SlickException
	 */
	private void getInput(GameContainer container, StateBasedGame sbg) throws SlickException {
		Transition t = new FadeOutTransition();

		Input input = container.getInput();

		if(input.isKeyDown(Input.KEY_ESCAPE)) 
			sbg.enterState(PongGame.MAINMENUSTATE, t, t);
		if(input.isKeyDown(Input.KEY_H)) 
			sbg.enterState(PongGame.HELPSTATE, t, t);	

		if(paddleLeft.isHuman()) {
			if(input.isKeyDown(Input.KEY_W))
				paddleLeft.paddleUp();
			if(input.isKeyDown(Input.KEY_S))
				paddleLeft.paddleDown();
			if(input.isKeyDown(Input.KEY_D) && ball.isServingLeft())
				paddleLeft.serve(ball);
		}
		if(paddleRight.isHuman()) {
			if(input.isKeyDown(Input.KEY_UP))
				paddleRight.paddleUp();
			if(input.isKeyDown(Input.KEY_DOWN))
				paddleRight.paddleDown();
			if(input.isKeyDown(Input.KEY_LEFT) && ball.isServingRight())
				paddleRight.serve(ball);
		}
		
		if(developerMode) {
			if(input.isKeyDown(Input.KEY_F1))
				ball.enlargeBall(ball);
			if(input.isKeyDown(Input.KEY_F2))
				ball.shrinkBall(ball);
			if(input.isKeyPressed(Input.KEY_F3))
				ball.setBallSpeed((float) (ball.getBallSpeed()*1.25));
			if(input.isKeyPressed(Input.KEY_F4))
				ball.setBallSpeed((float) (ball.getBallSpeed()*0.75));
			if(input.isKeyDown(Input.KEY_F5))
				paddleLeft.enlargePaddle();
			if(input.isKeyDown(Input.KEY_F6))
				paddleRight.enlargePaddle();
			if(input.isKeyDown(Input.KEY_F7))
				paddleLeft.shrinkPaddle();
			if(input.isKeyDown(Input.KEY_F8))
				paddleRight.shrinkPaddle();
			if(input.isKeyPressed(Input.KEY_F9))
				paddleLeft.setPaddleSpeed((float) (paddleLeft.getPaddleSpeed()*1.25));
			if(input.isKeyPressed(Input.KEY_F10))
				paddleRight.setPaddleSpeed((float) (paddleRight.getPaddleSpeed()*1.25));
			if(input.isKeyPressed(Input.KEY_F11))
				paddleLeft.setPaddleSpeed((float) (paddleLeft.getPaddleSpeed()*0.75));
			if(input.isKeyPressed(Input.KEY_F12))
				paddleRight.setPaddleSpeed((float) (paddleRight.getPaddleSpeed()*0.75));
			if(input.isKeyDown(Input.KEY_BACK)) {
				paddleRight.resetPaddle();
				ball.resetBall();
			}
		}
	}
	/**
	 * @throws SlickException 
	 * 
	 */
	private void playerScore() throws SlickException{
		if(ball.getCoordinates()[0] > Settings.getFrameWidth()/2){
			leftScore++;
		}else{
			rightScore++;
		}
		reset();
	}
	
	/**
	 * @throws SlickException
	 */
	private void reset() throws SlickException{
		paddleLeft = new Paddle(paddleLeftXPosition, Settings.isLeftPaddleHuman()); // both paddles start at same y-position
		paddleRightXPosition = Settings.getFrameWidth()-paddleLeftXPosition-paddleLeft.getImage().getWidth();
		paddleRight = new Paddle(paddleRightXPosition, Settings.isRightPaddleHuman());
		ball.resetBall();
		limit = 1 + rand.nextInt(30000);
		stopwatch = 0;
		effect = null;
	}

}