package model;

public class Booking
{
    private final String name;  // 購票者姓名
    private final String passportNum;  // 護照號碼
    private final String flightNum; // 航班編號
    private final CabinClass cabinClass;  // 艙等

    public Booking(String name, String passportNum, String flightNum, CabinClass cabinClass)
    {
        this.name = name;
        this.passportNum = passportNum;
        this.flightNum = flightNum;
        this.cabinClass = cabinClass;
    }

    public String getName()
    {
        return name;
    }

    public String getPassportNum()
    {
        return passportNum;
    }

    public String getFlightNum()
    {
        return flightNum;
    }

    public CabinClass getCabinClass()
    {
        return cabinClass;
    }
}