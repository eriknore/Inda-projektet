package Pong;

import java.util.Random;

public class AI {
	
//	private Ball ball;
	private Random rand;
	private int AIDelay = 0;

	public AI() {
//		this.ball = ball;
		rand = new Random();
	}
	
	public void getAIMovement(Paddle paddle, Ball ball) {
//		this.ball = ball;

		AIServe(paddle, ball);

//		int diameter = ball.getImage().getWidth();
//		int thisPaddleX = paddle.getX();
		int[] paddleCoordinates = paddle.getCoordinates();
		int paddleHeight = paddle.getHeight();
		float ballX = ball.getCoordinates()[0];
		float ballY = ball.getCoordinates()[1];
		float ballDeltaX = ball.getDeltaX();
		
		if(paddleCoordinates[0] < Settings.getFrameWidth()/2 && !Settings.getDifficulty().equals("Easy")) {
			if(ballDeltaX < 0) {
				if(Settings.getDifficulty().equals("Medium"))
					paddle.setGoal((int) ballY + ball.getRadius());
				if(ballX + 2*ball.getRadius() > Settings.getFrameWidth()-100)
					paddle.setRange(rand.nextInt(paddleHeight/2));
				if(Settings.getDifficulty().equals("Hard"))
					paddle.setGoal(calculateGoal(paddle, ball));
			} else if(ballDeltaX > 0 && ballX < paddleCoordinates[0] + paddle.getWidth() + 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		} else if(!Settings.getDifficulty().equals("Easy")){
			if(ballDeltaX > 0) {
				if(Settings.getDifficulty().equals("Medium"))
					paddle.setGoal((int) ballY + ball.getRadius());
				if(ballX < 100)
					paddle.setRange(rand.nextInt(paddleHeight/2));
				if(Settings.getDifficulty().equals("Hard"))
					paddle.setGoal(calculateGoal(paddle, ball));
			} else if (ballDeltaX < 0 && ballX + 2*ball.getRadius() < paddleCoordinates[0] - 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		}
		
//		int thisPaddleY = paddle.getY();
		int centerOfPaddle = paddleCoordinates[1] + paddle.getHeight()/2;
		int currentGoal = paddle.getGoal() + ball.getRadius();
		int range = paddle.getRange();
		if((centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal)) {
			if(Settings.getDifficulty().equals("Easy")) {
				int goal = paddleHeight/2 + rand.nextInt(Settings.getFrameHeight()-paddleHeight);
				paddle.setGoal(goal);
			}
			return;
		}
		if(centerOfPaddle > currentGoal && paddleCoordinates[1] > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && paddleCoordinates[1] < Settings.getFrameHeight()) {
			paddle.paddleDown();
		}
	}

	private void AIServe(Paddle paddle, Ball ball) {
//		int paddleHeight = paddle.getImage().getHeight();
		int paddleX = paddle.getCoordinates()[0];
		int width = Settings.getFrameWidth();

		if((paddleX < width/2 && ball.isServingLeft()) || (paddleX > width/2 && ball.isServingRight())) {
			AIDelay++;
			if(AIDelay < 2) 
				paddle.setGoal(rand.nextInt(Settings.getFrameHeight()-paddle.getHeight()/2));
			if(AIDelay > 50) {
				paddle.serve(ball);
				AIDelay = 0;
			}
		} else if (ball.isServingLeft() || ball.isServingRight()) {
			if(AIDelay < 2)
				paddle.setGoal(Settings.getFrameWidth()/2 - paddle.getHeight());
		}
	}
	
	private int calculateGoal(Paddle paddle, Ball ball) {
//		int diameter = ball.getImage().getHeight();
		float ballY = ball.getCoordinates()[1];
		float deltaY = ball.getDeltaY();
		if(deltaY == 0)
			return (int) ballY + 2*ball.getRadius();

		float ballX = ball.getCoordinates()[0];
		float deltaX = ball.getDeltaX();
		int numberOfFrames = 0;
		if(paddle.getCoordinates()[0] < Settings.getFrameWidth()/2) {
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