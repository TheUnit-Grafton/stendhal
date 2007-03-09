/* $Id$ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.client.entity;

import games.stendhal.client.Sprite;
import games.stendhal.client.SpriteStore;
import games.stendhal.client.StendhalClient;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;

import marauroa.common.game.AttributeNotFoundException;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;

/** A Sheep entity */
public class Sheep extends NPC {
	private int weight;

	public Sheep(RPObject object) throws AttributeNotFoundException {
		super(object);
	}

	@Override
	protected void buildAnimations(RPObject object) {
		SpriteStore store = SpriteStore.get();

		sprites.put("move_up", store.getAnimatedSprite(translate(object
				.get("type")), 0, 3, 1, 1));
		sprites.put("move_right", store.getAnimatedSprite(translate(object
				.get("type")), 1, 3, 1, 1));
		sprites.put("move_down", store.getAnimatedSprite(translate(object
				.get("type")), 2, 3, 1, 1));
		sprites.put("move_left", store.getAnimatedSprite(translate(object
				.get("type")), 3, 3, 1, 1));
		sprites.put("big_move_up", store.getAnimatedSprite(translate(object
				.get("type")), 4, 3, 1, 1));
		sprites.put("big_move_right", store.getAnimatedSprite(translate(object
				.get("type")), 5, 3, 1, 1));
		sprites.put("big_move_down", store.getAnimatedSprite(translate(object
				.get("type")), 6, 3, 1, 1));
		sprites.put("big_move_left", store.getAnimatedSprite(translate(object
				.get("type")), 7, 3, 1, 1));
	}

	@Override
	public void onChangedAdded(RPObject base, RPObject diff)
			throws AttributeNotFoundException {
		super.onChangedAdded(base, diff);

		if (diff.has("weight")) {
			int oldWeight = weight;
			weight = diff.getInt("weight");
			if (weight > oldWeight) {
				playSound("sheep-eat", 8, 15);
			}
		}

		if (diff.has("idea")) {
			String idea = diff.get("idea");
			if (idea.equals("eat")) {
				probableChat(15);
			} else if (idea.equals("food")) {
				probableChat(20);
			} else if (idea.equals("walk")) {
				probableChat(20);
			} else if (idea.equals("follow")) {
				probableChat(20);
			}
		}

	}

	@Override
	public Rectangle2D getArea() {
		return new Rectangle.Double(x, y, 1, 1);
	}

	@Override
	public Rectangle2D getDrawedArea() {
		return new Rectangle.Double(x, y, 1, 1);
	}

	@Override
	protected Sprite defaultAnimation() {
		animation = "move_up";
		return sprites.get("move_up")[0];
	}

	@Override
	public void onAction(ActionType at, String... params) {
		// ActionType at = handleAction(action);
		switch (at) {
		case OWN:
			RPAction rpaction = new RPAction();
			rpaction.put("type", at.toString());
			int id = getID().getObjectID();
			rpaction.put("target", id);
			at.send(rpaction);
			playSound("sheep-chat-2", 25, 60);
			break;

		default:
			playSound((weight > 50 ? "sheep-chat-2" : "sheep-chat"), 15, 40);
			super.onAction(at, params);
			break;
		}

	}

	private void probableChat(int chance) {
		String token = weight > 50 ? "sheep-mix2" : "sheep-mix";
		playSound(token, 20, 35, chance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.stendhal.client.entity.AnimatedEntity#nextFrame()
	 */
	@Override
	protected Sprite nextFrame() {
		if ((weight > 60) && !animation.startsWith("big_")) {
			animation = "big_" + animation;
		}
		return super.nextFrame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.stendhal.client.entity.RPEntity#buildOfferedActions(java.util.List)
	 */
	@Override
	protected void buildOfferedActions(List<String> list) {

		super.buildOfferedActions(list);
		if (!StendhalClient.get().getPlayer().has("sheep")) {
			list.add(ActionType.OWN.getRepresentation());
		}

	}

}
