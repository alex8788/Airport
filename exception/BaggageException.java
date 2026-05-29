package exception;

import model.Baggage;
import model.BoardingPass;

public class BaggageException extends AirportException
{
    // 建構子 1：處理「違禁品」異常
    public BaggageException(String location)
    {
        super(location + "失敗：行李違規。內含違禁品！");
    }

    // 建構子 2：處理「行李超重」異常
    public BaggageException(String location, Baggage baggage, BoardingPass pass)
    {
        super(String.format("%s失敗：行李違規。行李過重 (%.1fkg)，您的 %s 艙等載重上限為 %.1fkg！", location, baggage.getWeight(),
                pass.getCabinClass(), pass.getCabinClass().getMaxWeight()));
    }
}