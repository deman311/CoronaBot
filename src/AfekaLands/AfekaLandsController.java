package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import AfekaLands.Armor.aKind;
import AfekaLands.Item.Sort;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AfekaLandsController extends ListenerAdapter {

	public static Random rand = new Random();

	public static Character player1;
	public static Enemy enemy1;

	public static ArrayList<Shop> shops = new ArrayList<Shop>();
	private Shop currentShop;
	private int drunky = 0;
	private static String fightMesID, mapMesID, status1, statusID, status2, invID, shopID;
	private boolean mapOpen = false, activeFight = false, openInv = false, activeShop = false;
	private static boolean deadPlayer, has_Player = false, has_Enemy = false;

	private TextChannel statusC;

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		String sender = event.getMember().getEffectiveName();
		String messageRaw = event.getMessage().getContentRaw();
		String messageID = event.getMessageId();

		if (has_Player && player1 != null)
			if (player1.getHP() == 0) {
				deadPlayer = true;
				activeFight = false;
				mapOpen = false;
			} else
				deadPlayer = false;
		else
			deadPlayer = true;

		if (messageRaw.contains("FIGHT") && event.getAuthor().isBot())
			fightMesID = event.getMessageId();
		if (messageRaw.contains("🍻 The") && event.getAuthor().isBot())
			shopID = event.getMessageId();
		if (messageRaw.contains("MAP") && event.getAuthor().isBot())
			mapMesID = event.getMessageId();
		if (messageRaw.contains("INVENTORY") && event.getAuthor().isBot())
			invID = event.getMessageId();
		if (messageRaw.contains("Character Status") && event.getAuthor().isBot()) {
			statusID = event.getMessageId();
			statusC = event.getChannel();
		}
		if (messageRaw.contains("💢") || messageRaw.contains("🔥") && event.getAuthor().isBot())
			status1 = event.getMessageId();

		else if (event.getAuthor().isBot())
			return;

		if (statusID != null && event.getChannel() == statusC) {
			event.getChannel().deleteMessageById(statusID).queue();
			statusID = null;
		}

		if (sender.contains("Dima")) {
			// ----------------------------------------------------------------------------------------------//
			// ------------------------------------AfekaLands CODE
			// ------------------------------------------//
			// ----------------------------------------------------------------------------------------------//
			if (messageRaw.equalsIgnoreCase("@worldtest")) {
				MapGen2.createMap();
				MapGen2.readMap();
				event.getChannel().sendMessage(MapGen2.localMap()).queue();
			}

			if (messageRaw.contains("@move ")) {
				MapGen2.movePlayer(messageRaw.replace("@move ", "").charAt(0));
				event.getChannel().sendMessage(MapGen2.localMap()).queue();
			}

			if (messageRaw.contains("@fight") && !activeFight) {
				activeFight = true;
				event.getChannel().sendMessage(AfekaLandsUI.fightMenu(enemy1)).queue();
			}

			if (event.getMessage().getContentRaw().contains("@event")) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(Events.getEvent()).queue();
			}

			if (messageRaw.contains("@answer: ")) {
				String mes = messageRaw.replace("@answer: ", "");
				char ans = mes.charAt(0);
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(Events.giveAnswer(ans)).queue();
			}

			if (messageRaw.contains("@createmap")) {
				MapGen.createMap();
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("MAP GENERATED!").queue();
			}

			if (messageRaw.contains("@update: ") && has_Player) {
				String[] line = messageRaw.split(" ");
				try {
					player1.updateStatus(line[1], Integer.parseInt(line[2]));
				} catch (NumberFormatException e) {
					System.out.println("ERROR: INVALID NUMBER");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.out.println("ERROR: with file Character.txt");
					e.printStackTrace();
				}
			}

			if (messageRaw.contains("@hit: ")) {
				int Damage = Integer.parseInt(messageRaw.replace("@hit: ", ""));
				System.out.println(enemy1.dealDamage(Damage));
				if (activeFight) {
					event.getChannel().editMessageById(fightMesID, AfekaLandsUI.fightMenu(enemy1)).queue();
					if (enemy1.getHP() <= 0)
						activeFight = false;
				}
			}

			if (messageRaw.equalsIgnoreCase("@giveWEP")) {
				Weapon a = new Weapon(player1.getLevel());
				player1.putIn((Weapon) a);
				event.getChannel().sendMessage("Weapon Generated: " + a.getName() + " ATK: " + a.getDamage()).queue();
			}

			if (messageRaw.contains("@giveARM")) {
				Armor a = null;
				switch (Integer.parseInt(messageRaw.replace("@giveARM", ""))) {
				case 0:
					a = new Armor(aKind.HEAD, player1.getLevel());
					break;
				case 1:
					a = new Armor(aKind.TORSO, player1.getLevel());
					break;
				case 2:
					a = new Armor(aKind.LEG, player1.getLevel());
					break;
				case 3:
					a = new Armor(aKind.BOOT, player1.getLevel());
					break;
				case 4:
					a = new Armor(aKind.RING, player1.getLevel());
					break;
				}
				player1.putIn(a);
				event.getChannel().sendMessage("Armor Generated: " + a.getName() + " DEF: " + a.getDEF()).queue();
			}
		}

		// CHARACTER------------------------------------------------
		if (event.getMessage().getContentRaw().contains("@create: ")) {
			if (has_Player && player1.getHP() > 0) {
				event.getChannel().sendMessage("Already has a character! (" + checkPlayer() + ")").queue();
				return;
			} else if (has_Player && player1.getHP() == 0) {
				event.getChannel().sendMessage("Bury the character first with @bury").queue();
				return;
			} else {
				deadPlayer = false;
				String name = null;
				name = messageRaw.replace("@create: ", "");
				player1 = new Character(name, sender);
				event.getChannel().sendMessage(player1.getName()
						+ " has been born into AfekaLands! 🌞\nNew map generated." + "\n# AFEKA-LANDS MANUAL: #\r\n"
						+ "\r\n" + "@create: [name] - Create a new Character.\r\n" + "!m - To open the map.\r\n"
						+ "![wasd] - To move on the map.\r\n" + "@status - Show Character Status.\r\n"
						+ "@inv - Show Inventory.\r\n" + "@equip - Show equipped gear.\r\n" + "\r\n"
						+ "@useHP#[number] - Use a number of potions at once.\r\n"
						+ "@buy[item name] #[number] - Buy a number of items at once (space before #).\r\n" + "\r\n"
						+ "@closemap - Close the map manually.\r\n" + "@closeinv - Close the Inventory manually.")
						.queue();
				has_Player = true;
				MapGen2.createMap();
				MapGen2.readMap();
			}
		}

		if (event.getMessage().getContentRaw().contains("@status")) {
			event.getChannel().deleteMessageById(messageID).queue();
			try {
				String[] Char = null;
				statusID = event.getMessageId();
				Char = AfekaLandsUI.getStatus();
				if (player1.getHP() == 0)
					event.getChannel().sendMessage("🌟 Character Status 🌟\n" + "💀 " + Char[0] + " is DEAD. 💀")
							.queue();
				else
					event.getChannel()
							.sendMessage("----------------------\n🌟 Character Status 🌟\n\n" + "🧝‍♂️ Name:‍ "
									+ player1.getName() + "\n" + "⭐ Level: " + player1.getLevel() + "\n" + "✨EXP: "
									+ player1.getEXP() + "/" + player1.getMaxEXP() + "\n" + "♥ Health: "
									+ player1.getHP() + "/" + player1.getMaxHP() + "\n" + "⚡ Energy: "
									+ player1.getENG() + "/" + player1.getMaxENG() + "\n" + "🛡 Defense: "
									+ player1.getDEF() + "\n" + "🔪 Damage: " + player1.getDMG())
							.queue();
			} catch (NullPointerException e) {
				event.getChannel().sendMessage("No Character. Please create one using the command - @create: [name]")
						.queue();
			}
		}

		if (messageRaw.contains("@equip")) {
			event.getChannel().deleteMessageById(messageID).queue();
			event.getChannel().sendMessage(AfekaLandsUI.equipMenu()).queue();
		}

		if (messageRaw.contains("@inv") && !event.getAuthor().isBot()) {
			event.getChannel().deleteMessageById(messageID).queue();
			// if(!openInv) {
			event.getChannel().sendMessage(player1.invString()).queue();
			openInv = true;
			// }
			// else
			// event.getChannel().sendMessage("Inventory already open!\nClose it first:
			// @closeinv").queue();
		}

		if (messageRaw.contains("@closeinv")) {
			event.getChannel().deleteMessageById(messageID).queue();
			if (openInv) {
				event.getChannel().deleteMessageById(invID).queue();
				openInv = false;
			} else
				event.getChannel().sendMessage("Inv is not open!\nOpen with @inv").queue();
		}

		if (messageRaw.contains("@bury")) {
			event.getChannel().sendMessage(AfekaLandsUI.buryChar()).queue();
			;
		}

		if (messageRaw.equalsIgnoreCase("@Leaderboards")) {
			event.getChannel().sendMessage("🏆 LEADERBORADS 🏆\n" + Leaderboards.checkWrite()).queue();
		}

		if (!deadPlayer) {
			if (messageRaw.contains("@stats ")) {
				event.getChannel().deleteMessageById(messageID).queue();
				try {
					event.getChannel().sendMessage(Weapon.findByName(messageRaw.replace("@stats ", "")).getDes())
							.queue();
				} catch (NullPointerException e) {
					event.getChannel().sendMessage(Armor.findByName(messageRaw.replace("@stats ", "")).getDes())
							.queue();
				}
			}

			if (messageRaw.contains("@wear ")) {
				event.getChannel().deleteMessageById(messageID).queue();
				String mes = player1.wear(messageRaw.replace("@wear ", ""));
				if (mes == "Could not find a weapon with such name.") {
					mes = player1.wear(messageRaw.replace("@wear", ""));
				}
				event.getChannel().sendMessage(mes).queue();
			}

			if (messageRaw.contains("@takeoffWEP")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(player1.getWeapon())).queue();
			}
			Armor[] pA = player1.getArmor();
			if (messageRaw.contains("@takeoffHEAD")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(pA[0])).queue();
			}
			if (messageRaw.contains("@takeoffTORSO")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(pA[1])).queue();
			}
			if (messageRaw.contains("@takeoffLEG")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(pA[2])).queue();
			}
			if (messageRaw.contains("@takeoffBOOT")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(pA[3])).queue();
			}
			if (messageRaw.contains("@takeoffRING")) {
				event.getChannel().deleteMessageById(messageID).queue();
				event.getChannel().sendMessage(player1.takeoff(pA[4])).queue();
			}

		}

		if (!deadPlayer) {
			// MAP-------------------------------

			if (messageRaw.equalsIgnoreCase("!m")) {
				if (!mapOpen) {
					if (status1 != null) {
						event.getChannel().deleteMessageById(status1).queue();
						status1 = null;
					}
					if (statusID != null) {
						event.getChannel().deleteMessageById(statusID).queue();
						statusID = null;
					}
					if (status2 != null) {
						event.getChannel().deleteMessageById(status2).queue();
						status2 = null;
					}
					mapOpen = true;
					event.getChannel().sendMessage(AfekaLandsUI.mapMenu()).queue();
					event.getChannel().deleteMessageById(messageID).queue();
					System.out.println("MAP OPENED!");
				} else {
					event.getChannel().deleteMessageById(messageID).queue();
					event.getChannel().sendMessage("Map already open!\nClose with @closemap").queue();
				}
			}
			if (messageRaw.equalsIgnoreCase("@closeMap") && mapOpen) {
				event.getChannel().deleteMessageById(mapMesID).queue();
				event.getChannel().deleteMessageById(messageID).queue();
				System.out.println("MAP CLOSED!");
				mapOpen = false;
			}

			if (!deadPlayer)
				if (messageRaw.equalsIgnoreCase("!w") || messageRaw.equalsIgnoreCase("!a")
						|| messageRaw.equalsIgnoreCase("!s") || messageRaw.equalsIgnoreCase("!d")) {
					if (!activeFight && !activeShop) {
						if (mapOpen) {
							drunky = 0;
							String mes = messageRaw.replace("!", "");
							event.getChannel().deleteMessageById(messageID).queue();
							char ans = mes.charAt(0);
							try {
								MapGen2.movePlayer(ans);
								if (MapGen2.currentTile().contains("$"))
									throw new FileNotFoundException();
								if (MapGen2.checkTackle())
									throw new IOException();
								event.getChannel().editMessageById(mapMesID, AfekaLandsUI.mapMenu()).completeAfter(1,
										TimeUnit.SECONDS);
								System.out.println(MapGen2.getPOS());
								if (MapGen2.getLocalEnemies() < 30)
									MapGen2.genEnemies(30 - MapGen2.getLocalEnemies());
								MapGen2.moveTurn();
								if (enemy1 != null && !enemy1.isDead())
									throw new IOException();
								event.getChannel().editMessageById(mapMesID, AfekaLandsUI.mapMenu()).completeAfter(1,
										TimeUnit.SECONDS);
							} catch (FileNotFoundException c) {
								System.err.println("Tried deleting missing message.");
							} catch (IOException e) {
								MapGen2.setTackle(false);
								activeFight = true;
								enemy1 = Mods.chanceMod(enemy1);
								enemy1.createLoot();
								enemy1.writeEnemy();
								event.getChannel().deleteMessageById(mapMesID).queue();
								System.out.println("MAP CLOSED!");
								mapOpen = false;
								event.getChannel().sendMessage(AfekaLandsUI.fightMenu(enemy1)).queue();
							} catch (Exception e1) {
								System.out.println("Entered Shop!");
							}
							System.out.println("Local Enemies: " + MapGen2.allEnemies.size());

							if (MapGen2.currentTile().contains("$")) {
								activeShop = true;
								event.getChannel().deleteMessageById(mapMesID).queue();
								System.out.println("MAP CLOSED!");
								mapOpen = false;
								int[] shopos;
								String[] playerPos = MapGen2.getPOS().replace("Player POS: ", "").split(" ");
								for (Shop shop : shops) {
									shopos = shop.getPos();
									currentShop = shop;
									if (Integer.parseInt(playerPos[0]) == shopos[0]
											&& Integer.parseInt(playerPos[1]) == shopos[1]) {
										event.getChannel().sendMessage(AfekaLandsUI.ShopMenu(shop)).queue();
										break;
									}
								}
							}
						} else
							event.getChannel().sendMessage("Open the first map to move.").queue();
					} else if (activeFight)
						event.getChannel().sendMessage("Finish The Fight First! ⚔").queue();
					else if (activeShop)
						event.getChannel().sendMessage("Exit The Shop First! 💰").queue();
				}

			// SHOP-----------------------------------------------------------------------------------
			if (!deadPlayer) {
				if (MapGen.moves == 15) {
					MapGen.moves = 0;
					for (Shop shop : shops)
						shop.reStock();
					event.getChannel().sendMessage("🏘 Shops Restocked").queue();
				}
				if (activeShop) {
					if (messageRaw.equalsIgnoreCase("@exitShop") || messageRaw.equalsIgnoreCase("@exit")) {
						activeShop = false;
						event.getChannel().deleteMessageById(shopID).queue();
						event.getChannel().deleteMessageById(messageID).queue();
						String reply = null;
						switch (rand.nextInt(3)) {
						case 0:
							reply = currentShop.getOwnerName() + ": \"Until next time!\"";
							break;
						case 1:
							reply = currentShop.getOwnerName() + ": \"Bye Bye!\"";
							break;
						case 2:
							reply = currentShop.getOwnerName() + ": \"See you.\"";
							break;
						}
						event.getChannel().sendMessage(reply).queue();
					}

					if (messageRaw.equalsIgnoreCase("@selljunk")) {
						int profit = 0, counter = 0;
						event.getChannel().deleteMessageById(messageID).queue();
						for (Item item : player1.getInv().toArray(new Item[player1.getInv().size()]))
							if (item.getSort() == Sort.WEAPON) {
								counter++;
								profit += item.getPrice() / 5;
								Weapon.removeWeapon((Weapon) item);
								player1.takeOut((Weapon) item);
							} else if (item.getSort() == Sort.ARMOR) {
								counter++;
								profit += item.getPrice() / 5;
								Armor.removeArmor((Armor) item);
								player1.takeOut((Armor) item);
							}
						player1.putIn(Sort.COIN, profit);
						event.getChannel().editMessageById(shopID, AfekaLandsUI.ShopMenu(currentShop)).queue();
						if (counter != 1)
							event.getChannel().sendMessage("Sold " + counter + " Items for " + profit + " Coins.")
									.queue();
						else
							event.getChannel().sendMessage("Sold a single item for " + profit + " Coins.").queue();
					}

					if (messageRaw.contains("@buy ")) {
						String[] a;
						String item = messageRaw;
						int size = 1;
						if (messageRaw.contains(" #")) {
							a = messageRaw.split(" #");
							item = a[0];
							size = Integer.parseInt(a[1]);
						}
						event.getChannel().deleteMessageById(messageID).queue();
						item = item.replace("@buy ", "");
						ArrayList<Item> inv = currentShop.getShopINV();
						for (Item slot : inv)
							if (slot.getName().equalsIgnoreCase(item)) {
								if (player1.getCOINS() >= slot.getPrice() * size && slot.getSize() >= size)
									if (slot.getSort() == Sort.WEAPON) {
										Weapon wep = (Weapon) slot;
										player1.putIn(wep);
										player1.takeOut(Sort.COIN, slot.getPrice());
										currentShop.removeInv(slot.getName());
										event.getChannel().sendMessage("Successfully bought " + slot.getName() + ".")
												.queue();
										break;
									} else if (slot.getSort() == Sort.ARMOR) {
										Armor arm = (Armor) slot;
										player1.putIn(arm);
										player1.takeOut(Sort.COIN, slot.getPrice());
										currentShop.removeInv(slot.getName());
										event.getChannel().sendMessage("Successfully bought " + slot.getName() + ".")
												.queue();
										break;
									} else if (slot.getSort() == Sort.BEER) {
										player1.takeOut(Sort.COIN, slot.getPrice());
										currentShop.removeInv(slot.getSort());
										drunky++;
										if (drunky < 4) {
											try {
												if (drunky != 3) {
													event.getChannel().sendMessage("You chug a refreshing Beer! 🍺 (+"
															+ AfekaLandsUI.precent(20, player1.getMaxHP()) + " HP)")
															.queue();
													player1.updateStatus("HP",
															AfekaLandsUI.precent(20, player1.getMaxHP()));
												} else {
													event.getChannel().sendMessage(
															"You chug down a third Beer, your stomach feels funny... 🍺 (+"
																	+ AfekaLandsUI.precent(10, player1.getMaxHP())
																	+ " HP)")
															.queue();
													player1.updateStatus("HP",
															AfekaLandsUI.precent(10, player1.getMaxHP()));
												}
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										} else {
											drunky = 0;
											try {
												player1.updateStatus("HP", -25);
												event.getChannel().sendMessage(
														"https://tenor.com/view/kots-straal-spugen-vomit-puke-gif-14891152")
														.queue();
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											event.getChannel().sendMessage(
													"You barely chug down your Beer and after 2 gulps you puke all over the place. 🤮🤮🤮 (-25 HP)")
													.queue();
										}
									} else {
										player1.putIn(slot.getSort(), size);
										player1.takeOut(Sort.COIN, slot.getPrice() * size);
										currentShop.removeInv(slot.getSort(), size);
										if (size > 1)
											event.getChannel().sendMessage(
													"Successfully bought (" + size + ") " + slot.getName() + "'s.")
													.queue();
										else
											event.getChannel()
													.sendMessage("Successfully bought " + slot.getName() + ".").queue();
										break;
									}
								else {
									if (slot.getSize() < size)
										event.getChannel().sendMessage(currentShop.getOwnerName()
												+ ": \"We don't have enough in stock sorry.\" 🙇‍♂️").queue();
									else
										event.getChannel().sendMessage(currentShop.getOwnerName()
												+ ": \"You don't have enough COINS! are you trying to be funny?!\" 😠")
												.queue();
								}

								break;
							}
						event.getChannel().editMessageById(shopID, AfekaLandsUI.ShopMenu(currentShop)).queue();
					}
				}
			}

			// FIGHT--------------------------------------------------------------------------------

			if (!deadPlayer) {
				if (activeFight) {
					if (messageRaw.equalsIgnoreCase("@sword")) {
						if (status1 != null) {
							event.getChannel().deleteMessageById(status1).queue();
							status1 = null;
						}
						if (statusID != null) {
							event.getChannel().deleteMessageById(statusID).queue();
							statusID = null;
						}
						if (status2 != null) {
							event.getChannel().deleteMessageById(status2).queue();
							status2 = null;
						}

						event.getChannel().deleteMessageById(messageID).queue();
						event.getChannel().sendMessage(FightSystem.swordMove(player1, enemy1)).queue();
						event.getChannel().editMessageById(fightMesID, AfekaLandsUI.fightMenu(enemy1)).queue();
						if (enemy1.getHP() == 0)
							activeFight = false;
					} else if (messageRaw.equalsIgnoreCase("@fireball")) {
						if (status1 != null) {
							event.getChannel().deleteMessageById(status1).queue();
							status1 = null;
						}
						if (statusID != null) {
							event.getChannel().deleteMessageById(statusID).queue();
							statusID = null;
						}
						if (status2 != null) {
							event.getChannel().deleteMessageById(status2).queue();
							status2 = null;
						}

						if (player1.getENG() >= 20) {
							event.getChannel().deleteMessageById(messageID).queue();
							int Damage = player1.atk() * 2;
							try {
								player1.updateStatus("ENG", -20);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							event.getChannel().sendMessage("🔥 Used 20 energy to cast!\n" + enemy1.dealDamage(Damage))
									.queue();
							event.getChannel().editMessageById(fightMesID, AfekaLandsUI.fightMenu(enemy1)).queue();
							if (enemy1.getHP() <= 0)
								activeFight = false;
						} else
							event.getChannel().sendMessage("🔥 Not enough energy!").queue();
						;
					} else if (messageRaw.contains("@useHP")) {
						int size = 1;
						String[] a;
						if (messageRaw.contains("#")) {
							a = messageRaw.split("#");
							size = Integer.parseInt(a[1]);
						}
						if (status1 != null) {
							event.getChannel().deleteMessageById(status1).queue();
							status1 = null;
						}
						if (statusID != null) {
							event.getChannel().deleteMessageById(statusID).queue();
							statusID = null;
						}
						if (status2 != null) {
							event.getChannel().deleteMessageById(status2).queue();
							status2 = null;
						}
						if (player1.checkInv(Sort.HP, size)) {
							player1.takeOut(Sort.HP, size);
							try {
								player1.updateStatus("HP", 30 * size);
								event.getChannel().deleteMessageById(messageID).queue();
								event.getChannel().sendMessage("Used Health Potion\'s! (+" + 30 * size + " Health)")
										.queue();
								event.getChannel().editMessageById(fightMesID, AfekaLandsUI.fightMenu(enemy1)).queue();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							event.getChannel().deleteMessageById(messageID).queue();
							if (size > 1)
								event.getChannel().sendMessage("Not enough Health Potions in your INV!").queue();
							else
								event.getChannel().sendMessage("No Health Potions in your INV!").queue();
						}
					} else if (messageRaw.contains("@useEP")) {
						int size = 1;
						String[] a;
						if (messageRaw.contains("#")) {
							a = messageRaw.split("#");
							size = Integer.parseInt(a[1]);
						}
						if (status1 != null) {
							event.getChannel().deleteMessageById(status1).queue();
							status1 = null;
						}
						if (statusID != null) {
							event.getChannel().deleteMessageById(statusID).queue();
							statusID = null;
						}
						if (status2 != null) {
							event.getChannel().deleteMessageById(status2).queue();
							status2 = null;
						}
						if (player1.checkInv(Sort.EP, size)) {
							player1.takeOut(Sort.EP, size);
							try {
								player1.updateStatus("ENG", 40 * size);
								event.getChannel().deleteMessageById(messageID).queue();
								event.getChannel().sendMessage("Used Energy Potion\'s! (+" + 40 * size + " Energy)")
										.queue();
								event.getChannel().editMessageById(fightMesID, AfekaLandsUI.fightMenu(enemy1)).queue();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							event.getChannel().deleteMessageById(messageID).queue();
							if (size > 1)
								event.getChannel().sendMessage("Not enough Energy Potions in your INV!").queue();
							else
								event.getChannel().sendMessage("No Energy Potions in your INV!").queue();
						}
					}
					if (enemy1.getHP() == 0) {
						event.getChannel().sendMessage("EXP recieved: " + enemy1.getEXP()).queue();
						if (player1.addEXP(enemy1.getEXP()) != 0) {
							event.getChannel().sendMessage("LEVEL UP! (" + player1.getLevel() + ")").queue();
							event.getChannel().sendMessage("https://tenor.com/view/level-up-sparkle-gif-14592228")
									.queue();
						}
					}

					if (player1.getHP() == 0 && (messageRaw.charAt(0) == '!' || messageRaw.charAt(0) == '@'))
						event.getChannel().sendMessage("https://tenor.com/view/game-over-insert-coins-gif-12235828")
								.queue();
				} else if (messageRaw.equalsIgnoreCase("@loot")) {
					if (enemy1.isDead() && !enemy1.isLooted()) {
						StringBuilder sb = new StringBuilder();
						sb.append("Found:\n");
						for (Item item : enemy1.getLoot()) {
							if (item.getSort() != Sort.WEAPON && item.getSort() != Sort.ARMOR)
								player1.putIn(item.getSort(), item.getSize());
							else if (item.getSort() == Sort.ARMOR)
								player1.putIn((Armor) item);
							else
								player1.putIn((Weapon) item);
							sb.append("(" + item.getSize() + ")" + item.getName() + "\n");

							switch (item.getSort()) {
							case ARMOR:
								sb.deleteCharAt(sb.length() - 1);
								sb.append("[DEF:" + ((Armor) item).getDEF() + "]\n");
								break;
							case WEAPON:
								sb.deleteCharAt(sb.length() - 1);
								sb.append("[DMG:" + ((Weapon) item).getDamage() + "]\n");
								break;

							default:
								break;
							}
						}
						if (sb.toString().isEmpty())
							sb.append("Nothing.");
						else
							sb.deleteCharAt(sb.length() - 1);
						event.getChannel().sendMessage(sb.toString()).queue();
					} else
						event.getChannel().sendMessage("No one to loot.").queue();

					enemy1.setLooted(true);
					player1.updateStatus();
				}
			}
		} else {
			String[] cmds = { "openmap", "closemap", "sword", "move", "fireball", "loot" };
			for (String cmd : cmds)
				if (messageRaw.contains("@" + cmd))
					event.getChannel().sendMessage("Cannot do while " + player1.getName()
							+ " is Dead.\nPlease create a new character using @create: [name]").queue();
		}
	}

	public static String checkPlayer() {
		File file = new File("./Files/AfekaLands/Character.txt");
		String name = null, CREATOR = null;
		Scanner readFile;
		try {
			readFile = new Scanner(file);
			name = readFile.nextLine().replace("NAME: ", "");
			CREATOR = readFile.nextLine().replace("CREATOR: ", "");
			player1 = new Character(name, CREATOR);
			player1.setLevel(Integer.parseInt(readFile.nextLine().replace("LEVEL: ", "")));
			player1.setHP(Integer.parseInt(readFile.nextLine().replace("HP: ", "")));
			player1.setENG(Integer.parseInt(readFile.nextLine().replace("ENG: ", "")));
			player1.setEXP(Integer.parseInt(readFile.nextLine().replace("EXP: ", "")));
			for (String skill : readFile.nextLine().replace("SKILLS: ", "").split(", "))
				if (!skill.isEmpty() && skill != null)
					try {
						player1.addSkill(Skill.findSkillByName(skill));
					} catch (NullPointerException e) {
						e.printStackTrace();
						System.out.println("NO SUCH SKILL NAME FOUND!");
					}
			for (String item : readFile.nextLine().replace("EQP: ", "").split(", "))
				if (!item.isEmpty() && item != null) {
					if (item.contains("[w]")) {
						player1.setEQPw(item.replace("[w]", ""));
						player1.addDamage(player1.getWeapon().getDamage());
					} else if (item.contains("[a]"))
						player1.setEQPa(item.replace("[a]", ""));
				}
			for (String item : readFile.nextLine().replace("INV: ", "").split(", ")) {
				String[] two = new String[2];
				two = item.split("#");
				if (!item.isEmpty() && item != null)
					player1.putIn(two[0], Integer.parseInt(two[1]));
			}
			player1.updateStatus();
			AfekaLandsController.setHasPlayer(true);
			System.out.println("CURRENT PLAYER NAME: " + player1.getName());
			readFile.close();
		} catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR: Character.txt not found!");
			AfekaLandsController.setHasPlayer(false);
		} catch (NoSuchElementException e) {
			AfekaLandsController.setHasPlayer(false);
			return null;
		}
		return name;
	}

	public static void checkEnemy() {
		File file = new File("./Files/AfekaLands/Enemy/Enemy.txt");
		try {
			Scanner readFile = new Scanner(file);
//			if (readFile.nextLine() != null)
//				AfekaLandsController.setHasPlayer(true);
			readFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Character.txt not found!");
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			AfekaLandsController.setHasPlayer(false);
		}
	}

	public static boolean hasPlayer() {
		return has_Player;
	}

	public static boolean hasEnemy() {
		return has_Enemy;
	}

	public static void setHasPlayer(boolean bol) {
		has_Player = bol;
	}
}
