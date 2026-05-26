package model;

public class Passenger
{
    private final String name;  // 姓名
    private final String passportNumber; // 護照號碼
    private final Baggage baggage;  // 行李
    private final Ticket ticket;  // 機票

    // 建構子：包含姓名、護照與行李與票
    public Passenger(String name, String passportNumber, Baggage baggage, Ticket ticket)
    {
        this.name = name;
        this.passportNumber = passportNumber;
        this.baggage = baggage;
        this.ticket = ticket;
    }

    public String getName()
    {
        return name;
    }

    public String getPassportNumber()
    {
        return passportNumber;
    }

    public Baggage getBaggage()
    {
        return baggage;
    }

    public Ticket getTicket()
    {
        return ticket;
    }
}