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
public class Categoria {

    public enum Busqueda {

        DESCRIPCION
    }

    public enum Parametro {

        ID,
        NOMBRE
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
                return (Tipo.INT);
            case NOMBRE:
                return (Tipo.STRING);
        }
        return null;
    }

    private final String TABLA = "Categoria";

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
            case NOMBRE:
                this.nombre = s;
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

    // Variables de las tablas Condicion    
    private int id;
    private String nombre;

    // CONSTRUCTORES
    /**
     *
     * Recupera los datos a partir de la id.
     *
     * @param id
     * @throws SQLException
     */
    public Categoria(int id) throws SQLException {
        this.id = id;
        establecerConexion();
        sql = "Select " + Parametro.NOMBRE.name() + " FROM " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        this.nombre = rs.getString(Parametro.NOMBRE.name());
        conexion.con.close();
    }

    /**
     *
     * @param nombre
     * @throws SQLException
     */
    public Categoria(String nombre) throws SQLException {
        sql = "Insert INTO " + TABLA + " (" + Parametro.NOMBRE.name() + ") values (?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setString(1, nombre);
        stm.execute();
        getIdBBDD();
        this.nombre = nombre;
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

    // GETTERS
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the Nombre
     * @throws java.sql.SQLException
     */
    public String getNombre() throws SQLException {
        getAtributo(Parametro.NOMBRE);
        return nombre;
    }

    // Conexion a BDD
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
