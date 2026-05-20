package Third.strategy;

class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Оплачено " + amount + " через PayPal.");
    }
}
