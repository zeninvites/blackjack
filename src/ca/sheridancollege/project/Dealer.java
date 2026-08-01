/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

/**
 * The automated dealer. Follows a fixed rule: hits while the hand value is
 * below 17, stands at 17 or above (UC5).
 * @author Michael Turay
 */
public class Dealer extends Player
{
    private GroupOfCards hand;

    public Dealer()
    {
        super("Dealer");
        hand = new GroupOfCards(0);
    }

    /**
     * @return the dealer's current hand
     */
    public GroupOfCards getHand()
    {
        return hand;
    }

    /**
     * @return true if the dealer's hand value is below 17 and the dealer
     *         must draw another card, per fixed house rules
     */
    public boolean shouldHit()
    {
        return hand.getHandValue() < 17;
    }

    @Override
    public void play()
    {
        //the dealer's turn is driven by BlackjackGame calling shouldHit() in a
        //loop and drawing from the shared deck, since drawing requires access
        //to the deck instance that only BlackjackGame holds
    }

}//end class
