package service;

import java.util.List;
import java.util.Scanner;
import model.Booking;
import model.CabinClass;
import model.Flight;

public class TicketMachine
{
    private static final Scanner scanner = new Scanner(System.in);

    public void start(Flight[] flights, List<Booking> bookings)
    {
        System.out.println("=============================================");
        System.out.println("       [自助購票機] 歡迎使用購票系統");
        System.out.println("=============================================");

        // 1. 顯示並選擇航班
        System.out.println("--- 今日航班列表 ---");
        for (int i = 0; i < flights.length; i++)
        {
            System.out.printf("%d. %s飛往 %s (登機時間: %s)%n",
                (i + 1), flights[i].getNumber(), flights[i].getDestination(), flights[i].getBoardingTime());
        }

        int fIdx = readInt("請選擇欲搭乘的航班 (1-" + flights.length + "): ", 1, flights.length) - 1;
        Flight selected = flights[fIdx];

        // 2. 顯示並選擇艙等
        System.out.println("\n--- 艙等選擇 ---");
        CabinClass[] classes = CabinClass.values();
        for (int i = 0; i < classes.length; i++)
        {
            System.out.printf("%d. %s (限重 %.1fkg)%n", (i + 1), classes[i], classes[i].getMaxWeight());
        }
        
        int cIdx = readInt("請選擇艙等 (1-" + classes.length + "): ", 1, classes.length) - 1;
        CabinClass cabin = classes[cIdx];

        // 3. 輸入旅客身分
        System.out.println("\n--- 旅客身分登錄 ---");
        String name = readString("請輸入護照姓名: ");
        String passportNum = readString("請輸入護照號碼: ");

        // 4. 產生訂位紀錄
        bookings.add(new Booking(name, passportNum, selected, cabin));

        System.out.println("\n[自助購票機] 購票成功！已將您的訂位資訊加入系統。");
        System.out.println("=============================================\n");
    }

    // 輔助函式：讀取整數 (可選範圍)
    private int readInt(String prompt, int min, int max)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("  [輸入錯誤] 請輸入 " + min + " 到 " + max + " 之間的數字！");
            }
            catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！");
            }
        }
    }

    // 輔助函式：讀取非空字串
    private String readString(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [輸入錯誤] 此欄位不能為空，請重新輸入！");
        }
    }
}