package Pong;




import java.util.Random;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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

	private static final boolean DEBUG = false;

	private int stateID = -1;
	private Image background;
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private int updateInterval = 0;

	private int leftScore = 0, rightScore = 0;
	private String playerLeft, playerRight;

	private int AIDelay = 0;
	private Random rand = new Random();

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
		
		
		////////////////////effects
		effect = new Effect();
		///////////////////////
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
		updateInterval += delta;
		int limit = 20;
		if(updateInterval < limit)
			return;

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

		if(!paddleRight.isHuman()) {
			if(Settings.getDifficulty().equals("Easy")) {
				getAIEasy(paddleRight);
			} if(Settings.getDifficulty().equals("Medium")) {
				getAIMedium(paddleRight);
			} else {
				getAIHard(paddleRight);
			}
		}
		if (!paddleLeft.isHuman()) {
			if(Settings.getDifficulty().equals("Easy")) {
				getAIEasy(paddleLeft);
			} if(Settings.getDifficulty().equals("Medium")) {
				getAIMedium(paddleLeft);
			} else {
				getAIHard(paddleLeft);
			}
		}

		ball.moveBall(paddleLeft, paddleRight);

		if(ball.checkOutOfBounds(paddleLeft, paddleRight))
			playerScore();

		updateInterval -= limit;
		
		
		//effects
		checkEffectCollision();
		//effects
		
	}


	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		background.draw(0, 0);
		
		//effects
		if(oncePerRound == 0){
		setRandomEffect();
		}
		//effects
		
		paddleLeft.getImage().draw(paddleLeftXPosition, paddleLeft.getY());
		paddleRight.getImage().draw(paddleRightXPosition, paddleRight.getY());
		Double ballX = ball.getXPosition();
		Double ballY = ball.getYPosition();
		ball.getImage().draw(ballX.intValue(), ballY.intValue());

		//		g.drawString("" + rightScore, 500, 40);
		//		g.drawString("" + leftScore, 300, 40);
		g.drawString("Press 'H' for help", Settings.getFrameWidth()-200, Settings.getFrameHeight()-30);

		g.drawString(playerLeft + ":    " + leftScore, 200, 40);
		g.drawString(playerRight + ":    " + rightScore, 500, 40);
	}

	/**
	 * 
	 */
	private void playerScore(){
		if(ball.getXPosition() > Settings.getFrameWidth()/2){
			leftScore++;
		}else{
			rightScore++;
		}
	}

	private void getAIEasy(Paddle paddle) {
		AIServe(paddle);

		int paddleHeight = paddle.getImage().getHeight();
		int centerOfPaddle = paddle.getY() + paddleHeight/2 + paddle.getImage().getWidth()/2;
		int currentGoal = paddle.getGoal();
		if(centerOfPaddle + 2 >= currentGoal && centerOfPaddle - 2 <= currentGoal) { 
			int goal = paddleHeight/2 + rand.nextInt(Settings.getFrameHeight()-paddleHeight);
			paddle.setGoal(goal);
			return;
		}
		if(centerOfPaddle > currentGoal) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal) {
			paddle.paddleDown();
		}
	}

	private void getAIMedium(Paddle paddle) {
		int paddleHeight = paddle.getImage().getHeight();
		if(AIServe(paddle) && AIDelay < 200)
			paddle.setGoal(rand.nextInt(Settings.getFrameHeight()-paddleHeight/2));

		int thisPaddleX = paddle.getX();
		Double ballX = ball.getXPosition();
		Double ballY = ball.getYPosition();
		double ballDeltaX = ball.getDeltaX();
		if(thisPaddleX == paddleLeftXPosition) {
			if(ballDeltaX > 0 && ballX < thisPaddleX + paddle.getImage().getWidth() + 10) {
				paddle.setGoal(Settings.getFrameHeight()/2);
				paddle.setRange(rand.nextInt(paddleHeight/2));
			} else if(ball.getDeltaX() < 0) {
				paddle.setGoal(ballY.intValue() - ball.getImage().getHeight()/2);
			}
		} else {
			if(ballDeltaX < 0 && ballX + ball.getImage().getWidth() < thisPaddleX - 10) {
				paddle.setGoal(Settings.getFrameHeight()/2);
				paddle.setRange(rand.nextInt(paddleHeight/2));
			} else if(ball.getDeltaX() > 0) {
				paddle.setGoal(ballY.intValue() - ball.getImage().getHeight()/2);
			}
		}

		int thisPaddleY = paddle.getY();
		int centerOfPaddle = thisPaddleY + paddleHeight/2;
		int currentGoal = paddle.getGoal();
		int range = paddle.getRange();
		if((centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal))
			return;
		if(centerOfPaddle > currentGoal && thisPaddleY > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && thisPaddleY < Settings.getFrameHeight()) {
			paddle.paddleDown();
		}
	}

	private void getAIHard(Paddle paddle) {
		int paddleHeight = paddle.getImage().getHeight();
		if(AIServe(paddle) && AIDelay < 200)
			paddle.setGoal(rand.nextInt(Settings.getFrameHeight()-paddleHeight/2));

		int diameter = ball.getImage().getWidth();
		int thisPaddleX = paddle.getX();
		Double ballX = ball.getXPosition();
		double ballDeltaX = ball.getDeltaX();
		if(thisPaddleX == paddleRightXPosition) {
			if(ballDeltaX > 0 && ballX.intValue() < paddleLeft.getX() + paddleLeft.getImage().getWidth() + 10) {
				paddle.setGoal(calculateGoalFinal(paddle));
				paddle.setRange(rand.nextInt(paddleHeight/2));
			} else if (ballDeltaX < 0 && ballX.intValue() + diameter < thisPaddleX - 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		} else {
			if(ballDeltaX < 0 && ballX.intValue() + diameter > paddleRight.getX() - 10) {
				paddle.setGoal(calculateGoalFinal(paddle));
				paddle.setRange(rand.nextInt(paddleHeight/2));
			} else if(ballDeltaX > 0 && ballX.intValue() < thisPaddleX + paddle.getImage().getWidth() + 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		}

		int thisPaddleY = paddle.getY();
		int centerOfPaddle = thisPaddleY + paddle.getImage().getHeight()/2;
		int currentGoal = paddle.getGoal() + diameter/2;
		int range = paddle.getRange();
		if((centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal))
			return;
		if(centerOfPaddle > currentGoal && thisPaddleY > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && thisPaddleY < Settings.getFrameHeight()) {
			paddle.paddleDown();
		}
	}

	private boolean AIServe(Paddle paddle) {
		int paddleX = paddle.getX();
		int width = Settings.getFrameWidth();

		if((paddleX < width/2 && ball.isServingLeft()) || (paddleX > width/2 && ball.isServingRight())) {
			AIDelay += updateInterval;
			if(AIDelay > 1500) {
				paddle.serve(ball);
				AIDelay = 0;
			}
			return true;
		}
		return false;
	}
	
	private int calculateGoalFinal(Paddle paddle) {
		int diameter = ball.getImage().getHeight();
		Double ballY = ball.getYPosition();
		double deltaY = ball.getDeltaY();
		if(deltaY == 0)
			return ballY.intValue() + diameter;

		Double ballX = ball.getXPosition();
		double deltaX = ball.getDeltaX();
		int numberOfFrames = 0;
		if(paddle.getX() == paddleLeftXPosition) {
			while(ballX.intValue() > paddleLeftXPosition + paddleLeft.getImage().getWidth()) {
				numberOfFrames++;
				ballX += deltaX;
			}
		} else if(paddle.getX() == paddleRightXPosition) {
			while(ballX.intValue() + diameter < paddleRightXPosition) {
				numberOfFrames++;
				ballX += deltaX;
			}
		}
		
		for(int i = 0; i <= numberOfFrames; i++) {
			if(ballY.intValue() <= 0) {
				ballY = 0.0;
				deltaY = -deltaY;
			}
			if(ballY.intValue() + diameter >= Settings.getFrameHeight()) {
				double edge = Settings.getFrameHeight() - diameter; 
				ballY = edge;
				deltaY = -deltaY;
			}
			ballY += deltaY;
		}
		return ballY.intValue();
	}


	///////////////////////////////////////Effects
	private Effect effect; //flytta upp sen
	private boolean effectInGame = false; //flytta upp sen
	private int oncePerRound = 0; //flytta upp sen

	/**
	 * Sets an effect in the middle.
	 * @throws SlickException
	 */
	private void setRandomEffect() throws SlickException{
		effect.getImage().draw(Settings.getFrameWidth()/2, Settings.getFrameHeight()/2);
		effectInGame = true;
	}

	/**
	 * Checks if a player has obtained
	 * the effect.
	 * @throws SlickException 
	 */
	private void checkEffectCollision() throws SlickException{
		if(effectInGame == true){
			//Create 2 shapes identical to the effectImage and ballImage
			Double ballX = ball.getXPosition();
			Double ballY = ball.getYPosition();			
			Shape ballShape = new Circle(ballX.intValue(), ballY.intValue(), ball.getDiameter()/2);
			Shape effectShape = new Rectangle(Settings.getFrameWidth()/2, Settings.getFrameHeight()/2, effect.getWidth(), effect.getHeight());
			if(effectShape.intersects(ballShape)){
				removeEffect();
				givePlayerEffect();
			}
		}
	}
	
	/**
	 * Gives the effect to the player
	 * that obtained it.
	 * @throws SlickException 
	 */
	private void givePlayerEffect() throws SlickException {
		//paddle to give effect == last one to touch the ball
		//if paddleLeft touched it last, then: lastPaddle = paddleLeft , och ersätt med lastPaddle nedan
	
		if(effect.getEffectType().equals("largerpaddle"))
			paddleLeft = effect.largerPaddle(paddleLeft);
		if(effect.getEffectType().equals("smallerpaddle"))
			paddleLeft = effect.smallerPaddle(paddleLeft);
		if(effect.getEffectType().equals("smallerball"))
			ball = effect.smallerBall(ball);
		if(effect.getEffectType().equals("largerball"))
			ball = effect.largerBall(ball);
	}
	
	/**
	 * Removes the effect from the middle.
	 * (A player obtained it)
	 * @throws SlickException 
	 */
	private void removeEffect() throws SlickException{
		oncePerRound++;
		effectInGame = false;
	}

}