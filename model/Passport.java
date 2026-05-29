package model;

public class Passport
{
    private final String name; // 護照持有者姓名
    private final String number; // 護照號碼

    // 建構子：初始化護照持有者姓名
    public Passport(String name, String number)
    {
        this.name = name;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }
    
    public String getNumber()
    {
        return number;
    }
}