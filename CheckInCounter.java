package service;

import model.Baggage;
import model.Flight;
import model.Passenger;
import model.Ticket;
import model.TicketState;

public class CheckInCounter implements Processable {
    private final Flight flight; // 新增：需要知道航班才能劃位

    public CheckInCounter(Flight flight) {
        this.flight = flight;
    }

    @Override
    public void process(Passenger passenger) {
        Ticket ticket = passenger.getTicket();
        Baggage baggage = passenger.getBaggage();
        System.out.println("報到櫃檯處理中... 旅客：" + passenger.getName());

        // 新增：檢查行李違禁品與重量 (假設限重 30kg)
        if (baggage != null) {
            if (baggage.hasProhibitedItems()) {
                System.out.println("報到失敗：行李內含違禁品！\n");
                return; // 中斷流程
            }
            if (baggage.getWeight() > 30.0) {
                System.out.println("報到失敗：行李過重 (" + baggage.getWeight() + "kg)，超過限制 30kg！\n");
                return; // 中斷流程
            }
        }

        if (ticket.getState() == TicketState.BOOKED) {
            // 修改：呼叫航班的方法來隨機劃位
            String newSeat = flight.assignRandomSeat();
            ticket.assignSeat(newSeat);
            ticket.setState(TicketState.CHECKED_IN);
            System.out.println("報到成功！行李檢查通過。已分配座位：" + ticket.getSeatNumber() + "，狀態更新為：CHECKED_IN\n");
        } else {
            System.out.println("報到失敗：機票狀態異常。\n");
        }
    }
}