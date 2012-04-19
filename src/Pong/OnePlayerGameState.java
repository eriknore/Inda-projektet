package Pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class OnePlayerGameState extends GamePlayState {
	
	private int stateID = -1;

	public OnePlayerGameState(int stateID) {
		super(stateID);
		this.stateID = stateID;
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO
	}
}
