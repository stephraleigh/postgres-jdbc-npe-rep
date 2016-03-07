import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.Callable;

public class CreateAndDrop
    implements Callable<String>
{
    private String name;
    private String jdbcUrlStr;
    private String user;
    private String pw;

    public CreateAndDrop(String tableName, String jdbcUrlStr, String user, String pw)
    {
        this.name = tableName;
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
                System.out.println("drop/create loop");
                stmt.execute("drop schema if exists " + name + " cascade");
                stmt.execute("create schema if not exists " + name);
                stmt.execute("CREATE TABLE " + name + "." + name
                        + "( user_id serial PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, email VARCHAR (355) UNIQUE NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return "Done";
    }
}
