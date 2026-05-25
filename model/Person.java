package model;

public abstract class Person {
    private final String name; // 加入 final
    private final String idNumber; // 加入 final

    public Person(String name, String idNumber) {
        this.name = name;
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }
}