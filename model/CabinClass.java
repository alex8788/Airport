package model;

public enum CabinClass
{
    ECONOMY(20.0), // 經濟艙限重 20.0 kg
    BUSINESS(30.0), // 商務艙限重 30.0 kg
    FIRST(40.0); // 頭等艙限重 40.0 kg

    private final double maxWeight;

    CabinClass(double maxWeight)
    {
        this.maxWeight = maxWeight;
    }

    public double getMaxWeight()
    {
        return maxWeight;
    }
}