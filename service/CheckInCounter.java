package service;

import exception.AirportException;
import exception.BaggageException;
import exception.IdentityException;
import java.util.List;
import java.util.Scanner;
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

    private static final Scanner scanner = new Scanner(System.in);

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

        // 1.5 由訂票紀錄產生登機證
        BoardingPass pass = booking.getBoardingPass();
        System.out.println("  [地勤報到系統] 查獲訂票紀錄！旅客姓名：" + booking.getName() + "，艙等：" + pass.getCabinClass());

        // 2. 行李託運
        processBaggage(passenger, pass);

        // 3. 自動劃位與打印座位表
        System.out.println("\n  [地勤報到系統] 行李檢查通過。系統正在為您尋找空位...");
        String newSeat = flight.assignRandomSeat();
        flight.printSeatMap(newSeat);

        // 4. 更新登機證並發放給旅客
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

    // 行李託運與檢查
    private void processBaggage(Passenger passenger, BoardingPass pass)
    {
        System.out.println("\n  [地勤報到系統] 請將行李放上磅秤...");
        double weight = readNonNegDouble("  >> 請輸入行李重量 (kg，無託運請輸入 0)：");

        if (weight > 0)
        {
            boolean hasContraband = readBool("  >> 行李是否包含違禁品 (true / false)：");
            Baggage baggage = new Baggage(weight, hasContraband);
            passenger.setBaggage(baggage);

            // 檢查：違禁品或超重
            if (hasContraband)
            {
                throw new BaggageException("報到櫃檯");
            }
            if (weight > pass.getCabinClass().getMaxWeight())
            {
                throw new BaggageException("報到櫃檯", baggage, pass);
            }
        }
    }

    // 輸入輔助函式 (Boolean)
    private boolean readBool(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("true")) return true;
            if (input.equals("false")) return false;

            System.out.println("  [輸入錯誤] 請輸入 true 或 false！");
        }
    }

    // 輸入輔助函式 (重量)
    private double readNonNegDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= 0)
                    return val;
                System.out.println("  [輸入錯誤] 重量不能為負數！");
            } catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！");
            }
        }
    }
}