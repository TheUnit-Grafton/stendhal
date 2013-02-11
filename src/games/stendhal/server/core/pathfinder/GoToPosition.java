/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.pathfinder;

import games.stendhal.server.entity.GuidedEntity;
import games.stendhal.server.entity.Registrator;

import java.util.Observable;
import java.util.Observer;


/**
 * class for guide NPC to certain point in his zone
 * @author yoriy
 */
public class GoToPosition implements Observer {
		private final GuidedEntity ent;
		private final Registrator finishnotifier = new Registrator();
		private FixedPath path;
		private final Node position;
		// flag for checking started state
		private boolean s = false;
	
	/**
	 * constructor
	 * @param entity - pathnotifier owner
	 */
	public GoToPosition(
			final GuidedEntity entity,
			final Node position,
			final Observer o) {

		ent=entity;
		finishnotifier.setObserver(o);		
		this.position = position;
	}

	/**
	 * update function
	 * @param o - not used
	 * @param arg - arguments, not used
	 */
	public void update(Observable o, Object arg) {
		// do we started already?
		if(!s) {
			// no, adding ourselves to npc's path end notifications
			ent.pathnotifier.setObserver(this);
			path = new FixedPath(Path.searchPath(ent, position.getX(), position.getY()),false);
			ent.setPath(path);
			s=true;
		} else {
			// yes, removing ourselves from npc's path end notifications ... 
			ent.pathnotifier.removeObserver(this);
			// ... and notifying our observers.
			finishnotifier.setChanges();
			finishnotifier.notifyObservers();
			s=false;
		}
	}	
}

