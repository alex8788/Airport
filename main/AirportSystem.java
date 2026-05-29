package main;

import exception.AirportException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import model.Baggage;
import model.BoardingPass;
import model.Booking;
import model.Flight;
import model.Passenger;
import model.Passport;
import service.BoardingGate;
import service.CheckInCounter;
import service.Processable;
import service.SecurityCheck;
import service.TicketMachine;

public class AirportSystem
{
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // 航線列舉：含目的地與飛行時間
    private enum Route
    {
        BR198("東京(NRT)", 200), BR159("首爾(ICN)", 150), CI909("香港(HKG)", 100), CI751("新加坡(SIN)", 280),
        JX800("曼谷(BKK)", 220), CX421("吉隆坡(KUL)", 270);

        final String destination;
        final int duration;

        Route(String destination, int duration)
        {
            this.destination = destination;
            this.duration = duration;
        }
    }

    // 當前航班陣列
    private static final Flight[] flights = new Flight[6];
    // 系統訂票紀錄
    private static final List<Booking> bookings = new ArrayList<>();

    public static void main(String[] args)
    {
        initFlights();  // 初始化航班與虛擬訂票紀錄

        // 1. 旅客操作機台購票
        TicketMachine machine = new TicketMachine();
        machine.start(flights, bookings);

        // 2. 旅客攜帶護照與行李, 開始通關
        Passenger passenger = setupPassenger();
        runAirportFlow(passenger);

        System.out.println("\n[系統提示] 按 Enter 鍵關閉視窗...");
        scanner.nextLine();
    }
    
    private static void initFlights()
    {
        Route[] routes = Route.values();
        for (int i = 0; i < flights.length; i++)
        {
            Route r = routes[i];
            int hr = random.nextInt(17) + 6;
            int min = random.nextInt(6) * 10;
            
            flights[i] = new Flight(r.name(), r.destination, LocalTime.of(hr, min), r.duration);
            flights[i].preOccupySeats(30); // 提早為每班飛機預先佔位
        }
    }

    // 輸入旅客資訊, 初始化旅客物件
    private static Passenger setupPassenger()
    {
        System.out.println("--- 請輸入旅客資訊 ---");

        // 1. 輸入護照資訊
        String passportName = readString("1. 請輸入護照上的姓名：", false);
        String passportNum = readString("   請輸入護照號碼：", false);
        Passport passport = new Passport(passportName, passportNum);

        Passenger passenger = new Passenger(passportName, passport);

        // 2. 輸入行李資訊
        double weight = readNonNegDouble("2. 請輸入行李重量 (kg，輸入 0 代表無託運行李)：");
        
        if (weight > 0)
        {
            boolean hasProhibitedItems = readBool("3. 行李是否包含違禁品 (true / false)：");
            passenger.setBaggage(new Baggage(weight, hasProhibitedItems));
        }
        System.out.println("----------------------\n");

        return passenger;
    }

    // 初始化機場關卡, 讓旅客進行通關流程
    private static void runAirportFlow(Passenger passenger)
    {
        Processable counter = new CheckInCounter(bookings);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate();

        // 集中攔截機場通關異常
        try
        {
            pause("報到櫃檯");
            counter.process(passenger);

            showPass(passenger);

            pause("安檢站");
            security.process(passenger);

            pause("登機門");
            gate.process(passenger);

            System.out.println("=============================================");
            System.out.println(" [航站廣播] 旅客 " + passenger.getName() + " 已順利登機，祝您旅途愉快！");
            System.out.println("=============================================\n");
        } catch (AirportException e)
        {
            System.out.println(" [系統攔截] 通關程序終止: " + e.getMessage() + "\n");
            System.out.println("=============================================\n");
        }
    }

    // 輔助函式：顯示離境通關系統訊息
    private static void showPass(Passenger passenger)
    {
        BoardingPass pass = passenger.getBoardingPass();
        Flight flight = pass.getFlight();

        System.out.println("\n=============================================");
        System.out.println("       [桃園國際機場 (TPE) 離境通關系統]       ");
        System.out.println("=============================================");
        System.out.printf(" 旅客姓名: %-10s | 艙等: %s%n", passenger.getName(), pass.getCabinClass());
        System.out.printf(" 航班編號: %-10s | 目的地: %s%n", flight.getNumber(), flight.getDestination());
        System.out.printf(" 登機時間: %-10s | 起飛時間: %s%n", flight.getBoardingTime(), flight.getDepartureTime());
        System.out.println("=============================================\n");
    }

    // 輔助函式：系統訊息的停頓
    private static void pause(String location)
    {
        System.out.print("[系統提示] 旅客正前往【" + location + "】... 請按 Enter 鍵確認以繼續 >>> ");
        scanner.nextLine();
    }

    // 輔助函式：讀取合法字串 (可控制輸入空字串)
    private static String readString(String prompt, boolean allowEmpty)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowEmpty || !input.trim().isEmpty())
                return input;

            System.out.println("  [輸入錯誤] 此欄位不能為空，請重新輸入！\n");
        }
    }

    // 輔助函式：讀取非負數值
    private static double readNonNegDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val >= 0) return val;
                System.out.println("  [輸入錯誤] 重量不能為負數！");
            }
            catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！");
            }
        }
    }

    // 輔助函式：讀取合法 Boolean
    private static boolean readBool(String prompt)
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
}