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
public class Franquicia {

    private final String TABLA = "Franquicia";

    public enum Parametro {

        ID, FRANQUICIADOID,
        NAME, FULLADDRESS, CP, STATEORPROVINCE, CITY, COUNTRY, PROFILEIMAGEURL, BACKGROUND,
        LONGITUDE, LATITUDE
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case FRANQUICIADOID:
                return (Tipo.INT);
            case NAME:
            case FULLADDRESS:
            case CP:
            case STATEORPROVINCE:
            case CITY:
            case COUNTRY:
            case PROFILEIMAGEURL:
            case BACKGROUND:
                return (Tipo.STRING);
            case LATITUDE:
            case LONGITUDE:
                return (Tipo.DOUBLE);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case NAME:
                this.name = s;
                break;
            case FULLADDRESS:
                this.fulladdress = s;
                break;
            case CP:
                this.cp = s;
                break;
            case STATEORPROVINCE:
                this.stateorprovince = s;
                break;
            case CITY:
                this.city = s;
                break;
            case COUNTRY:
                this.country = s;
                break;
            case PROFILEIMAGEURL:
                this.profileimageurl = s;
                break;
            case BACKGROUND:
                this.background = s;
                break;
        }
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID:
                this.id = n;
                break;
            case FRANQUICIADOID:
                this.franquiciadoid = n;
                break;
        }
    }

    private void setParametro(Parametro p, double d) {
        switch (p) {
            case LATITUDE:
                this.latitude = d;
                break;
            case LONGITUDE:
                this.longitude = d;
                break;
        }
    }

    // Datos
    private int id, franquiciadoid;
    private String name, fulladdress, cp, stateorprovince, city, country, profileimageurl, background;
    private Double longitude, latitude;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     * Constructor que recupera los datos a traves de su id.
     *
     * @param id
     * @throws SQLException
     */
    public Franquicia(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "Select name, franquiciadoid, fulladdress, cp, city, stateorprovince, country "
                + "FROM" + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.name = rs.getString("name");
        this.franquiciadoid = rs.getInt("franquiciadoid");
        this.country = rs.getString("country");
        this.fulladdress = rs.getString("fulladdress");
        this.city = rs.getString("city");
        this.stateorprovince = rs.getString("stateorprovince");
        this.cp = rs.getString("cp");
        getAdditionals();
        conexion.con.close();

    }

    public Franquicia(String name, int franquiciadoid, String fulladdress, String cp, String city, String stateorprovince, String country) throws SQLException {
        sql = "Insert INTO " + TABLA + " (name, franquiciadoid, fulladdress, cp, city, "
                + "stateorprovince, country) VALUES(?,?,?,?,?,?,?)";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, name);
        stm.setInt(2, franquiciadoid);
        stm.setString(3, fulladdress);
        stm.setString(4, cp);
        stm.setString(5, city);
        stm.setString(6, stateorprovince);
        stm.setString(7, country);
        stm.executeUpdate();
        getIdBBDD();
        this.name = name;
        this.franquiciadoid = franquiciadoid;
        this.fulladdress = fulladdress;
        this.cp = cp;
        this.city = city;
        this.stateorprovince = stateorprovince;
        this.country = country;
        conexion.con.close();
    }

    // SETTERS
    public void setAtributo(Parametro p, String atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setString(1, atributo);
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
                case STRING:
                    setParametro(p, rs.getString(p.name()));
                    break;
            }
        }
        conexion.con.close();
    }

    // GETTERS
    /**
     * @return the id
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
     * @return the franquiciadoid
     * @throws java.sql.SQLException
     */
    public int getFranquiciadoid() throws SQLException {
        getAtributo(Parametro.FRANQUICIADOID);
        return franquiciadoid;
    }

    /**
     * @return the fulladdres
     * @throws java.sql.SQLException
     */
    public String getFulladdress() throws SQLException {
        getAtributo(Parametro.FULLADDRESS);
        return fulladdress;
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
     * @return the stateorprovince
     * @throws java.sql.SQLException
     */
    public String getStateorprovince() throws SQLException {
        getAtributo(Parametro.STATEORPROVINCE);
        return stateorprovince;
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
     * @return @throws SQLException
     */
    public String getCountry() throws SQLException {
        getAtributo(Parametro.COUNTRY);
        return country;
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

    /**
     * @return the profileimageurl
     * @throws java.sql.SQLException
     */
    public String getProfileimageurl() throws SQLException {
        getAtributo(Parametro.PROFILEIMAGEURL);
        return profileimageurl;
    }

    /**
     * @return the background
     * @throws java.sql.SQLException
     */
    public String getBackground() throws SQLException {
        getAtributo(Parametro.BACKGROUND);
        return background;
    }

    /**
     *
     * @throws SQLException
     */
    private void getAdditionals() throws SQLException {
        getAtributo(Parametro.LATITUDE);
        getAtributo(Parametro.LONGITUDE);
        getAtributo(Parametro.PROFILEIMAGEURL);
        getAtributo(Parametro.BACKGROUND);
    }

    // GETTER BDD
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
