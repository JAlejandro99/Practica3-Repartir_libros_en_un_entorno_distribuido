
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConectorBaseDatos {
    public ConectorBaseDatos(){
    }
    String[] pedirLibro(InetAddress[] direccion, int[] puertoCliente, String hora){
        String[] ret = new String[2];
        System.out.println(hora);
        ret[0] = "libro:Nombre,Autor,Editorial,Precio,";
        ret[1] = "huskies.jpg";
        return ret;
    }
    public void reiniciarBD(){
    }
    public void pedirLibro2(String IP, String hora, String nombreLibro){
    }
    public boolean isEmpty(){
        return false;
    }
}
