/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD.tables;

import BBDD.utilities.Conector;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {

    private final String TABLA = "Usuario";

    public enum Busqueda {

        USERNAME, EMAIL
    }

    public enum Parametro {

        ID,
        FBID, USERNAME, EMAIL, PASSWORD, COUNTRY, CITY, STATEORPROVINCE, FULLADDRESS, CP, PROFILEIMAGEURL,
        LASTLOGIN, REGISTERDATE,
        LATITUDE, LONGITUDE
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
                return (Tipo.INT);
            case FBID:
            case USERNAME:
            case EMAIL:
            case PASSWORD:
            case COUNTRY:
            case CITY:
            case STATEORPROVINCE:
            case FULLADDRESS:
            case CP:
            case PROFILEIMAGEURL:
                return (Tipo.STRING);
            case LASTLOGIN:
            case REGISTERDATE:
                return (Tipo.DATE);
            case LATITUDE:
            case LONGITUDE:
                return (Tipo.DOUBLE);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case FBID:
                this.fbid = s;
                break;
            case USERNAME:
                this.username = s;
                break;
            case EMAIL:
                this.email = s;
                break;
            case PASSWORD:
                this.password = s;
                break;
            case COUNTRY:
                this.country = s;
                break;
            case CITY:
                this.city = s;
                break;
            case STATEORPROVINCE:
                this.stateorprovince = s;
                break;
            case FULLADDRESS:
                this.fulladdress = s;
                break;
            case CP:
                this.cp = s;
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

    private void setParametro(Parametro p, double n) {
        switch (p) {
            case LATITUDE:
                this.latitude = n;
                break;
            case LONGITUDE:
                this.longitude = n;
                break;
        }
    }

    private void setParametro(Parametro p, Date d) {
        switch (p) {
            case LASTLOGIN:
                this.lastLogin = d;
                break;
            case REGISTERDATE:
                this.registerDate = d;
                break;
        }
    }

    // Datos
    private int id;
    private String fbid, username, email, password,
            country, city, stateorprovince,
            fulladdress, cp, profileimageurl;
    private Date lastLogin, registerDate;
    private Double latitude, longitude;

    // Variables Base de datos
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    /**
     * Este es un constructor que dada la ID carga todos los valores al objeto
     * creado
     *
     * @param id
     * @throws SQLException
     */
    public Usuario(int id) throws SQLException {
        establecerConexion();
        sql = "Select username, email, password, country, stateorprovince FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.id = id;
        this.username = rs.getString("username");
        this.email = rs.getString("email");
        this.password = rs.getString("password");
        this.country = rs.getString("country");
        this.stateorprovince = rs.getString("stateorprovince");
        rs.close();
        stm.close();
        getAdditionals();
        conexion.con.close();
    }

    /**
     * Este es un Constructor que dado el valor del nombre carga todos los
     * valores al objeto creado
     *
     * @param busqueda
     * @param p
     * @throws SQLException
     */
    public Usuario(String busqueda, Parametro p) throws SQLException {
        if (Parametro.USERNAME == p) {
            this.username = busqueda;
            sql = "Select id, email, password, country, stateorprovince FROM " + TABLA + " where username=?";
        }
        if (Parametro.EMAIL == p) {
            this.email = busqueda;
            sql = "Select username, id, password, country, stateorprovince FROM " + TABLA + " where email=?";
        }
        if (Parametro.FBID == p) {
            this.fbid = busqueda;
            sql = "Select username, email, id, password, country, stateorprovince FROM " + TABLA + " where fbid=?";
        }
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, busqueda);

        rs = stm.executeQuery();
        rs.next();
        this.id = rs.getInt("id");
        if (Parametro.EMAIL == p) {
            this.username = rs.getString("username");
        }
        if (Parametro.USERNAME == p) {
            this.email = rs.getString("email");
        }
        if (Parametro.FBID == p) {
            this.email = rs.getString("email");
            this.username = rs.getString("username");
        }

        this.password = rs.getString("password");
        this.country = rs.getString("country");
        this.stateorprovince = rs.getString("stateorprovince");

        rs.close();
        stm.close();
        conexion.con.close();

    }

    /**
     * Constuctor de RegistroComercio Sirve para crear un Comercio nuevo
     *
     * @param username
     * @param email
     * @param password
     * @param country
     * @param stateorprovince
     * @throws SQLException
     */
    public Usuario(String username, String email, String password, String country, String stateorprovince) throws SQLException {
        // Fecha actual
        Date register = new Date(new java.util.Date().getTime());
        sql = "Insert into " + TABLA + " (username, email, password, country, stateorprovince, registerdate) VALUES(?,?,?,?,?,?)";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, username);
        stm.setString(2, email);
        stm.setString(3, password);
        stm.setString(4, country);
        stm.setString(5, stateorprovince);
        stm.setDate(6, register);
        stm.executeUpdate();
        getIdBBDD();
        this.username = username;
        this.email = email;
        this.password = password;
        this.country = country;
        this.stateorprovince = stateorprovince;
        this.registerDate = register;
        conexion.con.close();
    }

    // Borrar
    /**
     *
     * @throws SQLException
     */
    public void deleteUsuario() throws SQLException {
        establecerConexion();
        sql = "Delete " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        stm.setInt(2, id);
        stm.executeUpdate();
        stm.close();
        conexion.con.close();
    }

    // ATTR
    public void setAtributo(Parametro p, String atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where id=?";
        stm.setInt(2, id);
        stm.setString(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, double atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where id=?";
        stm.setInt(2, id);
        stm.setDouble(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, Date atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where id=?";
        stm.setInt(2, id);
        stm.setDate(1, (java.sql.Date) atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void getAtributo(Parametro p) throws SQLException {
        establecerConexion();
        sql = "Select " + p.name() + " FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, getId());
        rs = stm.executeQuery();
        if (rs.next()) {

            switch (getTipo(p)) {
                case INT:
                    setParametro(p, rs.getInt(p.name()));
                    break;
                case DOUBLE:
                    setParametro(p, rs.getDouble(p.name()));
                    break;
                case DATE:
                    setParametro(p, rs.getDate(p.name()));
                    break;
                case STRING:
                    setParametro(p, rs.getString(p.name()));
                    break;
            }
        }
        conexion.con.close();
    }

    // GETTERS
    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the fbid
     * @throws SQLException
     */
    public String getFbid() throws SQLException {
        getAtributo(Parametro.FBID);
        return fbid;
    }

    /**
     * @return the username
     * @throws java.sql.SQLException
     */
    public String getUsername() throws SQLException {
        getAtributo(Parametro.USERNAME);
        return username;
    }

    /**
     * @return the email
     * @throws java.sql.SQLException
     */
    public String getEmail() throws SQLException {
        getAtributo(Parametro.EMAIL);
        return email;
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
     * @return the country
     * @throws java.sql.SQLException
     */
    public String getCountry() throws SQLException {
        getAtributo(Parametro.COUNTRY);
        return country;
    }

    /**
     * @return the stateorprovince
     * @throws java.sql.SQLException
     */
    public String getStateorprovince() throws SQLException {
        getAtributo(Parametro.STATEORPROVINCE);
        return stateorprovince;
    }

    /**
     * @return the cp
     * @throws java.sql.SQLException
     */
    public String getCp() throws SQLException {
        getAtributo(Parametro.CP);
        return cp;
    }

    /**
     * @return the fulladdress
     * @throws java.sql.SQLException
     */
    public String getFulladdress() throws SQLException {
        getAtributo(Parametro.FULLADDRESS);
        return fulladdress;
    }

    /**
     * @return the registerDate
     * @throws java.sql.SQLException
     */
    public Date getRegisterDate() throws SQLException {
        getAtributo(Parametro.REGISTERDATE);
        return registerDate;
    }

    /**
     * @return the latitude
     * @throws java.sql.SQLException
     */
    public Double getLatitude() throws SQLException {
        getAtributo(Parametro.LATITUDE);
        return latitude;
    }

    /**
     * @return the longitude
     * @throws java.sql.SQLException
     */
    public Double getLongitude() throws SQLException {
        getAtributo(Parametro.LONGITUDE);
        return longitude;
    }

    /**
     * @return the profilesimageurl
     * @throws java.sql.SQLException
     */
    public String getProfilesimageurl() throws SQLException {
        getAtributo(Parametro.PROFILEIMAGEURL);
        return profileimageurl;
    }

    /**
     * @return the lastLogin
     * @throws java.sql.SQLException
     */
    public Date getLastLogin() throws SQLException {
        getAtributo(Parametro.LASTLOGIN);
        return lastLogin;
    }

    /**
     * @return the city
     * @throws java.sql.SQLException
     */
    public String getCity() throws SQLException {
        getAtributo(Parametro.CITY);
        return city;
    }

    /**
     *
     * @throws SQLException
     */
    private void getAdditionals() throws SQLException {
        getAtributo(Parametro.LATITUDE);
        getAtributo(Parametro.LONGITUDE);
        getAtributo(Parametro.PROFILEIMAGEURL);
        getAtributo(Parametro.FBID);
        getAtributo(Parametro.REGISTERDATE);
        getAtributo(Parametro.LASTLOGIN);
        getAtributo(Parametro.FULLADDRESS);
        getAtributo(Parametro.CITY);

    }

    // GETTERS BDD
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

    // CONEXION BDD
    /**
     *
     * @throws SQLException
     */
    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

}
