package model;

public class Passenger extends Person {
    private final String passportNumber; // 新增：護照號碼
    private final Baggage baggage;
    private final Ticket ticket; // 加入 final

    public Passenger(String name, String idNumber, String passportNumber, Baggage baggage, Ticket ticket) {
        super(name, idNumber);
        this.passportNumber = passportNumber;
        this.baggage = baggage;
        this.ticket = ticket;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public Ticket getTicket() {
        return ticket;
    }
}