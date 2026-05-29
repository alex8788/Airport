package main;

import exception.AirportException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
        // 寫死兩筆其他旅客訂票紀錄 (包含各自的航班、艙等、購票者)
        bookings.add(new Booking("林小華", "A11223344", new BoardingPass(flights[1].getNumber(), CabinClass.BUSINESS, "林小華")));
        bookings.add(new Booking("陳阿明", "B99887766", new BoardingPass(flights[2].getNumber(), CabinClass.FIRST, "陳阿明")));

        // 一筆當前旅客的訂票紀錄 (護照號碼固定為 123456789)
        BoardingPass pass = new BoardingPass(flights[0].getNumber(), CabinClass.ECONOMY, "王大明");
        bookings.add(new Booking("王大明", "123456789", pass));
    }

    // 建立當期航線(3條)，並印出航班資訊
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

        // 輸入護照資訊
        String passportName = readString("1. 請輸入護照上的姓名：", false);
        String passportNum = readString("   請輸入護照號碼：", false);
        Passport passport = new Passport(passportName, passportNum);

        System.out.println("----------------------\n");

        // 旅客建立時，姓名沿用護照名字，且尚未領取實體登機證
        return new Passenger(passportName, passport);
    }

    // 初始化機場關卡, 讓旅客進行通關流程
    private static void runAirportFlow(Passenger passenger)
    {
        // 假設旅客皆搭乘首個航班
        Flight flight = flights[0];

        // 報到櫃檯需要連線到後台查詢訂票紀錄
        Processable counter = new CheckInCounter(flight, bookings);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 顯示旅客訊息
        System.out.println("\n=============================================");
        System.out.println("       [桃園國際機場 (TPE) 離境通關系統]       ");
        System.out.println("=============================================");
        System.out.printf(" 旅客姓名: %-10s | 艙等: %s%n", passenger.getName(), passenger.getBoardingPass().getCabinClass());
        System.out.printf(" 航班編號: %-10s | 目的地: %s%n", flight.getNumber(), flight.getDestination());
        System.out.printf(" 登機時間: %-10s | 起飛時間: %s%n", flight.getBoardingTime(), flight.getDepartureTime());
        System.out.println("=============================================\n");

        // 集中攔截機場通關異常
        try
        {
            pause("報到櫃檯");
            counter.process(passenger);

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

    // 隨機產生一個航班物件
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

    // 顯示當前某航班的資訊
    private static void showFlightInfo(Flight f)
    {
        System.out.printf("航班: %s | 目的地: %s | 登機時間: %s%n", f.getNumber(), f.getDestination(), f.getBoardingTime());
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
}