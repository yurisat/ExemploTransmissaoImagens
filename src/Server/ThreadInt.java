/*
O * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author ifpb
 */
public class ThreadInt implements Runnable {
    
    DataInputStream in;

    public ThreadInt(DataInputStream in) {
        this.in = in;
    }

    public void run(){
        while (true) {
            
            String msg_in;
            try {
                msg_in = in.readUTF();
                JOptionPane.showMessageDialog(null, "Mensagem do cliente: " + msg_in);
            } catch (IOException ex) {
                Logger.getLogger(ThreadInt.class.getName()).log(Level.SEVERE, null, ex);
            }
//trechoainserir  
        }
    }
}
