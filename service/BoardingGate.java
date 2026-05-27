package service;

import exception.TicketStateException;
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
        System.out.println("  [登機門掃描器] 讀取登機證中，旅客：" + passenger.getName());

        // 檢查：是否已完成安檢手續 (SECURITY_CLEARED)
        if (ticket.getState() != TicketState.SECURITY_CLEARED)
        {
            throw new TicketStateException("登機", TicketState.SECURITY_CLEARED, ticket.getState());
        }

        ticket.setState(TicketState.BOARDED);
        flight.addPassenger(passenger);
        System.out.println("  [登機門掃描器] 驗證成功！狀態更新為：BOARDED\n");
    }
}