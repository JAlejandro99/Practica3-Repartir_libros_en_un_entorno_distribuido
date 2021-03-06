import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Ventana2 extends javax.swing.JFrame {
    int PUERTO = 5000;
    String servidor="localhost";
    RelojGrafico r1;
    InetAddress direccionServidor;
    DatagramSocket socketUDP;
    JTextArea infoLibros;
    JScrollPane sp;
    byte[] buffer;
    int numReloj;
    public Ventana2() {
        initComponents();
    }
    public Ventana2(int p, String s) {
        initComponents();
        PUERTO = p;
        servidor = s;
        this.setLocationRelativeTo(null);
        r1 = new RelojGrafico(false,15,15);
        r1.remove(r1.boton);
        r1.remove(r1.boton2);
        r1.run();
        this.jPanel1.add(r1);
        iniciarCliente();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        infoLibros = new JTextArea(10,0);
        contentPane.add(infoLibros);
        contentPane.add(infoLibros, BorderLayout.CENTER);
        sp = new JScrollPane(infoLibros,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(sp);
    }
    public void iniciarCliente(){
        Thread cl = new Thread(){
            public void run(){
		buffer = new byte[1024];
                //
		try{
                    direccionServidor = InetAddress.getByName(servidor);
                    socketUDP = new DatagramSocket();
                    String mensaje = "Iniciar";
                    buffer = mensaje.getBytes();
                    DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
                    System.out.println("Envio el datagrama");
                    //Inicia el proceso de sincronización
                    socketUDP.send(pregunta);
                    buffer = new byte[1024];
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    socketUDP.receive(peticion);
                    System.out.println("Recibo la peticion");
                    mensaje = new String(peticion.getData());
                    System.out.println(mensaje);
                    Integer[] respuesta = new Integer[4];
                    int i,aux=0,k=0;
                    for(i=0;i<mensaje.length();i++){
                        if(mensaje.charAt(i)==','){
                            respuesta[k] = Integer.valueOf(mensaje.substring(aux,i));
                            aux=i+1;
                            //System.out.println(respuesta[k]);
                            k+=1;
                        }
                    }
                    r1.reasignarHora(respuesta[1], respuesta[2], respuesta[3]);
                    numReloj = respuesta[0];
                    //System.out.println(respuesta[3]);
                    while(true){
                        buffer = new byte[1024];
                        peticion = new DatagramPacket(buffer,buffer.length);
                        socketUDP.receive(peticion);
                        System.out.println("Recibo la peticion");
                        mensaje = new String(peticion.getData());
                        System.out.println(mensaje);
                        if(mensaje.startsWith("libro:")){
                            String[] respuesta2 = new String[4];
                            i=0;
                            aux=0;
                            k=0;
                            for(i=0;i<mensaje.length();i++){
                                if(mensaje.charAt(i)==','){
                                    //System.out.println(mensaje.substring(aux,i));
                                    respuesta2[k] = mensaje.substring(aux,i);
                                    aux=i+1;
                                    k+=1;
                                }
                            }
                            infoLibros.append(respuesta2[0].substring(6)+"\n");
                            infoLibros.append(respuesta2[1]+"\n");
                            infoLibros.append(respuesta2[2]+"\n");
                            infoLibros.append(respuesta2[3]+"\n\n");
                        }else if(mensaje.startsWith("Reiniciar")){
                            infoLibros.setText("");
                        }else if(mensaje.startsWith("vacio")){
                            int resp = JOptionPane.showConfirmDialog(null, "El sistema ha prestado todos los libros, ¿deseas seguir en el sistema?");
                            if(resp==1){
                                try{
                                    mensaje = "reiniciar"+String.valueOf(numReloj)+r1.getHora2();
                                    buffer = new byte[1024];
                                    buffer = mensaje.getBytes();
                                    pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
                                    System.out.println("Regresando libros");
                                    socketUDP.send(pregunta);
                                    infoLibros.setText("");
                                }catch(IOException e){}
                                System.exit(0);
                            }
                        }else{
                            respuesta = new Integer[4];
                            aux=0;
                            k=0;
                            for(i=0;i<mensaje.length();i++){
                                if(mensaje.charAt(i)==','){
                                    respuesta[k] = Integer.valueOf(mensaje.substring(aux,i));
                                    aux=i+1;
                                    //System.out.println(respuesta[k]);
                                    k+=1;
                                }
                            }
                            r1.reasignarHora(respuesta[1], respuesta[2], respuesta[3]);
                        }
                    }
                    //socketUDP.close();
		}catch(IOException e){}
            }
        };
        cl.start();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        contentPane = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        pedirLibro = new javax.swing.JButton();
        reiniciar = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(38, 70, 95));

        javax.swing.GroupLayout contentPaneLayout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        pedirLibro.setText("Pedir libro");
        pedirLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedirLibroActionPerformed(evt);
            }
        });

        reiniciar.setText("Reiniciar");
        reiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reiniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 200, Short.MAX_VALUE)
                        .addComponent(reiniciar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pedirLibro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salir)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salir)
                    .addComponent(pedirLibro)
                    .addComponent(reiniciar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        try{
            String mensaje = "reiniciar"+String.valueOf(numReloj)+r1.getHora2();
            buffer = new byte[1024];
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
            System.out.println("Regresando libros");
            socketUDP.send(pregunta);
            infoLibros.setText("");
        }catch(IOException e){}
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed

    private void pedirLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedirLibroActionPerformed
        try{
            String mensaje = "libro"+String.valueOf(numReloj)+r1.getHora2();
            buffer = new byte[1024];
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
            System.out.println("Pidiendo libro");
            socketUDP.send(pregunta);
        }catch(IOException e){}
    }//GEN-LAST:event_pedirLibroActionPerformed

    private void reiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reiniciarActionPerformed
        try{
            String mensaje = "reiniciar"+String.valueOf(numReloj)+r1.getHora2();
            buffer = new byte[1024];
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
            System.out.println("Regresando libros");
            socketUDP.send(pregunta);
            infoLibros.setText("");
        }catch(IOException e){}
    }//GEN-LAST:event_reiniciarActionPerformed
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana2().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton pedirLibro;
    private javax.swing.JButton reiniciar;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
