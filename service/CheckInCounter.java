package service;

import model.Baggage;
import model.Flight;
import model.Passenger;
import model.Ticket;
import model.TicketState;

public class CheckInCounter implements Processable
{
    private final Flight flight;  // 知道航班才能劃位

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

        // 檢查：旅客姓名與購票者相符
        if (!(passenger.getName()).equals(ticket.getOwnerName()))
        {
            System.out.println("報到失敗：旅客姓名與機票持有者不符！\n");
            return;  // 中斷流程
        }

        // 檢查：機票狀態是否為 BOOKED
        if (ticket.getState() != TicketState.BOOKED)
        {
            System.out.println("報到失敗：尚未訂購機票。\n");
            return;  // 狀態不對，中斷流程
        }

        // 行李託運：違禁品與超重檢查
        if (baggage != null)
        {
            if (baggage.hasProhibitedItems())
            {
                System.out.println("報到失敗：行李內含違禁品！\n");
                return;  // 中斷流程
            }
            
            // 根據旅客的艙等，檢查行李是否超重
            double maxWeight = ticket.getCabinClass().getMaxBaggageWeight();
            if (baggage.getWeight() > maxWeight)
            {
                System.out.println("報到失敗：行李過重 (" + baggage.getWeight() + "kg)，您的 " + ticket.getCabinClass() + " 艙等載重上限為 " + maxWeight + "kg!\n");
                return;  // 中斷流程
            }
        }

        // 隨機劃位
        String newSeat = flight.assignRandomSeat(); 
        ticket.assignSeat(newSeat);
        ticket.setState(TicketState.CHECKED_IN);
        System.out.println("報到成功！行李檢查通過。已分配座位：" + ticket.getSeatNumber() + "，狀態更新為：CHECKED_IN\n");
    }
}