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
import java.util.Date;

/**
 *
 * @author Boomeraling
 */
public class Cupon {

    private final String TABLA = "Cupon";

    public enum Busqueda {

        NAME, CODE, DESCRIPTION, TITLE
    }

    public enum Parametro {

        ID, BUSINESSID, AVAILQUOTA,
        CODE, NAME, TITLE, DESCRIPTION, BACKGROUNDIMAGEURL, COUPONIMAGEURL, QR, BARCODE, CATEGORIA, KEYWORDS,
        DATESTART, DATEEXPIRATION,
        PRICE, REALPRICE,
        ACTIVO
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case BUSINESSID:
            case AVAILQUOTA:
                return (Tipo.INT);
            case CODE:
            case NAME:
            case TITLE:
            case DESCRIPTION:
            case BACKGROUNDIMAGEURL:
            case COUPONIMAGEURL:
            case QR:
            case BARCODE:
                return (Tipo.STRING);
            case DATESTART:
            case DATEEXPIRATION:
                return (Tipo.DATE);
            case PRICE:
            case REALPRICE:
                return (Tipo.DOUBLE);
            case ACTIVO:
                return (Tipo.BOOLEAN);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case CODE:
                this.code = s;
                break;
            case NAME:
                this.name = s;
                break;
            case TITLE:
                this.title = s;
                break;
            case DESCRIPTION:
                this.description = s;
                break;
            case BACKGROUNDIMAGEURL:
                this.backgroundimageurl = s;
                break;
            case COUPONIMAGEURL:
                this.couponimageurl = s;
                break;
            case QR:
                this.QR = s;
                break;
            case BARCODE:
                this.barcode = s;
                break;
            case CATEGORIA:
                this.categoria = s;
                break;
            case KEYWORDS:
                this.keywords = s;
                break;
        }
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID:
                this.id = n;
                break;
            case BUSINESSID:
                this.businessid = n;
                break;
            case AVAILQUOTA:
                this.availquota = n;
                break;
        }
    }

    private void setParametro(Parametro p, double n) {
        switch (p) {
            case PRICE:
                this.price = n;
                break;
            case REALPRICE:
                this.realprice = n;
                break;
        }
    }

    private void setParametro(Parametro p, Date d) {
        switch (p) {
            case DATESTART:
                this.datestart = d;
                break;
            case DATEEXPIRATION:
                this.dateexpiration = d;
                break;
        }
    }

    private void setParametro(Parametro p, boolean v) {
        switch (p) {
            case ACTIVO:
                this.activo = v;
                break;
        }
    }
    // Datos del cupon
    private int id, businessid, availquota;
    private String code, name, title, description, backgroundimageurl, couponimageurl, QR, barcode, categoria, keywords;
    private Date datestart, dateexpiration;
    private double price, realprice;
    private boolean activo;
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
    public Cupon(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "SELECT " + Parametro.BUSINESSID.name() + ", code, name, title, datestart, dateexpiration, "
                + "availquota, couponimageurl, activo, categoria FROM " + TABLA + " WHERE " + Parametro.ID.name() + "= ? ;";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.businessid = rs.getInt("businessid");
        this.code = rs.getString("code");
        this.name = rs.getString("name");
        this.title = rs.getString("title");
        this.datestart = rs.getDate("datestart");
        this.dateexpiration = rs.getDate("dateexpiration");
        this.availquota = rs.getInt("availquota");
        this.couponimageurl = rs.getString("couponimageurl");
        this.activo = rs.getBoolean("active");
        setParametro(Parametro.CATEGORIA, rs.getString(Parametro.CATEGORIA.name()));
        rs.close();
        stm.close();
        getAdditionals();
        conexion.con.close();

    }

    /**
     * Constructor que recupera los datos a traves del campo de busqueda. Puede
     * buscar segun el name, code, description o title
     *
     * @param busqueda
     * @param b
     * @throws SQLException
     */
    public Cupon(String busqueda, Busqueda b) throws SQLException {

        switch (b) {
            case NAME:
                sql = "SELECT " + Parametro.ID.name() + ", businessid, code, title, datestart, dateexpiration, "
                        + "availquota, couponimageurl, activo,categoria FROM " + TABLA + " WHERE name = ? ;";
                this.name = busqueda;
                break;
            case CODE:
                sql = "SELECT " + Parametro.ID.name() + ", businessid, name, title, datestart, dateexpiration, "
                        + "availquota, couponimageurl, activo, categoria FROM " + TABLA + " WHERE code = ? ;";
                this.code = busqueda;
                break;
            case DESCRIPTION:
                // description LIKE maybe?
                sql = "SELECT " + Parametro.ID.name() + ", businessid, name, code, title, datestart, dateexpiration, "
                        + "availquota, couponimageurl, activo, categoria FROM " + TABLA + " WHERE description = ? ;";
                this.description = busqueda;
                break;
            case TITLE:
                sql = "SELECT " + Parametro.ID.name() + ", businessid, name, code, datestart, dateexpiration, "
                        + "availquota, couponimageurl, activo, categoria FROM " + TABLA + " WHERE title = ? ;";
                this.title = busqueda;
                break;
        }
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, busqueda);
        rs = stm.executeQuery();
        rs.next();
        if (b == Busqueda.NAME) {
            this.code = rs.getString("code");
            this.title = rs.getString("title");
        }
        if (b == Busqueda.CODE) {
            this.title = rs.getString("title");
            this.name = rs.getString("name");
        }
        if (b == Busqueda.DESCRIPTION) {
            this.code = rs.getString("code");
            this.title = rs.getString("title");
            this.name = rs.getString("name");
        }
        if (b == Busqueda.TITLE) {
            this.code = rs.getString("code");
            this.name = rs.getString("name");
        }
        this.id = rs.getInt("id");
        this.businessid = rs.getInt("businessid");
        this.datestart = rs.getDate("datestart");
        this.dateexpiration = rs.getDate("dateexpiration");
        this.availquota = rs.getInt("availquota");
        this.couponimageurl = rs.getString("couponimageurl");
        this.activo = rs.getBoolean(Parametro.ACTIVO.name());
        setParametro(Parametro.CATEGORIA, rs.getString(Parametro.CATEGORIA.name()));
        rs.close();
        stm.close();
        getAdditionals();
        conexion.con.close();
    }

    /**
     * Constructor de RegistroCupon
     *
     * @param businessid
     * @param code
     * @param name
     * @param title
     * @param datestart
     * @param dateexpiration
     * @param availquota
     * @param couponimageurl
     * @param categoria
     * @throws SQLException
     */
    public Cupon(int businessid, String code, String name, String title, Date datestart,
            Date dateexpiration, int availquota, String couponimageurl, String categoria)
            throws SQLException {
        this.businessid = businessid;
        this.code = code;
        this.name = name;
        this.title = title;
        this.datestart = datestart;
        this.dateexpiration = dateexpiration;
        this.availquota = availquota;
        this.couponimageurl = couponimageurl;
        this.activo = false;
        this.categoria = categoria;
        sql = "Insert INTO " + TABLA + " (businessid, code, name, title, datestart, dateexpiration, "
                + "availquota, couponimageurl, activo, categoria) VALUES(?,?,?,?,?,?,?,?,?,?)";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, businessid);
        stm.setString(2, code);
        stm.setString(3, name);
        stm.setString(4, title);
        stm.setDate(5, (java.sql.Date) datestart);
        stm.setDate(6, (java.sql.Date) dateexpiration);
        stm.setInt(7, availquota);
        stm.setString(8, couponimageurl);
        stm.setBoolean(9, activo);
        stm.setString(10, categoria);
        stm.executeUpdate();
        getIdBBDD();
        conexion.con.close();

    }

    // ELIMINAR
    /**
     *
     * @throws SQLException
     */
    public void deleteCupon() throws SQLException {
        establecerConexion();
        sql = "Delete " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, getId());
        stm.executeUpdate();
        stm.close();
        conexion.con.close();
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
        stm.setDate(1, (java.sql.Date) atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

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

    // GETTERS
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the QR
     * @throws java.sql.SQLException
     */
    public String getQR() throws SQLException {
        getAtributo(Parametro.QR);
        return QR;
    }

    /**
     * @return the businessid
     * @throws java.sql.SQLException
     */
    public int getBusinessid() throws SQLException {
        getAtributo(Parametro.BUSINESSID);
        return businessid;
    }

    /**
     * @return the code
     * @throws java.sql.SQLException
     */
    public String getCode() throws SQLException {
        getAtributo(Parametro.BARCODE);
        return code;
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
     * @return the barcode
     * @throws java.sql.SQLException
     */
    public String getBarcode() throws SQLException {
        getAtributo(Parametro.BARCODE);
        return barcode;
    }

    /**
     * @return the title
     * @throws java.sql.SQLException
     */
    public String getTitle() throws SQLException {
        getAtributo(Parametro.TITLE);
        return title;
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
     * @return the datestart
     * @throws java.sql.SQLException
     */
    public Date getDatestart() throws SQLException {
        getAtributo(Parametro.DATESTART);
        return datestart;
    }

    /**
     * @return the dateexpiration
     * @throws java.sql.SQLException
     */
    public Date getDateexpiration() throws SQLException {
        getAtributo(Parametro.DATEEXPIRATION);
        return dateexpiration;
    }

    /**
     * @return the backgroundimageurl
     * @throws java.sql.SQLException
     */
    public String getBackgroundimageurl() throws SQLException {
        getAtributo(Parametro.BACKGROUNDIMAGEURL);
        return backgroundimageurl;
    }

    /**
     * @return the price
     * @throws java.sql.SQLException
     */
    public double getPrice() throws SQLException {
        getAtributo(Parametro.PRICE);
        return price;
    }

    /**
     * @return the realprice
     * @throws java.sql.SQLException
     */
    public double getRealprice() throws SQLException {
        getAtributo(Parametro.REALPRICE);
        return realprice;
    }

    /**
     * @return the availquota
     * @throws java.sql.SQLException
     */
    public int getAvailquota() throws SQLException {
        getAtributo(Parametro.AVAILQUOTA);
        return availquota;
    }

    /**
     * @return the couponimageurl
     * @throws java.sql.SQLException
     */
    public String getCouponimageurl() throws SQLException {
        getAtributo(Parametro.COUPONIMAGEURL);
        return couponimageurl;
    }

    /**
     *
     * @return @throws SQLException
     */
    public String getCategoria() throws SQLException {
        getAtributo(Parametro.CATEGORIA);
        return couponimageurl;
    }

    /**
     *
     * @return @throws SQLException
     */
    public String getKeywords() throws SQLException {
        getAtributo(Parametro.KEYWORDS);
        return couponimageurl;
    }

    // GETTERS BDD
    /**
     *
     * @throws SQLException
     */
    private void getIdBBDD() throws SQLException {
        establecerConexion();
        sql = "Select max(id) from " + TABLA;
        stm = conexion.con.prepareStatement(sql);
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            this.id = rs.getInt(1);
        }
        rs.close();
        stm.close();
        conexion.con.close();
    }

    private void getAdditionals() throws SQLException {
        getAtributo(Parametro.DESCRIPTION);
        getAtributo(Parametro.BACKGROUNDIMAGEURL);
        getAtributo(Parametro.PRICE);
        getAtributo(Parametro.REALPRICE);
        getAtributo(Parametro.QR);
        getAtributo(Parametro.BARCODE);
        getAtributo(Parametro.KEYWORDS);
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
