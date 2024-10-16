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
public class CondicionesCupon {

    private final String TABLA = "CondicionesCupon";

    public enum Parametro {

        ID_CONDICION, ID_CUPON
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID_CUPON:
            case ID_CONDICION:
                return (Tipo.INT);
        }
        return null;
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID_CUPON:
                this.id_cupon = n;
                break;
            case ID_CONDICION:
                this.id_condicion = n;
                break;
        }
    }

    // Datos
    private int id_condicion, id_cupon;
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
     * @param id_cond
     * @throws java.sql.SQLException
     */
    public CondicionesCupon(int id_cond, int id_cupon) throws SQLException {
        this.id_condicion = id_cond;
        this.id_cupon = id_cupon;
        sql = "Insert into " + TABLA + " (id_condicion, id_cupon) "
                + "VALUES(?,?) ;";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id_cond);
        stm.setInt(2, id_cupon);
        stm.executeUpdate();
        conexion.con.close();
    }

    public void setAtributo(Parametro p, int atributo, int other_id) throws SQLException {
        establecerConexion();
        switch (p) {
            case ID_CONDICION:
                sql = "Update " + TABLA + " set  " + p.name() + "=? where id_cupon=?";
                break;
            case ID_CUPON:
                sql = "Update " + TABLA + " set  " + p.name() + "=? where id_condicion=?";
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
        if (p == Parametro.ID_CONDICION) {
            sql = "Select " + p.name() + " FROM " + TABLA + " where id_cupon=?";
        } else {
            sql = "Select " + p.name() + " FROM " + TABLA + " where id_condicion=?";
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
     * @return the id_condicion
     * @throws java.sql.SQLException
     */
    public int getId_condicion(int other_id) throws SQLException {
        getAtributo(Parametro.ID_CONDICION, other_id);
        return id_condicion;
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
