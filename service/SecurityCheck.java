package service;

import model.Passenger;
import model.TicketState;

public class SecurityCheck implements Processable {
    @Override
    public void process(Passenger passenger) {
        // 印出處理中的旅客姓名
        System.out.println("安檢站處理中... 旅客：" + passenger.getName());

        // 檢查護照是否有帶，且僅在已報到狀態時通過安檢
        if (passenger.getPassportNumber() == null || passenger.getPassportNumber().isEmpty()) {
            System.out.println("安檢失敗：未攜帶護照。\n");
        } else if (passenger.getTicket().getState() == TicketState.CHECKED_IN) {
            passenger.getTicket().setState(TicketState.SECURITY_CLEARED);
            System.out.println("安檢通過！已確認護照，狀態更新為：SECURITY_CLEARED\n");
        } else {
            System.out.println("安檢失敗：狀態異常，請先完成報到。\n");
        }
    }
}