/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for hand-value calculation (including Ace handling), deck
 * building, wallet balance rules, and dealer hit/stand logic.
 * @author Michael Turay
 */
public class BlackjackLogicTest
{
    // ---------- GroupOfCards.getHandValue() ----------

    @Test
    public void testHandValueNoAces()
    {
        GroupOfCards hand = new GroupOfCards(0);
        hand.addCard(new PlayingCard("Hearts", "10", 10));
        hand.addCard(new PlayingCard("Spades", "7", 7));

        assertEquals(17, hand.getHandValue());
    }

    @Test
    public void testHandValueEmptyHand()
    {
        GroupOfCards hand = new GroupOfCards(0);

        assertEquals(0, hand.getHandValue());
    }

    @Test
    public void testAceCountedHighWhenSafe()
    {
        GroupOfCards hand = new GroupOfCards(0);
        hand.addCard(new PlayingCard("Hearts", "Ace", 11));
        hand.addCard(new PlayingCard("Spades", "9", 9));

        // Ace + 9 = 20, no need to downgrade the Ace
        assertEquals(20, hand.getHandValue());
    }

    @Test
    public void testAceCountedLowToAvoidBust()
    {
        GroupOfCards hand = new GroupOfCards(0);
        hand.addCard(new PlayingCard("Hearts", "Ace", 11));
        hand.addCard(new PlayingCard("Spades", "King", 10));
        hand.addCard(new PlayingCard("Clubs", "5", 5));

        // 11 + 10 + 5 = 26 if Ace stays high -> must downgrade to 1 -> 16
        assertEquals(16, hand.getHandValue());
    }

    @Test
    public void testTwoAcesResolveCorrectly()
    {
        GroupOfCards hand = new GroupOfCards(0);
        hand.addCard(new PlayingCard("Hearts", "Ace", 11));
        hand.addCard(new PlayingCard("Spades", "Ace", 11));
        hand.addCard(new PlayingCard("Clubs", "9", 9));

        // 11 + 11 + 9 = 31 -> downgrade one Ace -> 21 -> still fine, no second downgrade needed
        assertEquals(21, hand.getHandValue());
    }

    @Test
    public void testNaturalBlackjackValue()
    {
        GroupOfCards hand = new GroupOfCards(0);
        hand.addCard(new PlayingCard("Hearts", "Ace", 11));
        hand.addCard(new PlayingCard("Spades", "Queen", 10));

        assertEquals(21, hand.getHandValue());
    }

    // ---------- GroupOfCards.buildStandardDeck() ----------

    @Test
    public void testDeckHasFiftyTwoCards()
    {
        GroupOfCards deck = new GroupOfCards(52);
        deck.buildStandardDeck();

        assertEquals(52, deck.showCards().size());
    }

    @Test
    public void testDeckHasNoDuplicateCards()
    {
        GroupOfCards deck = new GroupOfCards(52);
        deck.buildStandardDeck();

        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Card card : deck.showCards())
        {
            String description = card.toString();
            assertFalse("Duplicate card found: " + description, seen.contains(description));
            seen.add(description);
        }
    }

    @Test
    public void testDrawCardRemovesFromDeck()
    {
        GroupOfCards deck = new GroupOfCards(52);
        deck.buildStandardDeck();
        int sizeBefore = deck.showCards().size();

        Card drawn = deck.drawCard();

        assertNotNull(drawn);
        assertEquals(sizeBefore - 1, deck.showCards().size());
    }

    // ---------- Wallet ----------

    @Test
    public void testWalletDeductWithinBalance()
    {
        Wallet wallet = new Wallet(100);
        wallet.deduct(40);

        assertEquals(60, wallet.getBalance());
    }

    @Test
    public void testWalletCannotOverdraw()
    {
        Wallet wallet = new Wallet(50);
        wallet.deduct(1000); // should be ignored, not go negative

        assertEquals(50, wallet.getBalance());
    }

    @Test
    public void testWalletDeposit()
    {
        Wallet wallet = new Wallet(50);
        wallet.deposit(25);

        assertEquals(75, wallet.getBalance());
    }

    // ---------- Dealer.shouldHit() ----------

    @Test
    public void testDealerHitsBelowSeventeen()
    {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard(new PlayingCard("Hearts", "10", 10));
        dealer.getHand().addCard(new PlayingCard("Spades", "6", 6));

        assertTrue(dealer.shouldHit()); // 16, must hit
    }

    @Test
    public void testDealerStandsAtSeventeen()
    {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard(new PlayingCard("Hearts", "10", 10));
        dealer.getHand().addCard(new PlayingCard("Spades", "7", 7));

        assertFalse(dealer.shouldHit()); // exactly 17, must stand
    }

    @Test
    public void testDealerStandsAboveSeventeen()
    {
        Dealer dealer = new Dealer();
        dealer.getHand().addCard(new PlayingCard("Hearts", "King", 10));
        dealer.getHand().addCard(new PlayingCard("Spades", "9", 9));

        assertFalse(dealer.shouldHit()); // 19, must stand
    }

}//end class