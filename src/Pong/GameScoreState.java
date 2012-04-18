package Pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameScoreState extends BasicGameState {
	
	private Image testpicture;
	private int stateID = -1;
	
	public GameScoreState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		testpicture = new Image("data/menu/quitgame.png");
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		testpicture.draw(20f, 30f);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the state ID.
	 * @return Returns the state ID
	 */
	@Override
	public int getID() {
		return stateID;
	}

}
