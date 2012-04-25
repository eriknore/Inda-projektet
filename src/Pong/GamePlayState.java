package Pong;



import java.util.Random;
import java.util.concurrent.DelayQueue;

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
	
	private static final boolean DEBUG = true;

	private int stateID = -1;
	private Image background;
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private int updateInterval = 0;

	private int leftScore = 0;
	private int rightScore = 0;
	
	private int AIDelay = 0;
	
	//temp
	private double goaltemp, timeToImpact, yDistance, actualYDistance, distanceToFirstBounce, yDistanceAfterOneBounce, yDistanceAfterAllBounces, numberOfBouncesEvenOrOdd;
	private int distanceToOtherPaddle, numberOfBounces;

	
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
		Settings.setGameIsRunning(true);
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		if(!(paddleLeft.isHuman() == Settings.isLeftPaddleHuman()) || !(paddleRight.isHuman() == Settings.isRightPaddleHuman()))
			init(container, sbg);
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
			getAIHard(paddleRight);
		}
		if (!paddleLeft.isHuman()) {
			getAIHard(paddleLeft);
		}
		
		ball.moveBall(paddleLeft, paddleRight);
		
		if(ball.checkOutOfBounds())
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
		g.drawString("Press 'H' for help", Settings.getFrameWidth()-200, Settings.getFrameHeight()-30);
		
		if(DEBUG) {
//			double deltaX = ball.getDeltaX();
//			double deltaY = ball.getDeltaY();
			int width = Settings.getFrameWidth();
//			g.drawString("Delta X =" + deltaX, 100, 100);
//			g.drawString("Delta Y=" + deltaY, 500, 100);
//			g.drawString("Vector value: " + Math.hypot(deltaX, deltaY), 220, 120);
//			g.drawString("UpsateInterval: " + updateInterval, 500, 120);
//			g.drawString("X:" + paddleLeft.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 100, 100);
//			g.drawString("X:" + paddleLeft.getX() + " > " + width/2 + " && " + ball.isServingRight(), 100, 120);
//			g.drawString("X:" + paddleRight.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 500, 100);
//			g.drawString("X:" + paddleRight.getX() + " > " + width/2 + " && " + ball.isServingRight(), 500, 120);
//			g.drawString("Left goal: " + paddleLeft.getGoal(), 100, 140);
//			g.drawString("Right goal: " + paddleRight.getGoal(), 500, 140);
//			
//			int paddleHeight = paddleLeft.getImage().getHeight();
//			int centerOfPaddle = paddleLeft.getY() + paddleHeight/2 + paddleLeft.getImage().getWidth()/2;
//			g.drawString("" + centerOfPaddle, 250, 140);
//			paddleHeight = paddleRight.getImage().getHeight();
//			centerOfPaddle = paddleRight.getY() + paddleHeight/2 + paddleRight.getImage().getWidth()/2;
//			g.drawString("" + centerOfPaddle, 650, 140);
			
			int distance = 100;
			
			g.drawString("Paddle-center: " + (paddleRight.getY() + paddleRight.getImage().getHeight()/2), 500, distance);
			distance += 15;
			g.drawString("Ball-center Y-coordinate: " + ball.getYPosition(), 500, distance);
			if(ball.getDeltaY() == 0) {
				goaltemp = ball.getYPosition() + ball.getImage().getHeight()/2;
				g.drawString("Goal: " + goaltemp, 100, distance);
				return;
			}
			
			distance += 30;
			g.drawString("distanceToOtherPaddle: " + distanceToOtherPaddle, 100, distance);
			distance += 15;
			g.drawString("timeToImpact: " + timeToImpact, 100, distance);
			distance += 15;
			g.drawString("yDistance: " + yDistance, 100, distance);
			distance += 15;
			g.drawString("actualYDistance: " + actualYDistance, 500, distance);
//			g.drawString("distanceToFirstBounce :" + distanceToFirstBounce, 100, distance);
//			distance += 15;
//			
//
//			
//			
//			g.drawString("yDistanceAfterOneBounce: " + yDistanceAfterOneBounce, 100, distance);
//			distance += 15;
//			g.drawString("numberOfBounces: " + numberOfBounces, 100, distance);
//			distance += 15;
//			g.drawString("yDistanceAfterAllBounces: " + yDistanceAfterAllBounces, 100, distance);
//			distance += 15;
//			g.drawString("numberOfBouncesEvenOrOdd: " + numberOfBouncesEvenOrOdd, 100, distance);
//			distance += 15;
//			Double goal;
//			g.drawString("Goal: " + goaltemp, 100, distance);
//			distance += 15;
//			g.drawString("Goal-int: " + goaltemp, 100, distance);
//
//			g.drawString("Goal: " + goaltemp, 100, 115);
//			g.drawString("Ball-goal: " + paddleRight.getGoal(), 100, 130);
			
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
			actualYDistance = 0;
		}
	}
	
	private void getAIEasy(Paddle paddle) {
		AIServe(paddle);
		
		int paddleHeight = paddle.getImage().getHeight();
		int centerOfPaddle = paddle.getY() + paddleHeight/2 + paddle.getImage().getWidth()/2;
		int currentGoal = paddle.getGoal();
		if(centerOfPaddle + 5 >= currentGoal && centerOfPaddle - 5 <= currentGoal) { 
			Random rand = new Random();
			int goal = rand.nextInt(Settings.getFrameHeight()-paddleHeight);
			paddle.setGoal(goal+paddleHeight/2);
			return;
		}
		if(centerOfPaddle > currentGoal) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal) {
			paddle.paddleDown();
		}
	}
	
	private void getAIHard(Paddle paddle) {
		if(AIServe(paddle) && AIDelay < 200) {
			int paddleHeight = paddle.getImage().getHeight();
			Random rand = new Random();
			int goal = rand.nextInt(Settings.getFrameHeight()-paddleHeight);
			paddle.setGoal(goal+paddleHeight/2);
		}
		// Borttaget nedan : ' && ball.getXPosition() < paddleLeft.getX() + paddleLeft.getImage().getWidth() + 10'
		if(ball.getDeltaX() > 0 && ball.getXPosition() >= paddleLeft.getX() + paddleLeft.getImage().getWidth() && ball.getXPosition() < paddleLeft.getX() + paddleLeft.getImage().getWidth() + 10) {
			paddle.setGoal(calculateGoal(paddle));
		}
		if(ball.getDeltaX() > 0 && ball.getXPosition() + ball.getImage().getWidth() < paddleRight.getX()) {
			actualYDistance += Math.abs(ball.getDeltaY());
		} else {
//			actualYDistance = 0;
		}
		
//		if(paddle.getX() > Settings.getFrameWidth()/2) {
//			if(ball.getXPosition() < 100)
//				paddle.setGoal(calculateGoal(paddle));
//		} else {
//			if(ball.getXPosition() > Settings.getFrameWidth() - 100) {
//				paddle.setGoal(calculateGoal(paddle));
//			}
//		}
		int centerOfPaddle = paddle.getY() + paddle.getImage().getHeight()/2 + paddle.getImage().getWidth()/2;
		int currentGoal = paddle.getGoal();
		if((centerOfPaddle + 2 >= currentGoal && centerOfPaddle - 2 <= currentGoal))
			return;
		if(centerOfPaddle > currentGoal && paddle.getY() > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && paddle.getY() < Settings.getFrameHeight()) {
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
	
	private int calculateGoal(Paddle paddle) {
		if(ball.getDeltaY() == 0)
			return ball.getYPosition() + ball.getImage().getHeight()/2;
		
//		if(ball.getDeltaX() > 0 && ball.getXPosition() > paddleLeft.getX() + paddleLeft.getImage().getWidth() && ball.getXPosition() < paddleLeft.getX() + paddleLeft.getImage().getWidth() + 10) {
			distanceToOtherPaddle = (paddleRight.getX() + paddleRight.getImage().getWidth()) - (paddleLeft.getX());
			timeToImpact = distanceToOtherPaddle / ball.getDeltaX(); // bort med Settings.get...
			yDistance = Math.abs(timeToImpact * ball.getDeltaY());
			
			return 300;

//			distanceToFirstBounce = 0;
//			boolean willBounce = false;
//			int numberOfBounces = 0;
//			if(yDistance > ball.getYPosition()) {	
//				if(ball.getDeltaY() > 0) {
//					distanceToFirstBounce = Math.abs(Settings.getFrameHeight() - (ball.getYPosition() + ball.getImage().getHeight()));
//					willBounce = true;
//				} else {
//					distanceToFirstBounce = ball.getYPosition();
//					willBounce = true;
//				}
//			}
//			if(willBounce) {
//				yDistanceAfterOneBounce -= yDistance - distanceToFirstBounce;
//				numberOfBounces = 1 + (int) (yDistance/Settings.getFrameHeight());
//				yDistanceAfterAllBounces = yDistance - (numberOfBounces-1)*Settings.getFrameHeight();
//				numberOfBouncesEvenOrOdd = numberOfBounces % 2; // odd or even
//			}
//			Double goal;
//			if(ball.getDeltaY() > 0) {
//				if(numberOfBounces == 1) {
//					goal = Settings.getFrameHeight() - yDistance;
//				} else {
//					goal = yDistance;
//				}
//			} else {
//				if(numberOfBounces == 0) {
//					goal = Settings.getFrameHeight() - yDistance;
//				} else {
//					goal = yDistance;
//				}
//			}
//			return goal.intValue()+ball.getImage().getHeight();

//		}
//		return 0;
		
		
//		int distanceToOtherPaddle = paddleRight.getX() - (paddleLeft.getX() + paddleLeft.getImage().getWidth() + ball.getImage().getWidth());
//		double timeToImpact = distanceToOtherPaddle / (ball.getDeltaX() * ball.getBallSpeed());
//		Double yDistance = timeToImpact * Math.abs(ball.getDeltaY()) * ball.getBallSpeed();
//		double distanceToFirstBounce = 0;
//		if(yDistance > Settings.getFrameHeight()) {	
//			if(ball.getDeltaY() > 0) {
//				distanceToFirstBounce = Math.abs(Settings.getFrameHeight() - (ball.getYPosition() + ball.getImage().getHeight()));
//			} else {
//				distanceToFirstBounce = ball.getYPosition();
//			}
//		}
//		yDistance -= distanceToFirstBounce;
//		int numberOfBounces = yDistance.intValue()/Settings.getFrameHeight();
//		yDistance = yDistance % numberOfBounces;
//		numberOfBounces = numberOfBounces % 2; // odd or even 
//		Double goal;
//		if(ball.getDeltaY() > 0) {
//			if(numberOfBounces == 1) {
//				goal = Settings.getFrameHeight() - yDistance*Settings.getFrameHeight();
//			} else {
//				goal = yDistance*Settings.getFrameHeight();
//			}
//		} else {
//			if(numberOfBounces == 0) {
//				goal = Settings.getFrameHeight() - yDistance*Settings.getFrameHeight();
//			} else {
//				goal = yDistance*Settings.getFrameHeight();
//			}
//		}
//		return goal.intValue();
	}
}