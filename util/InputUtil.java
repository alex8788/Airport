package util;

import java.util.Random;
import java.util.Scanner;

public class InputUtil
{
    // 隨機數產生器
    public static final Random RANDOM = new Random();
    // 標準輸入工具
    public static final Scanner SCANNER = new Scanner(System.in);

    // 讀取字串 (可控制是否允許空值)
    public static String readString(String prompt, boolean allowEmpty)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = SCANNER.nextLine();

            if (allowEmpty || !input.trim().isEmpty())
                return input.trim();

            System.out.println("  [輸入錯誤] 此欄位不能為空，請重新輸入！\n");
        }
    }

    // 讀取整數 (指定範圍)
    public static int readInt(String prompt, int min, int max)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                int val = Integer.parseInt(SCANNER.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("  [輸入錯誤] 請輸入 " + min + " 到 " + max + " 之間的數字！");
            }
            catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！");
            }
        }
    }

    // 讀取非負數值
    public static double readNonNegDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                double val = Double.parseDouble(SCANNER.nextLine().trim());
                if (val >= 0) return val;
                System.out.println("  [輸入錯誤] 重量不能為負數！");
            }
            catch (NumberFormatException e)
            {
                System.out.println("  [輸入錯誤] 請輸入有效的數字格式！");
            }
        }
    }

    // 讀取合法布林值
    public static boolean readBool(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim().toLowerCase();

            if (input.equals("true")) return true;
            if (input.equals("false")) return false;

            System.out.println("  [輸入錯誤] 請輸入 true 或 false！");
        }
    }
}