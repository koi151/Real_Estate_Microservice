package customExceptions;

public class PaymentScheduleNotFoundException extends RuntimeException {
    public PaymentScheduleNotFoundException(String message) {
        super(message);
    }

}
