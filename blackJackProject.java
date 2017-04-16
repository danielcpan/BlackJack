import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

class Card {
	int value;
	String suit;
	String name;
}
public class blackJackProject {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int playerChips = 500;
		int dealerChips = 500;
		int wager = 0;
		int num_players = 2;
		boolean play = welcome();												

		while (playerChips > 0 && dealerChips > 0 && play == true) {								
			int chips[] = betChips(playerChips, dealerChips, wager);						//Handles how much the player wants to bet
			playerChips = chips[0];
			dealerChips = chips[1];
			wager = chips[2];
			Card[] deck = build_deck();														//Refreshes the deck after each game
			int scores[] = game(deck,num_players);
			if (scores[0] > scores[1]) {													//Handles money being given to the winner
				playerChips += wager * 2;													//Gives money to Player 1 if they win
				System.out.println("Player 1 Wins!");				
			}
			else if (scores[0] < scores[1]) {
				dealerChips += wager * 2;													//Gives money to Dealer if they win
				System.out.println("Dealer Wins!");							
			}
			else {
				playerChips += wager;
				dealerChips += wager;														//Returns money to both in case of a tie
				System.out.println("Tie!");									
			}
			lineSeparator();
		}	
	}

	public static int[] betChips(int playerChips, int dealerChips, int wager) {
		Scanner scan = new Scanner(System.in);
		int[] chips = new int[3];
		chips[0] = playerChips;
		chips[1] = dealerChips;
		chips[2] = wager;

		while (playerChips > 0 && dealerChips > 0) {
			System.out.printf("You have %d chips left\n", playerChips);
			System.out.printf("Dealer has %d chips left\n", dealerChips);
			System.out.println("How much do you want to bet?");
			wager = scan.nextInt(); 
			lineSeparator();
			if (wager > dealerChips && wager > playerChips) {
			System.out.println("Both Player 1 and Dealer have insufficient chips");
			}
			else if (wager > playerChips) {
				System.out.println("Player 1 has insufficient chips");
			}
			else if (wager > dealerChips) {
				System.out.println("Dealer has insufficent chips");
			}
			else if (wager == 0) {
				System.out.println("Cannot bet 0 chips");
			}
			else if (wager > 0 && wager <= playerChips && wager <= dealerChips) {			//If all conditions are met, 
				chips[0] = playerChips - wager;												//the wage is set and both Player 1
				chips[1] = dealerChips - wager;												//and Dealer set aside their chips betted
				chips[2] = wager;
				playerChips = chips[0];
				dealerChips = chips[1];
				System.out.printf("The dealer matches your bet of %d chips\n", wager);
				System.out.printf("You have %d chips left\n", playerChips);
				System.out.printf("Dealer has %d chips left\n", dealerChips);
				lineSeparator();
				break;
			}
			else {
				System.out.println("Please enter a valid input");
			}
			lineSeparator();
		}
		return chips;
	}

	public static int[] game(Card[] deck, int num_players) {
		Scanner scan = new Scanner(System.in);
		int[] scores = new int[num_players];
		ArrayList<Integer> playerdrawvalue = new ArrayList<Integer>();
		ArrayList<String> playerdrawname = new ArrayList<String>();
		int[] dealerstartvalue = new int[2];
		String[] dealerstartname = new String[2];
		boolean again = true;

		// Generates two cards for each player
		for (int i = 0; i < num_players; i++) {
			int sum = 0;
			for (int j = 1; j <= 2; j++) {
				int card_drawn = pick_card(deck);
				if (i == 0) {
					System.out.println("Player 1 drew a " + deck[card_drawn].name + " of " + deck[card_drawn].suit);
					playerdrawvalue.add(deck[card_drawn].value);
					playerdrawname.add(deck[card_drawn].name);											//First two cards for the Player
					sum += deck[card_drawn].value;														// and gets the sum of those two cards
					deck = remove_card(deck, card_drawn);
				}
				else {
					System.out.println("Dealer drew a " + deck[card_drawn].name + " of " + deck[card_drawn].suit);
					dealerstartvalue[j-1] = deck[card_drawn].value;
					dealerstartname[j-1] = deck[card_drawn].name;										//First two cards for the Dealer
					sum+=deck[card_drawn].value;														//and gets the sum of those two cards.
					deck = remove_card(deck, card_drawn);
				}
			}
			lineSeparator();

			//Ace Optimization: Checks to see if Ace needs to be converted into a 1
			if(i % 2 != 0) {
				for (int k = 0; k < 2; k++) {
					if(dealerstartname[k].equals("ace") && sum != 21) {									//Change aces equal to one if 
						dealerstartvalue[k] = 1;														//no card value of 10 is present in the first
					}																					//two cards
				}
				sum = dealerstartvalue[0] + dealerstartvalue[1];
			}
			else {
				if (playerdrawname.get(0).equals("ace") && playerdrawname.get(1).equals("ace")) {		//If two aces are drawn on the first
					playerdrawvalue.set(1,1);															//two, set only one equal to 1
				}
				sum = playerdrawvalue.get(0) + playerdrawvalue.get(1);
			}									
			scores[i] = sum;				
		}
		//Game after the first two cards are dealt to each player
		for (int a = 0; a < num_players; a++) {
			if (a == 0) {
				if (scores[a] == 21) {
				System.out.println("Player 1 got blackjack!");
				lineSeparator();
				continue;
				}
				//Ace Optimization: Checks to see if Ace needs to be converted into a 1
				if (scores[a] > 21) {
					for (int b = 0; b < playerdrawvalue.size(); b++) {
						if (playerdrawname.get(b).equals("ace")) {	
							playerdrawvalue.set(b,1);
							System.out.println("Your ace has changed to a 1");
						}
					}
					scores[a] = 0;
					for (int c = 0; c < playerdrawvalue.size(); c++) {
						scores[a] = scores[a] + playerdrawvalue.get(c);
					}
				}
				if (scores[a] > 21) {
					scores[a] = 0;
					System.out.println("Player 1 busted.");
					lineSeparator();
					continue;
				}
				while (again == true) {
					System.out.println("Player 1 has a total of " + scores[a]);
					System.out.println("Player 1 would you like to draw another card? (y/n)");
					String hit = scan.next();
					lineSeparator();
					if (hit.equals("y")) {
						int card_drawn = pick_card(deck);
						System.out.println("Player 1 drew a " + deck[card_drawn].name + " of " + deck[card_drawn].suit);
						playerdrawvalue.add(deck[card_drawn].value);
						playerdrawname.add(deck[card_drawn].name);		
						scores[a] += deck[card_drawn].value;
						if (deck[card_drawn].name.equals("ace") && scores[a] > 21) {
							playerdrawvalue.remove(playerdrawvalue.size() - 1);						//Changes ace equal to 1 if 
							playerdrawvalue.add(1);													//the ace drawn causes player go over 21
							scores[a] = scores[a] - 10;
							break;
						}
						deck = remove_card(deck, card_drawn);
						a--;
						break;
					}
					else if (hit.equals("n")) {
						again = false;
						System.out.println("Turn over.");
						continue;
					}
					else {
						System.out.println("Please enter \"y\" for yes and \"n\" for no");
					}
				}
			}
			else {
				System.out.println("Dealer has a total of " + scores[a]);
				if (scores[a] == 21) {
					System.out.println("Dealer got blackjack!");
					lineSeparator();
					continue;
				}
				else if (scores[a] > 21) {
					System.out.println("Dealer busted.");
					lineSeparator();
					scores[a] = 0;	
					continue;
				}
				else if (scores[a] > 17) {															//If dealer gets a sum of over 17, he stops.
					continue;
				}
				else if (scores[a] <= 17) {
					int card_drawn = pick_card(deck);
					System.out.println("Dealer drew a " + deck[card_drawn].name + " of " + deck[card_drawn].suit);
					scores[a]+=deck[card_drawn].value;												//Dealer keeps drawing if his total is under
					deck = remove_card(deck, card_drawn);											//or equal to 17.
					a--;
				}	
			}
		}
		return scores;
	}

	public static Card[] build_deck() {
		//build your deck of 52 cards
		//4 suits, 13 cards each suit
		//Assign each card a value
		String[] suits = {"clubs","diamonds","hearts","spades"};
		String[] names = {"zero","one","two","three","four","five",
							"six","seven","eight","nine","ten","jack",
							"queen","king","ace"};
		int i = 0;
		Card[] deck = new Card[52];
		for (String s:suits) {
			for (int j = 2; j <= 14; j++) {
				Card c = new Card();
				c.suit = s;
				c.name = names[j];
				if (j == 14)
					c.value = 11;
				else if ((j > 10) && (j < 14))
					c.value = 10;
				else 
					c.value = j;
				deck[i] = c;
				i++;
			}
		}
		return deck;
	}

	public static int pick_card(Card[] deck) {
		int card_number = (int) (Math.random() * deck.length);
		return card_number;
	}

	public static Card[] remove_card(Card[] deck, int remove_index) {
		int n = deck.length;
		Card[] new_deck = new Card[n-1];
		int j = 0;
		for (int i = 0; i < n; i++) {
			Card c = new Card();
			if (i != remove_index){
				c.value = deck[i].value;
				c.suit = deck[i].suit;
				c.name = deck[i].name;
				new_deck[j] = c;
				j++;
			}
		}
		return new_deck;
	}

	public static boolean welcome() {
		Scanner scan = new Scanner(System.in);
		boolean play = false;
		System.out.println("───█▒▒░░░░░░░░░▒▒█───");
		System.out.println("────█░░█░░░░░█░░█────");
		System.out.println("─▄▄──█░░░▀█▀░░░█──▄▄─");
		System.out.println("█░░█─▀▄░░░░░░░▄▀─█░░█");
		System.out.println("█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█");
		System.out.println("█░░╦─╦╔╗╦─╔╗╔╗╔╦╗╔╗░░█");
		System.out.println("█░░║║║╠─║─║─║║║║║╠─░░█");
		System.out.println("█░░╚╩╝╚╝╚╝╚╝╚╝╩─╩╚╝░░█\n");
		while(play == false) {
			System.out.println("Would you like to play a game of BlackJack? (y/n)");
			String userResponse = scan.next();
			if (userResponse.equals("y")) {
				play = true;
			}
			else if (userResponse.equals("n")) {
				System.out.println("Oh wow... Lame. Bye!");
				break;
			}
			else {
				System.out.println("Please enter \"y\" for yes and \"n\" for no");
			}
			lineSeparator();
		}
			return play;
	}
	public static void lineSeparator() {
		System.out.println("---------------------------------------------------");
	}
}