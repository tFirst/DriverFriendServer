import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Stanislav Trushin on 18.11.2016.
 */
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

    class ClientHandler extends Thread {
        private DataOutputStream sOut;
        private DataInputStream sIn;
        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                String result = null;
                sIn = new DataInputStream(client.getInputStream());
                sOut = new DataOutputStream(client.getOutputStream());
                String line = sIn.readUTF();
                System.out.println(line);
                if (line.equals("aaa")) {
                    ConnectionWithDataBase connectionWithDataBase =
                            new ConnectionWithDataBase(line);
                    line = connectionWithDataBase.getLineOut();
                }
//                String[] s = line.split(":");
//                if (s[0].equals("insert")) {
//                    ConnectionWithDataBase connectionWithDataBase =
//                            new ConnectionWithDataBase(s[1]+":"+s[2]+":"+s[3]);
//                }
//                assert result != null;
                sOut.writeUTF(line);
                sOut.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
