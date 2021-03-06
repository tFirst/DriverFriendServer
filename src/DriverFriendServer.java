import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DriverFriendServer {
    private ServerSocket server;
    private Socket client;

    public static void main(String[] args) {
        new DriverFriendServer().start();
    }

    public void start() {
        try {
            server = new ServerSocket(3333);
            while (true) {
                client = server.accept();
                new ClientHandler(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler extends Thread {
        private DataOutputStream sOut;
        private DataInputStream sIn;
        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                String result;
                sIn = new DataInputStream(client.getInputStream());
                sOut = new DataOutputStream(client.getOutputStream());
                String line = sIn.readUTF();
                String[] s = line.split(":");
                if (!s[0].equals("selectmarkers")) {
                    ConnectionWithDataBase connectionWithDataBase =
                            new ConnectionWithDataBase(s[0], s[1] + ":" + s[2] + ":" + s[3] + ":" + s[4] + ":" + s[5] + ":" + s[6]);
                    result = connectionWithDataBase.getLineOut();
                    sOut.writeUTF(result);
                    sOut.flush();
                }
                else {
                    ConnectionWithDataBase connectionWithDataBase =
                            new ConnectionWithDataBase();
                    String string = connectionWithDataBase.selectMarkers();
                    sOut.writeUTF(string);
                    sOut.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
