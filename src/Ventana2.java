import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Ventana2 extends javax.swing.JFrame {
    int PUERTO = 5000;
    String servidor="localhost";
    RelojGrafico r1;
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
    }
    public void iniciarCliente(){
        Thread cl = new Thread(){
            public void run(){
		byte[] buffer = new byte[1024];
                //
		try{
                    InetAddress direccionServidor = InetAddress.getByName(servidor);
                    DatagramSocket socketUDP = new DatagramSocket();
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
                    r1.reasignarHora(respuesta[0], respuesta[1], respuesta[2]);
                    //System.out.println(respuesta[3]);
                    while(true){
                        buffer = new byte[1024];
                        peticion = new DatagramPacket(buffer,buffer.length);
                        socketUDP.receive(peticion);
                        System.out.println("Recibo la peticion");
                        mensaje = new String(peticion.getData());
                        System.out.println(mensaje);
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
                        r1.reasignarHora(respuesta[0], respuesta[1], respuesta[2]);
                        //
                        /*long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
                        TInicio = System.currentTimeMillis();*/
                        //Iniciamos a contar, esto es T0
                        /*mensaje = String.valueOf(respuesta[3]);
                        System.out.println(mensaje);
                        buffer = new byte[1024];
                        buffer = mensaje.getBytes();
                        pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
                        System.out.println("Envio el datagrama");
                        //Inicia el proceso de sincronización
                        socketUDP.send(pregunta);
                        buffer = new byte[1024];
                        peticion = new DatagramPacket(buffer,buffer.length);
                        socketUDP.receive(peticion);
                        System.out.println("Recibo la peticion");
                        mensaje = new String(peticion.getData());
                        System.out.println(mensaje);
                        respuesta = new Integer[4];
                        aux=0;
                        k=0;
                        for(i=0;i<mensaje.length();i++){
                            if(mensaje.charAt(i)==','){
                                respuesta[k] = Integer.valueOf(mensaje.substring(aux,i));
                                aux=i+1;
                                System.out.println(respuesta[k]);
                                k+=1;
                            }
                        }
                        /*TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
                        tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
                        System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
                        int C=0;
                        if(tiempo){
                            
                        }*/
                        //r1.reasignarHora(respuesta[0], respuesta[1], respuesta[2]);
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
        salir = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(38, 70, 95));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );

        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
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
                        .addGap(0, 221, Short.MAX_VALUE)
                        .addComponent(salir)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(salir)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed
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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
