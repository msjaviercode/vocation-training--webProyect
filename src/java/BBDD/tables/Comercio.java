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

/**
 * @author Boomeraling
 */
/**
 * id businessname email fbid password registerdate lastlogin country fulladress
 * city stateorprovince cp latitude longitude description profileimageurl active
 */
public class Comercio {

    private final String TABLA = "Comercio";

    public enum Busqueda {

        BUSINESSNAME, EMAIL, FBID
    }

    public enum Parametro {

        ID, BUSINESSNAME, EMAIL, FBID, FRANQUICIAID, PASSWORD, COUNTRY, FULLADDRESS, CITY, STATEORPROVINCE,
        CP, ACTIVE, DESCRIPTION, PROFILEIMAGEURL, LASTLOGIN, REGISTERDATE, LONGITUDE, LATITUDE
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case FRANQUICIAID:
                return (Tipo.INT);
            case BUSINESSNAME:
            case EMAIL:
            case FBID:
            case PASSWORD:
            case COUNTRY:
            case FULLADDRESS:
            case CITY:
            case STATEORPROVINCE:
            case CP:
            case DESCRIPTION:
            case PROFILEIMAGEURL:
                return (Tipo.STRING);
            case ACTIVE:
                return (Tipo.BOOLEAN);
            case LASTLOGIN:
            case REGISTERDATE:
                return (Tipo.DATE);
            case LONGITUDE:
            case LATITUDE:
                return (Tipo.DOUBLE);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case BUSINESSNAME:
                this.businessname = s;
                break;
            case EMAIL:
                this.email = s;
                break;
            case FBID:
                this.fbid = s;
                break;
            case PASSWORD:
                this.password = s;
                break;
            case COUNTRY:
                this.country = s;
                break;
            case FULLADDRESS:
                this.fulladdress = s;
                break;
            case CITY:
                this.city = s;
                break;
            case STATEORPROVINCE:
                this.stateorprovince = s;
                break;
            case CP:
                this.cp = s;
                break;
            case DESCRIPTION:
                this.description = s;
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
            case FRANQUICIAID:
                this.franquiciaID = n;
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

    private void setParametro(Parametro p, Double n) {
        switch (p) {
            case LATITUDE:
                this.latitude = n;
                break;
            case LONGITUDE:
                this.longitude = n;
                break;
        }
    }

    private void setParametro(Parametro p, boolean v) {
        switch (p) {
            case ACTIVE:
                this.active = v;
                break;
        }
    }

    // Datos
    private int id, franquiciaID;
    private String businessname, email, fbid, password, country, fulladdress, city, stateorprovince, cp, description, profileimageurl;
    private boolean active;
    private Date lastLogin, registerDate;
    private Double longitude, latitude;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     * Recupera los datos a traves de su id.
     *
     * @param id
     * @throws SQLException
     */
    public Comercio(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "Select " + Parametro.BUSINESSNAME.name() + "," + Parametro.EMAIL.name() + ", " + Parametro.PASSWORD.name() + ", " + Parametro.COUNTRY.name() + ", " + Parametro.FULLADDRESS.name() + "," + Parametro.CITY.name() + "," + Parametro.STATEORPROVINCE.name() + ", " + Parametro.CP.name() + ", " + Parametro.ACTIVE.name() + " FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.businessname = rs.getString("businessname");
        this.email = rs.getString("email");
        this.password = rs.getString("password");
        this.country = rs.getString("country");
        this.fulladdress = rs.getString("fulladdress");
        this.city = rs.getString("city");
        this.stateorprovince = rs.getString("stateorprovince");
        this.cp = rs.getString("cp");
        this.active = rs.getBoolean("active");
        getAdditionals();
        conexion.con.close();

    }

    /**
     *
     * @param busqueda
     * @param p
     * @throws SQLException
     */
    public Comercio(String busqueda, Parametro p) throws SQLException {
        if (Parametro.BUSINESSNAME == p) {
            this.businessname = busqueda;
            sql = "Select " + Parametro.ID.name() + ", email, password, country, fulladdress, city, stateorprovince, cp, active FROM " + TABLA + " where businessname=?";
        }
        if (Parametro.EMAIL == p) {
            this.email = busqueda;
            sql = "Select " + Parametro.BUSINESSNAME.name() + ", " + Parametro.ID.name() + ", password, country, fulladdress, city, stateorprovince, cp, active FROM " + TABLA + " where email=?";
        }
        if (Parametro.FBID == p) {
            this.fbid = busqueda;
            sql = "Select " + Parametro.ID.name() + ", " + Parametro.BUSINESSNAME.name() + ", email, password, country, fulladdress, city, stateorprovince, cp, active FROM " + TABLA + " where fbid=?";
        }
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        if (Parametro.BUSINESSNAME == p) {
            stm.setString(1, businessname);
        }
        if (Parametro.EMAIL == p) {
            stm.setString(1, email);
        }
        if (Parametro.FBID == p) {
            stm.setString(1, fbid);
        }
        rs = stm.executeQuery();
        rs.next();
        this.id = rs.getInt("id");
        if (Parametro.EMAIL == p) {
            this.businessname = rs.getString("businessname");
        }
        if (Parametro.BUSINESSNAME == p) {
            this.email = rs.getString("email");
        }
        if (Parametro.FBID == p) {
            this.email = rs.getString("email");
            this.businessname = rs.getString("businessname");
        }
        this.password = rs.getString("password");
        this.country = rs.getString("country");
        this.fulladdress = rs.getString("fulladdress");
        this.city = rs.getString("city");
        this.stateorprovince = rs.getString("stateorprovince");
        this.cp = rs.getString("cp");
        this.active = rs.getBoolean("active");
        rs.close();
        getAdditionals();
        conexion.con.close();

    }

    /**
     *
     * @param businessname
     * @param email
     * @param password
     * @param country
     * @param fulladdress
     * @param city
     * @param stateorprovince
     * @param cp
     * @param active
     * @throws SQLException
     */
    public Comercio(String businessname, String email, String password, String country, String fulladdress, String city, String stateorprovince, String cp, boolean active) throws SQLException {
        // Fecha actual
        Date register = new Date(new java.util.Date().getTime());
        this.businessname = businessname;
        this.email = email;
        this.password = password;
        this.country = country;
        this.fulladdress = fulladdress;
        this.city = city;
        this.stateorprovince = stateorprovince;
        this.cp = cp;
        this.active = active;
        this.registerDate = register;
        sql = "Insert INTO " + TABLA + " (" + Parametro.BUSINESSNAME.name() + ", email, password, country, fulladdress, city, stateorprovince, cp, active, registerdate) VALUES(?,?,?,?,?,?,?,?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, businessname);
        stm.setString(2, email);
        stm.setString(3, password);
        stm.setString(4, country);
        stm.setString(5, fulladdress);
        stm.setString(6, city);
        stm.setString(7, stateorprovince);
        stm.setString(8, cp);
        stm.setBoolean(9, active);
        stm.setDate(10, register);
        stm.execute();
        getIdBBDD();
        conexion.con.close();

    }

    /**
     *
     * @throws SQLException
     */
    public void deleteComercio() throws SQLException {
        establecerConexion();
        sql = "Delete " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, getId());
        stm.executeUpdate();
        stm.close();
        conexion.con.close();

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

    public void setAtributo(Parametro p, String atributo) throws SQLException {
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setString(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);

    }

    public void setAtributo(Parametro p, int atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setInt(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, boolean atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setBoolean(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, double atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setDouble(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, Date atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setDate(1, atributo);
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
                case DOUBLE:
                    setParametro(p, rs.getDouble(p.name()));
                    break;
                case DATE:
                    setParametro(p, rs.getDate(p.name()));
                    break;
                case STRING:
                    setParametro(p, rs.getString(p.name()));
                    break;
                case BOOLEAN:
                    setParametro(p, rs.getBoolean(p.name()));
                    break;
            }
        }
        conexion.con.close();
    }

    private void getAdditionals() throws SQLException {
        getAtributo(Parametro.LATITUDE);
        getAtributo(Parametro.LONGITUDE);
        getAtributo(Parametro.PROFILEIMAGEURL);
        getAtributo(Parametro.DESCRIPTION);
        getAtributo(Parametro.FRANQUICIAID);
        getAtributo(Parametro.FBID);
        getAtributo(Parametro.REGISTERDATE);
        getAtributo(Parametro.LASTLOGIN);
    }

    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

    public int getId() {
        return id;
    }

    /**
     * @return the franquiciaID
     * @throws java.sql.SQLException
     */
    public int getFranquiciaID() throws SQLException {
        getAtributo(Parametro.FRANQUICIAID);
        return franquiciaID;
    }

    /**
     * @return the businessname
     * @throws java.sql.SQLException
     */
    public String getBusinessname() throws SQLException {
        getAtributo(Parametro.BUSINESSNAME);
        return businessname;
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
     * @return the fbid
     * @throws java.sql.SQLException
     */
    public String getFbid() throws SQLException {
        getAtributo(Parametro.FBID);
        return fbid;
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
     * @return the fulladdress
     * @throws java.sql.SQLException
     */
    public String getFulladdress() throws SQLException {
        getAtributo(Parametro.FULLADDRESS);
        return fulladdress;
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
     * @return the description
     * @throws java.sql.SQLException
     */
    public String getDescription() throws SQLException {
        getAtributo(Parametro.DESCRIPTION);
        return description;
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
     * @return the active
     * @throws java.sql.SQLException
     */
    public boolean isActive() throws SQLException {
        getAtributo(Parametro.ACTIVE);
        return active;
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
     * @return the registerDate
     * @throws java.sql.SQLException
     */
    public Date getRegisterDate() throws SQLException {
        getAtributo(Parametro.REGISTERDATE);
        return registerDate;
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
     * @return the latitude
     * @throws java.sql.SQLException
     */
    public Double getLatitude() throws SQLException {
        getAtributo(Parametro.LATITUDE);
        return latitude;
    }
}
