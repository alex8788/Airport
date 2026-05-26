package main;

import java.time.LocalTime;
import java.util.Random;
import model.Baggage;
import model.CabinClass;
import model.Flight;
import model.Passenger;
import model.Ticket;
import service.BoardingGate;
import service.CheckInCounter;
import service.Processable;
import service.SecurityCheck;

public class AirportSystem
{
    // 1. 生成航班集合/陣列，存放定期航班資訊 (一律由台北出發，擴充至 6 個航班)
    private static final String[] FLIGHT_NUMS = { "BR198", "BR159", "CI909", "CI751", "JX800", "CX421" };
    private static final String[] DESTINATIONS = { "東京(NRT)", "首爾(ICN)", "香港(HKG)", "新加坡(SIN)", "曼谷(BKK)", "吉隆坡(KUL)" };
    private static final int[] DURATIONS = { 200, 150, 100, 280, 220, 270 };  // 各目的地的飛行時間 (分鐘)

    public static void main(String[] args)
    {
        Random random = new Random();

        // 隨機為旅客抽取一組航班與目的地
        int idx = random.nextInt(FLIGHT_NUMS.length);
        String flightNum = FLIGHT_NUMS[idx];
        String dest = DESTINATIONS[idx];
        int duration = DURATIONS[idx];

        // 隨機生成登機時間 (時: 6~22，分: 10的倍數)
        int hr = random.nextInt(17) + 6;  // 6 到 22 時
        int min = random.nextInt(6) * 10;  // 0, 10, 20, 30, 40, 50 分
        LocalTime boardingTime = LocalTime.of(hr, min);

        // 建立包含終點、時間的航班 (內部會自動由登機時間計算起飛與抵達時間)
        Flight flight = new Flight(flightNum, dest, boardingTime, duration);
        
        // 2. 建立機場的各個關卡
        Processable counter = new CheckInCounter(flight);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 3. 旅客抵達機場前，已經買好機票並整理好行李 (使用隨機抽取到的航班建立機票)
        Baggage baggage = new Baggage(15.5, false);  // 行李 15.5kg，無違禁品
        Ticket ticket = new Ticket(flightNum, CabinClass.ECONOMY, "John");  // 預先購買好經濟艙，姓名登記為 John
        
        // 4. 旅客帶著護照、行李與機票抵達機場
        Passenger passenger = new Passenger("John", true, baggage, ticket);

        // 流程開始
        System.out.println("--- 旅客抵達機場 ---");
        System.out.println("旅客所持機票航班: " + flight.getFlightNumber());
        System.out.println("目的地: " + flight.getDestination());
        System.out.println("登機時間: " + flight.getBoardingTimeStr());
        System.out.println("預計起飛時間: " + flight.getDepartureTimeStr());
        System.out.println("預計抵達時間: " + flight.getArrivalTimeStr());
        System.out.println("--------------------");
        
        // 第一關：報到 (櫃檯檢查行李，並為已訂位的機票分配隨機座位)
        counter.process(passenger);

        // 第二關：安檢 (檢查護照，確認機票狀態為已報到)
        security.process(passenger);

        // 第三關：登機 (確認安檢通過，正式登機)
        gate.process(passenger);
    }
}