package model;

public class Baggage
{
    private final double weight; // 行李重量
    private final boolean hasContraband; // 是否有違禁品

    // 建構子：設定行李重量與是否含違禁品
    public Baggage(double weight, boolean hasContraband)
    {
        this.weight = weight;
        this.hasContraband = hasContraband;
    }

    public double getWeight()
    {
        return weight;
    }

    public boolean hasContraband()
    {
        return hasContraband;
    }
}