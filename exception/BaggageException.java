package exception;

import model.Baggage;
import model.Ticket;

public class BaggageException extends AirportException
{
    // 建構子 1：處理「違禁品」異常
    public BaggageException(String location)
    {
        super(location + "失敗：行李違規。內含違禁品！");
    }

    // 建構子 2：處理「行李超重」異常
    public BaggageException(String location, Baggage baggage, Ticket ticket)
    {
        super(location + "失敗：行李違規。行李過重 (" + baggage.getWeight() + "kg)，您的 " + ticket.getCabinClass() + " 艙等載重上限為 "
                + ticket.getCabinClass().getMaxWeight() + "kg！");
    }
}