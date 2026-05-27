package model;

public class Passport
{
    private final String name; // 護照持有者姓名

    // 建構子：初始化護照持有者姓名
    public Passport(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}