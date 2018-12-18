package Server;

import Client.Main;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageReceiverServer implements Runnable {

    private Socket sock;
    public int count;

    public ImageReceiverServer(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {

            DataInputStream input = new DataInputStream(sock.getInputStream());
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            
            String nome;
            while (true) {

                long stream_size = input.readLong();
                if (stream_size == 0)break;
                System.out.println("Esperando receber imagem de cliente" + stream_size + " bytes");
                nome = input.readUTF();
                System.out.println("Nome do arquivo " + nome);

                int brilho = input.readInt();

                System.out.println("Brilho a ser aplicado: " + brilho);

                nome = nome + "_out";
                
                byte[] stream = new byte[16 * 1024];
                int count;
                int lidos = 0;
                ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
                while (lidos < stream_size) {
                    long tam;
                    if((stream_size-lidos)>=16*1024){
                        tam=16 *1024;
                                           }
                    else{
                    tam=(stream_size-lidos);            
                    }
                   
                    count = input.read(stream, 0, (int)tam);
                    byte_out.write(stream, 0, count);
                    lidos+=count;
                    System.out.println("Recebeu " + lidos + " bytes");
                }

                //criando a imagem a partir do array de stream de bytes
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(byte_out.toByteArray()));

                ProcessadorImagens.brilho(img, brilho);
                System.out.println("Imagem processada...");

                File f2 = new File(nome + "_process.jpg");  //output file path      
                ImageIO.write(img, "jpg", f2);
                System.out.println("Imagem salva...");

                InputStream in = new FileInputStream(f2);
                // stream_size = input.readLong();
               
                /*  --------------------------*/
                long length =0;
                length = f2.length();
                byte[] bytes = new byte[16 * 1024]; //criando o array de bytes usado para enviar os dados da imgagem

                //enviando a quantidade de bytes do arquivo
                output.writeLong(length);
                System.out.println("Size " + length + " bytes");
                //enviadno nome do arquivo
                output.writeUTF(nome);
                //enviando o arquivo pela rede
                //enquanto houver bytes para enviar, obtém do arquivo e manda pela rede
                while ((count = in.read(bytes, 0, bytes.length)) > 0) { //count recebe a qtd de bytes lidos do arquivo para serem enviados
                    //  System.out.println("Enviou " + count + " bytes");
                    output.write(bytes, 0, count); //envia count bytes pela rede
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                /*  --------------------------*/
                in.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(ImageReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
