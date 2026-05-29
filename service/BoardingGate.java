package service;

import exception.TicketStateException;
import model.BoardingPass;
import model.BoardingPassState;
import model.Flight;
import model.Passenger;

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
        BoardingPass pass = passenger.getBoardingPass();
        System.out.println("  [登機門掃描器] 讀取登機證中，旅客：" + passenger.getName());

        // 檢查：是否已完成安檢手續 (SECURITY_CLEARED)
        if (pass.getState() != BoardingPassState.SECURITY_CLEARED)
        {
            throw new TicketStateException("登機", BoardingPassState.SECURITY_CLEARED, pass.getState());
        }

        pass.setState(BoardingPassState.BOARDED);
        flight.addPassenger(passenger);
        System.out.println("  [登機門掃描器] 驗證成功！狀態更新為：BOARDED\n");
    }
}