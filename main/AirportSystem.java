package main;

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
    // 航班陣列，存放定期航班資訊 (一律由台北出發，6 個航班)
    private static final String[] FLIGHT_NUMS = { "BR198", "BR159", "CI909", "CI751", "JX800", "CX421" };  // 航班編號
    private static final String[] DESTINATIONS = { "東京(NRT)", "首爾(ICN)", "香港(HKG)", "新加坡(SIN)", "曼谷(BKK)", "吉隆坡(KUL)" };  // 目的地
    private static final int[] DURATIONS = { 200, 150, 100, 280, 220, 270 };  // 各目的地的飛行時間 (分鐘)

    public static void main(String[] args)
    {
        Random random = new Random();

        System.out.println("--- 當前機場航班看板 ---");
        // 隨機生成三個獨立的航班並顯示出來
        Flight f1 = createFlight(random);
        Flight f2 = createFlight(random);
        Flight f3 = createFlight(random);

        showFlights(f1);
        showFlights(f2);
        showFlights(f3);
        System.out.println("------------------------\n");

        // 從當前三個航班中，抽取一個作為該名旅客的航班
        Flight[] availableFlights = { f1, f2, f3 };
        Flight passengerFlight = availableFlights[random.nextInt(availableFlights.length)];

        // 建立旅客的所有相關資訊
        Passenger passenger = createPassenger(random, "John", passengerFlight);

        // 旅客開始進行機場通關流程
        runAirportFlow(passenger, passengerFlight);
    }

    // 建立旅客物件
    private static Passenger createPassenger(Random random, String name, Flight flight)
    {
        // 隨機抽取一個艙等
        CabinClass[] classes = CabinClass.values();
        CabinClass randomCabinClass = classes[random.nextInt(classes.length)];

        // 旅客抵達機場前，已經買好機票並整理好行李
        Baggage baggage = new Baggage(15.5, false);  // 行李 15.5kg，無違禁品
        Ticket ticket = new Ticket(flight.getFlightNumber(), randomCabinClass, name);  // 預先購買好的機票

        // 預先準備好的護照
        Passport passport = new Passport(name);
        
        // 旅客帶著護照、行李與機票抵達機場
        return new Passenger(name, passport, baggage, ticket);
    }

    // 初始化機場關卡並進行通關流程
    private static void runAirportFlow(Passenger passenger, Flight flight)
    {
        // 建立機場的各個關卡 (使用旅客搭乘的航班)
        Processable counter = new CheckInCounter(flight);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 流程開始
        System.out.println("--- 旅客抵達機場 ---");
        System.out.println("旅客所持機票航班: " + flight.getFlightNumber());
        System.out.println("機票艙等: " + passenger.getTicket().getCabinClass());
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

    // 隨機抽取航班及其對應的登機時間
    private static Flight createFlight(Random random)
    {
        int idx = random.nextInt(FLIGHT_NUMS.length);
        String flightNum = FLIGHT_NUMS[idx];
        String dest = DESTINATIONS[idx];
        int duration = DURATIONS[idx];

        // 隨機生成登機時間 (時: 6~22，分: 10的倍數)
        int hr = random.nextInt(17) + 6;  // 6 到 22 時
        int min = random.nextInt(6) * 10;  // 0, 10, 20, 30, 40, 50 分
        LocalTime boardingTime = LocalTime.of(hr, min);

        return new Flight(flightNum, dest, boardingTime, duration);
    }

    // 顯示當前的航班資訊
    private static void showFlights(Flight f)
    {
        System.out.println("航班: " + f.getFlightNumber() + " | 目的地: " + f.getDestination() + " | 登機時間: " + f.getBoardingTimeStr());
    }
}