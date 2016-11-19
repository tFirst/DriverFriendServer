import java.sql.*;

public class ConnectionWithDataBase {

    private static Connection connection;
    private String lineOut;

    ConnectionWithDataBase(String action, String args) throws SQLException {
        connection = getConnection();
        if (action.equals("insert")) {
            insertIntoTable(args);
        }
        else if (action.equals("select")) {
            selectFromTable(args);
        }
    }

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        return DriverManager.getConnection("jdbc:mysql://90.189.192.217:3306/sv", "sv", "6fBwWohC");
    }

    private void selectFromTypesOfEvents(String type) throws SQLException {
        String line = "";
        PreparedStatement preparedStatement =
                connection.prepareStatement("select id from TypesOfEvents where name = ?");
        preparedStatement.setString(1, type);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Getted the response...");
        while (resultSet.next()) {
            line = resultSet.getString("id");
        }
        this.lineOut = line;
    }

    public void selectFromTable(String line) throws SQLException {
        String[] s = line.split(":");
        if (s[0].equals("events"))
            selectFromEvents(s);
        else if (s[0].equals("typesofevents"))
            selectFromTypesOfEvents(s[4]);
    }

    private void selectFromEvents(String[] s) throws SQLException {
        if (s[1].equals("types"))
            this.lineOut = selectForTypesFromEvents(s);
    }

    private String selectForTypesFromEvents(String[] s)
            throws SQLException {
        String line = "";
        PreparedStatement preparedStatement;
        if (s[2].equals("address")) {
            preparedStatement =
                    connection.prepareStatement("select address from Events where type = ?");
            selectFromTypesOfEvents(s[3]);
            preparedStatement.setInt(1, Integer.parseInt(getLineOut()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                line += resultSet.getString("address") + ",";
            preparedStatement.close();
        }
        else if (s[2].equals("description")) {
            preparedStatement =
                    connection.prepareStatement("select description from Events where type = ?");
            selectFromTypesOfEvents(s[3]);
            preparedStatement.setInt(1, Integer.parseInt(getLineOut()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                line += resultSet.getString("description") + ",";
            preparedStatement.close();
        }
        return line;
    }

    public void insertIntoTable(String line) throws SQLException {
        String[] s = line.split(":");
        if (s[0].equals("events"))
            insertIntoEvents(s);
        else if (s[0].equals("markerscoordinats"))
            insertIntoMarkersCoordinats(s);
    }

    private void insertIntoEvents(String[] s) throws SQLException {
        int resIdType = 0, resCount = 0;
        PreparedStatement preparedStatement =
                connection.prepareStatement("select id from Events order by id");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.last()) {
            resCount = resultSet.getInt("id");
        }
        else
            resultSet.next();
        System.out.println(resCount);
        preparedStatement.close();
        preparedStatement =
                connection.prepareStatement("select id from TypesOfEvents where name = ?");
        preparedStatement.setString(1, s[1]);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            resIdType = resultSet.getInt("id");
        }
        preparedStatement.close();
        preparedStatement =
                connection.prepareStatement("insert into Events (id, type, address, description) values (?, ?, ?, ?)");
        preparedStatement.setInt(1, resCount+1);
        preparedStatement.setInt(2, resIdType);
        preparedStatement.setString(3, s[2]);
        preparedStatement.setString(4, s[3]);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Insert into Events!!!");
        this.lineOut = "Событие успешно добавлено!";
    }

    private void insertIntoMarkersCoordinats(String[] s) throws SQLException {
        int resIdType = 0, resCount = 0;
        PreparedStatement preparedStatement =
                connection.prepareStatement("select id from MarkersCoordinats order by id");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.last()) {
            resCount = resultSet.getInt("id");
        }
        else
            resultSet.next();
        System.out.println(resCount);
        preparedStatement.close();
        preparedStatement =
                connection.prepareStatement("select id from TypesOfEvents where name = ?");
        preparedStatement.setString(1, s[1]);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            resIdType = resultSet.getInt("id");
        }
        preparedStatement.close();
        preparedStatement =
                connection.prepareStatement("insert into MarkersCoordinats (id, type, Longitude, Latitude) values (?, ?, ?, ?)");
        preparedStatement.setInt(1, resCount+1);
        preparedStatement.setInt(2, resIdType);
        preparedStatement.setDouble(3, Double.valueOf(s[2]));
        preparedStatement.setDouble(4, Double.valueOf(s[3]));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Insert into MarkersCoodrinats!!!");
        this.lineOut = "Событие успешно добавлено!";
    }

    public String getLineOut() { return lineOut; }
}