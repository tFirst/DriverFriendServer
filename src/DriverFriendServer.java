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
                switch (line) {
                    case "fillingspinner":
                        ConnectionWithDataBase connectionWithDataBase =
                                new ConnectionWithDataBase("fillingspinner");
                        result = connectionWithDataBase.getLineOut();
                }
                assert result != null;
                sOut.writeUTF(result);
                sOut.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
