package model;

public class Booking
{
    private final String name;
    private final String passportNum;
    private final Ticket ticket;

    public Booking(String name, String passportNum, Ticket ticket)
    {
        this.name = name;
        this.passportNum = passportNum;
        this.ticket = ticket;
    }

    public String getName()
    {
        return name;
    }

    public String getPassportNum()
    {
        return passportNum;
    }

    public Ticket getTicket()
    {
        return ticket;
    }
}