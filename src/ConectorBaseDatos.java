import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
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
        //cad = c.pedirLibro("el muerto");
        //System.out.println(cad[0]+"\n"+cad[1]);
        c.escribeRegistroBD("8.8.8.8", "18:01", "NombreX", "ClienteCachondo");
        System.out.println(c.esLibrosVacio());
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
    
    String[] pedirLibro(String nombreLibro){
        /*
        ret[0] = "libro:Nombre,Autor,Editorial,Precio,";
        ret[1] = "huskies.jpg";
        */
        String[] ret = new String[2];
        Connection con = abrirConexion();
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
    public void reiniciarBD(){
        Connection con = abrirConexion();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("DELETE from libro; ");
            ps.executeUpdate();         
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor1\", \"Delfin\", \"el muerto\", \"100.75\",\"imagen1.jpg\")");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor2\", \"Picasso\", \"Mil años de soledad\", \"134.34\",\"imagen2.jpg\")");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor3\", \"Solman\", \"Si hay sol hay playa\", \"53.36\",\"imagen3.jpg\");");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor4\", \"Playa\", \"Conejo Malo\", \"234.25\",\"imagen4.jpg\");\n");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor5\", \"Ariel\", \"El Travieso Scott\", \"354.65\",\"imagen5.jpg\");");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor6\", \"Tecnos\", \"La Biblia\", \"500.03\",\"imagen6.jpg\");\n");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor7\", \"Alianza\", \"Harry Potter\", \"68.68\",\"imagen7.jpg\");");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor8\", \"Akal\", \"Crespusculo\", \"143.96\",\"imagen8.jpg\");");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor9\", \"Sintesis\", \"Heidi\", \"111.22\",\"imagen9.jpg\");");
            executeUpdate("INSERT INTO libro (autor,editorial,nombre,precio,portada) VALUES(\"autor10\", \"Aranzadi\", \"Quijote\", \"123.12\",\"imagen10.jpg\");");
            con.close();   
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void escribeRegistroBD(String IP, String hora, String nombreLibro, String nombreUsuario){
        Connection con = abrirConexion();
        PreparedStatement ps;
        try {                                            
            ps = con.prepareStatement("INSERT INTO usuario (IP, nombre) VALUES(?, ?);");
            ps.setString(1, IP);
            ps.setString(2, nombreUsuario);
            ps.executeUpdate();  
            ps = con.prepareStatement("INSERT INTO usuariosesion (tiempo_usuario,lugar_jugador) VALUES (?,?)");
            ps.setString(1,hora);
            ps.setString(2,nombreLibro);
            ps.executeUpdate();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList getLibros(){
        ArrayList<String> libros = new ArrayList();
        Connection con = abrirConexion();
        PreparedStatement ps;
        try{
            ps = con.prepareStatement("SELECT nombre FROM libro");
            ResultSet res; 
            res = ps.executeQuery();
            while(res.next())
                libros.add(res.getString("nombre"));
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return libros;
    }
    
    public boolean esLibrosVacio(){
        boolean des=true;
        Connection con = abrirConexion();
        PreparedStatement ps;
        try{
            ps = con.prepareStatement("SELECT * FROM libro");
            ResultSet res; 
            res = ps.executeQuery();          
     
            if(res.getRow()==0)
                des=true;
            else
                des=false;
        } catch (SQLException ex) {
            Logger.getLogger(ConectorBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return des;
    }
    
    public void executeUpdate(String update) {
        Connection connection = abrirConexion();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
            }
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
    }
}