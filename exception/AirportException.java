package exception;

public class AirportException extends RuntimeException
{
    // 接收錯誤訊息
    public AirportException(String message)
    {
        super(message);
    }
}