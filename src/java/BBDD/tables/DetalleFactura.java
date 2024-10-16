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
 * @author Axel
 */
public class DetalleFactura {

    private final String TABLA = "DetalleFactura";

    public enum Busqueda {

        FACTURAID, IM_A_POTATO
    }

    public enum Parametro {

        ID, FACTURAID, CANTIDAD,
        CONCEPTO,
        PRECIO
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case FACTURAID:
            case CANTIDAD:
                return (Tipo.INT);
            case CONCEPTO:
                return (Tipo.STRING);
            case PRECIO:
                return (Tipo.DOUBLE);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case CONCEPTO:
                this.concepto = s;
                break;
        }
    }

    private void setParametro(Parametro p, int i) {
        switch (p) {
            case ID:
                this.id = i;
                break;
            case FACTURAID:
                this.facturaid = i;
                break;
            case CANTIDAD:
                this.cantidad = i;
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
    private int id, facturaid, cantidad;
    private String concepto;
    private double precio;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     * Este es un constructor que dada la ID carga todos los valores al objeto
     * creado
     *
     * @param id
     * @throws SQLException
     */
    public DetalleFactura(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "Select facturaid, concepto, cantidad, precio FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.facturaid = rs.getInt("facturaid");
        this.concepto = rs.getString("concepto");
        this.cantidad = rs.getInt("cantidad");
        this.precio = rs.getDouble("precio");
        conexion.con.close();
    }

    public DetalleFactura(int facturaid, String concepto, int cantidad, double precio) throws SQLException {
        this.facturaid = facturaid;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.precio = precio;
        sql = "Insert INTO " + TABLA + " (facturaid, concepto, cantidad, precio) VALUES(?,?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, facturaid);
        stm.setString(2, concepto);
        stm.setInt(3, cantidad);
        stm.setDouble(4, precio);
        stm.execute();
        getIdBBDD();
        conexion.con.close();
    }

    // ELIMINAR
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

    // SET
    public void setAtributo(Parametro p, double atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setDouble(1, atributo);
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

    public void setAtributo(Parametro p, int atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, getId());
        stm.setInt(1, atributo);
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
     * @return the facturaid
     * @throws java.sql.SQLException
     */
    public int getFacturaid() throws SQLException {
        getAtributo(Parametro.FACTURAID);
        return facturaid;
    }

    /**
     * @return the concepto
     * @throws java.sql.SQLException
     */
    public String getConcepto() throws SQLException {
        getAtributo(Parametro.CONCEPTO);
        return concepto;
    }

    /**
     * @return the cantidad
     * @throws java.sql.SQLException
     */
    public int getCantidad() throws SQLException {
        getAtributo(Parametro.CANTIDAD);
        return cantidad;
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
    /**
     * Conecta con la base de Datos
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
