package model;

public class Baggage {
    private final double weight;
    private final boolean hasProhibitedItems; // 是否有違禁品

    // 建構子：設定行李重量與是否含違禁品
    public Baggage(double weight, boolean hasProhibitedItems) {
        this.weight = weight;
        this.hasProhibitedItems = hasProhibitedItems;
    }

    public double getWeight() {
        return weight;
    }

    public boolean hasProhibitedItems() {
        return hasProhibitedItems;
    }
}