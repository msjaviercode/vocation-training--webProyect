/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD.tables;

import BBDD.utilities.Conector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Boomeraling
 */
public class Franquiciado {

    private final String TABLA = "Franquiciado";

    public enum Busqueda {

        NAME, EMAIL, FBID
    }

    public enum Parametro {

        ID,
        NAME, SURNAME, NIF, ADDRESS, EMAIL, TELEFONO, PASSWORD, FBID, PROFILEIMAGEURL
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
                return (Tipo.INT);
            case NAME:
            case SURNAME:
            case NIF:
            case ADDRESS:
            case EMAIL:
            case TELEFONO:
            case PASSWORD:
            case FBID:
            case PROFILEIMAGEURL:
                return (Tipo.STRING);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case NAME:
                this.name = s;
                break;
            case SURNAME:
                this.surname = s;
                break;
            case NIF:
                this.NIF = s;
                break;
            case ADDRESS:
                this.address = s;
                break;
            case EMAIL:
                this.email = s;
                break;
            case TELEFONO:
                this.telefono = s;
                break;
            case PASSWORD:
                this.password = s;
                break;
            case FBID:
                this.fbid = s;
                break;
            case PROFILEIMAGEURL:
                this.profileimageurl = s;
                break;
        }
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID:
                this.id = n;
                break;
        }
    }

    // Datos
    private int id;
    private String name, surname, NIF, address, email, telefono, password, fbid, profileimageurl;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     *
     * Recupera los datos a partir del id.
     *
     * @param id
     * @throws SQLException
     */
    public Franquiciado(int id) throws SQLException {
        establecerConexion();
        sql = "Select name, surname, password, NIF, address, email, telefono FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.password = rs.getString("password");
        this.NIF = rs.getString("NIF");
        this.address = rs.getString("address");
        this.email = rs.getString("email");
        this.telefono = rs.getString("telefono");
        getAdditionals();
        conexion.con.close();
    }

    /**
     *
     * @param busqueda
     * @param b
     * @throws SQLException
     */
    public Franquiciado(String busqueda, Busqueda b) throws SQLException {
        if (Busqueda.NAME == b) {
            sql = "Select id, surname, password, NIF, address, email, telefono FROM " + TABLA + " where name=?";
        }
        if (Busqueda.EMAIL == b) {
            sql = "Select id, name, surname, password, NIF, address, telefono FROM " + TABLA + " where email=?";
        }
        if (Busqueda.FBID == b) {
            sql = "Select id, name, email, surname, password, NIF, address, telefono FROM " + TABLA + " where fbid=?";
        }
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, busqueda);
        rs = stm.executeQuery();
        rs.next();
        this.id = rs.getInt("id");
        if (Busqueda.EMAIL == b) {
            this.email = busqueda;
            this.name = rs.getString("name");
        }
        if (Busqueda.NAME == b) {
            this.name = busqueda;
            this.email = rs.getString("email");
        }
        if (Busqueda.FBID == b) {
            this.fbid = busqueda;
            this.email = rs.getString("email");
            this.name = rs.getString("name");
        }
        this.password = rs.getString("password");
        this.surname = rs.getString("surname");
        this.NIF = rs.getString("NIF");
        this.address = rs.getString("address");
        this.telefono = rs.getString("telefono");
        rs.close();
        stm.close();
        getAdditionals();
        conexion.con.close();
    }

    /**
     *
     * @param name
     * @param surname
     * @param password
     * @param NIF
     * @param address
     * @param email
     * @param telefono
     * @throws SQLException
     */
    public Franquiciado(String name, String surname, String password, String NIF, String address, String email, String telefono) throws SQLException {
        sql = "Insert INTO " + TABLA + " (name,surname,NIF,address,email,telefono,password) VALUES(?,?,?,?,?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, surname);
        stm.setString(3, NIF);
        stm.setString(4, address);
        stm.setString(5, email);
        stm.setString(6, telefono);
        stm.setString(7, password);
        stm.executeUpdate();
        getIdBBDD();
        this.NIF = NIF;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.address = address;
        this.email = email;
        this.telefono = telefono;
        conexion.con.close();
    }

    // SET
    public void setAtributo(Parametro p, String atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setString(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void getAtributo(Parametro p) throws SQLException {
        establecerConexion();
        sql = "Select " + p.name() + " FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, getId());
        rs = stm.executeQuery();
        if (rs.next()) {
            switch (getTipo(p)) {
                case INT:
                    setParametro(p, rs.getInt(p.name()));
                    break;
                case STRING:
                    setParametro(p, rs.getString(p.name()));
                    break;
            }
        }
        conexion.con.close();
    }

    // GET
    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     * @throws java.sql.SQLException
     */
    public String getName() throws SQLException {
        getAtributo(Parametro.NAME);
        return name;
    }

    /**
     * @return the password
     * @throws java.sql.SQLException
     */
    public String getPassword() throws SQLException {
        getAtributo(Parametro.PASSWORD);
        return password;
    }

    /**
     * @return the surname
     * @throws java.sql.SQLException
     */
    public String getSurname() throws SQLException {
        getAtributo(Parametro.SURNAME);
        return surname;
    }

    /**
     * @return the profileimageurl
     * @throws java.sql.SQLException
     */
    public String getProfileimageurl() throws SQLException {
        getAtributo(Parametro.PROFILEIMAGEURL);
        return profileimageurl;
    }

    /**
     * @return the NIF
     * @throws java.sql.SQLException
     */
    public String getNIF() throws SQLException {
        getAtributo(Parametro.NIF);
        return NIF;
    }

    /**
     * @return the address
     * @throws java.sql.SQLException
     */
    public String getAddress() throws SQLException {
        getAtributo(Parametro.ADDRESS);
        return address;
    }

    /**
     * @return the mail
     * @throws java.sql.SQLException
     */
    public String getMail() throws SQLException {
        getAtributo(Parametro.EMAIL);
        return email;
    }

    /**
     * @return the telefono
     * @throws java.sql.SQLException
     */
    public String getTelefono() throws SQLException {
        getAtributo(Parametro.TELEFONO);
        return telefono;
    }

    /**
     * @return the fbid
     * @throws java.sql.SQLException
     */
    public String getfbid() throws SQLException {
        getAtributo(Parametro.FBID);
        return fbid;
    }

    /**
     *
     * @throws SQLException
     */
    private void getAdditionals() throws SQLException {
        getAtributo(Parametro.FBID);
        getAtributo(Parametro.PROFILEIMAGEURL);
    }

    // GET BDD
    /**
     *
     * @throws SQLException
     */
    private void getIdBBDD() throws SQLException {
        establecerConexion();
        sql = "Select max(id) from " + TABLA;
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            this.id = rs.getInt(1);
        }
        conexion.con.close();
    }

    // CONEXION A BDD
    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

}
