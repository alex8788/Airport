package model;

public class Booking
{
    private final String name;
    private final String passportNum;
    private final BoardingPass boardingPass;

    public Booking(String name, String passportNum, BoardingPass ticket)
    {
        this.name = name;
        this.passportNum = passportNum;
        this.boardingPass = ticket;
    }

    public String getName()
    {
        return name;
    }

    public String getPassportNum()
    {
        return passportNum;
    }

    public BoardingPass getBoardingPass()
    {
        return boardingPass;
    }
}