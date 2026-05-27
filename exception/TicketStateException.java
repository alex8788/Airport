package exception;

import model.TicketState;

public class TicketStateException extends AirportException {
    // 錯誤訊息模板，包含發生位置、預期狀態、實際狀態
    public TicketStateException(String location, TicketState expected, TicketState actual) {
        super(location + "失敗：機票狀態異常。預期應為 " + expected + "，但目前為 " + actual + "！");
    }
}