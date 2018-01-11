/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 

/*
 * @author Rajesh R. Patkar
 */
public class ConcurrentServer {
static String Username;
    public static ArrayList<PrintWriter> al = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server signing On");
        ServerSocket ss = new ServerSocket(9081);
       Username = JOptionPane.showInputDialog(
                              "Please Enter you Username"
                          );
        for (int i = 2; i < 12; i++) {
            Socket soc = ss.accept();
            Conversation c = new Conversation(soc);
            c.start();
        }
        System.out.println("Server signing Off");
    }
    
       

}
class Conversation extends Thread {

    Socket soc;
    
      
            
    public Conversation(Socket soc) {
        this.soc = soc;
        
    }

    @Override
    public void run() {
        
        
        System.out.println("Conversation thread  "
                + Thread.currentThread().getName()
                + "   signing On");
        try {

            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(
                            soc.getInputStream()
                    )
            );
            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    soc.getOutputStream()
                            )
                    ), true
            );
         
        JFrame f1 = new JFrame(ConcurrentServer.Username + "`s Chat");
        JButton b1 = new JButton("Send");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JTextField tf = new JTextField(20);
        JPanel p1 = new JPanel();
        p1.add(tf);
        p1.add(b1);
        f1.add(ta);
        f1.add(BorderLayout.SOUTH,p1);
         ConcurrentServer.al.add(nos);
        ChatListener1 l1 = new ChatListener1(tf,ta,nos,ConcurrentServer.Username);
        b1.addActionListener(l1);
        tf.addActionListener(l1);
         
        f1.setSize(400,400);
        f1.setVisible(true);
        String message = nis.readLine();
            while (!message.equalsIgnoreCase("End")) {

                System.out.println("Server Recieved  " + message);

                for (PrintWriter o : ConcurrentServer.al) {
                    o.println(message);
                    
                      
                }
               ta.append(message+"\n");
                message = nis.readLine();
            }
            nos.println("End");
            ConcurrentServer.al.remove(nos);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
      
        
           
           
           
        } catch (Exception e) {
            System.out.println(
                    "Client Seems to have abruptly closed the connection"
            );
        }
        System.out.println("Conversation thread  "
                + Thread.currentThread().getName()
                + "   signing Off");
    }
}
class ChatListener1 implements ActionListener{
    JTextArea ta;
   JTextField tf ;
   PrintWriter nos;
   String Username;
    public ChatListener1(JTextField tf,JTextArea ta,PrintWriter nos,String Username){
        this.ta=ta;
        this.tf = tf;
        this.nos = nos;
        this.Username = Username;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String str  = tf.getText();
        ta.append(Username+": "+str+"\n");
        nos.println(Username+": "+str);
        tf.setText("");
    }
    
}