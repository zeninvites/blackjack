/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Drives a full session of Blackjack: setup, betting, dealing, player turn,
 * dealer turn, and result/scoring, looping until the player quits or runs
 * out of chips. Implements UC1 through UC6.
 * @author Michael Turay
 */
public class BlackjackGame extends Game
{
    private GroupOfCards deck;
    private int score; //running count of rounds won by the player
    private int roundsPlayed; //running count of total rounds completed

    private BlackjackPlayer player;
    private Dealer dealer;
    private Scanner input;

    public BlackjackGame()
    {
        super("Blackjack");
        deck = new GroupOfCards(52);
        score = 0;
        roundsPlayed = 0;
        input = new Scanner(System.in);
    }

    /**
     * Sets up a new session: prompts for a player name (UC1), builds and
     * shuffles the deck, then runs rounds until the player quits or is out
     * of chips (UC2-UC6).
     */
    @Override
    public void play()
    {
        System.out.println("=== Welcome to Blackjack ===");

        //UC1 - Start New Game
        String name = "";
        while (name.isBlank())
        {
            System.out.print("Enter your player name: ");
            name = input.nextLine().trim();
            if (name.isBlank())
            {
                System.out.println("Name cannot be blank.");
            }
        }

        player = new BlackjackPlayer(name, 100); //starting wallet balance of 100 chips
        dealer = new Dealer();

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(dealer);
        setPlayers(players);

        deck.buildStandardDeck();
        deck.shuffle();

        boolean continuePlaying = true;
        while (continuePlaying)
        {
            continuePlaying = playRound();
        }

        declareWinner();
    }

    /**
     * Plays a single round: bet, deal, player turn, dealer turn, result (UC2-UC5).
     * @return true if the player chose (and is able) to play another round, false otherwise
     */
    private boolean playRound()
    {
        //UC2 - Place Bet
        if (!promptBet())
        {
            return false; //out of chips, session over
        }

        //UC3 - Deal Initial Hand
        dealInitialHand();
        roundsPlayed++;

        boolean playerBust = false;

        //natural Blackjack check (UC3 - A1)
        if (player.getHand().getHandValue() == 21)
        {
            System.out.println("Blackjack! " + player.getPlayerID() + " wins immediately.");
            resolveRound(false, true);
        }
        else
        {
            //UC4 - Hit loop
            while (player.getHand().getHandValue() < 21 && player.promptHitOrStand(input))
            {
                Card drawn = deck.drawCard();
                player.getHand().addCard(drawn);
                System.out.println("You drew: " + drawn);

                if (player.getHand().getHandValue() > 21)
                {
                    playerBust = true;
                    System.out.println("Bust! Hand value: " + player.getHand().getHandValue());
                    break;
                }
            }

            if (playerBust)
            {
                //UC4 - A1: bust, wager already forfeited (no wallet credit)
            }
            else
            {
                //UC5 - Stand: dealer's turn and result
                playDealerTurn();
                resolveRound(false, false);
            }
        }

        //UC6 - Play Another Round
        return promptPlayAgain();
    }

    /**
     * UC2 - Place Bet: displays balance, validates and deducts a bet.
     * @return true if a bet was placed successfully, false if the player has no chips left
     */
    private boolean promptBet()
    {
        if (player.getWallet().getBalance() <= 0)
        {
            System.out.println("You are out of chips. Final score: " + score + " of " + roundsPlayed + " round(s) played.");
            return false;
        }

        while (true)
        {
            System.out.println("Current balance: " + player.getWallet().getBalance() + " chips");
            System.out.print("Enter your bet: ");
            String line = input.nextLine().trim();

            try
            {
                int amount = Integer.parseInt(line);
                if (amount <= 0)
                {
                    System.out.println("Bet must be a positive whole number.");
                }
                else if (amount > player.getWallet().getBalance())
                {
                    System.out.println("Bet exceeds your balance. Maximum bet is "
                            + player.getWallet().getBalance() + " chips.");
                }
                else
                {
                    player.placeBet(amount);
                    return true;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    /**
     * UC3 - Deal Initial Hand: deals two cards each to player and dealer,
     * reshuffling a fresh deck first if fewer than 4 cards remain.
     */
    public void dealInitialHand()
    {
        if (deck.showCards().size() < 4)
        {
            deck.buildStandardDeck();
            deck.shuffle();
        }

        player.getHand().clear();
        dealer.getHand().clear();

        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());

        System.out.println("Your hand: " + player.getHand().showCards()
                + " (value: " + player.getHand().getHandValue() + ")");
        System.out.println("Dealer shows: " + dealer.getHand().showCards().get(0));
    }

    /**
     * UC5 - Stand: reveals the dealer's hand and draws while below 17.
     */
    private void playDealerTurn()
    {
        System.out.println("Dealer's hand: " + dealer.getHand().showCards()
                + " (value: " + dealer.getHand().getHandValue() + ")");

        while (dealer.shouldHit())
        {
            Card drawn = deck.drawCard();
            dealer.getHand().addCard(drawn);
            System.out.println("Dealer draws: " + drawn + " (value: "
                    + dealer.getHand().getHandValue() + ")");
        }
    }

    /**
     * Compares hands (or applies a bust/blackjack override) and updates the
     * wallet and running score accordingly (UC5, including A1-A3).
     * @param dealerBust true if the dealer has already been determined to have busted
     * @param playerBlackjack true if the player won via a natural Blackjack
     */
    private void resolveRound(boolean dealerBust, boolean playerBlackjack)
    {
        int playerValue = player.getHand().getHandValue();
        int dealerValue = dealer.getHand().getHandValue();
        int bet = player.getCurrentBet();

        if (playerBlackjack || dealerValue > 21 || playerValue > dealerValue)
        {
            //player win: original wager returned plus winnings (UC5 basic flow, step 7 / A1)
            player.getWallet().deposit(bet * 2);
            score++;
            System.out.println(player.getPlayerID() + " wins the round!");
        }
        else if (playerValue == dealerValue)
        {
            //push: original bet returned only (UC5 - A2)
            player.getWallet().deposit(bet);
            System.out.println("Push - bet returned.");
        }
        else
        {
            //dealer wins: bet already deducted, no further change (UC5 - A3)
            System.out.println("Dealer wins the round.");
        }

        System.out.println("Score: " + score + " win" + (score == 1 ? "" : "s") + " / "
                + roundsPlayed + " round" + (roundsPlayed == 1 ? "" : "s") + " played"
                + " | Wallet: " + player.getWallet().getBalance() + " chips");
    }

    /**
     * UC6 - Play Another Round: asks the player whether to continue.
     * Accepts either the single letter shortcut ("y"/"n") or the full word
     * ("Yes"/"No"), case-insensitive.
     * @return true if the player chose to continue and has chips remaining, false otherwise
     */
    private boolean promptPlayAgain()
    {
        if (player.getWallet().getBalance() <= 0)
        {
            System.out.println("You are out of chips. Final score: " + score + " of " + roundsPlayed + " round(s) played.");
            return false;
        }

        while (true)
        {
            System.out.print("Play another round? (Y/N): ");
            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("y") || choice.equals("yes"))
            {
                return true;
            }
            else if (choice.equals("n") || choice.equals("no"))
            {
                return false;
            }
            else
            {
                System.out.println("Please enter Y or N.");
            }
        }
    }

    @Override
    public void declareWinner()
    {
        System.out.println("=== Session Over ===");
        System.out.println(player.getPlayerID() + " won " + score + " of " + roundsPlayed + " round(s) played.");
        System.out.println("Final wallet balance: " + player.getWallet().getBalance() + " chips");
    }

}//end class