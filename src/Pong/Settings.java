package Pong;

/**
 * This class just keeps record of game settings, also
 * having setters and getters available to all classes
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-05-10
 */
public class Settings {
	
	private static final int WIDTH = 800, HEIGHT = 600;
	
	private static boolean leftPaddleIsHuman = false;
	private static boolean rightPaddleIsHuman = false;
	private static boolean gameIsRunning = false;
	
	private static String difficulty = "Easy";
	
	public Settings() {}
	
	/**
	 * Returns the width of frame
	 * @return Width of frame
	 */
	public static int getFrameWidth() {
		return WIDTH;
	}
	
	/**
	 * Returns the height of frame
	 * @return Height of frame
	 */
	public static int getFrameHeight() {
		return HEIGHT;
	}
	
	/**
	 * Returns true if left paddle is controlled by user
	 * @return True if left paddle is controlled by user
	 */
	public static boolean isLeftPaddleHuman() {
		return leftPaddleIsHuman;
	}
	
	/**
	 * Returns true if right paddle is controlled by user
	 * @return True if right paddle is controlled by user
	 */
	public static boolean isRightPaddleHuman() {
		return rightPaddleIsHuman;
	}
	
	/**
	 * Returns true if game is ongoing
	 * @return True if game is ongoing
	 */
	public static boolean isGameRunning() {
		return gameIsRunning;
	}
	
	/**
	 * Returns a String describing the current difficulty
	 * ("Easy", "Medium" or "Hard")
	 * @return String describing the current difficulty
	 */
	public static String getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Sets if left paddle is to be controlled by user (true) or
	 * by PC (false)
	 * @param answer True if left paddle is to be controlled by user
	 */
	public static void setIsLeftHuman(boolean answer) {
		leftPaddleIsHuman = answer;
	}
	
	/**
	 * Sets if right paddle is to be controlled by user (true) or
	 * by PC (false)
	 * @param answer True if right paddle is to be controlled by user
	 */
	public static void setIsRightHuman(boolean answer) {
		rightPaddleIsHuman = answer;
	}
	
	/**
	 * Sets if game is ongoing (true) or not (false)
	 * @param answer True if game is ongoing
	 */
	public static void setGameIsRunning(boolean answer) {
		gameIsRunning = answer;
	}
	
	/**
	 * Set difficulty using a String describing the difficulty
	 * level ("Easy", "Medium" or "Hard")
	 * @param newDifficulty String describing the current difficulty
	 */
	public static void setDifficulty(String newDifficulty) {
		difficulty = newDifficulty;
	}
}