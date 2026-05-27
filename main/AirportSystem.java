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
    // 全域的輔助物件
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // 航線列舉：包含目的地與飛行時間
    private enum Route
    {
        BR198("東京(NRT)", 200),
        BR159("首爾(ICN)", 150),
        CI909("香港(HKG)", 100),
        CI751("新加坡(SIN)", 280),
        JX800("曼谷(BKK)", 220),
        CX421("吉隆坡(KUL)", 270);

        final String destination;
        final int duration;

        Route(String destination, int duration)
        {
            this.destination = destination;
            this.duration = duration;
        }
    }
    
    // 當前航班陣列
    private static final Flight[] flights = new Flight[3];

    public static void main(String[] args)
    {
        initializeFlights();
        Passenger passenger = setupPassenger();
        runAirportFlow(passenger);
    }

    // 建立當期個航線(3條)，並印出航班資訊
    private static void initializeFlights()
    {
        System.out.println("--- 機場航班看板 ---");
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

        String name = readString("1. 請輸入旅客姓名：", false);

        String passportName = readString("2. 請輸入護照上的姓名 (若未攜帶請直接略過)：", true);
        Passport passport = passportName.trim().isEmpty() ? null : new Passport(passportName);

        double weight = readNonNegDouble("3. 請輸入行李重量 (kg，輸入 0 代表無託運行李)：");

        boolean hasProhibitedItems = readBool("4. 行李是否包含違禁品 (true / false)：");

        // 若重量大於 0 才建立行李物件
        Baggage baggage = (weight > 0) ? new Baggage(weight, hasProhibitedItems) : null;

        String ticketOwner = readString("5. 請輸入機票購買人姓名：", false);
        System.out.println("----------------------\n");

        // 指定旅客搭乘陣列的第一個航班
        Flight flight = flights[0];

        // 分配其他乘客的座位 (預設 30 人)
        flight.preOccupySeats(30);

        // 分配一個艙等
        CabinClass[] classes = CabinClass.values();
        CabinClass cabinClass = classes[random.nextInt(classes.length)];

        // 依據輸入的購買人姓名產生機票
        Ticket ticket = new Ticket(flight.getNumber(), cabinClass, ticketOwner);

        // 建立並回傳完整的旅客物件
        return new Passenger(name, passport, baggage, ticket);
    }

    // 初始化機場關卡, 讓旅客進行通關流程
    private static void runAirportFlow(Passenger passenger)
    {
        // 假設旅客皆搭乘首個航班
        Flight flight = flights[0];

        // 建立機場的各個關卡
        Processable counter = new CheckInCounter(flight);
        Processable security = new SecurityCheck();
        Processable gate = new BoardingGate(flight);

        // 顯示旅客訊息
        System.out.println("\n=============================================");
        System.out.println("       [桃園國際機場 (TPE) 離境通關系統]       ");
        System.out.println("=============================================");
        System.out.printf(" 旅客姓名: %-10s | 艙等: %s%n", passenger.getName(), passenger.getTicket().getCabinClass());
        System.out.printf(" 航班編號: %-10s | 目的地: %s%n", flight.getNumber(), flight.getDestination());
        System.out.printf(" 登機時間: %-10s | 起飛時間: %s%n", flight.getBoardingTime(), flight.getDepartureTime());
        System.out.println("=============================================\n");

        // 集中攔截機場通關異常
        try
        {
            pause("報到櫃檯");
            counter.process(passenger);

            pause("安檢站");
            security.process(passenger);

            pause("登機門");
            gate.process(passenger);

            System.out.println("=============================================");
            System.out.println(" [航站廣播] 旅客 " + passenger.getName() + " 已順利登機，祝您旅途愉快！");
            System.out.println("=============================================\n");
        }
        catch (AirportException e)
        {
            System.out.println(" [系統攔截] 通關程序終止: " + e.getMessage() + "\n");
            System.out.println("=============================================\n");
        }
    }

    // 隨機產生一個航班物件
    private static Flight createRandomFlight()
    {
        // 隨機抽取一條航線
        Route[] routes = Route.values();
        Route route = routes[random.nextInt(routes.length)];

        // 隨機生成登機時間 (時: 6~22，分: 10的倍數)
        int hr = random.nextInt(17) + 6; // 6 到 22 時
        int min = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50 分
        LocalTime boardingTime = LocalTime.of(hr, min);

        return new Flight(route.name(), route.destination, boardingTime, route.duration);
    }

    // 顯示當前某航班的資訊
    private static void showFlightInfo(Flight f)
    {
        System.out.printf("航班: %s | 目的地: %s | 登機時間: %s%n", 
                f.getNumber(), f.getDestination(), f.getBoardingTime());
    }
    
    // 輔助函式：系統訊息的停頓
    private static void pause(String location)
    {
        System.out.print("[系統提示] 旅客正前往【" + location + "】... 請按 Enter 鍵確認以繼續 >>> ");
        scanner.nextLine();
    }

    // 輔助函式：讀取合法數值 (非負)
    private static double readNonNegDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try
            {
                double value = Double.parseDouble(input);
                if (value >= 0)
                    return value;

                System.out.println("  [輸入錯誤] 重量不能為負數，請重新輸入！\n");
            }
            catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！\n");
            }
        }
    }

    // 輔助函式：讀取合法字串 (可控制輸入空字串)
    private static String readString(String prompt, boolean allowEmpty)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine();
            
            if (allowEmpty || !input.trim().isEmpty())
                return input;

            System.out.println("  [輸入錯誤] 此欄位不能為空，請重新輸入！\n");
        }
    }

    // 輔助函式：讀取違禁品狀態
    private static boolean readBool(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("true"))
                return true;
            if (input.equals("false"))
                return false;
            
            System.out.println("  [輸入錯誤] 請輸入 true 或 false！\n");
        }
    }
}