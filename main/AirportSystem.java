package main;

import exception.AirportException;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;
import model.Baggage;
import model.CabinClass;
import model.Flight;
import model.Passenger;
import model.Passport;
import model.Ticket;
import service.BoardingGate;
import service.CheckInCounter;
import service.Processable;
import service.SecurityCheck;

public class AirportSystem
{
    // 全域共用的 Random 物件
    private static final Random random = new Random();

    // 全域共用的 Scanner 物件
    private static final Scanner scanner = new Scanner(System.in);

    // 定期航班資訊 (一律由台北出發，6 個航班)
    private static final String[] FLIGHT_NUMS =
    { "BR198", "BR159", "CI909", "CI751", "JX800", "CX421" }; // 航班編號
    private static final String[] DESTINATIONS =
    { "東京(NRT)", "首爾(ICN)", "香港(HKG)", "新加坡(SIN)", "曼谷(BKK)", "吉隆坡(KUL)" }; // 目的地
    private static final int[] DURATIONS =
    { 200, 150, 100, 280, 220, 270 }; // 各目的地的飛行時間 (分鐘)

    // 當前航班陣列
    private static final Flight[] flights = new Flight[3];

    public static void main(String[] args)
    {
        // 一次性建立並顯示 3 個當前航班
        initializeFlights();

        // 初始化旅客物件, 並輸入相關資訊
        Passenger passenger = setupPassenger();
        // 旅客開始進行機場通關流程
        runAirportFlow(passenger);

        scanner.close();
    }

    // 建立 3 個航班，並顯示航班資訊
    private static void initializeFlights()
    {
        System.out.println("--- 當前機場航班看板 ---");
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

        System.out.print("1. 請輸入旅客姓名：");
        String name = scanner.nextLine();

        System.out.print("2. 請輸入護照上的姓名 (若未攜帶請直接略過)：");
        String passportName = scanner.nextLine();
        // null 表示沒帶護照
        Passport passport = passportName.trim().isEmpty() ? null : new Passport(passportName);

        System.out.print("3. 請輸入行李重量 (kg，輸入 0 代表無託運行李)：");
        double weight = scanner.nextDouble();

        System.out.print("4. 行李是否包含違禁品 (true / false)：");
        boolean hasProhibitedItems = scanner.nextBoolean();

        // 略過緩衝區的換行符
        scanner.nextLine();

        // 若重量大於 0 或有違禁品，才建立行李物件
        Baggage baggage = (weight > 0 || hasProhibitedItems) ? new Baggage(weight, hasProhibitedItems) : null;

        System.out.print("5. 請輸入機票購買人姓名：");
        String ticketOwner = scanner.nextLine();
        System.out.println("----------------------\n");

        // 指定旅客搭乘陣列的第一個航班
        Flight flight = flights[0];

        // 隨機抽取一個艙等
        CabinClass[] classes = CabinClass.values();
        CabinClass cabinClass = classes[random.nextInt(classes.length)];

        // 使用使用者輸入的 ticketOwner 建立機票
        Ticket ticket = new Ticket(flight.getNumber(), cabinClass, ticketOwner);

        // 建立並回傳完整的旅客物件
        return new Passenger(name, passport, baggage, ticket);
    }

    // 初始化機場關卡並進行通關流程
    private static void runAirportFlow(Passenger passenger)
    {
        // 假設旅客皆搭乘首個航班
        Flight flight = flights[0];

        // 建立機場的各個關卡
        Processable counter = new CheckInCounter(flight);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 通關流程開始
        System.out.println("--- 旅客抵達機場 ---");
        System.out.println("旅客所持機票航班: " + flight.getNumber());
        System.out.println("機票艙等: " + passenger.getTicket().getCabinClass());
        System.out.println("目的地: " + flight.getDestination());
        System.out.println("登機時間: " + flight.getBoardingTime());
        System.out.println("預計起飛時間: " + flight.getDepartureTime());
        System.out.println("預計抵達時間: " + flight.getArrivalTime());
        System.out.println("--------------------");

        // 集中攔截機場通關異常
        try
        {
            pause("報到櫃檯");
            counter.process(passenger);

            pause("安檢站");
            security.process(passenger);

            pause("登機門");
            gate.process(passenger);
        }
        catch (AirportException e)
        {
            System.out.println("通關流程異常中斷：" + e.getMessage() + "\n");
        }
    }

    // 系統訊息輔助函式
    private static void pause(String location)
    {
        System.out.print("\n[系統] 即將前往【" + location + "】, 按下Enter繼續...");
        scanner.nextLine();
    }

    // 隨機產生一個航班及其對應的登機時間
    private static Flight createRandomFlight()
    {
        int idx = random.nextInt(FLIGHT_NUMS.length);
        String flightNum = FLIGHT_NUMS[idx];
        String dest = DESTINATIONS[idx];
        int duration = DURATIONS[idx];

        // 隨機生成登機時間 (時: 6~22，分: 10的倍數)
        int hr = random.nextInt(17) + 6; // 6 到 22 時
        int min = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50 分
        LocalTime boardingTime = LocalTime.of(hr, min);

        return new Flight(flightNum, dest, boardingTime, duration);
    }

    // 顯示當前某航班的資訊
    private static void showFlightInfo(Flight f)
    {
        System.out
                .println("航班: " + f.getNumber() + " | 目的地: " + f.getDestination() + " | 登機時間: " + f.getBoardingTime());
    }
}