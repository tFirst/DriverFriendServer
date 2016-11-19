import java.sql.*;

public class ConnectionWithDataBase {

    private static Connection connection;
    private String lineOut;

    ConnectionWithDataBase(String args) throws SQLException {
        connection = getConnection();
        lineOut = getTypesOfEvents();
        //insertIntoTable(args);
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
                connection.prepareStatement("select name from TypesOfEvents where id = 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Getted the response...");
        while (resultSet.next()) {
//            if (!resultSet.last())
//                line += resultSet.getString("name") + ",";
//            else
//                line += resultSet.getString("name");
            line = resultSet.getString("name");
        }
        return line;
    }

    public void insertIntoTable(String line) throws SQLException {
        int resIdType = 0, resCount = 0;
        String[] s = line.split(":");
        if (s[0].equals("events")) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select id from TypesOfEvents order by id");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.last()) {
                resCount = resultSet.getInt("id");
            }
            preparedStatement =
                    connection.prepareStatement("select id from typesOfEvents where name = ?");
            preparedStatement.setString(0, s[1]);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resIdType = resultSet.getInt("id");
            }

            preparedStatement =
                    connection.prepareStatement("insert into events(id, type, address, description) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, resCount+1);
            preparedStatement.setInt(2, resIdType);
            preparedStatement.setString(3, s[2]);
            preparedStatement.setString(4, s[3]);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Insert!!!");
        }
    }

    public String getLineOut() { return lineOut; }
}

