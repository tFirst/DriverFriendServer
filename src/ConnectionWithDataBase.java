import java.sql.*;

public class ConnectionWithDataBase {

    private static Connection connection;
    private String lineOut;

    ConnectionWithDataBase(String args) throws SQLException {
        connection = getConnection();
        lineOut = getInformation(args);
    }

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        return DriverManager.getConnection("jdbc:mysql://90.189.192.217:3306/sv", "sv", "6fBwWohC");
    }

    public String getInformation(String lineIn) throws SQLException {
        String line = null;
        try {
            System.out.println("Entered in try...");
            System.out.println(lineIn);
            if (lineIn.equals("fillingspinner"))
                line = getTypesOfEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

    private String getTypesOfEvents() throws SQLException {
        String line = "";
        PreparedStatement preparedStatement =
                connection.prepareStatement("select name from TypesOfEvents");
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Getted the response...");
        while (resultSet.next()) {
            if (!resultSet.last())
                line += resultSet.getString("name") + ",";
            else
                line += resultSet.getString("name");
        }
        return line;
    }

    public String getLineOut() { return lineOut; }
}

