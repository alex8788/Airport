package model;

public class Ticket
{
    private final String flightNumber;  // 航班編號（已決定）
    private final CabinClass cabinClass; // 艙等（已決定）
    private final String ownerName;  // 機票購買者姓名
    private String seatNumber;  // 尚未決定的座位編號
    private TicketState state;  // 隨流程變化的機票狀態

    // 建構子：設定航班、艙等與購票者，預設座位未分配、狀態為 BOOKED
    public Ticket(String flightNumber, CabinClass cabinClass, String ownerName)
    {
        this.flightNumber = flightNumber;
        this.cabinClass = cabinClass;
        this.ownerName = ownerName;
        this.seatNumber = "Unassigned";
        this.state = TicketState.BOOKED;
    }
    
    public String getFlightNumber()
    {
        return flightNumber;
    }

    public CabinClass getCabinClass()
    {
        return cabinClass;
    }
    
    public String getOwnerName()
    {
        return ownerName;  // 新增：取得購票者姓名
    }

    public String getSeatNumber()
    {
        return seatNumber;
    }

    public void assignSeat(String seatNumber)
    {
        this.seatNumber = seatNumber;
    }

    public TicketState getState()
    {
        return state;
    }

    public void setState(TicketState state)
    {
        this.state = state;
    }
}