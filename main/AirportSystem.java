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

public class AirportSystem {
    public static void main(String[] args) {
        // 1. 建立包含起點、終點、時間的航班
        Flight flightBR198 = new Flight("BR198", "台北(TPE)", "東京(NRT)", "14:30");

        // 2. 建立關卡 (櫃檯現在需要知道是哪個航班才能劃位)
        Processable counter = new CheckInCounter(flightBR198);
        Processable security = new SecurityCheck(); // 新增安檢站
        Processable gate = new BoardingGate(flightBR198);

        // 3. 建立行李 (15kg，無違禁品) 與機票 (經濟艙)
        Baggage baggage = new Baggage(15.5, false);
        Ticket ticket = new Ticket("BR198", CabinClass.ECONOMY);

        // 4. 建立旅客 (加入護照號碼與行李)
        Passenger passenger = new Passenger("John", "A123456789", "TW987654321", baggage, ticket);

        // 流程開始
        System.out.println("--- 旅客抵達機場 ---");

        // 第一關：報到 (檢查行李、分配隨機座位)
        counter.process(passenger);

        // 第二關：安檢 (檢查護照、更新狀態)
        security.process(passenger);

        // 第三關：登機 (確認安檢通過)
        gate.process(passenger);
    }
}