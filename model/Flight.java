package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Flight
{
    private final String flightNumber;
    private final String destination;  // 新增：目的地
    private final LocalTime boardingTime;  // 新增：登機時間
    private final LocalTime departureTime;  // 新增：起飛時間 (由登機時間計算)
    private final LocalTime arrivalTime;  // 新增：抵達時間 (由登機時間與飛行時間計算)

    private final List<Passenger> boardedPassengers;  // 已登機的乘客列表
    private final Set<String> occupiedSeats; // 記錄已被佔用的座位

    // 時間格式化工具
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // 建構子：初始化航班資訊與容器
    public Flight(String flightNumber, String destination, LocalTime boardingTime, int flightDurationMinutes)
    {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.boardingTime = boardingTime;
        
        // 起飛時間設為定值：登機時間後 30 分鐘
        this.departureTime = boardingTime.plusMinutes(30);
        
        // 抵達時間：起飛時間 + 該目的地的飛行時間
        this.arrivalTime = this.departureTime.plusMinutes(flightDurationMinutes);
        
        this.boardedPassengers = new ArrayList<>();
        this.occupiedSeats = new HashSet<>();
    }

    // 隨機生成不重複座位的邏輯
    public String assignRandomSeat()
    {
        Random random = new Random();
        String[] seatLetters = { "A", "B", "C", "D", "E", "F" };
        String generatedSeat;

        // 只要座位被佔用，就重新生成
        do
        {
            int row = random.nextInt(30) + 1;  // 假設有 30 排 (1~30)
            String letter = seatLetters[random.nextInt(seatLetters.length)];
            generatedSeat = row + letter;
        }
        while (occupiedSeats.contains(generatedSeat));

        // 找到沒人坐的位置後，加入已佔用名單並回傳
        occupiedSeats.add(generatedSeat);
        return generatedSeat;
    }

    // 新增旅客到已登機名單並廣播
    public void addPassenger(Passenger passenger)
    {
        boardedPassengers.add(passenger);
        System.out.println("航班 " + flightNumber + " 廣播：" + passenger.getName() + " 歡迎登機！");
    }

    public String getFlightNumber()
    {
        return flightNumber;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getBoardingTimeStr()
    {
        return boardingTime.format(TIME_FORMATTER);
    }

    public String getDepartureTimeStr()
    {
        return departureTime.format(TIME_FORMATTER);
    }

    public String getArrivalTimeStr()
    {
        return arrivalTime.format(TIME_FORMATTER);
    }
}