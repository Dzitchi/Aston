package Third.strategy;

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Оплачено " + amount + " через Credit Card.");
    }
}
