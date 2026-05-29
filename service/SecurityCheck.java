package service;

import exception.IdentityException;
import exception.TicketStateException;
import model.BoardingPassState;
import model.Passenger;
import model.Passport;

public class SecurityCheck implements Processable
{
    @Override
    public void process(Passenger passenger)
    {
        System.out.println("  [安檢掃描系統] 啟動身分核驗與 X 光檢查，旅客：" + passenger.getName());

        // 檢查：是否已經報到 (CHECKED_IN)
        if (passenger.getBoardingPass().getState() != BoardingPassState.CHECKED_IN)
        {
            throw new TicketStateException("安檢站", BoardingPassState.CHECKED_IN, passenger.getBoardingPass().getState());
        }

        // 檢查：護照姓名是否與旅客姓名相符
        Passport passport = passenger.getPassport();
        if (!passenger.getName().equals(passport.getName()))
        {
            throw new IdentityException("安檢站", passenger.getName(), passport.getName());
        }

        // 所有檢查皆通過，更新狀態為 SECURITY_CLEARED
        passenger.getBoardingPass().setState(BoardingPassState.SECURITY_CLEARED);
        System.out.println("  [安檢掃描系統] 核驗通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
    }
}