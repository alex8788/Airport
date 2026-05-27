package service;

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
        if (!passenger.getName().equals(ticket.getOwnerName()))
        {
            throw new IdentityException("報到櫃檯", passenger.getName(), ticket.getOwnerName());
        }

        // 檢查：機票狀態是否為 BOOKED
        if (ticket.getState() != TicketState.BOOKED)
        {
            throw new TicketStateException("報到櫃檯", TicketState.BOOKED, ticket.getState());
        }

        // 行李託運：違禁品與超重檢查
        if (baggage != null)
        {
            if (baggage.hasProhibitedItems())
            {
                System.out.println("報到失敗：行李內含違禁品！\n");
                return;
            }

            // 根據旅客的艙等，檢查行李是否超重
            double maxWeight = ticket.getCabinClass().getMaxBaggageWeight();
            if (baggage.getWeight() > maxWeight)
            {
                System.out.println("報到失敗：行李過重 (" + baggage.getWeight() + "kg)，您的 " + ticket.getCabinClass()
                        + " 艙等載重上限為 " + maxWeight + "kg!\n");
                return;
            }
        }

        // 隨機劃位
        String newSeat = flight.assignRandomSeat();
        ticket.assignSeat(newSeat);
        ticket.setState(TicketState.CHECKED_IN);
        System.out.println("報到成功！行李檢查通過。已分配座位：" + ticket.getSeatNumber() + "，狀態更新為：CHECKED_IN\n");
    }
}