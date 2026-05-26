package service;

import model.Passenger;
import model.TicketState;

public class SecurityCheck implements Processable
{
    @Override
    public void process(Passenger passenger)
    {
        // 印出處理中的旅客姓名
        System.out.println("安檢站處理中... 旅客：" + passenger.getName());

        // 檢查是否已經報到 (CHECKED_IN)
        if (passenger.getTicket().getState() != TicketState.CHECKED_IN)
        {
            System.out.println("安檢失敗：狀態異常，請先完成報到。\n");
            return;  // 狀態不對，中斷流程
        }

        // 檢查是否有帶護照
        if (!passenger.hasPassport())
        {
            System.out.println("安檢失敗：未攜帶護照。\n");
        }
        else
        {
            passenger.getTicket().setState(TicketState.SECURITY_CLEARED);
            System.out.println("安檢通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
        }
    }
}