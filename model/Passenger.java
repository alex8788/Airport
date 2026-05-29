package model;

public class Passenger
{
    private final String name; // 姓名
    private final Passport passport; // 護照
    private Baggage baggage; // 行李
    private BoardingPass boardingPass; // 登機證

    // 建構子：包含姓名、護照、行李與機票
    public Passenger(String name, Passport passport)
    {
        this.name = name;
        this.passport = passport;
        this.baggage = null;
        this.boardingPass = null;
    }

    public void setBaggage(Baggage baggage)
    {
        this.baggage = baggage;
    }

    public void setBoardingPass(BoardingPass boardingPass)
    {
        this.boardingPass = boardingPass;
    }

    public String getName()
    {
        return name;
    }

    public Passport getPassport()
    {
        return passport;
    }

    public boolean hasBaggage()
    {
        return baggage != null;
    }

    public Baggage getBaggage()
    {
        return baggage;
    }

    public BoardingPass getBoardingPass()
    {
        return boardingPass;
    }
}