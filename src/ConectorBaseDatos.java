
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConectorBaseDatos {
    
    public ConectorBaseDatos(){      
        try{
            Class.forName("com.mysql.jdbc.Driver");           
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args){
        ConectorBaseDatos c = new ConectorBaseDatos();
        String[] cad = new String[2];
        cad = c.pedirLibro("gobmx");
        System.out.println(cad[0]+"\n"+cad[1]);
    }
    
    public static Connection abrirConexion(){
        Connection conexion = null;
        try {                     
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }
    String[] pedirLibro(InetAddress direccion, int puertoCliente, String hora){
        String[] ret = new String[2];
        ret[0] = "libro:Nombre,Autor,Editorial,Precio,";
        ret[1] = "huskies.jpg";
        return ret;
    }
    String[] pedirLibro(String nombreLibro){
        /*
        ret[0] = "libro:Nombre,Autor,Editorial,Precio,";
        ret[1] = "huskies.jpg";
        */
        String[] ret = new String[2];
        Connection con = null;
        PreparedStatement ps;
        try{         
            con = abrirConexion();
            ResultSet res;
            ps = con.prepareStatement("SELECT nombre,autor,editorial,precio,portada FROM libro WHERE nombre = ?");
            ps.setString(1,nombreLibro);
            res = ps.executeQuery();
            if(res.next()){      
                ret[0] = "Libro:"+res.getString("nombre")+","+res.getString("autor")+","+res.getString("editorial")+","+res.getString("precio")+",";
                ret[1] = res.getString("portada");        
            }       
            ps = con.prepareStatement("DELETE FROM libro WHERE nombre = ?");
            ps.setString(1,nombreLibro);
            ps.executeUpdate();
            con.close();           
        }catch(Exception e){
            System.out.println(e);
        }
        return ret;
    }
    void reiniciarUsuario(InetAddress[] direccion, int[] puertoCliente, String hora){
        System.out.println(hora);
    }
    public void reiniciarBD(){
        //Hacer un reseteo de la bd
    }
    /*
    public void ejecutaSolicitud(String IP, String hora, String nombreLibro){
        Connection con = null;
        PreparedStatement ps;
        try {          
            
            //Escribe un 
            escribeRegistroBD();            
            ps = con.prepareStatement("INSERT INTO usuario (idUsuario, IP, nombre) VALUES(?, ?, ?);");
            ps.setString(1, idUsuario);
            ps.setString(2, hora);
            ps.setString(3, nombreLibro);
            int res = ps.executeUpdate();                       
            System.out.println("Producto "+((res>0)?"":"no ")+"insertado.");
                       
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    public void escribeRegistroBD(){
    
    }
    public boolean isEmpty(){
        
        return false;
    }
}
