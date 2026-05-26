package main;

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
    public static void main(String[] args)
    {
        // 1. 建立包含起點、終點、時間的航班
        Flight flightBR198 = new Flight("BR198", "台北(TPE)", "東京(NRT)", "14:30");
        
        // 2. 建立機場的各個處理關卡
        Processable counter = new CheckInCounter(flightBR198);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flightBR198);

        // 3. 旅客抵達機場前，已經買好機票並整理好行李
        Baggage baggage = new Baggage(15.5, false);  // 行李 15.5kg，無違禁品
        Ticket ticket = new Ticket("BR198", CabinClass.ECONOMY);  // 預先購買好經濟艙，尚未劃位
        
        // 4. 旅客帶著護照、行李與機票抵達機場
        Passenger passenger = new Passenger("John", "A123456789", "TW987654321", baggage, ticket);

        // 流程開始
        System.out.println("--- 旅客抵達機場 ---");
        
        // 第一關：報到 (櫃檯檢查行李，並為已訂位的機票分配隨機座位)
        counter.process(passenger);

        // 第二關：安檢 (檢查護照，確認機票狀態為已報到)
        security.process(passenger);

        // 第三關：登機 (確認安檢通過，正式登機)
        gate.process(passenger);
    }
}