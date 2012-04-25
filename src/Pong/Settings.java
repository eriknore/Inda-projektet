package Pong;

/**
 * @author Erik Norell & Daniel Aceituno
 *
 */
public class Settings {
	
	private static int width = 800, height = 600;
	
	private static boolean leftPaddleIsHuman = false;
	private static boolean rightPaddleIsHuman = false;
	private static boolean gameIsRunning = false;
	
	private static String difficulty = "Easy";
	
	public Settings() {}
	
	public static int getFrameWidth() {
		return width;
	}
	
	public static int getFrameHeight() {
		return height;
	}
	
	public static boolean isLeftPaddleHuman() {
		return leftPaddleIsHuman;
	}
	
	public static boolean isRightPaddleHuman() {
		return rightPaddleIsHuman;
	}
	
	public static boolean isGameRunning() {
		return gameIsRunning;
	}
	
	public static String getDifficulty() {
		return difficulty;
	}
	
	public static void setFrameWidth(int newWidth) {
		width = newWidth;
	}
	
	public static void setFrameHeight(int newHeight) {
		height = newHeight;
	}
	
	public static void setIsLeftHuman(boolean answer) {
		leftPaddleIsHuman = answer;
	}
	
	public static void setIsRightHuman(boolean answer) {
		rightPaddleIsHuman = answer;
	}
	
	public static void setGameIsRunning(boolean answer) {
		gameIsRunning = answer;
	}
	
	public static void setDifficulty(String newDifficulty) {
		difficulty = newDifficulty;
	}
}
