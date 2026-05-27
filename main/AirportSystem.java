package main;

import exception.AirportException;
import java.time.LocalTime;
import java.util.Random;
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

    // 航班陣列，存放定期航班資訊 (一律由台北出發，6 個航班)
    private static final String[] FLIGHT_NUMS =
    { "BR198", "BR159", "CI909", "CI751", "JX800", "CX421" }; // 航班編號
    private static final String[] DESTINATIONS =
    { "東京(NRT)", "首爾(ICN)", "香港(HKG)", "新加坡(SIN)", "曼谷(BKK)", "吉隆坡(KUL)" }; // 目的地
    private static final int[] DURATIONS =
    { 200, 150, 100, 280, 220, 270 }; // 各目的地的飛行時間 (分鐘)

    // 當前航班陣列
    private static final Flight[] currentFlights = new Flight[3];

    public static void main(String[] args)
    {
        // 一次性建立並顯示 3 個當前航班
        initializeFlights();

        // 建立旅客的所有相關資訊
        Passenger passenger = createPassenger("John");

        // 旅客開始進行機場通關流程
        runAirportFlow(passenger);
    }

    // 建立 3 個航班並儲存至陣列，並顯示航班資訊
    private static void initializeFlights()
    {
        System.out.println("--- 當前機場航班看板 ---");
        for (int i = 0; i < currentFlights.length; i++)
        {
            currentFlights[i] = createRandomFlight();
            showFlightInfo(currentFlights[i]);
        }
        System.out.println("------------------------\n");
    }

    // 建立旅客物件
    private static Passenger createPassenger(String name)
    {
        // 從當前航班中抽一個作為該名旅客的航班
        Flight flight = currentFlights[random.nextInt(currentFlights.length)];

        // 隨機抽取一個艙等
        CabinClass[] classes = CabinClass.values();
        CabinClass randomCabinClass = classes[random.nextInt(classes.length)];

        // 旅客抵達機場前，已經買好機票並整理好行李
        Baggage baggage = new Baggage(15.5, false); // 行李 15.5kg，無違禁品
        Ticket ticket = new Ticket(flight.getFlightNumber(), randomCabinClass, name); // 預先購買好的機票

        // 預先準備好的護照
        Passport passport = new Passport(name);

        // 旅客帶著護照、行李與機票抵達機場
        return new Passenger(name, passport, baggage, ticket);
    }

    // 初始化機場關卡並進行通關流程
    private static void runAirportFlow(Passenger passenger)
    {
        // 機票上的航班編號
        Flight flight = getFlightByNumber(passenger.getTicket().getFlightNumber());

        if (flight == null)
        {
            System.out.println("系統錯誤：找不到機票對應的航班！\n");
            return;
        }

        // 建立機場的各個關卡
        Processable counter = new CheckInCounter(flight);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 通關流程開始
        System.out.println("--- 旅客抵達機場 ---");
        System.out.println("旅客所持機票航班: " + flight.getFlightNumber());
        System.out.println("機票艙等: " + passenger.getTicket().getCabinClass());
        System.out.println("目的地: " + flight.getDestination());
        System.out.println("登機時間: " + flight.getBoardingTimeStr());
        System.out.println("預計起飛時間: " + flight.getDepartureTimeStr());
        System.out.println("預計抵達時間: " + flight.getArrivalTimeStr());
        System.out.println("--------------------");

        // 集中攔截機場通關異常
        try
        {
            // 第一關：報到 (櫃檯檢查行李，並為已訂位的機票分配隨機座位)
            counter.process(passenger);

            // 第二關：安檢 (檢查護照，確認機票狀態為已報到)
            security.process(passenger);

            // 第三關：登機 (確認安檢通過，正式登機)
            gate.process(passenger);
        } catch (AirportException e)
        {
            System.out.println("❌ 通關流程異常中斷：" + e.getMessage() + "\n");
        }
    }

    // 根據航班編號從陣列中取得航班物件
    private static Flight getFlightByNumber(String flightNumber)
    {
        for (Flight f : currentFlights)
        {
            if (f != null && f.getFlightNumber().equals(flightNumber))
                return f;
        }
        return null;
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
        System.out.println(
                "航班: " + f.getFlightNumber() + " | 目的地: " + f.getDestination() + " | 登機時間: " + f.getBoardingTimeStr());
    }
}