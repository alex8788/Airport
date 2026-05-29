package service;

import exception.AirportException;
import exception.BaggageException;
import exception.IdentityException;
import java.util.List;
import model.Baggage;
import model.BoardingPass;
import model.BoardingPassState;
import model.Booking;
import model.Flight;
import model.Passenger;

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

        // 1. 查詢訂票紀錄與身分核對
        Booking booking = findBooking(passenger.getPassport().getNumber());

        // 檢查：姓名是否相符 (護照 vs 訂票紀錄)
        if (!passenger.getPassport().getName().equals(booking.getName()))
        {
            throw new IdentityException("報到櫃檯", passenger.getPassport().getName(), booking.getName());
        }

        System.out.println("  [地勤報到系統] 查獲訂票紀錄！旅客姓名：" + booking.getName() + "，艙等：" + booking.getCabinClass());

        // 2. 行李託運
        Baggage baggage = passenger.getBaggage();
        if (baggage != null)
        {
            // 檢查：違禁品或超重
            if (baggage.hasContraband())
            {
                throw new BaggageException("報到櫃檯");
            }
            if (baggage.getWeight() > booking.getCabinClass().getMaxWeight())
            {
                throw new BaggageException("報到櫃檯", baggage, booking.getCabinClass());
            }
        }

        // 3. 自動劃位與打印座位表
        System.out.println("\n  [地勤報到系統] 行李檢查通過。系統正在為您尋找空位...");
        String newSeat = flight.assignRandomSeat();
        flight.printSeatMap(newSeat);

        // 4. 生成登機證並發放給旅客
        BoardingPass pass = new BoardingPass(booking.getFlightNum(), booking.getCabinClass(), booking.getName());
        pass.assignSeat(newSeat);
        pass.setState(BoardingPassState.CHECKED_IN);
        passenger.setBoardingPass(pass);

        System.out.println("  [地勤報到系統] 報到完成！已發放登機證。座位：" + pass.getSeatId() + "，狀態更新為：CHECKED_IN\n");
    }

    // 查詢旅客訂票紀錄
    private Booking findBooking(String passportNum)
    {
        for (Booking b : bookings)
        {
            if (b.getPassportNum().equals(passportNum))
            {
                return b;
            }
        }
        throw new AirportException("報到櫃檯失敗：找不到護照號碼 [" + passportNum + "] 的訂票紀錄！");
    }
}