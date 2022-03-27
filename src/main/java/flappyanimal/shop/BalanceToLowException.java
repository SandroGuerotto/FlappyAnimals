package flappyanimal.shop;

/**
 * Balance to low to buy a character
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class BalanceToLowException extends Exception {

    public BalanceToLowException(String msg) {
        super(msg);
    }
}
