package exception;

public class DocumentException extends AirportException
{
    // 接收「發生關卡」與「缺失的證件名稱」
    public DocumentException(String location, String document)
    {
        super(location + "：未攜帶" + document + "！");
    }
}