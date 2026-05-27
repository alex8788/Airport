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
    private final String number; // 航班編號
    private final String destination; // 目的地
    private final LocalTime boardingTime; // 登機時間
    private final LocalTime departureTime; // 起飛時間 (由登機時間計算)
    private final LocalTime arrivalTime; // 抵達時間 (由登機時間與飛行時間計算)

    private final List<Passenger> boardedPassengers; // 已登機的乘客列表
    private final Set<String> occupiedSeats; // 已被劃位的座位集合

    // 時間格式化工具
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // 建構子：初始化航班資訊與容器
    public Flight(String flightNumber, String destination, LocalTime boardingTime, int duration)
    {
        this.number = flightNumber;
        this.destination = destination;
        this.boardingTime = boardingTime;

        // 起飛時間：設為定值（登機時間後 30 分鐘）
        this.departureTime = boardingTime.plusMinutes(30);

        // 抵達時間：起飛時間 + 飛行時間
        this.arrivalTime = this.departureTime.plusMinutes(duration);

        this.boardedPassengers = new ArrayList<>();
        this.occupiedSeats = new HashSet<>();
    }

    // 隨機分配一個空的座位
    public String assignRandomSeat()
    {
        Random random = new Random();
        String[] seatLetters =
        { "A", "B", "C", "D", "E", "F" };
        String newSeat;

        // 只要座位被佔用，就重新生成
        do
        {
            int row = random.nextInt(30) + 1; // 假設有 30 排 (1~30)
            String letter = seatLetters[random.nextInt(seatLetters.length)];
            newSeat = row + letter;
        } while (occupiedSeats.contains(newSeat));

        // 找到沒人坐的位置後，加入已佔用名單並回傳
        occupiedSeats.add(newSeat);
        return newSeat;
    }

    // 新增旅客到已登機名單並廣播
    public void addPassenger(Passenger passenger)
    {
        boardedPassengers.add(passenger);
        System.out.println("航班 " + number + " 廣播：" + passenger.getName() + " 歡迎登機！");
    }

    public String getNumber()
    {
        return number;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getBoardingTime()
    {
        return boardingTime.format(TIME_FORMATTER);
    }

    public String getDepartureTime()
    {
        return departureTime.format(TIME_FORMATTER);
    }

    public String getArrivalTime()
    {
        return arrivalTime.format(TIME_FORMATTER);
    }
}