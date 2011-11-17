/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.client.gui.map;

import games.stendhal.client.entity.User;
import games.stendhal.client.gui.layout.SBoxLayout;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Area for displaying information about a zone.
 */
public class InformationPanel extends JComponent {
	/** Maximum number of skull icons in the danger indicator. */
	private static final int MAX_SKULLS = 5;
	/**
	 * Textual description of the danger level. There should be MAX_SKULLS + 1
	 * of these.
	 */
	private static final String[] dangerLevelStrings = {
		"The area feels safe.",
		"The area feels relatively safe.",
		"The area feels somewhat dangerous.",
		"The area feels dangerous.",
		"The area feels very dangerous!",
		"The area feels extremely dangerous. Run away!"
	};
	
	/** Zone name display. */
	private final JLabel nameLabel;
	/** Danger level icons */
	private final DangerIndicator dangerIndicator;
	/** Current relative danger level. */
	private int dangerLevel;
	
	/**
	 * Create a new InformationPanel.
	 */
	InformationPanel() {
		setLayout(new SBoxLayout(SBoxLayout.VERTICAL));
		
		// ** Zone name **
		nameLabel = new JLabel();
		// "unbold". The label is wide enough as it is
		Font f = nameLabel.getFont();
		if ((f.getStyle() & Font.BOLD) != 0) {
			nameLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
		}
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		nameLabel.setOpaque(true);
		nameLabel.setBackground(getBackground());
		add(nameLabel);
		
		// ** Danger display **
		dangerIndicator = new DangerIndicator(MAX_SKULLS);
		dangerIndicator.setAlignmentX(CENTER_ALIGNMENT);
		add(dangerIndicator);
		// Default to safe, so that we always have a tooltip
		setToolTipText(dangerLevelStrings[0]);
	}
	
	/**
	 * Set the name of the zone.
	 * 
	 * @param name
	 */
	void setZoneName(String name) {
		nameLabel.setText(name);
	}
	
	/**
	 * Set the zone danger level.
	 * 
	 * @param dangerLevel danger level
	 */
	void setDangerLevel(double dangerLevel) {
		int skulls = (int) Math.min(5, Math.round(2 * dangerLevel / (User.getPlayerLevel() + 1)));
		if (this.dangerLevel != skulls) {
			this.dangerLevel = skulls;
			dangerIndicator.setRelativeDanger(skulls);
			setToolTipText(dangerLevelStrings[skulls]);
		}
	}
	
	/**
	 * A skull row component for danger level display.
	 */
	private static class DangerIndicator extends JComponent {
		/** Indicator icon image. */
		private static final ImageIcon skullIcon = new ImageIcon(DangerIndicator.class.getClassLoader().getResource("data/gui/danger.png"));

		/** The indicator icons */
		private final JComponent[] indicators;
		
		/**
		 * Create a new DangerIndicator.
		 * 
		 * @param maxSkulls maximum number of skulls to display
		 */
		DangerIndicator(int maxSkulls) {
			setLayout(new SBoxLayout(SBoxLayout.HORIZONTAL));
			indicators = new JComponent[maxSkulls];
			for (int i = 0; i < maxSkulls; i++) {
				JLabel indicator = new JLabel(skullIcon);
				// Avoid showing a row of skulls on login
				indicator.setVisible(false);
				add(indicator);
				indicators[i] = indicator;
			}
		}
		
		/**
		 * Set the relative danger level.
		 * 
		 * @param skulls amount of skulls to show
		 */
		void setRelativeDanger(int skulls) {
			for (int i = 0; i < indicators.length; i++) {
				indicators[i].setVisible(i < skulls);
			}
		}
	}
}
