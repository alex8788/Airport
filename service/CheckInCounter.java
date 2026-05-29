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
    private final List<Booking> bookings; // 訂票紀錄

    public CheckInCounter(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    @Override
    public void process(Passenger passenger)
    {
        System.out.println("  [地勤報到系統] 開始處理，旅客：" + passenger.getName());

        // 1. 查詢訂票紀錄與身分核對
        Booking booking = verifyBooking(passenger);

        // 2. 行李託運檢查
        checkBaggage(passenger, booking);

        // 3. 自動劃位
        String seat = arrangeSeat(booking.getFlight());

        // 4. 發放登機證
        issueBoardingPass(passenger, booking, seat);
    }

    // 身分核對
    private Booking verifyBooking(Passenger passenger)
    {
        Booking booking = findBooking(passenger.getPassport().getNumber());

        // 檢查：姓名是否相符 (護照 vs 訂票紀錄)
        if (!passenger.getPassport().getName().equals(booking.getName()))
        {
            throw new IdentityException("報到櫃檯", passenger.getPassport().getName(), booking.getName());
        }

        System.out.println("  [地勤報到系統] 查獲訂票紀錄！旅客姓名：" + booking.getName() + "，艙等：" + booking.getCabinClass());
        return booking;
    }

    // 行李託運檢查
    private void checkBaggage(Passenger passenger, Booking booking)
    {
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
    }

    // 自動劃位
    private String arrangeSeat(Flight flight)
    {
        System.out.println("\n  [地勤報到系統] 行李檢查通過。系統正在為您尋找空位...");
        String newSeat = flight.assignSeat();
        flight.showSeatMap(newSeat);
        return newSeat;
    }

    // 發放登機證
    private void issueBoardingPass(Passenger passenger, Booking booking, String seat)
    {
        BoardingPass pass = new BoardingPass(booking.getFlight(), booking.getCabinClass(), booking.getName());
        pass.setSeatId(seat);
        pass.setState(BoardingPassState.CHECKED_IN);
        passenger.setBoardingPass(pass);

        System.out.println("  [地勤報到系統] 報到完成！已發放登機證。座位：" + pass.getSeatId() + "，狀態更新為：CHECKED_IN\n");
    }

    // 查詢旅客訂票紀錄
    private Booking findBooking(String passportNum)
    {
        return bookings.stream().filter(b -> b.getPassportNum().equals(passportNum)).findFirst()
                .orElseThrow(() -> new AirportException("報到櫃檯失敗：找不到護照號碼 [" + passportNum + "] 的訂票紀錄！"));
    }
}