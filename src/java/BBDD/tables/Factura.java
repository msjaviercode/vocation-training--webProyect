/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD.tables;

import BBDD.utilities.Conector;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author Mushi
 */
public class Factura {

    private final String TABLA = "FACTURA";

    public enum Busqueda {

        ID, DESTINATARIOID, EMISORID,
        NUMEROFACTURA
    }

    public enum Parametro {

        ID, DESTINATARIOID, EMISORID,
        NUMEROFACTURA, TIPODOCUMENTO,
        FECHAEMISION, FECHAVENCIMIENTO, FECHACOBRO,
        IMPORTE,
        COBRADO,
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    public enum TipoFactura {

        NORMAL, RECTIFICATIVA
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case DESTINATARIOID:
            case EMISORID:
                return (Tipo.INT);
            case NUMEROFACTURA:
            case TIPODOCUMENTO:
                return (Tipo.STRING);
            case FECHAEMISION:
            case FECHAVENCIMIENTO:
            case FECHACOBRO:
                return (Tipo.DATE);
            case IMPORTE:
                return (Tipo.DOUBLE);
            case COBRADO:
                return (Tipo.BOOLEAN);
        }
        return null;
    }

    private void setParametro(Parametro p, int i) {
        switch (p) {
            case DESTINATARIOID:
                this.destinatarioid = i;
                break;
            case EMISORID:
                this.emisorid = i;
                break;
        }
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case NUMEROFACTURA:
                this.numerofactura = s;
                break;
            case TIPODOCUMENTO:
                this.tipodocumento = s;
                break;
        }
    }

    private void setParametro(Parametro p, double n) {
        switch (p) {
            case IMPORTE:
                this.importe = n;
                break;
        }
    }

    private void setParametro(Parametro p, Date d) {
        switch (p) {
            case FECHAEMISION:
                this.fechaemision = d;
                break;
            case FECHAVENCIMIENTO:
                this.fechavencimiento = d;
                break;
            case FECHACOBRO:
                this.fechacobro = d;
                break;
        }
    }

    private void setParametro(Parametro p, boolean v) {
        switch (p) {
            case COBRADO:
                this.cobrado = v;
                break;
        }
    }

    // Datos
    private int id, destinatarioid, emisorid;
    private String numerofactura, tipodocumento;
    private Date fechaemision, fechavencimiento, fechacobro;
    private double importe;
    private boolean cobrado;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     * Constructor patata
     *
     * @param t
     * @param destinatarioid
     * @param fechaemision
     * @param fechavencimiento
     * @param emisorid
     * @param tipodocumento
     * @param importe
     * @param cobrado
     * @throws SQLException
     */
    public Factura(TipoFactura t, int destinatarioid, Date fechaemision, Date fechavencimiento, int emisorid, String tipodocumento, Double importe, boolean cobrado) throws SQLException {
        this.numerofactura = numeroFactura(t);
        sql = "Insert INTO " + TABLA + " (numerofactura, destinatarioid, fechaemision, fechavencimiento, "
                + "emisorid, tipodocumento, importe, cobrado)  VALUES(?,?,?,?,?,?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, numerofactura);
        stm.setInt(2, destinatarioid);
        stm.setDate(3, fechaemision);
        stm.setDate(4, fechavencimiento);
        stm.setInt(5, emisorid);
        stm.setString(6, tipodocumento);
        stm.setDouble(7, importe);
        stm.setBoolean(8, cobrado);
        stm.executeUpdate();
        getIdBBDD();
        this.destinatarioid = destinatarioid;
        this.fechaemision = fechaemision;
        this.fechavencimiento = fechavencimiento;
        this.emisorid = emisorid;
        this.tipodocumento = tipodocumento;
        this.importe = importe;
        this.cobrado = cobrado;
        conexion.con.close();
    }

    /**
     *
     * @param busqueda
     * @param b
     * @throws SQLException
     */
    public Factura(String busqueda, Busqueda b) throws SQLException {

        switch (b) {
            case NUMEROFACTURA:
                sql = "Select id, emisorid, fechaemision, fechavencimiento, destinatarioid, tipodocumento, "
                        + "importe, cobrado FROM " + TABLA + " where numerofactura=?";
                break;
        }
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, busqueda);
        rs = stm.executeQuery();
        rs.next();
        if (b == Busqueda.NUMEROFACTURA) {
            this.numerofactura = busqueda;
            this.id = rs.getInt("id");
            this.destinatarioid = rs.getInt("destinatarioid");
            this.fechaemision = rs.getDate("fechaemision");
            this.fechavencimiento = rs.getDate("fechavencimiento");
            this.emisorid = rs.getInt("emisorid");
            this.tipodocumento = rs.getString("tipodocumento");
            this.importe = rs.getDouble("importe");
            this.cobrado = rs.getBoolean("cobrado");
        }
        rs.close();
        stm.close();
        getFechacobroBBDD();
        conexion.con.close();
    }

    /**
     *
     * @param t
     * @return
     * @throws SQLException
     */
    private String numeroFactura(TipoFactura t) throws SQLException {
        Calendar c1 = Calendar.getInstance();
        int actual = c1.get(Calendar.YEAR);
        //KK<año>F<id:6>
        //RKK<año>F<id:6>
        String temp = "";
        sql = "Select " + Parametro.NUMEROFACTURA.name() + " FROM " + TABLA + " WHERE id=(Select max(id) from " + TABLA + ")";
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            temp = rs.getString(1);
        }

        //Creación de subStrings
        String Kyear = temp.split("F")[0]; //KK2014
        String fid = temp.split("F")[1]; //000000
        int intid;
        //Tratamiento de la Factura-ID
        intid = Integer.parseInt(fid) + 1;
        fid = "" + intid;
        for (int i = 6; i > fid.length(); i--) {
            fid = "0" + fid;
        }
        //Tratamiento del YEAR   
        Kyear = Kyear.substring(Kyear.length() - 4, Kyear.length());
        if (actual > Integer.parseInt(Kyear)) {
            fid = "000000";
            Kyear = "" + actual;
        }
        temp = "KK" + Kyear + "F" + fid;
        if (t == TipoFactura.RECTIFICATIVA) {
            temp = "R" + temp;
        }
        return temp;
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
     * @return the numerofactura
     * @throws java.sql.SQLException
     */
    public String getNumerofactura() throws SQLException {
        getAtributo(Parametro.NUMEROFACTURA);
        return numerofactura;
    }

    /**
     * @return the distinatarioid
     * @throws java.sql.SQLException
     */
    public int getDistinatarioid() throws SQLException {
        getAtributo(Parametro.DESTINATARIOID);
        return destinatarioid;
    }

    /**
     * @return the fechaemision
     * @throws java.sql.SQLException
     */
    public Date getFechaemision() throws SQLException {
        getAtributo(Parametro.FECHAEMISION);
        return fechaemision;
    }

    /**
     * @return the fechavencimiento
     * @throws java.sql.SQLException
     */
    public Date getFechavencimiento() throws SQLException {
        getAtributo(Parametro.FECHAVENCIMIENTO);
        return fechavencimiento;
    }

    /**
     * @return the emisorid
     * @throws java.sql.SQLException
     */
    public int getEmisorid() throws SQLException {
        getAtributo(Parametro.EMISORID);
        return emisorid;
    }

    /**
     * @return the tipodocumento
     * @throws java.sql.SQLException
     */
    public String getTipodocumento() throws SQLException {
        getAtributo(Parametro.TIPODOCUMENTO);
        return tipodocumento;
    }

    /**
     * @return the importe
     * @throws java.sql.SQLException
     */
    public double getImporte() throws SQLException {
        getAtributo(Parametro.IMPORTE);
        return importe;
    }

    /**
     * @return the cobrado
     * @throws java.sql.SQLException
     */
    public boolean isCobrado() throws SQLException {
        getAtributo(Parametro.COBRADO);
        return cobrado;
    }

    /**
     * @return the fechacobro
     * @throws java.sql.SQLException
     */
    public Date getFechacobro() throws SQLException {
        getAtributo(Parametro.FECHACOBRO);
        return fechacobro;
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

    /**
     *
     * @throws SQLException
     */
    private void getFechacobroBBDD() throws SQLException {
        establecerConexion();
        sql = "Select " + Parametro.FECHACOBRO.name() + " FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        if (rs.next()) {
            this.fechacobro = rs.getDate(Parametro.FECHACOBRO.name());
        }
        conexion.con.close();
    }

    // CONEXION A BDD
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
