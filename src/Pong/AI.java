package Pong;

import java.util.Random;

public class AI {
	
	private Ball ball;
	private Random rand;
	private int AIDelay = 0;

	public AI(Ball ball) {
		this.ball = ball;
		rand = new Random();
	}
	
	public void getAIMovement(Paddle paddle, Ball ball) {
		this.ball = ball;

		AIServe(paddle);

		int diameter = ball.getImage().getWidth();
		int thisPaddleX = paddle.getX();
		int paddleHeight = paddle.getImage().getHeight();
		Double ballX = ball.getXPosition();
		Double ballY = ball.getYPosition();
		double ballDeltaX = ball.getDeltaX();
		
		if(thisPaddleX < Settings.getFrameWidth()/2 && !Settings.getDifficulty().equals("Easy")) {
			if(ballDeltaX < 0) {
				if(Settings.getDifficulty().equals("Medium"))
					paddle.setGoal(ballY.intValue() + ball.getImage().getHeight()/2);
				if(ballX.intValue() + diameter > Settings.getFrameWidth()-100)
					paddle.setRange(rand.nextInt(paddleHeight/2));
				if(Settings.getDifficulty().equals("Hard"))
					paddle.setGoal(calculateGoal(paddle));
			} else if(ballDeltaX > 0 && ballX.intValue() < thisPaddleX + paddle.getImage().getWidth() + 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		} else if(!Settings.getDifficulty().equals("Easy")){
			if(ballDeltaX > 0) {
				if(Settings.getDifficulty().equals("Medium"))
					paddle.setGoal(ballY.intValue() + ball.getImage().getHeight()/2);
				if(ballX.intValue() < 100)
					paddle.setRange(rand.nextInt(paddleHeight/2));
				if(Settings.getDifficulty().equals("Hard"))
					paddle.setGoal(calculateGoal(paddle));
			} else if (ballDeltaX < 0 && ballX.intValue() + diameter < thisPaddleX - 10) {
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
				paddle.setRange(2);
			}
		}
		
		int thisPaddleY = paddle.getY();
		int centerOfPaddle = thisPaddleY + paddle.getImage().getHeight()/2;
		int currentGoal = paddle.getGoal() + diameter/2;
		int range = paddle.getRange();
		if((centerOfPaddle + range >= currentGoal && centerOfPaddle - range <= currentGoal)) {
			if(Settings.getDifficulty().equals("Easy")) {
				int goal = paddleHeight/2 + rand.nextInt(Settings.getFrameHeight()-paddleHeight);
				paddle.setGoal(goal);
			}
			return;
		}
		if(centerOfPaddle > currentGoal && thisPaddleY > 0) {
			paddle.paddleUp();
		} else if(centerOfPaddle < currentGoal && thisPaddleY < Settings.getFrameHeight()) {
			paddle.paddleDown();
		}
	}

	private void AIServe(Paddle paddle) {
		int paddleHeight = paddle.getImage().getHeight();
		int paddleX = paddle.getX();
		int width = Settings.getFrameWidth();

		if((paddleX < width/2 && ball.isServingLeft()) || (paddleX > width/2 && ball.isServingRight())) {
			AIDelay++;
			if(AIDelay < 2) 
				paddle.setGoal(rand.nextInt(Settings.getFrameHeight()-paddleHeight/2));
			if(AIDelay > 50) {
				paddle.serve(ball);
				AIDelay = 0;
			}
		} else if (ball.isServingLeft() || ball.isServingRight()) {
			if(AIDelay < 2)
				paddle.setGoal(Settings.getFrameWidth()/2 - paddleHeight);
		}
	}
	
	private int calculateGoal(Paddle paddle) {
		int diameter = ball.getImage().getHeight();
		Double ballY = ball.getYPosition();
		double deltaY = ball.getDeltaY();
		if(deltaY == 0)
			return ballY.intValue() + diameter;

		Double ballX = ball.getXPosition();
		double deltaX = ball.getDeltaX();
		int numberOfFrames = 0;
		if(paddle.getX() < Settings.getFrameWidth()/2) {
			while(ballX.intValue() > paddle.getX() + paddle.getImage().getWidth()) {
				numberOfFrames++;
				ballX += deltaX;
			}
		} else {
			while(ballX.intValue() + diameter < paddle.getX()) {
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
}