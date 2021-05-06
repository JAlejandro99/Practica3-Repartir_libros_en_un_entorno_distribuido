import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Ventana1 extends javax.swing.JFrame {
    RelojGrafico r1,r2,r3,r4;
    Ilustrador panel;
    int numeroReloj;
    DatagramSocket socketUDP;
    InetAddress[] direccion;
    int[] puertoCliente;
    public Ventana1() {
        initComponents();
        panel = new Ilustrador(850,700,"");
        panel.dibujar("huskies.jpg");
        this.setLocationRelativeTo(null);
        r1 = new RelojGrafico(false,328,50);
        r2 = new RelojGrafico(false,38,205);
        r3 = new RelojGrafico(false,328,205);
        r4 = new RelojGrafico(false,618,205);
        r1.run();
        r2.run();
        r3.run();
        r4.run();
        this.jPanel1.add(r1);
        this.jPanel1.add(r2);
        this.jPanel1.add(r3);
        this.jPanel1.add(r4);
        this.jPanel1.add(panel);
        numeroReloj = 0;
        direccion = new InetAddress[4];
        puertoCliente = new int[4];
        r2.setVisible(false);
        r3.setVisible(false);
        r4.setVisible(false);
        iniciarServidor();
    }
    public void enviar(int reloj){
        try{
            Integer[] hora;
            if(reloj==0)
                hora = r2.getHora();
            else if(reloj==1)
                hora = r3.getHora();
            else
                hora = r4.getHora();
            String resp = String.valueOf(hora[0])+",";
            resp += String.valueOf(hora[1])+",";
            resp += String.valueOf(hora[2])+",";
            resp += String.valueOf(reloj)+",";
            System.out.println(resp);
            byte[] buffer = new byte[1024];
            buffer = resp.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[reloj],puertoCliente[reloj]);
            System.out.println("Envio la informacion del cliente");
            socketUDP.send(respuesta);
        }catch(IOException e){}
    }
    public void iniciarServidor(){
        Thread sv = new Thread(){
            public void run(){
                r2.boton2.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        enviar(0);
                    }
                });
                r3.boton2.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        enviar(1);
                    }
                });
                r4.boton2.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        enviar(2);
                    }
                });
                final int PUERTO = 5000;
		byte[] buffer = new byte[1024];
		try{
                    System.out.println("Iniciando el servidor UDP");
                    socketUDP = new DatagramSocket(PUERTO);
                    while(true){
                        buffer = new byte[1024];
			DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                        socketUDP.receive(peticion);
                        System.out.println("Recibo la informacion del cliente");
                        String mensaje = new String(peticion.getData());
                        //El cliente solo puede pedir la hora de su reloj
                        System.out.println("Recibido: "+mensaje);
                        if(mensaje.startsWith("libro")){
                            numeroReloj = Integer.valueOf(mensaje.substring(5,6));
                            if(numeroReloj>2){
                                puertoCliente[3] = peticion.getPort();
                                direccion[3] = peticion.getAddress();
                            }else{
                                puertoCliente[numeroReloj] = peticion.getPort();
                                direccion[numeroReloj] = peticion.getAddress();
                            }
                            System.out.println(numeroReloj);
                            String resp = "libro:1,";
                            resp += "dato1,";
                            resp += "dato2,";
                            resp += "dato3,";
                            resp += "dato4,";
                            System.out.println(resp);
                            buffer = new byte[1024];
                            buffer = resp.getBytes();
                            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[numeroReloj],puertoCliente[numeroReloj]);
                            System.out.println("Envio la informacion del cliente");
                            socketUDP.send(respuesta);
                        }else{
                            if(!mensaje.startsWith("Iniciar")){
                                numeroReloj = Integer.valueOf(mensaje.substring(0,1));
                            }
                            if(numeroReloj>2){
                                puertoCliente[3] = peticion.getPort();
                                direccion[3] = peticion.getAddress();
                            }else{
                                puertoCliente[numeroReloj] = peticion.getPort();
                                direccion[numeroReloj] = peticion.getAddress();
                            }
                            Integer[] hora;
                            if(numeroReloj==0)
                                hora = r2.getHora();
                            else if(numeroReloj==1)
                                hora = r3.getHora();
                            else
                                hora = r4.getHora();
                            String resp = String.valueOf(numeroReloj)+",";
                            resp += String.valueOf(hora[0])+",";
                            resp += String.valueOf(hora[1])+",";
                            resp += String.valueOf(hora[2])+",";
                            System.out.println(resp);
                            buffer = new byte[1024];
                            buffer = resp.getBytes();
                            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[numeroReloj],puertoCliente[numeroReloj]);
                            System.out.println("Envio la informacion del cliente");
                            socketUDP.send(respuesta);
                            numeroReloj+=1;
                        }
                    }
                }catch(IOException e){}
            }
        };
        sv.start();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        reiniciar = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(923, 1000));

        jPanel1.setBackground(new java.awt.Color(38, 70, 95));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 738, Short.MAX_VALUE)
                        .addComponent(reiniciar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(salir)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salir)
                    .addComponent(reiniciar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed

    private void reiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reiniciarActionPerformed
        //Solicitar a los clientes la eliminación de sus Datos
        //Reiniciar BD
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
            java.util.logging.Logger.getLogger(Ventana1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana1().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton reiniciar;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
