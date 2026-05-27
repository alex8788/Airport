package service;

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
            System.out.println("安檢失敗：狀態異常，請先完成報到。\n");
            return;
        }

        // 檢查：護照姓名是否與旅客姓名相符
        Passport passport = passenger.getPassport();
        if (!passenger.getName().equals(passport.getHolderName()))
        {
            System.out.println("安檢失敗：護照姓名與旅客姓名不符！\n");
            return;  // 姓名不符，中斷流程
        }

        // 所有檢查皆通過，更新狀態為 SECURITY_CLEARED
        passenger.getTicket().setState(TicketState.SECURITY_CLEARED);
        System.out.println("安檢通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
    }
}