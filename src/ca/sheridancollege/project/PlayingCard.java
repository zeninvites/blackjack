/**
 * SYST 17796 Project - Deliverable 3
 * Modified/extended by Michael Turay, 2026
 */
package ca.sheridancollege.project;

/**
 * A concrete Card representing a single standard playing card, with a suit,
 * a rank, and a Blackjack point value.
 * @author Michael Turay
 */
public class PlayingCard extends Card
{
    private String suit; //Hearts, Diamonds, Clubs, Spades
    private String rank; //2-10, Jack, Queen, King, Ace

    /**
     * @param suit the suit of the card (e.g. "Hearts")
     * @param rank the rank of the card (e.g. "Ace")
     * @param value the Blackjack point value of the card (Ace defaults to 11;
     *              hand-level logic in GroupOfCards adjusts Aces down to 1 as needed)
     */
    public PlayingCard(String suit, String rank, int value)
    {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    /**
     * @return the suit
     */
    public String getSuit()
    {
        return suit;
    }

    /**
     * @return the rank
     */
    public String getRank()
    {
        return rank;
    }

    @Override
    public int getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }

}//end class
