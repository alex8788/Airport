package model;

public class Booking
{
    private final String name;  // 購票者姓名
    private final String passportNum;  // 護照號碼
    private final Flight flight;  // 航班物件
    private final CabinClass cabinClass;  // 艙等

    public Booking(String name, String passportNum, Flight flight, CabinClass cabinClass)
    {
        this.name = name;
        this.passportNum = passportNum;
        this.flight = flight;
        this.cabinClass = cabinClass;
    }

    public Flight getFlight() // 修正：回傳航班物件
    {
        return flight;
    }

    public String getName()
    {
        return name;
    }

    public String getPassportNum()
    {
        return passportNum;
    }

    public CabinClass getCabinClass()
    {
        return cabinClass;
    }
}