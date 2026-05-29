package service;

import exception.IdentityException;
import exception.TicketStateException;
import model.BoardingPass;
import model.BoardingPassState;
import model.Passenger;
import model.Passport;

public class SecurityCheck implements Processable
{
    @Override
    public void process(Passenger passenger)
    {
        System.out.println("  [安檢掃描系統] 啟動身分核驗與 X 光檢查，旅客：" + passenger.getName());

        BoardingPass pass = passenger.getBoardingPass();

        // 檢查：是否已經報到 (CHECKED_IN)
        if (pass.getState() != BoardingPassState.CHECKED_IN)
        {
            throw new TicketStateException("安檢站", BoardingPassState.CHECKED_IN, pass.getState());
        }

        // 檢查：姓名是否相符 (登機證 vs 護照)
        Passport passport = passenger.getPassport();
        if (!pass.getOwner().equals(passport.getName()))
        {
            throw new IdentityException("安檢站", pass.getOwner(), passport.getName());
        }

        // 所有檢查皆通過，更新狀態為 SECURITY_CLEARED
        pass.setState(BoardingPassState.SECURITY_CLEARED);
        System.out.println("  [安檢掃描系統] 核驗通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
    }
}