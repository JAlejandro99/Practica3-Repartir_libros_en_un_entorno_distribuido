import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Ventana1 extends javax.swing.JFrame {
    RelojGrafico r1,r2,r3,r4;
    Ilustrador panel;
    int numServidor;
    //numeroReloj = #Cliente
    int numeroReloj;
    DatagramSocket socketUDP;
    DatagramSocket socketUDPServidor2;
    InetAddress[] direccion;
    int[] puertoCliente;
    InetAddress direccionServidor2;
    int puertoServidor2;
    JTextArea listaLibros;
    JScrollPane sp;
    ConectorBaseDatos cbd;
    public Ventana1(){
        initComponents();
    }
    public Ventana1(int numServidor) {
        initComponents();
        panel = new Ilustrador(450,700,"");
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
        /*if(numServidor == 2){
            conectarServidor1();
            System.out.println("Servidor de reespaldo iniciado");
        }*/
        iniciarServidor();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        listaLibros = new JTextArea(10,0);
        contentPane.add(listaLibros);
        contentPane.add(listaLibros, BorderLayout.CENTER);
        sp = new JScrollPane(listaLibros,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(sp);
        //Escribir los libros de la bd en listaLibros
        cbd = new ConectorBaseDatos();
        cbd.reiniciarBD();
        ArrayList<String> libros = cbd.getLibros();
        listaLibros.setText("");
        for(int i=0;i<libros.size();i++){
            listaLibros.append(libros.get(i)+"\n");
            //System.out.println(libros.get(i));
        }
    }
    /*public void conectarServidor1(){
        byte[] buffer = new byte[1024];
        int PUERTO = 5001;
        String servidor="localhost";
        InetAddress direccionServidor;
        DatagramSocket socketUDP;
	try{
            direccionServidor = InetAddress.getByName(servidor);
            socketUDP = new DatagramSocket();
            String mensaje = "Servidor2";
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor,PUERTO);
            System.out.println("Conexi??n con Servidor 2");
            socketUDP.send(pregunta);
            Boolean seguir = true;
            while(seguir){
                buffer = new byte[1024];
		DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                socketUDP.receive(peticion);
                System.out.println("Recibo la informacion del Servidor 1");
                mensaje = new String(peticion.getData());
                //El cliente solo puede pedir la hora de su reloj
                System.out.println("Recibido: "+mensaje);
                if(mensaje.startsWith("Iniciar")){
                    seguir = false;
                }else if(mensaje.equals("AgregarCliente")){
                }
            }
            System.out.println("Aqu??");
            socketUDP.close();
        }catch(IOException e){}
    }*/
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
                            String hora = mensaje.substring(6,14);
                            if(numeroReloj>2){
                                puertoCliente[3] = peticion.getPort();
                                direccion[3] = peticion.getAddress();
                            }else{
                                puertoCliente[numeroReloj] = peticion.getPort();
                                direccion[numeroReloj] = peticion.getAddress();
                            }
                            //System.out.println(numeroReloj); //Prestando el libro al cliente ...
                            byte[] ip = new byte[4];
                            ip = direccion[numeroReloj].getAddress();
                            String IP = (0xff & (int)ip[0]) + "." +(0xff & (int)ip[1]) + "." +(0xff & (int)ip[2]) + "." +(0xff & (int)ip[3]);
                            System.out.println("Direcci??n IP: " + IP);
                            String[] resp = cbd.pedirLibro(IP,hora,"Cliente"+String.valueOf(numeroReloj));
                            System.out.println(resp[0]);
                            System.out.println(resp[1]);
                            panel.dibujar(resp[1]);
                            buffer = new byte[1024];
                            buffer = resp[0].getBytes();
                            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[numeroReloj],puertoCliente[numeroReloj]);
                            System.out.println("Envio la informacion del cliente");
                            socketUDP.send(respuesta);
                            ////
                            //Escribir los libros de la bd en listaLibros
                            ////
                            ArrayList<String> libros = cbd.getLibros();
                            listaLibros.setText("");
                            for(int i=0;i<libros.size();i++){
                                listaLibros.append(libros.get(i)+"\n");
                            }
                            if(cbd.esLibrosVacio()){
                                //Informar a todos los clientes que el prestamo de libros ha terminado
                                informarVacio();
                            }
                        }else if(mensaje.startsWith("reiniciar")){
                            numeroReloj = Integer.valueOf(mensaje.substring(9,10));
                            String hora = mensaje.substring(10);
                            if(numeroReloj>2){
                                puertoCliente[3] = peticion.getPort();
                                direccion[3] = peticion.getAddress();
                            }else{
                                puertoCliente[numeroReloj] = peticion.getPort();
                                direccion[numeroReloj] = peticion.getAddress();
                            }
                            //Regresar los datos de un usuario a la tabla de los libros a prestar
                            /*byte[] ip = new byte[4];
                            ip = direccion[numeroReloj].getAddress();
                            String IP = (0xff & (int)ip[0]) + "." +(0xff & (int)ip[1]) + "." +(0xff & (int)ip[2]) + "." +(0xff & (int)ip[3]);
                            System.out.println("Direcci??n IP: " + IP);*/
                            //cbd.reiniciarUsuario(direccion,puertoCliente,hora);
                            System.out.println("Reiniciar de usuario "+String.valueOf(numeroReloj));
                            ////
                            //Escribir los libros de la bd en listaLibros
                            ////
                            ArrayList<String> libros = cbd.getLibros();
                            listaLibros.setText("");
                            for(int i=0;i<libros.size();i++){
                                listaLibros.append(libros.get(i)+"\n");
                            }
                            //Agregar a la Base de Datos los libros prestados a este usuario
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
        /*Thread sv2 = new Thread(){
            public void run(){
                final int PUERTO = 5001;
		byte[] buffer = new byte[1024];
		try{
                    System.out.println("Iniciando el servidor UDP para los servidores");
                    socketUDPServidor2 = new DatagramSocket(PUERTO);
                    buffer = new byte[1024];
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    socketUDPServidor2.receive(peticion);
                    System.out.println("Recibo la informacion del Servidor 2");
                    String mensaje = new String(peticion.getData());
                    System.out.println("Recibido: "+mensaje);
                    direccionServidor2 = peticion.getAddress();
                    puertoServidor2 = peticion.getPort();
                    //while(true){}
                }catch(IOException e){}
            }
        };
        sv2.start();*/
    }
    public void informarVacio(){
        try{
            byte[] buffer = new byte[1024];
            buffer = "vacio".getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[0],puertoCliente[0]);
            socketUDP.send(respuesta);
        }catch(IOException e){}
        try{
            byte[] buffer = new byte[1024];
            buffer = "vacio".getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[1],puertoCliente[1]);
            socketUDP.send(respuesta);
        }catch(IOException e){}
        try{
            byte[] buffer = new byte[1024];
            buffer = "vacio".getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[2],puertoCliente[2]);
            socketUDP.send(respuesta);
        }catch(IOException e){}
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        contentPane = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        reiniciar = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(923, 1000));

        jPanel1.setBackground(new java.awt.Color(38, 70, 95));

        contentPane.setPreferredSize(new java.awt.Dimension(300, 700));

        javax.swing.GroupLayout contentPaneLayout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
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
                        .addGap(0, 1115, Short.MAX_VALUE)
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
        /*try{
            String mensaje = "Iniciar";
            byte[] buffer = new byte[1024];
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer,buffer.length,direccionServidor2,puertoServidor2);
            System.out.println("Pidiendo la salida");
            socketUDPServidor2.send(pregunta);
        }catch(IOException e){}*/
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed

    private void reiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reiniciarActionPerformed
        //Solicitar a los clientes la eliminaci??n de sus Datos
        try{
            String resp = "Reiniciar";
            System.out.println(resp);
            byte[] buffer = new byte[1024];
            buffer = resp.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[0],puertoCliente[0]);
            socketUDP.send(respuesta);
            System.out.println("Envio el reinicio al cliente 1");
        }catch(IOException e){}
        try{
            String resp = "Reiniciar";
            System.out.println(resp);
            byte[] buffer = new byte[1024];
            buffer = resp.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[1],puertoCliente[1]);
            socketUDP.send(respuesta);
            System.out.println("Envio el reinicio al cliente 2");
        }catch(IOException e){}
        try{
            String resp = "Reiniciar";
            System.out.println(resp);
            byte[] buffer = new byte[1024];
            buffer = resp.getBytes();
            DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,direccion[2],puertoCliente[2]);
            socketUDP.send(respuesta);
            System.out.println("Envio el reinicio al cliente 3");
        }catch(IOException e){}
        //Reiniciar BD
        cbd.reiniciarBD();
        ////
        //Escribir los libros de la bd en listaLibros
        ////
        ArrayList<String> libros = cbd.getLibros();
        listaLibros.setText("");
        for(int i=0;i<libros.size();i++){
            listaLibros.append(libros.get(i)+"\n");
        }
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
    private javax.swing.JPanel contentPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton reiniciar;
    private javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
