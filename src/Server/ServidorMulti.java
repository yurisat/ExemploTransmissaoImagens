package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorMulti {

    @SuppressWarnings("resource")
	public static void main(String[] args) {
        try {

            InetAddress ia = InetAddress.getByName("localhost");
            int port = 5001;
            ServerSocket server = new ServerSocket(port, 10, ia);

            while (true) {
                System.out.println("Aguardando conexão do próximo cliente...");
                Socket sock = server.accept();

                ImageReceiverServer IR = new ImageReceiverServer(sock);
                Thread t = new Thread(IR);
                t.start();

            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(ServidorMulti.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ServidorMulti.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
