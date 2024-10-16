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
public class CategoriasCupon {

    private final String TABLA = "CategoriasCupon";

    public enum Parametro {

        ID_CATEGORIA, ID_CUPON
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID_CUPON:
            case ID_CATEGORIA:
                return (Tipo.INT);
        }
        return null;
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID_CUPON:
                this.id_cupon = n;
                break;
            case ID_CATEGORIA:
                this.id_categoria = n;
                break;
        }
    }

    // Datos
    private int id_categoria, id_cupon;
    // Variables BDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    /**
     *
     * Constructor para crear uno nuevo con los valores elegidos.
     *
     * @param id_cupon
     * @param id_cat
     * @throws java.sql.SQLException
     */
    public CategoriasCupon(int id_cat, int id_cupon) throws SQLException {
        this.id_categoria = id_cat;
        this.id_cupon = id_cupon;
        sql = "Insert into " + TABLA + " (id_categoria, id_cupon) "
                + "VALUES(?,?) ;";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id_cat);
        stm.setInt(2, id_cupon);
        stm.executeUpdate();
        conexion.con.close();
    }

    public void setAtributo(Parametro p, int atributo, int other_id) throws SQLException {
        establecerConexion();
        switch (p) {
            case ID_CATEGORIA:
                sql = "Update " + TABLA + " set  " + p.name() + "=? where id_cupon=?";
                break;
            case ID_CUPON:
                sql = "Update " + TABLA + " set  " + p.name() + "=? where id_categoria=?";
                break;
        }
        stm.setInt(2, other_id);
        stm.setInt(1, atributo);
        stm.executeUpdate();
        conexion.con.close();
        setParametro(p, atributo);
    }

    public void getAtributo(Parametro p, int other_id) throws SQLException {
        establecerConexion();
        if (p == Parametro.ID_CATEGORIA) {
            sql = "Select " + p.name() + " FROM " + TABLA + " where id_cupon=?";
        } else {
            sql = "Select " + p.name() + " FROM " + TABLA + " where id_categoria=?";
        }
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, other_id);
        rs = stm.executeQuery();
        if (rs.next()) {
            switch (getTipo(p)) {
                case INT:
                    setParametro(p, rs.getInt(p.name()));
                    break;
            }
        }
        conexion.con.close();
    }

    // GETTERS
    /**
     * @param other_id
     * @return the id_categoria
     * @throws java.sql.SQLException
     */
    public int getId_categoria(int other_id) throws SQLException {
        getAtributo(Parametro.ID_CATEGORIA, other_id);
        return id_categoria;
    }

    /**
     * @param other_id
     * @return the id_cupon
     * @throws java.sql.SQLException
     */
    public int getId_cupon(int other_id) throws SQLException {
        getAtributo(Parametro.ID_CUPON, other_id);
        return id_cupon;
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
