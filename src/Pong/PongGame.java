package Pong;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * @author Erik Norell & Daniel Aceituno
 * @version 2012-04-16
 */
public class PongGame {
	
	private static int width = 800, height = 600; // resolution

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new GamePlayState());
			app.setDisplayMode(width, height, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
