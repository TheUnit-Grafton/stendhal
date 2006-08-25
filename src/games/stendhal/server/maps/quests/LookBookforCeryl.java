package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.List;

import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.entity.Player;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

/** 
 * QUEST: Look book for Ceryl
 * PARTICIPANTS: 
 * - Ceryl
 * - Jynath
 * 
 * STEPS: 
 * - Talk with Ceryl to activate the quest.
 * - Talk with Jynath for the book.
 * - Return the book to Ceryl
 *
 * REWARD: 
 * - 100 XP
 * - 50 gold coins
 *
 * REPETITIONS:
 * - None.
 */
public class LookBookforCeryl extends AbstractQuest {
	private static final String QUEST_SLOT = "ceryl_book";

	@Override
	public void init(String name) {
		super.init(name, QUEST_SLOT);
	}

	@Override
	public List<String> getHistory(Player player) {
		List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("FIRST_CHAT");
		String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("QUEST_REJECTED");
		}
		if (player.isQuestInState(QUEST_SLOT, "start", "jynath", "done")) {
			res.add("QUEST_ACCEPTED");
		}
		if ((questState.equals("jynath") && player.isEquipped("book_black")) || questState.equals("done")) {
			res.add("FOUND_ITEM");
		}
		if (questState.equals("jynath") && !player.isEquipped("book_black")) {
			res.add("LOST_ITEM");
		}
		if (questState.equals("done")) {
			res.add("DONE");
		}
		return res;
	}

	private void step_1() {

		SpeakerNPC npc = npcs.get("Ceryl");

		npc.add(ConversationStates.ATTENDING,
				SpeakerNPC.QUEST_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				null,
				new SpeakerNPC.ChatAction() {
					@Override
					public void fire(Player player, String text,
							SpeakerNPC engine) {
						if (player.isQuestCompleted(QUEST_SLOT)) {
							engine.say("I have nothing for you now.");
						} else {
							engine.say("I am looking for a very special #book.");
						}
					}
				});

		/** In case quest is completed */
		npc.add(ConversationStates.ATTENDING,
				"book",
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.isQuestCompleted(QUEST_SLOT);
					}
				},
				ConversationStates.ATTENDING,
				"I already got the book. Thank you!",
				null);

		/** If quest is not started yet, start it. */
		npc.add(ConversationStates.ATTENDING,
				"book",
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return !player.hasQuest(QUEST_SLOT);
					}
				},
				ConversationStates.QUEST_OFFERED,
				"Could you ask #Jynath for a book that I am looking for?",
				null);

		npc.add(ConversationStates.QUEST_OFFERED,
				SpeakerNPC.YES_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"Great! Start the quest now!",
				new SpeakerNPC.ChatAction() {
					@Override
					public void fire(Player player, String text, SpeakerNPC engine) {
						player.setQuest(QUEST_SLOT, "start");
					}
				});

		npc.add(ConversationStates.QUEST_OFFERED,
				"no",
				null,
				ConversationStates.ATTENDING,
				"Oh! Ok :(",
				null);

		npc.add(ConversationStates.QUEST_OFFERED,
				"jynath",
				null,
				ConversationStates.QUEST_OFFERED,
				"Jynath is a witch who lives South of Or'ril castle. So will you get me the book?",
				null);

		/** Remind player about the quest */
		npc.add(ConversationStates.ATTENDING,
				"book",
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest(QUEST_SLOT)
								&& player.getQuest(QUEST_SLOT).equals("start");
					}
				},
				ConversationStates.ATTENDING,
				"I really need that book now! Go to talk with #Jynath.",
				null);

		npc.add(ConversationStates.ATTENDING,
				"jynath",
				null,
				ConversationStates.ATTENDING,
				"Jynath is a witch who lives South of Or'ril castle.",
				null);
	}

	private void step_2() {
		SpeakerNPC npc = npcs.get("Jynath");

		/** If player has quest and is in the correct state, just give him the book. */
		npc.add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest(QUEST_SLOT)
								&& player.getQuest(QUEST_SLOT)
										.equals("start");
					}
				},
				ConversationStates.ATTENDING,
				"I see you talked with Ceryl. Here you have the book he is looking for.",
				new SpeakerNPC.ChatAction() {
					@Override
					public void fire(Player player, String text, SpeakerNPC engine) {
						player.setQuest(QUEST_SLOT, "jynath");

						Item item = StendhalRPWorld.get().getRuleManager().getEntityManager()
								.getItem("book_black");
						player.equip(item, true);
					}
				});

		/** If player keep asking for book, just tell him to hurry up */
		npc.add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest(QUEST_SLOT)
								&& player.getQuest(QUEST_SLOT).equals(
										"jynath");
					}
				},
				ConversationStates.ATTENDING,
				"Hurry up! Bring the book to #Ceryl.",
				null);

		npc.add(ConversationStates.ATTENDING,
				"ceryl",
				null,
				ConversationStates.ATTENDING,
				"Ceryl is the book keeper at Semos's library",
				null);

		/** Finally if player didn't start the quest, just ignore him/her */
		npc.add(ConversationStates.ATTENDING,
				"book",
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return !player.hasQuest(QUEST_SLOT);
					}
				},
				ConversationStates.ATTENDING,
				"Shhhh!!! I am working on a new potion!",
				null);
	}

	private void step_3() {
		SpeakerNPC npc = npcs.get("Ceryl");

		/** Complete the quest */
		npc.add(ConversationStates.IDLE,
				SpeakerNPC.GREETING_MESSAGES,
				new SpeakerNPC.ChatCondition() {
					@Override
					public boolean fire(Player player, SpeakerNPC npc) {
						return player.hasQuest(QUEST_SLOT)
								&& player.getQuest(QUEST_SLOT).equals(
										"jynath");
					}
				},
				ConversationStates.ATTENDING,
				null,
				new SpeakerNPC.ChatAction() {
					@Override
					public void fire(Player player, String text,
							SpeakerNPC engine) {
						if (player.drop("book_black")) {
							engine.say("Oh! The book! Thanks!");
							StackableItem money = (StackableItem) StendhalRPWorld.get()
									.getRuleManager().getEntityManager()
									.getItem("money");

							money.setQuantity(50);
							player.equip(money);
							player.addXP(100);

							player.notifyWorldAboutChanges();

							player.setQuest(QUEST_SLOT, "done");
						} else {
							engine.say("Where did you put #Jynath's #book?. You need to start the search again.");
							player.removeQuest(QUEST_SLOT);
						}
					}
				});
	}

	@Override
	public void addToWorld() {
		super.addToWorld();

		step_1();
		step_2();
		step_3();
	}
}