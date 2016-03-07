import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class GetTypeInfo
    implements Callable<String>
{
    private final CountDownLatch doneSignal;
    private final String jdbcUrlStr;
    private final String user;
    private final String pw;

    public GetTypeInfo(CountDownLatch doneSignal, String jdbcUrlStr, String user, String pw)
    {
        this.doneSignal = doneSignal;
        this.jdbcUrlStr = jdbcUrlStr;
        this.user = user;
        this.pw = pw;
    }

    @Override
    public String call()
        throws Exception
    {
        try (Connection connection = DriverManager.getConnection(jdbcUrlStr, user, pw);
                Statement stmt = connection.createStatement();)
        {
            while (!Thread.currentThread().isInterrupted())
            {
                connection.getMetaData().getTypeInfo();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            doneSignal.countDown();
        }
        return "Done";
    }
}
