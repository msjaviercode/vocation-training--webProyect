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
 *
 * @author Axel
 */
public class CuponesUsuario {

    private final String TABLA = "CuponesUsuario";

    public enum Busqueda {

        NAME, CODE, DESCRIPTION, TITLE
    }

    public enum Parametro {

        ID, ID_USUARIO, ID_CUPON,
        FECHA_COMPRA,
        PRECIO,
        ESTADO
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case ID_CUPON:
            case ID_USUARIO:
                return (Tipo.INT);
            case FECHA_COMPRA:
                return Tipo.DATE;
            case PRECIO:
                return Tipo.DOUBLE;
            case ESTADO:
                return (Tipo.STRING);
        }
        return null;
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID:
                this.id = n;
                break;
            case ID_CUPON:
                this.id_cupon = n;
                break;
            case ID_USUARIO:
                this.id_usuario = n;
                break;
        }
    }

    private void setParametro(Parametro p, Date d) {
        switch (p) {
            case FECHA_COMPRA:
                this.fecha_compra = d;
                break;
        }
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case ESTADO:
                this.estado = s;
                break;
        }
    }

    private void setParametro(Parametro p, double d) {
        switch (p) {
            case PRECIO:
                this.precio = d;
                break;
        }
    }

    // Datos
    private int id, id_usuario, id_cupon;
    private Date fecha_compra;
    private double precio;
    private String estado;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    /**
     * Este es un constructor que dada la ID carga todos los valores al objeto
     * creado
     *
     * @param busqueda
     * @param b
     * @throws SQLException
     */
    public CuponesUsuario(int busqueda, Parametro b) throws SQLException {

        if (Parametro.ID == b) {
            this.id = busqueda;
            sql = "Select id_usuario, id_cupon, fecha_compra, precio, estado FROM "
                    + TABLA + " where " + Parametro.ID.name() + "=?";
        }

        if (Parametro.ID_CUPON == b) {
            this.id_cupon = busqueda;
            sql = "Select id, id_usuario, fecha_compra, precio, estado FROM "
                    + TABLA + " where id_cupon=?";
        }

        if (Parametro.ID_USUARIO == b) {
            this.id_usuario = busqueda;
            sql = "Select id, id_cupon, fecha_compra, precio, estado FROM "
                    + TABLA + " where id_usuario=?";
        }

        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, busqueda);
        rs = stm.executeQuery();
        rs.next();
        this.fecha_compra = rs.getDate("fecha_compra");
        this.precio = rs.getDouble("precio");
        this.estado = rs.getString("estado");
        if (Parametro.ID == b) {
            this.id_usuario = rs.getInt("id_usuario");
            this.id_cupon = rs.getInt("id_cupon");
        }

        if (Parametro.ID_CUPON == b) {
            this.id_usuario = rs.getInt("id_usuario");
            this.id = rs.getInt("id");
        }

        if (Parametro.ID_USUARIO == b) {
            this.id = rs.getInt("id");
            this.id_cupon = rs.getInt("id_cupon");
        }
        rs.close();
        stm.close();
        conexion.con.close();
    }

    /**
     *
     * @param id_usuario
     * @param fecha
     * @throws SQLException
     */
    public CuponesUsuario(int id_usuario, Date fecha) throws SQLException {
        this.id_usuario = id_usuario;
        sql = "Select id, id_cupon, fecha_compra, precio, estado FROM "
                + TABLA + " where id_usuario=? AND fecha_compra >= ?";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id_usuario);
        stm.setDate(2, fecha);
        rs = stm.executeQuery();
        rs.next();
        this.id = rs.getInt("id");
        this.id_cupon = rs.getInt("id_cupon");
        this.fecha_compra = rs.getDate("fecha_compra");
        this.precio = rs.getDouble("precio");
        this.estado = rs.getString("estado");
        rs.close();
        stm.close();
        conexion.con.close();
    }

    /**
     *
     * Constructor para crear uno nuevo con los valores elegidos.
     *
     * @param id_usuario
     * @param id_cupon
     * @param fecha_compra
     * @param precio
     * @param estado
     * @throws java.sql.SQLException
     */
    public CuponesUsuario(int id_usuario, int id_cupon, Date fecha_compra, double precio, String estado) throws SQLException {
        this.id_usuario = id_usuario;
        this.id_cupon = id_cupon;
        this.fecha_compra = fecha_compra;
        this.precio = precio;
        this.estado = estado;
        sql = "Insert into " + TABLA + " (id_usuario, id_cupon, fecha_compra, precio, estado) "
                + "VALUES(?,?,?,?,?) ;";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id_usuario);
        stm.setInt(2, id_cupon);
        stm.setDate(3, fecha_compra);
        stm.setDouble(4, precio);
        stm.setString(5, estado);
        stm.executeUpdate();
        getIdBBDD();
        conexion.con.close();
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
     * @return the id_usuario
     * @throws java.sql.SQLException
     */
    public int getId_usuario() throws SQLException {
        getAtributo(Parametro.ID_USUARIO);
        return id_usuario;
    }

    /**
     * @return the id_comercio
     * @throws java.sql.SQLException
     */
    public int getId_cupon() throws SQLException {
        getAtributo(Parametro.ID_CUPON);
        return id_cupon;
    }

    /**
     * @return the fecha_compra
     * @throws java.sql.SQLException
     */
    public Date getFecha_compra() throws SQLException {
        getAtributo(Parametro.FECHA_COMPRA);
        return fecha_compra;
    }

    /**
     * @return the estado
     * @throws java.sql.SQLException
     */
    public String getEstado() throws SQLException {
        getAtributo(Parametro.ESTADO);
        return estado;
    }

    /**
     * @return the precio
     * @throws java.sql.SQLException
     */
    public double getPrecio() throws SQLException {
        getAtributo(Parametro.PRECIO);
        return precio;
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

    // CONEXION A BDD
    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

}
