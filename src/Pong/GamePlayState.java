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
	// start-position, width between paddle and frame
	private final int paddleLeftXPosition = 20;
	private int paddleRightXPosition;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private int updateInterval = 0;

	private int leftScore = 0;
	private int rightScore = 0;
	
	private int AIDelay = 0, range;
	private Random rand = new Random();
	
	//temp
	private double distanceToFirstBounce, yDistanceAfterOneBounce, yDistanceAfterAllBounces, numberOfBouncesEvenOrOdd, timeToImpact, yDistance, actualYDistance;
	private int distanceToOtherPaddle, numberOfBounces, goal;

	
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
			double deltaX = ball.getDeltaX();
			double deltaY = ball.getDeltaY();
			int width = Settings.getFrameWidth();
			g.drawString("Delta X =" + deltaX, 100, 100);
			g.drawString("Delta Y=" + deltaY, 500, 100);
//			g.drawString("Vector value: " + Math.hypot(deltaX, deltaY), 220, 120);
//			g.drawString("UpsateInterval: " + updateInterval, 500, 120);
//			g.drawString("X:" + paddleLeft.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 100, 100);
//			g.drawString("X:" + paddleLeft.getX() + " > " + width/2 + " && " + ball.isServingRight(), 100, 120);
//			g.drawString("X:" + paddleRight.getX() + " < " + width/2 + " && " + ball.isServingLeft(), 500, 100);
//			g.drawString("X:" + paddleRight.getX() + " > " + width/2 + " && " + ball.isServingRight(), 500, 120);
			g.drawString("Left goal: " + paddleLeft.getGoal(), 100, 440);
			g.drawString("Right goal: " + paddleRight.getGoal(), 500, 440);
//			
//			int paddleHeight = paddleLeft.getImage().getHeight();
//			int centerOfPaddle = paddleLeft.getY() + paddleHeight/2 + paddleLeft.getImage().getWidth()/2;
//			g.drawString("" + centerOfPaddle, 250, 140);
//			paddleHeight = paddleRight.getImage().getHeight();
//			centerOfPaddle = paddleRight.getY() + paddleHeight/2 + paddleRight.getImage().getWidth()/2;
//			g.drawString("" + centerOfPaddle, 650, 140);
			
			int distance = 115;
			
			g.drawString("Paddle-center: " + (paddleRight.getY() + paddleRight.getImage().getHeight()/2), 500, distance);
			distance += 15;
			g.drawString("Ball-center Y-coordinate: " + ball.getYPosition(), 500, distance);
			if(ball.getDeltaY() == 0) {
				goal = ball.getYPosition() + ball.getImage().getHeight()/2;
				g.drawString("Goal: " + goal, 100, distance);
				return;
			}
			
			distance += 30;
//			g.drawString("distanceToOtherPaddle: " + distanceToOtherPaddle, 100, distance);
//			distance += 15;
//			g.drawString("timeToImpact: " + timeToImpact, 100, distance);
//			distance += 15;
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
//			g.drawString("Goal: " + goal, 100, distance);
//			distance += 15;
//			g.drawString("Goal-int: " + goal, 100, distance);
//
//			g.drawString("Goal: " + goal, 100, 115);
//			g.drawString("Ball-goal: " + paddleRight.getGoal(), 100, 130);
			
		}
		

	}
	
	/**
	 * 
	 */
	private void playerScore(){
		if(ball.getXPosition() > 0){
			leftScore++;
			actualYDistance = 0;
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
		if(centerOfPaddle + 2 >= currentGoal && centerOfPaddle - 2 <= currentGoal) { 
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
	
	private void getAIMedium(Paddle paddle) {
		if(AIServe(paddle) && AIDelay < 200) {
			int paddleHeight = paddle.getImage().getHeight();
			int goal = rand.nextInt(Settings.getFrameHeight()-paddleHeight);
			paddle.setGoal(goal+paddleHeight/2);
		}
		
		if(paddle.getX() < Settings.getFrameWidth()/2) {
			if(ball.getDeltaX() > 0) {
				paddle.setGoal(Settings.getFrameHeight()/2);
				range = 2 + rand.nextInt(5);
			} else {
				paddle.setGoal(ball.getYPosition() - ball.getImage().getHeight()/2);
			}
		} else {
			if(ball.getDeltaX() < 0) {
				paddle.setGoal(Settings.getFrameHeight()/2);
				range = 2 + rand.nextInt(8);
			} else {
				paddle.setGoal(ball.getYPosition() - ball.getImage().getHeight()/2);
			}
		}
		
		int paddleHeight = paddle.getImage().getHeight();
		int centerOfPaddle = paddle.getY() + paddleHeight/2 + paddle.getImage().getWidth()/2;
		int currentGoal = paddle.getGoal();
		if(centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal) { 
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
			int goal = rand.nextInt(Settings.getFrameHeight()-paddleHeight);
			paddle.setGoal(goal+paddleHeight/2);
		}

		if(paddle.getX() > Settings.getFrameWidth()/2 && ball.getDeltaX() > 0 && ball.getXPosition() >= paddleLeft.getX() + paddleLeft.getImage().getWidth() && ball.getXPosition() < paddleLeft.getX() + paddleLeft.getImage().getWidth() + 10) {
			paddle.setGoal(calculateGoalRight(paddle));
		} else if(paddle.getX() < Settings.getFrameWidth()/2) {
				if(ball.getDeltaX() < 0) {
					if(ball.getXPosition() + ball.getImage().getWidth() <= paddleRight.getX()) {
						if(ball.getXPosition() + ball.getImage().getWidth() > paddleRight.getX() - 10) {
							paddle.setGoal(calculateGoal(paddle));
						}
					}

			}
		}

		
		if(ball.getDeltaX() > 0 && ball.getXPosition() + ball.getImage().getWidth() < paddleRight.getX()) {
			if(Math.abs(ball.getDeltaY()) > 1) {
				actualYDistance += (int) Math.abs(ball.getDeltaY());
			} else {
				actualYDistance += Math.abs(ball.getDeltaY());
			}
		}
		
//		if(ball.getDeltaX() > 0 && ball.getXPosition() + ball.getImage().getWidth() < paddleRight.getX()) {
//			actualYDistance += Math.abs(ball.getDeltaY());
//		}
		

		if(paddle.getX() > Settings.getFrameWidth()/2 && ball.getDeltaX() < 0) {
			paddle.setGoal(Settings.getFrameHeight()/2);
		} else if(paddle.getX() < Settings.getFrameWidth()/2 && ball.getDeltaX() > 0) {
			paddle.setGoal(Settings.getFrameHeight()/2);
		}
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
			return ball.getYPosition() + ball.getImage().getHeight();
		
		if(Math.abs(ball.getDeltaY()) > 0) {	
			distanceToOtherPaddle = paddleRight.getX() - (paddleLeft.getX()+ paddleRight.getImage().getWidth() + ball.getImage().getWidth());
			timeToImpact = distanceToOtherPaddle / (int) ball.getDeltaX();
			yDistance = Math.abs(timeToImpact * (int) ball.getDeltaY());
		} else {
			distanceToOtherPaddle = paddleRight.getX() - (paddleLeft.getX()+ paddleRight.getImage().getWidth() + ball.getImage().getWidth());
			timeToImpact = (distanceToOtherPaddle / ball.getDeltaX());
			yDistance = (Math.abs(timeToImpact * ball.getDeltaY()));
		}
			yDistanceAfterAllBounces = yDistance;
			distanceToFirstBounce = 0;
			boolean willBounce = false;
			numberOfBounces = 2;
			if(ball.getDeltaY() < 0) {
				if(yDistance > ball.getYPosition()) {	
					distanceToFirstBounce = ball.getYPosition();
					willBounce = true;
				}
			} else {
				if(yDistance > Settings.getFrameHeight() - (ball.getYPosition() )) { // + ball.getImage().getHeight())) {	
					distanceToFirstBounce = Math.abs(Settings.getFrameHeight() - (ball.getYPosition() )); //+ ball.getImage().getHeight()));
					willBounce = true;
				}
			}
			if(willBounce) {
				yDistanceAfterOneBounce = Math.abs(yDistance - distanceToFirstBounce);
				numberOfBounces = 1;
				yDistanceAfterAllBounces = yDistanceAfterOneBounce;
				while((int) yDistanceAfterAllBounces > Settings.getFrameHeight() - ball.getImage().getHeight()) {
					yDistanceAfterAllBounces = yDistanceAfterOneBounce - (Settings.getFrameHeight() - ball.getImage().getHeight());
					numberOfBounces++;
				}
				numberOfBouncesEvenOrOdd = numberOfBounces % 2; // odd or even
			} else {
				if(ball.getDeltaY() < 0) {
					// ingen studs - på väg åt uppåt
					goal = (int) ball.getYPosition() - (int) yDistanceAfterAllBounces + ball.getImage().getHeight()/2 - paddle.getImage().getHeight()/2;
					return goal;
				} else {
					// ingen studs - på väg åt nedåt
					goal = (int) (ball.getYPosition() + (int) yDistanceAfterAllBounces) + ball.getImage().getHeight()/2 - paddle.getImage().getHeight()/2;
					return goal;
				}
			}

			if(ball.getDeltaY() < 0) {
				if(numberOfBouncesEvenOrOdd == 1) {
					// en studs - på väg ner
					goal = (int) yDistanceAfterAllBounces - ball.getImage().getHeight()/2;
				} else {
					// två studs - på väg upp
					goal = Settings.getFrameHeight() - (int) yDistanceAfterAllBounces + paddle.getImage().getHeight();
				}
			} else {
				if(numberOfBouncesEvenOrOdd == 0) {
					// två studs - på väg ner
					goal = (int) yDistanceAfterAllBounces + paddle.getImage().getHeight()/2;// - paddle.getImage().getHeight()/2;
				} else {
					// en studs - på väg upp
					goal = Settings.getFrameHeight() - (int) yDistanceAfterAllBounces + ball.getImage().getHeight()/2;
				}
			}
			return goal;
		}
	
	private int calculateGoalRight(Paddle paddle) {
		if(ball.getDeltaY() == 0)
			return ball.getYPosition() + ball.getImage().getHeight();
		
		if(Math.abs(ball.getDeltaY()) > 0) {	
			distanceToOtherPaddle = paddleRight.getX() - (paddleLeft.getX()+ paddleRight.getImage().getWidth() + ball.getImage().getWidth());
			timeToImpact = distanceToOtherPaddle / (int) ball.getDeltaX();
			yDistance = Math.abs(timeToImpact * (int) ball.getDeltaY());
		} else {
			distanceToOtherPaddle = paddleRight.getX() - (paddleLeft.getX()+ paddleRight.getImage().getWidth() + ball.getImage().getWidth());
			timeToImpact = (distanceToOtherPaddle / ball.getDeltaX());
			yDistance = (Math.abs(timeToImpact * ball.getDeltaY()));
		}
			yDistanceAfterAllBounces = yDistance;
			distanceToFirstBounce = 0;
			boolean willBounce = false;
			numberOfBounces = 2;
			if(ball.getDeltaY() < 0) {
				if(yDistance > ball.getYPosition()) {	
					distanceToFirstBounce = ball.getYPosition();
					willBounce = true;
				}
			} else {
				if(yDistance > Settings.getFrameHeight() - (ball.getYPosition() )) { // + ball.getImage().getHeight())) {	
					distanceToFirstBounce = Math.abs(Settings.getFrameHeight() - (ball.getYPosition() )); //+ ball.getImage().getHeight()));
					willBounce = true;
				}
			}
			if(willBounce) {
				yDistanceAfterOneBounce = Math.abs(yDistance - distanceToFirstBounce);
				numberOfBounces = 1;
				yDistanceAfterAllBounces = yDistanceAfterOneBounce;
				while((int) yDistanceAfterAllBounces > Settings.getFrameHeight() - ball.getImage().getHeight()) {
					yDistanceAfterAllBounces = yDistanceAfterOneBounce - (Settings.getFrameHeight() - ball.getImage().getHeight());
					numberOfBounces++;
				}
				numberOfBouncesEvenOrOdd = numberOfBounces % 2; // odd or even
			} else {
				if(ball.getDeltaY() < 0) {
					goal = (int) ((ball.getYPosition() - ball.getImage().getHeight()/2) - (int) yDistanceAfterAllBounces) - paddle.getImage().getHeight()/2;
					return goal;
				} else {
					goal = (int) ((ball.getYPosition() - ball.getImage().getHeight()/2) + (int) yDistanceAfterAllBounces) + paddle.getImage().getHeight()/2;
					return goal;
				}
			}

			if(ball.getDeltaY() < 0) {
				if(numberOfBouncesEvenOrOdd == 1) {
					// en studs - på väg ner
					goal = (int) yDistanceAfterAllBounces + paddle.getImage().getHeight()/2;
				} else {
					// två - på väg upp
					goal = Settings.getFrameHeight() - (int) yDistanceAfterAllBounces - paddle.getImage().getHeight()/2;
				}
			} else {
				if(numberOfBouncesEvenOrOdd == 0) {
					// två studs - på väg ner
					goal = (int) yDistanceAfterAllBounces + paddle.getImage().getHeight();
				} else {
					// en studs - på väg upp
					goal = Settings.getFrameHeight() - (int) yDistanceAfterAllBounces - paddle.getImage().getHeight()/2;
				}
			}
			return goal;
		}
}