package games.stendhal.server.entity.npc.action;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPC.ChatAction;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Starts the recording of kills
 *
 * @author hendrik
 */
public class StartRecodingKillsAction extends ChatAction {
	private List<String> toKill;

	/**
	 * creates a new StartRecodingKillsAction
	 *
	 * @param toKill list of creatures which should be killed by the player
	 */
	public StartRecodingKillsAction(List<String> toKill) {
		this.toKill = toKill;
	}

	/**
	 * creates a new StartRecodingKillsAction
	 *
	 * @param toKill creatures which should be killed by the player
	 */
	public StartRecodingKillsAction(String... toKill) {
		this.toKill = Arrays.asList(toKill);
	}

	@Override
	public void fire(Player player, String text, SpeakerNPC npc) {
		for (String creature : toKill) {
			player.removeKill(creature);
		}
	}

	@Override
	public String toString() {
		return "StartRecodringKillsActions <" + toKill + ">"; 
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false, StartRecodingKillsAction.class);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(toKill).toHashCode();
	}

	
}
