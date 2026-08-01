/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

/**
 * Application entry point. Creates and starts a game of Blackjack.
 * @author Michael Turay
 */
public class Main
{
    public static void main(String[] args)
    {
        BlackjackGame game = new BlackjackGame();
        game.play();
    }
}
