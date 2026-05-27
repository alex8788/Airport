package service;

import exception.BaggageException;
import exception.IdentityException;
import exception.TicketStateException;
import model.Baggage;
import model.Flight;
import model.Passenger;
import model.Ticket;
import model.TicketState;

public class CheckInCounter implements Processable
{
    private final Flight flight; // 知道航班才能劃位

    public CheckInCounter(Flight flight)
    {
        this.flight = flight;
    }

    @Override
    public void process(Passenger passenger)
    {
        Ticket ticket = passenger.getTicket();
        Baggage baggage = passenger.getBaggage();
        System.out.println("報到櫃檯處理中... 旅客：" + passenger.getName());

        // 檢查：旅客姓名是否與購票者相符
        if (!passenger.getName().equals(ticket.getOwner()))
        {
            throw new IdentityException("報到櫃檯", passenger.getName(), ticket.getOwner());
        }

        // 檢查：機票狀態是否為 BOOKED
        if (ticket.getState() != TicketState.BOOKED)
        {
            throw new TicketStateException("報到櫃檯", TicketState.BOOKED, ticket.getState());
        }

        // 行李託運：違禁品與超重檢查
        if (passenger.hasBaggage())
        {
            if (baggage.hasProhibitedItems())
            {
                throw new BaggageException("報到櫃檯");
            }

            // 根據旅客的艙等，檢查行李是否超重
            if (baggage.getWeight() > ticket.getCabinClass().getMaxWeight())
            {
                // 修改：直接傳入 baggage 與 ticket 物件
                throw new BaggageException("報到櫃檯", baggage, ticket);
            }
        }

        // 隨機劃位
        String newSeat = flight.assignRandomSeat();
        ticket.assignSeat(newSeat);
        ticket.setState(TicketState.CHECKED_IN);
        System.out.println("報到成功！行李檢查通過。已分配座位：" + ticket.getSeatId() + "，狀態更新為：CHECKED_IN\n");
    }
}