/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD.tables;

import BBDD.utilities.Conector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin {

    private final String TABLA = "Admin";

    private enum Parametro {

        ADMIN, PASSWORD, ID
    }
    private int id;
    private String admin;
    private String password;

    /**
     *
     * Enlace con la base de datos
     *
     */
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case ADMIN:
                this.admin = s;
                break;
            case PASSWORD:
                this.password = s;
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
    }

    public Admin(String nombre) {
        admin = nombre;
    }

    public void setAtributo(Parametro p, String atributo) throws SQLException {
        establecerConexion();
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

    private int getAtributoBBDD(Parametro p, int atributo) throws SQLException {
        establecerConexion();
        int temp = -1;
        sql = "Select " + p.name() + " FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        if (rs.next()) {

            setParametro(p, rs.getInt(p.name()));
            temp = rs.getInt(p.name());
        }
        conexion.con.close();
        return temp;
    }

    private Boolean getAtributoBBDD(Parametro p, boolean atributo) throws SQLException {
        establecerConexion();
        boolean temp = false;
        sql = "Select " + p.name() + " FROM " + TABLA + " where " + Parametro.ID.name() + "=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        if (rs.next()) {

            setParametro(p, rs.getBoolean(p.name()));
            temp = rs.getBoolean(p.name());
        }
        conexion.con.close();
        return temp;
    }

    private void establecerConexion() throws SQLException {
        if (conexion == null) {
            conexion = new Conector();
        } else if (conexion.con.isClosed()) {
            conexion = new Conector();
        }
    }

}
