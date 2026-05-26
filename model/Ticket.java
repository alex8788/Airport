package model;

public class Ticket {
    private final String flightNumber;
    private final CabinClass cabinClass; // 新增：艙等
    private String seatNumber;
    private TicketState state;

    // 建構子：設定航班、艙等，預設座位未分配、狀態為 BOOKED
    public Ticket(String flightNumber, CabinClass cabinClass) {
        this.flightNumber = flightNumber;
        this.cabinClass = cabinClass;
        this.seatNumber = "Unassigned";
        this.state = TicketState.BOOKED;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void assignSeat(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }
}