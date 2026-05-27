package exception;

public class IdentityException extends AirportException
{
    // 接收「發生關卡」、「旅客姓名」與「證件姓名」
    public IdentityException(String location, String expectedName, String actualName)
    {
        super(String.format("%s失敗：身分核對異常。旅客姓名為 [%s]，但證件登記為 [%s]！", 
                location, expectedName, actualName));
    }
}