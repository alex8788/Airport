package model;

public class BoardingPass
{
    private final Flight flight; // 航班物件
    private final CabinClass cabinClass; // 艙等
    private final String owner; // 持有者姓名
    private String seatId; // 座位編號
    private BoardingPassState state; // 機票通關狀態

    // 建構子：設定航班、艙等與購票者，預設座位未分配、狀態為 BOOKED
    public BoardingPass(Flight flight, CabinClass cabinClass, String owner)
    {
        this.flight = flight;
        this.cabinClass = cabinClass;
        this.owner = owner;
        this.seatId = "Unassigned";
        this.state = BoardingPassState.BOOKED;
    }

    public Flight getFlight()
    {
        return flight;
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

    public void setSeatId(String seatId)
    {
        this.seatId = seatId;
    }

    public BoardingPassState getState()
    {
        return state;
    }

    public void setState(BoardingPassState state)
    {
        this.state = state;
    }
}