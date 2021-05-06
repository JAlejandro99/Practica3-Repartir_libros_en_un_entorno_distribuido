import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica_3RepartirLibrosCliente {
    static int PUERTO;
    static String servidor;
    public static void main(String[] args) {
        pedirServidoryPuerto();
    }
    public static void pedirServidoryPuerto(){
        ServidoryPuerto sp = new ServidoryPuerto();
        sp.setTitle("Práctica 2 - Cliente, Servidor y Puerto");
        sp.setVisible(true);
        sp.setResizable(false);
        sp.aceptar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                PUERTO = Integer.valueOf(sp.puerto.getText());
                servidor = sp.IP.getText();
                sp.setVisible(false);
                Ventana2 v2 = new Ventana2(PUERTO,servidor);
                v2.setTitle("Práctica 2 - Relojes con maestro, Cliente");
                v2.setVisible(true);
                v2.setResizable(false);
            }
        });
    }
}
