package games.stendhal.server.maps.quests;

import games.stendhal.common.Grammar;
import games.stendhal.common.MathHelper;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.parser.Sentence;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * QUEST: The mithril shield forging.
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Baldemar, mithrilbourgh elite wizard, will forge a mithril shield.
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Baldemar tells you about shield.
 * <li> He offers to forge a mithril shield for you if you bring him what he
 * needs.
 * <li> You give him all he asks for.
 * <li> Baldemar checks if you have ever killed a black giant alone, or not
 * <li> Baldemar forges the shield for you
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li> mithril shield
 * <li>95000 XP
 * </ul>
 * 
 * 
 * REPETITIONS:
 * <ul>
 * <li> None.
 * </ul>
 */
public class StuffForBaldemar extends AbstractQuest {
	private static Map<Integer, ItemData> neededItems = initneededitems();
	private static Map<Integer, ItemData> initneededitems() {
		neededItems = new TreeMap<Integer, ItemData>();
		ItemData data = new ItemData("mithril bar", 
									REQUIRED_MITHRIL_BAR, 
									"I cannot #forge it without the missing ", 
									". After all, this IS a mithril shield.");
		neededItems.put(1, data);
		data = new ItemData("obsidian",	REQUIRED_OBSIDIAN, 
				"I need several gems to grind into dust to mix with the mithril. I need ", 
				" still.");
		neededItems.put(2, data);
		data = new ItemData("diamond",	REQUIRED_DIAMOND , 
				"I need several gems to grind into dust to mix with the mithril. I need ", 
				" still.");
		neededItems.put(3, data);
		
		
		data = new ItemData("emerald",	REQUIRED_EMERALD , 
				"I need several gems to grind into dust to mix with the mithril. I need ", 
				" still.");
		neededItems.put(4, data);
		
		data = new ItemData("carbuncle",	REQUIRED_CARBUNCLE , 
				"I need several gems to grind into dust to mix with the mithril. I need ", 
				" still.");
		neededItems.put(5, data);
		
		data = new ItemData("sapphire",	REQUIRED_SAPPHIRE, 
				"I need several gems to grind into dust to mix with the mithril. I need ", 
				" still.");
		neededItems.put(6, data);
		int i = 7;
		data = new ItemData("black shield",	REQUIRED_BLACK_SHIELD , 
				"I need ", 
				" to form the framework for your new shield.");
		neededItems.put(i, data);
		
		 i = 8;
		data = new ItemData("magic plate shield",	REQUIRED_MAGIC_PLATE_SHIELD , 
				"I need ", 
				" for the pieces and parts for your new shield.");
		neededItems.put(i, data);
	
		 i = 9;
			data = new ItemData("gold bar",	REQUIRED_GOLD_BAR , 
					"I need ", 
					" to melt down with the mithril and iron.");
			neededItems.put(i, data);

		 i = 10;
			data = new ItemData("iron",	REQUIRED_IRON , 
					"I need ", 
					" to melt down with the mithril and gold.");
			neededItems.put(i, data);
		 i = 11;
			data = new ItemData("black pearl",	REQUIRED_BLACK_PEARL , 
					"I need ", 
					" to crush into fine powder to sprinkle onto shield to give it a nice sheen.");
			neededItems.put(i, data);

		 i = 12;
			data = new ItemData("shuriken",	REQUIRED_SHURIKEN , 
					"I need ", 
					" to melt down with the mithril, gold and iron. It is a 'secret' ingredient that only you and I know about. ;)");
			neededItems.put(i, data);
			i = 13;
			data = new ItemData("marbles",	REQUIRED_MARBLES , 
					"My son wants some new toys. I need ", 
					" still.");
			neededItems.put(i, data);

			data = new ItemData("snowglobe",	REQUIRED_SNOWGLOBE, 
					"I just LOVE those trinkets from athor. I need ", 
					" still.");
			neededItems.put(14, data);
		return neededItems;
	}

	
	protected static final class ItemData {

		private int neededAmount;
		private final String itemName;
		private final String itemPrefix;
		private final String itemSuffix;
		private final int requiredAmount;

		public ItemData(final String name, final int needed, final String prefix, final String suffix) {
			this.requiredAmount = needed;
			this.neededAmount = needed;
			this.itemName = name;
			this.itemPrefix = prefix;
			this.itemSuffix = suffix;
		}

		public int getStillNeeded() {
			return neededAmount;
		}

		public void setAmount(final int needed) {
			neededAmount = needed;
			
		}

		public String getName() {
			return itemName;
		}

		public String getPrefix() {
			return itemPrefix;
		}

		public String getSuffix() {
			return itemSuffix;
		}

		public void subAmount(final String string) {
			subAmount(Integer.parseInt(string));
			
		}

		String getAnswer() {
			return itemPrefix
				+ Grammar.quantityplnoun(
						neededAmount, itemName)
				+ itemSuffix;
		}

		public int getRequired() {
			return requiredAmount;
		}

		public int getAlreadyBrought() {
			return requiredAmount - neededAmount;
		}

		public void subAmount(final int amount) {
			neededAmount -= amount;
			
		}

		public void resetAmount() {
			neededAmount = requiredAmount;
			
		}
	}

	private static final int REQUIRED_MITHRIL_BAR = 20;

	private static final int REQUIRED_OBSIDIAN = 1;

	private static final int REQUIRED_DIAMOND = 1;

	private static final int REQUIRED_EMERALD = 5;
	
	private static final int REQUIRED_CARBUNCLE = 10;

	private static final int REQUIRED_SAPPHIRE = 10;

	private static final int REQUIRED_BLACK_SHIELD = 1;

	private static final int REQUIRED_MAGIC_PLATE_SHIELD = 1;

	private static final int REQUIRED_GOLD_BAR = 10;

	private static final int REQUIRED_IRON = 20;

	private static final int REQUIRED_BLACK_PEARL = 10;

	private static final int REQUIRED_SHURIKEN = 20;
	
	private static final int REQUIRED_MARBLES = 15;

	private static final int REQUIRED_SNOWGLOBE = 1;

	private static final String I_WILL_NEED_MANY_THINGS = "I will need many, many things: "
							+ REQUIRED_MITHRIL_BAR
							+ " mithril bars, "
							+ REQUIRED_OBSIDIAN
							+ " obsidian, "
							+ REQUIRED_DIAMOND
							+ " diamond, "
							+ REQUIRED_EMERALD
							+ " emeralds," 
							+ REQUIRED_CARBUNCLE
							+ " carbuncles, "
							+ REQUIRED_SAPPHIRE
							+ " sapphires, "
							+ REQUIRED_BLACK_SHIELD
							+ " black shield, "
							+ REQUIRED_MAGIC_PLATE_SHIELD
							+ " magic plate shield, "
							+ REQUIRED_GOLD_BAR
							+ " gold bars, "
							+ REQUIRED_IRON
							+ " iron bars, "
							+ REQUIRED_BLACK_PEARL
							+ " black pearls, " 
							+ REQUIRED_SHURIKEN
							+ " shuriken, "
							+ REQUIRED_MARBLES
							+ " marbles and "
							+ REQUIRED_SNOWGLOBE
							+ " snowglobe. Come back when you have them in the same #exact order!";
	
	private static final int REQUIRED_MINUTES = 10;

	private static final String QUEST_SLOT = "mithrilshield_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	private void step_1() {
		final SpeakerNPC npc = npcs.get("Baldemar");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						engine.say("I can forge a shield made from mithril along with several other items. Would you like me to do that?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						engine.say("I would prefer you left me to my entertainment.");
						engine.setCurrentState(ConversationStates.ATTENDING);
					} else {
						engine.say("Why are you bothering me when you haven't completed your quest yet?");
						engine.setCurrentState(ConversationStates.ATTENDING);
					}
				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {
					engine.say(I_WILL_NEED_MANY_THINGS);
					player.setQuest(QUEST_SLOT, "start;0;0;0;0;0;0;0;0;0;0;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"I can't believe you are going to pass up this opportunity! You must be daft!!!",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

		npc.addReply("exact",
			"As I have listed them here, you must provide them in that order.");
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Baldemar");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new QuestStateStartsWithCondition(QUEST_SLOT, "start"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
					
					int idx1 = 1;
					for (ItemData itemdata : neededItems.values()) {
							itemdata.resetAmount();
							itemdata.subAmount(tokens[idx1]);
							idx1++;
					}

					boolean missingSomething = false;

					int size = neededItems.size();
					for (int idx = 1; !missingSomething && idx <= size; idx++) {
						ItemData itemData = neededItems.get(idx);
						missingSomething = proceedItem(player, engine,
								itemData);
					}
					
					if (player.hasKilledSolo("black giant") && !missingSomething) {
						engine.say("You've brought everything I need to forge the shield. Come back in "
							+ REQUIRED_MINUTES
							+ " minutes and it will be ready.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilledSolo("black giant") && !missingSomething) {
							engine.say("This shield can only be given to those who have killed a black giant, and without the help of others.");
						}

						StringBuilder sb = new StringBuilder(30);
						sb.append("start");
						for (ItemData id : neededItems.values()) {
							sb.append(";");
							sb.append(id.getAlreadyBrought());
						}
						player.setQuest(QUEST_SLOT, sb.toString());
							
					}
				}

	
				private boolean proceedItem(final Player player,
						final SpeakerNPC engine, final ItemData itemData) {
					if (itemData.getStillNeeded() > 0) {
						
						if (player.isEquipped(itemData.getName(), itemData.getStillNeeded())) {
							player.drop(itemData.getName(), itemData.getStillNeeded());
							itemData.setAmount(0);
						} else {
							final int amount = player.getNumberOfEquipped(itemData.getName());
							if (amount > 0) {
								player.drop(itemData.getName(), amount);
								itemData.subAmount(amount);
							}

							engine.say(itemData.getAnswer());
							return true;
						}
					}
					return false;
				}
			});

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new QuestStateStartsWithCondition(QUEST_SLOT, "forging;"),
				ConversationStates.IDLE, null, new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {

					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
					
					final long delay = REQUIRED_MINUTES * MathHelper.MILLISECONDS_IN_ONE_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						engine.say("I haven't finished forging your shield. Please check back in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					engine.say("I have finished forging your new mithril shield. Enjoy. Now I will see what Trillium has stored behind the counter for me. ;)");
					player.addXP(95000);
					player.addKarma(25);
					final Item mithrilshield = SingletonRepository.getEntityManager().getItem("mithril shield");
					mithrilshield.setBoundTo(player.getName());
					player.equipOrPutOnGround(mithrilshield);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("forge", "missing"), 
			new QuestStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final SpeakerNPC engine) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					final int neededMithrilBar = REQUIRED_MITHRIL_BAR
							- Integer.parseInt(tokens[1]);
					final int neededObsidian = REQUIRED_OBSIDIAN
							- Integer.parseInt(tokens[2]);
					final int neededDiamond = REQUIRED_DIAMOND
							- Integer.parseInt(tokens[3]);
					final int neededEmerald = REQUIRED_EMERALD
							- Integer.parseInt(tokens[4]);
					final int neededCarbuncle = REQUIRED_CARBUNCLE
							- Integer.parseInt(tokens[5]);
					final int neededSapphire = REQUIRED_SAPPHIRE
							- Integer.parseInt(tokens[6]);
					final int neededBlackShield = REQUIRED_BLACK_SHIELD
							- Integer.parseInt(tokens[7]);
					final int neededMagicPlateShield = REQUIRED_MAGIC_PLATE_SHIELD
							- Integer.parseInt(tokens[8]);
					final int neededGoldBars = REQUIRED_GOLD_BAR
							- Integer.parseInt(tokens[9]);
					final int neededIron = REQUIRED_IRON
							- Integer.parseInt(tokens[10]);
					final int neededBlackPearl = REQUIRED_BLACK_PEARL
							- Integer.parseInt(tokens[11]);
					final int neededShuriken = REQUIRED_SHURIKEN
							- Integer.parseInt(tokens[12]);
					final int neededMarbles = REQUIRED_MARBLES
							- Integer.parseInt(tokens[13]);
					final int neededSnowglobe = REQUIRED_SNOWGLOBE
							- Integer.parseInt(tokens[14]);
					
					engine.say("I will need " + neededMithrilBar + " mithril bars, "
							+ neededObsidian + " obsidian, "
							+ neededDiamond + " diamond, "
							+ neededEmerald + " emeralds, "
							+ neededCarbuncle + " carbuncles, "
							+ neededSapphire + " sapphires, "
							+ neededBlackShield + " black shield, "
							+ neededMagicPlateShield + " magic plate shield, "
							+ neededGoldBars + " gold bars, "
							+ neededIron + " iron bars, "
							+ neededBlackPearl + " black pearls, "
							+ neededShuriken + " shuriken, "
							+ neededMarbles + " marbles and "
							+ neededSnowglobe + " snowglobe");
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
