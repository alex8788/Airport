package service;

import model.Flight;
import model.Passenger;
import model.Ticket;
import model.TicketState;

public class BoardingGate implements Processable
{
    private final Flight flight;

    public BoardingGate(Flight flight)
    {
        this.flight = flight;
    }

    @Override
    public void process(Passenger passenger)
    {
        Ticket ticket = passenger.getTicket();
        System.out.println("登機門處理中... 旅客：" + passenger.getName());

        // 檢查：是否已完成安檢手續 (SECURITY_CLEARED)
        if (ticket.getState() != TicketState.SECURITY_CLEARED)
        {
            System.out.println("登機失敗：請確認是否已完成報到與安檢手續。\n");
            return;  // 中斷流程
        }

        ticket.setState(TicketState.BOARDED);
        flight.addPassenger(passenger);
        System.out.println("登機成功！狀態更新為：BOARDED\n");
    }
}