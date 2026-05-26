package service;

import model.Passenger;

public interface Processable {
    // 處理旅客的介面方法，實作類別會提供實際邏輯
    void process(Passenger passenger);
}