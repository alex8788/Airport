package service;

import exception.DocumentException;
import exception.IdentityException;
import exception.TicketStateException;
import model.Passenger;
import model.Passport;
import model.TicketState;

public class SecurityCheck implements Processable
{
    @Override
    public void process(Passenger passenger)
    {
        System.out.println("安檢站處理中... 旅客：" + passenger.getName());

        // 檢查：是否已經報到 (CHECKED_IN)
        if (passenger.getTicket().getState() != TicketState.CHECKED_IN)
        {
            throw new TicketStateException("安檢站", TicketState.CHECKED_IN, passenger.getTicket().getState());
        }

        // 檢查：是否攜帶護照
        if (!passenger.hasPassport())
        {
            throw new DocumentException("安檢站", "護照");
        }

        // 檢查：護照姓名是否與旅客姓名相符
        Passport passport = passenger.getPassport();
        if (!passenger.getName().equals(passport.getName()))
        {
            throw new IdentityException("安檢站", passenger.getName(), passport.getName());
        }

        // 所有檢查皆通過，更新狀態為 SECURITY_CLEARED
        passenger.getTicket().setState(TicketState.SECURITY_CLEARED);
        System.out.println("安檢通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
    }
}