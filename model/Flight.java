package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Flight {
    private final String flightNumber;
    private final String origin; // 新增：起點
    private final String destination; // 新增：目的地
    private final String arrivalTime; // 新增：抵達時間

    private final List<Passenger> boardedPassengers;
    private final Set<String> occupiedSeats; // 新增：記錄已被佔用的座位

    // 建構子：初始化航班資訊與容器
    public Flight(String flightNumber, String origin, String destination, String arrivalTime) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.boardedPassengers = new ArrayList<>();
        this.occupiedSeats = new HashSet<>();
    }

    // 隨機生成不重複座位的邏輯
    public String assignRandomSeat() {
        Random random = new Random();
        String[] seatLetters = { "A", "B", "C", "D", "E", "F" };
        String generatedSeat;

        // 只要座位被佔用，就重新生成
        do {
            int row = random.nextInt(30) + 1; // 假設有 30 排 (1~30)
            String letter = seatLetters[random.nextInt(seatLetters.length)];
            generatedSeat = row + letter;
        } while (occupiedSeats.contains(generatedSeat));

        // 找到沒人坐的位置後，加入已佔用名單並回傳
        occupiedSeats.add(generatedSeat);
        return generatedSeat;
    }

    // 新增旅客到已登機名單並公告
    public void addPassenger(Passenger passenger) {
        boardedPassengers.add(passenger);
        System.out.println("航班 " + flightNumber + " 廣播：" + passenger.getName() + " 歡迎登機！");
    }
}