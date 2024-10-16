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
 * @author Administrador
 */
public class Condicion {

    public enum Busqueda {

        DESCRIPCION
    }

    public enum Parametro {

        ID, DESCRIPCION, IMAGEN, ESTADO
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
                return (Tipo.INT);
            case DESCRIPCION:
            case IMAGEN:
                return (Tipo.STRING);
            case ESTADO:
                return (Tipo.BOOLEAN);
        }
        return null;
    }

    private final String TABLA = "Condicion";

    /**
     *
     * Base de datos
     *
     */
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case DESCRIPCION:
                this.descripcion = s;
                break;
            case IMAGEN:
                this.imagen = s;
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

    private void setParametro(Parametro p, boolean b) {
        switch (p) {
            case ESTADO:
                this.estado = b;
                break;

        }
    }

    // Variables de las tablas Condicion    
    private int id;
    private String descripcion;
    private String imagen;
    private boolean estado;

    // CONSTRUCTORES
    /**
     *
     * Recupera los datos a partir de la id.
     *
     * @param id
     * @throws SQLException
     */
    public Condicion(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "Select " + Parametro.DESCRIPCION.name() + "," + Parametro.ESTADO.name() + "," + Parametro.IMAGEN.name() + " FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.descripcion = rs.getString(Parametro.DESCRIPCION.name());
        this.imagen = rs.getString(Parametro.IMAGEN.name());
        this.estado = rs.getBoolean(Parametro.ESTADO.name());
        conexion.con.close();
    }

    /**
     * 
     * @param descripcion
     * @param imagen
     * @throws SQLException 
     */
    public Condicion(String descripcion, String imagen) throws SQLException {
        this.estado = false;
        this.descripcion = descripcion;
        this.imagen = imagen;
        sql = "Insert INTO " + TABLA + " (" + Parametro.DESCRIPCION.name() + ", " + Parametro.IMAGEN.name() + ", " + Parametro.ESTADO.name() + ") values (?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, descripcion);
        stm.setString(2, imagen);
        stm.setBoolean(3, estado);
        stm.execute();
        getIdBBDD();
        conexion.con.close();
    }

    public void setAtributo(Parametro p, String atributo) throws SQLException {
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, id);
        stm.setString(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);

    }

    public void setAtributo(Parametro p, int atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, id);
        stm.setInt(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void setAtributo(Parametro p, boolean atributo) throws SQLException {
        establecerConexion();
        sql = "Update " + TABLA + " set  " + p.name() + "=? where " + Parametro.ID.name() + "=?";
        stm.setInt(2, id);
        stm.setBoolean(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    private String getAtributoBBDD(Parametro p, String atributo) throws SQLException {

        establecerConexion();
        String temp = null;
        sql = "Select " + p.name() + " FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        if (rs.next()) {

            setParametro(p, rs.getString(p.name()));
            temp = rs.getString(p.name());
        }
        conexion.con.close();
        return temp;
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
     * @return the Description
     * @throws java.sql.SQLException
     */
    public String getDescripcion() throws SQLException {
        getAtributo(Parametro.DESCRIPCION);
        return descripcion;
    }

    /**
     * @return the Image url
     * @throws java.sql.SQLException
     */
    public String getImage() throws SQLException {
        getAtributo(Parametro.IMAGEN);
        return imagen;
    }

    /**
     * @return the state
     * @throws java.sql.SQLException
     */
    public boolean getEstado() throws SQLException {
        getAtributo(Parametro.ESTADO);
        return estado;
    }

    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

    private void getIdBBDD() throws SQLException {
        establecerConexion();
        sql = "Select max(id) from " + TABLA;
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            this.id = rs.getInt(1);
        }
        conexion.con.close();
    }
}
