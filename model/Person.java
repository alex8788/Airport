package model;

public abstract class Person {
    private final String name; // 加入 final
    private final String idNumber; // 加入 final

    // 建構子：設定姓名與身分證號
    public Person(String name, String idNumber) {
        this.name = name;
        this.idNumber = idNumber;
    }

    // 取得姓名
    public String getName() {
        return name;
    }
}