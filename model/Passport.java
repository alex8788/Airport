package model;

public class Passport
{
    private final String holderName;  // 護照持有者姓名

    // 建構子：初始化護照持有者姓名
    public Passport(String holderName)
    {
        this.holderName = holderName;
    }

    public String getHolderName()
    {
        return holderName;
    }
}