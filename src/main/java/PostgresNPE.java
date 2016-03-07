import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Driver that recreates NPE in TypeInfoCache.getSQLType
 */
public class PostgresNPE
{

    public void run()
    {
        final String jdbcUrlStr = "jdbc:postgresql://<yourHost>:5432/<yourDatabase>";
        final String user = "<yourUser>";
        final String pw = "<yourPassword>";
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch doneSignal = new CountDownLatch(1);

        Callable<String> thread1 = new CreateAndDrop("mytablename", jdbcUrlStr, user, pw);
        Callable<String> thread2 = new GetTypeInfo(doneSignal, jdbcUrlStr, user, pw);
        executor.submit(thread1);
        executor.submit(thread2);

        try
        {
            doneSignal.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        executor.shutdownNow();
        System.out.println("\nFinished all threads.");
    }

    public static void main(String[] args)
    {
        new PostgresNPE().run();
    }
}
