package service;

import exception.AirportException;
import exception.BaggageException;
import exception.IdentityException;
import java.util.List;
import model.Baggage;
import model.Booking;
import model.Flight;
import model.Passenger;
import model.Ticket;
import model.TicketState;

public class CheckInCounter implements Processable
{
    private final Flight flight; // 航班物件, 劃位用
    private final List<Booking> bookings; // 接收後台訂票紀錄

    public CheckInCounter(Flight flight, List<Booking> bookings)
    {
        this.flight = flight;
        this.bookings = bookings;
    }

    @Override
    public void process(Passenger passenger)
    {
        System.out.println("  [地勤報到系統] 開始處理，旅客：" + passenger.getName());

        // 1. 根據護照號碼，查詢訂票紀錄
        String passportNum = passenger.getPassport().getNumber();
        Booking booking = null;

        for (Booking b : bookings)
        {
            if (b.getPassportNum().equals(passportNum))
            {
                booking = b;
                break;
            }
        }

        // 找不到訂票紀錄
        if (booking == null)
        {
            throw new AirportException("報到櫃檯失敗：找不到護照號碼 [" + passportNum + "] 的訂票紀錄！");
        }

        // 2. 檢查：姓名是否相符 (護照 vs 訂票紀錄)
        if (!passenger.getPassport().getName().equals(booking.getName()))
        {
            throw new IdentityException("報到櫃檯", passenger.getPassport().getName(), booking.getName());
        }

        // 產生登機證
        Ticket ticket = booking.getTicket();

        Baggage baggage = passenger.getBaggage();

        // 檢查：託運行李是否含違禁品或超重
        if (passenger.hasBaggage())
        {
            if (baggage.hasContraband())
            {
                throw new BaggageException("報到櫃檯");
            }

            // 根據旅客的艙等，檢查行李是否超重
            if (baggage.getWeight() > ticket.getCabinClass().getMaxWeight())
            {
                throw new BaggageException("報到櫃檯", baggage, ticket);
            }
        }

        // 隨機劃位
        String newSeat = flight.assignRandomSeat();
        ticket.assignSeat(newSeat);
        ticket.setState(TicketState.CHECKED_IN);

        System.out.println("  [地勤報到系統] 報到完成！行李檢查通過。已劃位：" + ticket.getSeatId() + "，狀態更新為：CHECKED_IN\n");

        // 顯示當前座位表
        flight.printSeatMap(ticket.getSeatId());
    }
}