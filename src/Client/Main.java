package Client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {
  
        int port = 5001;
        
        Socket s = new Socket(InetAddress.getByName("192.168.15.4"), port);
        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        //DataInputStream input = new DataInputStream(s.getInputStream());
        
        Socket s2 = new Socket(InetAddress.getByName("localhost"), port);
        DataOutputStream output2 = new DataOutputStream(s2.getOutputStream());
        //DataInputStream input2 = new DataInputStream(s2.getInputStream());
        
        int i = 2120;
           
        ImageSaveClient IR = new ImageSaveClient(s);
        Thread t = new Thread(IR);
        t.start();
        
        ImageSaveClient IR2 = new ImageSaveClient(s2);
        Thread t2 = new Thread(IR2);
        t2.start();
   
        for (;i<=2124;i++){ 
            String file_name = "video/3840_RIGHT_0" + i;
        
            System.out.println("Enviando " + file_name);
            File f = new File(file_name + ".jpg");

            //criando um input stream para ler os bytes do arquivo (não decodifica a imagem)
            InputStream in = new FileInputStream(f);

            long length = f.length();
            byte[] bytes = new byte[16 * 1024]; //criando o array de bytes usado para enviar os dados da imgagem

            if (i%2==0){
            System.out.println("Enviando para servidor 1"); 
                
            //enviando a quantidade de bytes do arquivo
            output.writeLong(length);

            //enviadno nome do arquivo
            output.writeUTF(file_name);

            int brilho = 100;
            //enviando o brilho a ser aplicado
            output.writeInt(brilho);

            //enviando o arquivo pela rede
            int count;
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
            output.writeLong(0);
            
            in.close();
        }
              
        else{
            System.out.println("Enviando para servidor 2"); 
//enviando a quantidade de bytes do arquivo
            output2.writeLong(length);

            //enviadno nome do arquivo
            output2.writeUTF(file_name);

            int brilho = 100;
            //enviando o brilho a ser aplicado
            output2.writeInt(brilho);

            //enviando o arquivo pela rede
            int count;
            //enquanto houver bytes para enviar, obtém do arquivo e manda pela rede
            while ((count = in.read(bytes, 0, bytes.length)) > 0) { //count recebe a qtd de bytes lidos do arquivo para serem enviados
                //  System.out.println("Enviou " + count + " bytes");
                output2.write(bytes, 0, count); //envia count bytes pela rede
            }
     
                try {
                    Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        output.writeLong(0);
                }
    }

}
