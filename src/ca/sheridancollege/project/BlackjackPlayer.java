/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

import java.util.Scanner;

/**
 * The human player in a game of Blackjack. Holds a hand and a wallet, and
 * makes Hit/Stand decisions based on console input (UC4, UC5).
 * @author Michael Turay
 */
public class BlackjackPlayer extends Player
{
    private GroupOfCards hand;
    private Wallet wallet;
    private int currentBet;

    /**
     * @param name the player's chosen name
     * @param startingBalance the chip balance to start the session with
     */
    public BlackjackPlayer(String name, int startingBalance)
    {
        super(name);
        hand = new GroupOfCards(0); //a hand grows dynamically, no fixed max needed
        wallet = new Wallet(startingBalance);
        currentBet = 0;
    }

    /**
     * @return this player's current hand
     */
    public GroupOfCards getHand()
    {
        return hand;
    }

    /**
     * @return this player's wallet
     */
    public Wallet getWallet()
    {
        return wallet;
    }

    /**
     * @return the amount currently wagered for this round
     */
    public int getCurrentBet()
    {
        return currentBet;
    }

    /**
     * Deducts a validated bet amount from the wallet and holds it for the round (UC2).
     * @param amount the amount to wager; must be a positive whole number not
     *               exceeding the current balance, otherwise no action is taken
     */
    public void placeBet(int amount)
    {
        if (amount > 0 && amount <= wallet.getBalance())
        {
            wallet.deduct(amount);
            currentBet = amount;
        }
    }

    /**
     * Draws one card into this player's hand (UC4 - Hit).
     */
    public void hit()
    {
        //the caller (BlackjackGame) is responsible for drawing from the shared deck
        //and passing the card in via getHand().addCard(card)
    }

    /**
     * Ends the player's turn. Turn-ending itself requires no state change here;
     * BlackjackGame reads the current hand value once Stand is chosen (UC5).
     */
    public void stand()
    {
        //no state change needed - BlackjackGame proceeds to the dealer's turn
    }

    /**
     * Prompts the player at the console for Hit or Stand (UC4/UC5), with
     * basic input validation. Accepts either the single letter shortcut
     * ("h"/"s") or the full word ("Hit"/"Stand"), case-insensitive.
     * @param input the Scanner reading console input
     * @return true if the player chose to Hit, false if they chose to Stand
     */
    public boolean promptHitOrStand(Scanner input)
    {
        while (true)
        {
            System.out.print("Hand: " + hand.showCards() + " (value: " + hand.getHandValue()
                    + "). Hit/Stand [H/S]? ");
            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("h") || choice.equals("hit"))
            {
                return true;
            }
            else if (choice.equals("s") || choice.equals("stand"))
            {
                return false;
            }
            else
            {
                System.out.println("Invalid input. Please type H for Hit or S for Stand.");
            }
        }
    }

    @Override
    public void play()
    {
        //turn logic is driven interactively by BlackjackGame calling
        //promptHitOrStand() in a loop, since the outcome depends on live
        //console input rather than a single self-contained decision
    }

}//end class