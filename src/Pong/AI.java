package Pong;

import java.util.Random;

/**
 * This class handles all movement of paddle or paddles if controlled
 * by PC. If only user controlled, no AI will be created.
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-05-10
 *
 */
public class AI {
	
	private Random rand;
	private int AIDelay = 0;

	/**
	 * Creates a new AI (Artificial Intelligence)
	 */
	public AI() {
		rand = new Random();
	}
	
	/**
	 * Get movement for a paddle controlled by PC
	 * @param paddle The paddle to move
	 * @param ball The ball
	 */
	public void getAIMovement(Paddle paddle, Ball ball) {

		AIServe(paddle, ball);

		int[] paddleCoordinates = paddle.getCoordinates();
		int paddleHeight = paddle.getHeight();
		float ballX = ball.getCoordinates()[0];
		float ballY = ball.getCoordinates()[1];
		float ballDeltaX = ball.getDeltaX();
		
		if(paddle.isLeft() && !Settings.getDifficulty().equals("Easy")) { // left paddle
			if(ballDeltaX < 0) { // i.e. ball is traveling toward the left paddle
				if(Settings.getDifficulty().equals("Medium")) {
					paddle.setGoal((int) ballY + ball.getRadius()); // follow the ball 
					if(2*ball.getRadius() > Settings.getFrameWidth()-100)
						paddle.setRange(rand.nextInt(5)+2); // range from center of paddle
				} else if(Settings.getDifficulty().equals("Hard") && ballX + 2*ball.getRadius() > Settings.getFrameWidth()-100) {
					paddle.setRange(rand.nextInt(paddleHeight/2)); // range from center of paddle
					paddle.setGoal(calculateGoal(paddle, ball)); // calculate ball destination on y-axis
				}
			} else if(ballDeltaX > 0 && ballX < paddleCoordinates[0] + paddle.getWidth() + 10) {
				// if ball is traveling from paddle, stand in the middle of the screen
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		} else if(!Settings.getDifficulty().equals("Easy")){ // right paddle
			if(ballDeltaX > 0) { // i.e. ball is traveling toward the right paddle
				if(Settings.getDifficulty().equals("Medium")) {
					paddle.setGoal((int) ballY + ball.getRadius()); // follow the ball
					if(2*ball.getRadius() > Settings.getFrameWidth()-100)
						paddle.setRange(rand.nextInt(5)+2); // range from center of paddle
				} else if(Settings.getDifficulty().equals("Hard") && ballX < 100) {
					paddle.setRange(rand.nextInt(paddleHeight/2)); // range from center of paddle
					paddle.setGoal(calculateGoal(paddle, ball)); // calculate ball destination on y-axis
				}
			} else if (ballDeltaX < 0 && ballX + 2*ball.getRadius() < paddleCoordinates[0] - 10) {
				// if ball is traveling from paddle, stand in the middle of the screen
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		}
		
		int centerOfPaddle = paddleCoordinates[1] + paddle.getHeight()/2;
		int currentGoal = paddle.getGoal() + ball.getRadius();
		int range = paddle.getRange();
		if((centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal)) {
			// ball is where it should be
			if(Settings.getDifficulty().equals("Easy")) {
				// Easy just sets a random goal each time
				int goal = paddleHeight/2 + rand.nextInt(Settings.getFrameHeight()-paddleHeight);
				paddle.setGoal(goal);
			}
			return; // do nothing
		}
		if(centerOfPaddle > currentGoal && paddleCoordinates[1] > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && paddleCoordinates[1] < Settings.getFrameHeight()) {
			paddle.paddleDown();
		}
	}

	/**
	 * Determines if either side is serving and sets a goal. If current paddle
	 * is the one serving, a goal-coordinate is set and paddle will serve after
	 * a small delay
	 * @param paddle A paddle
	 * @param ball The ball
	 */
	private void AIServe(Paddle paddle, Ball ball) {
		if((paddle.isLeft() && ball.isServingLeft()) || (!paddle.isLeft() && ball.isServingRight())) {
			AIDelay++;
			if(AIDelay < 5) 
				paddle.setGoal(rand.nextInt(Settings.getFrameHeight()-paddle.getHeight()/2));
			if(AIDelay > 50) {
				paddle.serve(ball);
				AIDelay = 0;
			}
		} else if (ball.isServingLeft() || ball.isServingRight()) {
			if(AIDelay < 5)
				paddle.setGoal(Settings.getFrameWidth()/2 - paddle.getHeight());
		}
	}
	
	/**
	 * Calculates where on Y-axis the ball will be when it is at the paddle's
	 * x-position (i.e. traveling through the screen). It is optimally calculated
	 * just as it has bounced of the opposite paddle.
	 * @param paddle The receiving paddle
	 * @param ball The ball
	 * @return the Y-coordinate when ball arrives at paddle
	 */
	private int calculateGoal(Paddle paddle, Ball ball) {
		float ballY = ball.getCoordinates()[1];
		float deltaY = ball.getDeltaY();
		if(deltaY == 0) // ball is moving straight
			return (int) ballY + 2*ball.getRadius();

		float ballX = ball.getCoordinates()[0];
		float deltaX = ball.getDeltaX();
		int numberOfFrames = 0;
		// calculate number of frames it will take the ball to travel
		// in reference to x-axis from right to left or vice versa.
		if(paddle.isLeft()) {
			while(ballX > paddle.getCoordinates()[0] + paddle.getWidth()) {
				numberOfFrames++;
				ballX += deltaX;
			}
		} else {
			while(ballX + 2*ball.getRadius() < paddle.getCoordinates()[0]) {
				numberOfFrames++;
				ballX += deltaX;
			}
		}
		
		// using the calculated number of frames from above to calculate
		// where on y-axis ball will be, taking walls into account
		for(int i = 0; i <= numberOfFrames; i++) {
			if(ballY <= 0) {
				ballY = 0;
				deltaY = -deltaY;
			}
			if(ballY + 2*ball.getRadius() >= Settings.getFrameHeight()) {
				ballY = Settings.getFrameHeight() - 2*ball.getRadius(); 
				deltaY = -deltaY;
			}
			ballY += deltaY;
		}
		return (int) ballY;
	}
}