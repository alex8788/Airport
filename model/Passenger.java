package model;

public class Passenger
{
    private final String name;  // 姓名
    private final Passport passport;  // 護照
    private final Baggage baggage;  // 行李
    private final Ticket ticket;  // 機票

    // 建構子：包含姓名、護照、行李與機票
    public Passenger(String name, Passport passport, Baggage baggage, Ticket ticket)
    {
        this.name = name;
        this.passport = passport;
        this.baggage = baggage;
        this.ticket = ticket;
    }

    public String getName()
    {
        return name;
    }
    
    public Passport getPassport()
    {
        return passport;
    }

    public boolean hasPassport()
    {
        return passport != null;
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