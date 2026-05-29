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
    private static final Random RANDOM = new Random(); // 全域random物件
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); // 時間格式化工具

    private static final String[] SEAT_LETTERS =
    { "A", "B", "C", "D", "E", "F" };
    private static final int MAX_ROWS = 15; // 座位排數

    private final String number; // 航班編號
    private final String destination; // 目的地
    private final LocalTime boardingTime; // 登機時間
    private final LocalTime departureTime; // 起飛時間 (由登機時間計算)
    private final LocalTime arrivalTime; // 抵達時間 (由登機時間與飛行時間計算)

    private final List<Passenger> passengerList; // 已登機的乘客列表
    private final Set<String> occupiedSeats; // 已被劃位的座位集合

    // 建構子：初始化航班資訊
    public Flight(String flightNumber, String destination, LocalTime boardingTime, int duration)
    {
        this.number = flightNumber;
        this.destination = destination;
        this.boardingTime = boardingTime;

        // 起飛時間：設為定值（登機時間後 30 分鐘）
        this.departureTime = boardingTime.plusMinutes(30);

        // 抵達時間：起飛時間 + 飛行時間
        this.arrivalTime = this.departureTime.plusMinutes(duration);

        this.passengerList = new ArrayList<>();
        this.occupiedSeats = new HashSet<>();
    }

    // 輔助函式：隨機生成座位編號
    private String genSeatId()
    {
        int row = RANDOM.nextInt(MAX_ROWS) + 1;
        String letter = SEAT_LETTERS[RANDOM.nextInt(SEAT_LETTERS.length)];
        return row + letter;
    }

    // 隨機劃位
    public String assignSeat()
    {
        String newSeat;

        // 只要座位被佔用，就重新生成
        do
        {
            newSeat = genSeatId();
        } while (occupiedSeats.contains(newSeat));

        // 找到沒人坐的位置後，加入已佔用名單並回傳
        occupiedSeats.add(newSeat);
        return newSeat;
    }

    // 新增旅客到已登機名單並廣播
    public void addPassenger(Passenger passenger)
    {
        passengerList.add(passenger);
        System.out.println("  [航站廣播] 航班 " + number + " 旅客：" + passenger.getName() + " 已順利登機。");
    }

    // 分配其他旅客的座位
    public void preOccupySeats(int count)
    {
        while (occupiedSeats.size() < count)
        {
            int row = RANDOM.nextInt(MAX_ROWS) + 1;
            String letter = SEAT_LETTERS[RANDOM.nextInt(SEAT_LETTERS.length)];
            occupiedSeats.add(row + letter);
        }
    }

    // 繪製並打印航班的座位狀態表
    public void showSeatMap(String passengerSeat)
    {
        System.out.println("  [地勤報到系統] 航班 " + number + " 座位配置圖 (X: 已佔用, .: 空位, P: 您的座位)：");

        // 打印頂部的排數編號 (01 ~ 15)
        System.out.print("    ");
        for (int row = 1; row <= MAX_ROWS; row++)
            System.out.printf(" %02d ", row);
        System.out.println();

        // 外層迴圈控制橫列 (A ~ F)
        for (int i = 0; i < SEAT_LETTERS.length; i++)
        {
            String letter = SEAT_LETTERS[i];
            System.out.print("  " + letter + " ");

            // 內層迴圈印出該字母對應的排數
            for (int row = 1; row <= MAX_ROWS; row++)
            {
                String seat = row + letter;

                if (seat.equals(passengerSeat)) // 該旅客
                    System.out.print("[P] ");
                else if (occupiedSeats.contains(seat)) // 其他乘客
                    System.out.print("[X] ");
                else
                    System.out.print("[ ] "); // 空位
            }
            System.out.println();

            // 模擬雙走道 (2-2-2 配置)
            if (i == 1 || i == 3)
                System.out.println();
        }
        System.out.println();
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