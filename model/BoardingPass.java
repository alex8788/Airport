package model;

public class BoardingPass
{
    private final String flightNum; // 航班編號（已決定）
    private final CabinClass cabinClass; // 艙等（已決定）
    private final String owner; // 機票購買者姓名
    private String seatId; // 座位編號(尚未決定)
    private BoardingPassState state; // 機票的通關狀態

    // 建構子：設定航班、艙等與購票者，預設座位未分配、狀態為 BOOKED
    public BoardingPass(String flightNum, CabinClass cabinClass, String owner)
    {
        this.flightNum = flightNum;
        this.cabinClass = cabinClass;
        this.owner = owner;
        this.seatId = "Unassigned";
        this.state = BoardingPassState.BOOKED;
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

    public void assignSeat(String seatId)
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