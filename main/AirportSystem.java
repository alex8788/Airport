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
import model.CabinClass;
import model.Flight;
import model.Passenger;
import model.Passport;
import service.BoardingGate;
import service.CheckInCounter;
import service.Processable;
import service.SecurityCheck;

public class AirportSystem
{
    // 全域的輔助物件
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // 航線列舉：包含目的地與飛行時間
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
    private static final Flight[] flights = new Flight[3];
    // 訂票紀錄集合
    private static final List<Booking> bookings = new ArrayList<>();

    public static void main(String[] args)
    {
        initializeFlights();
        setupBookings();

        Passenger passenger = setupPassenger();
        runAirportFlow(passenger);

        System.out.println("\n[系統提示] 按 Enter 鍵關閉視窗...");
        scanner.nextLine();
    }

    // 初始化後台訂票紀錄
    private static void setupBookings()
    {
        // 當前旅客的訂票紀錄
        bookings.add(new Booking("Tom", "123456789", flights[0].getNumber(), CabinClass.ECONOMY));
        // 寫死兩筆其他旅客訂票紀錄
        bookings.add(new Booking("Mia", "A11223344", flights[1].getNumber(), CabinClass.BUSINESS));
        bookings.add(new Booking("Jason", "B99887766", flights[2].getNumber(), CabinClass.FIRST));
    }

    // 建立當期航線，並顯示航班資訊
    private static void initializeFlights()
    {
        System.out.println("--- 機場航班看板 ---");
        for (int i = 0; i < flights.length; i++)
        {
            flights[i] = createRandomFlight();
            showFlightInfo(flights[i]);
        }
        System.out.println("------------------------\n");
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
        Flight flight = flights[0];  // 假設當前旅客搭乘首個航班
        flight.preOccupySeats(30);  // 同班機其他乘客的座位

        Processable counter = new CheckInCounter(flight, bookings);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 顯示旅客訊息
        System.out.println("\n=============================================");
        System.out.println("       [桃園國際機場 (TPE) 離境通關系統]       ");
        System.out.println("=============================================");
        System.out.printf(" 旅客姓名: %-10s | 護照號碼: %s%n", passenger.getName(), passenger.getPassport().getNumber());
        System.out.printf(" 航班編號: %-10s | 目的地: %s%n", flight.getNumber(), flight.getDestination());
        System.out.printf(" 登機時間: %-10s | 起飛時間: %s%n", flight.getBoardingTime(), flight.getDepartureTime());
        System.out.println("=============================================\n");

        // 集中攔截機場通關異常
        try
        {
            pause("報到櫃檯");
            counter.process(passenger);

            showPass(passenger, flight);

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

    // 輔助函式：隨機產生一個航班物件
    private static Flight createRandomFlight()
    {
        // 隨機抽取一條航線
        Route[] routes = Route.values();
        Route route = routes[random.nextInt(routes.length)];

        // 隨機生成登機時間 (時: 6~22，分: 10的倍數)
        int hr = random.nextInt(17) + 6; // 6 到 22 時
        int min = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50 分
        LocalTime boardingTime = LocalTime.of(hr, min);

        return new Flight(route.name(), route.destination, boardingTime, route.duration);
    }

    // 輔助函式：顯示當前某航班的資訊
    private static void showFlightInfo(Flight f)
    {
        System.out.printf("航班: %s | 目的地: %s | 登機時間: %s%n", f.getNumber(), f.getDestination(), f.getBoardingTime());
    }

    // 輔助函式：顯示離境通關系統訊息
    private static void showPass(Passenger passenger, Flight flight)
    {
        BoardingPass pass = passenger.getBoardingPass();

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