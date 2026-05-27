package model;

public class Ticket
{
    private final String flightNum; // 航班編號（已決定）
    private final CabinClass cabinClass; // 艙等（已決定）
    private final String owner; // 機票購買者姓名
    private String seatId; // 座位編號(尚未決定)
    private TicketState state; // 機票的通關狀態

    // 建構子：設定航班、艙等與購票者，預設座位未分配、狀態為 BOOKED
    public Ticket(String flightNum, CabinClass cabinClass, String owner)
    {
        this.flightNum = flightNum;
        this.cabinClass = cabinClass;
        this.owner = owner;
        this.seatId = "Unassigned";
        this.state = TicketState.BOOKED;
    }

    public String getFlightNum()
    {
        return flightNum;
    }

    public CabinClass getCabinClass()
    {
        return cabinClass;
    }

    public String getOwner()
    {
        return owner;
    }

    public String getSeatId()
    {
        return seatId;
    }

    public void assignSeat(String seatNumber)
    {
        this.seatId = seatNumber;
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