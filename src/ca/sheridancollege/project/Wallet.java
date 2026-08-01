/**
 * SYST 17796 Project - Deliverable 3
 * Created by Michael Turay, 2026
 */
package ca.sheridancollege.project;

/**
 * Tracks a player's chip balance. Enforces that the balance can never go
 * negative, since a bet can only ever deduct up to the current balance.
 * @author Michael Turay
 */
public class Wallet 
{
    private int balance;

    /**
     * @param startingBalance the chip balance to start the session with
     */
    public Wallet(int startingBalance)
    {
        balance = startingBalance;
    }

    /**
     * Adds chips to the balance (e.g. round winnings or a returned bet).
     * @param amount the amount to add
     */
    public void deposit(int amount)
    {
        balance += amount;
    }

    /**
     * Removes chips from the balance (e.g. placing a bet).
     * @param amount the amount to deduct; ignored if it would overdraw the balance
     */
    public void deduct(int amount)
    {
        if (amount <= balance)
        {
            balance -= amount;
        }
    }

    /**
     * @return the current chip balance
     */
    public int getBalance()
    {
        return balance;
    }

}//end class
