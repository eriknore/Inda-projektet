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
	
	private static final boolean DEBUG = false;

	private int stateID = -1;
	private Image background;
	private int height, width;
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private int updateInterval = 0;

	private int leftScore = 0;
	private int rightScore = 0;
	
	private int AIDelay = 0;

	
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
		paddleRight = new Paddle(height, paddleRightXPosition, false);
		// mirror the startposition of paddle1
		ball = new Ball();
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		
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
			getAIEasy(paddleRight);
		}
		if (!paddleLeft.isHuman()) {
			getAIEasy(paddleLeft);
		}
		
		ball.moveBall(paddleLeft, paddleRight, height);
		
		if(ball.checkOutOfBounds(width))
			playerScore();
		
		updateInterval -= limit;
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
		
		if(DEBUG) {
//			double deltaX = ball.getDeltaX();
//			double deltaY = ball.getDeltaY();
//			g.drawString("Delta X =" + deltaX, 100, 100);
//			g.drawString("Delta Y=" + deltaY, 500, 100);
//			g.drawString("Vector value: " + Math.hypot(deltaX, deltaY), 220, 120);
//			g.drawString("UpsateInterval: " + updateInterval, 500, 120);
			g.drawString("X:" + paddleLeft.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 100, 100);
			g.drawString("X:" + paddleLeft.getX() + " > " + width/2 + " && " + ball.isServingRight(), 100, 120);
			g.drawString("X:" + paddleRight.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 500, 100);
			g.drawString("X:" + paddleRight.getX() + " > " + width/2 + " && " + ball.isServingRight(), 500, 120);
			g.drawString("Left goal: " + paddleLeft.getGoal(), 100, 140);
			g.drawString("Right goal: " + paddleRight.getGoal(), 500, 140);
			
			int paddleHeight = paddleLeft.getImage().getHeight();
			int centerOfPaddle = paddleLeft.getY() + paddleHeight/2 + paddleLeft.getImage().getWidth()/2;
			g.drawString("" + centerOfPaddle, 250, 140);
			paddleHeight = paddleRight.getImage().getHeight();
			centerOfPaddle = paddleRight.getY() + paddleHeight/2 + paddleRight.getImage().getWidth()/2;
			g.drawString("" + centerOfPaddle, 650, 140);
			
		}
		

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
	
	private void getAIEasy(Paddle paddle) {
		int paddleX = paddle.getX();
		if((paddleX < width/2 && ball.isServingLeft()) || (paddleX > width/2 && ball.isServingRight())) {
			AIDelay += updateInterval;
			if(AIDelay > 2000) {
				paddle.serve(ball);
				AIDelay = 0;
			}
		}
		
		int paddleHeight = paddle.getImage().getHeight();
		int centerOfPaddle = paddle.getY() + paddleHeight/2 + paddle.getImage().getWidth()/2;
		int currentGoal = paddle.getGoal();
		if(centerOfPaddle + 5 >= currentGoal && centerOfPaddle - 5 <= currentGoal) { 
			Random rand = new Random();
			int goal = rand.nextInt(height-paddleHeight);
			paddle.setGoal(goal+paddleHeight/2);
			return;
		}
		if(centerOfPaddle > currentGoal) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal) {
			paddle.paddleDown();
		}
	}
}