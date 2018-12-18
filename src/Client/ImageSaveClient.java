package Client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageSaveClient implements Runnable {

    private Socket sock;
    public int count;

    public ImageSaveClient(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {

            DataInputStream input = new DataInputStream(sock.getInputStream());
            long stream_size = input.readLong();
            
            String nome;
            
            while (stream_size != 0) {

                System.out.println("Esperando receber imagem de " + stream_size + " bytes");

                nome = input.readUTF();

                System.out.println("Nome do arquivo " + nome);

                byte[] stream = new byte[16 * 1024];
                int count;
                int lidos = 0;
                ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
                while (lidos < stream_size) {
                    count = input.read(stream, 0, 16 * 1024);
                    byte_out.write(stream, 0, count);
                    System.out.println("Recebeu " + count + " bytes");
                    lidos += count;
                }

                //criando a imagem a partir do array de stream de bytes
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(byte_out.toByteArray()));

                File f2 = new File(nome + "_process.jpg");  //output file path      
                ImageIO.write(img, "jpg", f2);
                System.out.println("Imagem salva...");

                //InputStream in = new FileInputStream(f2);
                stream_size = input.readLong();

            }

        } catch (IOException ex) {
            Logger.getLogger(ImageSaveClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
