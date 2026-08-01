/**
 * SYST 17796 Project Winter 2019 Base code.
 * Modified/extended by Michael Turay, 2026
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A concrete class that represents any grouping of cards for a Game.
 * Reused for the deck, the player's hand, and the dealer's hand by varying
 * the givenSize constructor parameter.
 * @author dancye, extended by Michael Turay
 */
public class GroupOfCards 
{
   
    //The group of cards, stored in an ArrayList
    private ArrayList <Card> cards;
    private int size;//the size of the grouping
    
    public GroupOfCards(int givenSize)
    {
        size = givenSize;
        cards = new ArrayList<>(); //initialize so this can be used immediately, empty or full
    }
    
    /**
     * A method that will get the group of cards as an ArrayList
     * @return the group of cards.
     */
    public ArrayList<Card> showCards()
    {
        return cards;
    }
    
    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    /**
     * @return the size of the group of cards
     */
    public int getSize() {
        return size;
    }

    /**
     * @param givenSize the max size for the group of cards
     */
    public void setSize(int givenSize) {
        size = givenSize;
    }

    /**
     * Adds a single card to this group (e.g. dealing a card into a hand).
     * @param card the card to add
     */
    public void addCard(Card card)
    {
        cards.add(card);
    }

    /**
     * Removes and returns the top card of this group (e.g. drawing from the deck).
     * @return the card drawn, or null if the group is empty
     */
    public Card drawCard()
    {
        if (cards.isEmpty())
        {
            return null;
        }
        return cards.remove(0);
    }

    /**
     * Removes all cards from this group. Used to clear a hand between rounds.
     */
    public void clear()
    {
        cards.clear();
    }

    /**
     * Calculates the Blackjack value of this group of cards, treating this
     * group as a hand. Aces default to a value of 11 (set in PlayingCard) but
     * are downgraded to 1, one at a time, for as long as the hand total
     * exceeds 21 and an Ace counted as 11 remains.
     * @return the best possible Blackjack hand value for this group of cards
     */
    public int getHandValue()
    {
        int total = 0;
        int acesCountedHigh = 0;

        for (Card card : cards)
        {
            total += card.getValue();
            if (card instanceof PlayingCard && ((PlayingCard) card).getRank().equals("Ace"))
            {
                acesCountedHigh++;
            }
        }

        while (total > 21 && acesCountedHigh > 0)
        {
            total -= 10; //convert one Ace from 11 down to 1
            acesCountedHigh--;
        }

        return total;
    }

    /**
     * Populates this group of cards with a standard 52-card deck
     * (13 ranks x 4 suits). Intended to be called on the deck instance only.
     */
    public void buildStandardDeck()
    {
        cards.clear();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
                           "Jack", "Queen", "King", "Ace"};

        for (String suit : suits)
        {
            for (String rank : ranks)
            {
                int value = getStandardValue(rank);
                cards.add(new PlayingCard(suit, rank, value));
            }
        }
    }

    /**
     * Maps a rank string to its default Blackjack value.
     * @param rank the rank (e.g. "King", "7", "Ace")
     * @return the point value: 10 for face cards, 11 for Ace (high, adjusted
     *         later by getHandValue()), or the numeric face value otherwise
     */
    private int getStandardValue(String rank)
    {
        switch (rank)
        {
            case "Jack":
            case "Queen":
            case "King":
                return 10;
            case "Ace":
                return 11;
            default:
                return Integer.parseInt(rank);
        }
    }

}//end class
