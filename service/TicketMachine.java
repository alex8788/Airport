package service;

import java.util.List;
import model.Booking;
import model.CabinClass;
import model.Flight;
import util.InputUtil;

public class TicketMachine
{
    public void start(Flight[] flights, List<Booking> bookings)
    {
        System.out.println("=============================================");
        System.out.println("       [自助購票機] 歡迎使用購票系統");
        System.out.println("=============================================");

        // 1. 顯示並選擇航班
        Flight selected = selectFlight(flights);

        // 2. 顯示並選擇艙等
        CabinClass cabin = selectCabinClass();

        // 3. 輸入旅客身分並產生訂位紀錄
        createBooking(selected, cabin, bookings);

        System.out.println("\n[自助購票機] 購票成功！已將您的訂位資訊加入系統. ");
        System.out.println("=============================================\n");
    }

    // 選擇航班
    private Flight selectFlight(Flight[] flights)
    {
        System.out.println("--- 今日航班列表 ---");
        for (int i = 0; i < flights.length; i++)
        {
            System.out.printf("%d. %s飛往 %s (登機時間: %s)%n", (i + 1), flights[i].getNumber(), flights[i].getDestination(),
                    flights[i].getBoardingTime());
        }

        int fIdx = InputUtil.readInt("請選擇欲搭乘的航班 (1-" + flights.length + "): ", 1, flights.length) - 1;
        return flights[fIdx];
    }

    // 選擇艙等
    private CabinClass selectCabinClass()
    {
        System.out.println("\n--- 艙等選擇 ---");
        CabinClass[] classes = CabinClass.values();
        for (int i = 0; i < classes.length; i++)
        {
            System.out.printf("%d. %s (限重 %.1fkg)%n", (i + 1), classes[i], classes[i].getMaxWeight());
        }

        int cIdx = InputUtil.readInt("請選擇艙等 (1-" + classes.length + "): ", 1, classes.length) - 1;
        return classes[cIdx];
    }

    // 購票
    private void createBooking(Flight selected, CabinClass cabin, List<Booking> bookings)
    {
        System.out.println("\n--- 旅客身分登錄 ---");
        String name = InputUtil.readString("請輸入護照姓名: ", false);
        String passportNum = InputUtil.readString("請輸入護照號碼: ", false);

        bookings.add(new Booking(name, passportNum, selected, cabin));
    }
}