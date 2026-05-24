package Third.strategy;

public class MainStrategy {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout(1500);

        cart.setPaymentStrategy(new PayPalPayment());
        cart.checkout(800);
    }
}
