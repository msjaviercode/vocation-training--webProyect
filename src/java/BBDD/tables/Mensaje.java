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

/**
 *
 * @author Mushi
 */
public class Mensaje {

    private final String TABLA = "Mensaje";

    public enum Busqueda {

        ID, FROM, TO
    }

    public enum Parametro {

        ID, FROM_ID, TO_ID,
        FECHA_ENVIADO,
        ASUNTO, MENSAJE
    }

    public enum Tipo {

        INT, DOUBLE, DATE, STRING, BOOLEAN
    }

    private Tipo getTipo(Parametro p) {
        switch (p) {
            case ID:
            case FROM_ID:
            case TO_ID:
                return (Tipo.INT);
            case ASUNTO:
            case MENSAJE:
                return (Tipo.STRING);
            case FECHA_ENVIADO:
                return (Tipo.DATE);
        }
        return null;
    }

    private void setParametro(Parametro p, String s) {
        switch (p) {
            case ASUNTO:
                this.asunto = s;
                break;
            case MENSAJE:
                this.mensaje = s;
                break;
        }
    }

    private void setParametro(Parametro p, int n) {
        switch (p) {
            case ID:
                this.id = n;
                break;
            case FROM_ID:
                this.from_id = n;
                break;
            case TO_ID:
                this.to_id = n;
                break;
        }
    }

    private void setParametro(Parametro p, Date d) {
        switch (p) {
            case FECHA_ENVIADO:
                this.fecha_enviado = d;
                break;
        }
    }

    // Datos
    private int id, from_id, to_id;
    private Date fecha_enviado;
    private String asunto, mensaje;

    // Variables de la BBDD
    private Conector conexion = null;
    private PreparedStatement stm = null;
    private String sql;
    private ResultSet rs;

    // CONSTRUCTORES
    /**
     *
     * @param id
     * @param b
     * @throws SQLException
     */
    public Mensaje(int id, Busqueda b) throws SQLException {
        switch (b) {
            case ID:
                sql = "Select from_id, to_id, fecha_enviado, asunto, mensaje FROM " + TABLA + " where id=?";
                break;
            case FROM:
                sql = "Select id, to_id, fecha_enviado, asunto, mensaje FROM " + TABLA + " where from_id=?";
                break;
            case TO:
                sql = "Select from_id, id, fecha_enviado, asunto, mensaje FROM " + TABLA + " where to_id=?";
                break;
        }
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        rs = stm.executeQuery();
        rs.next();
        switch (b) {
            case ID:
                this.id = id;
                this.from_id = rs.getInt("from_id");
                this.to_id = rs.getInt("to_id");
                break;
            case FROM:
                this.id = rs.getInt("id");
                this.from_id = id;
                this.to_id = rs.getInt("to_id");
                break;
            case TO:
                this.id = rs.getInt("id");
                this.from_id = rs.getInt("from_id");
                this.to_id = id;
                break;
        }
        this.fecha_enviado = rs.getDate("fecha_enviado");
        this.asunto = rs.getString("asunto");
        this.mensaje = rs.getString("mensaje");
        rs.close();
        stm.close();
        conexion.con.close();
    }

    /**
     *
     * @param from_id
     * @param to_id
     * @param fecha_enviado
     * @param asunto
     * @param mensaje
     * @throws SQLException
     */
    public Mensaje(int from_id, int to_id, Date fecha_enviado, String asunto, String mensaje) throws SQLException {
        sql = "Insert INTO " + TABLA + " (from_id, to_id, fecha_enviado, asunto,mensaje) VALUES(?,?,?,?,?) ";
        establecerConexion();
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, from_id);
        stm.setInt(2, to_id);
        stm.setDate(3, fecha_enviado);
        stm.setString(4, asunto);
        stm.setString(5, mensaje);
        stm.execute();
        getIdBBDD();
        this.from_id = from_id;
        this.to_id = id;
        this.fecha_enviado = fecha_enviado;
        this.asunto = asunto;
        this.mensaje = mensaje;
        conexion.con.close();
    }

    // Borrar
    /**
     *
     * @throws SQLException
     */
    public void delete() throws SQLException {
        establecerConexion();
        sql = "Delete " + TABLA + " where id=?";
        stm = conexion.con.prepareStatement(sql);
        stm.setInt(1, id);
        stm.setInt(2, id);
        stm.executeUpdate();
        stm.close();
        conexion.con.close();
    }

    // ATTR
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
     * @return the from_id
     * @throws java.sql.SQLException
     */
    public int getFrom_id() throws SQLException {
        getAtributo(Parametro.FROM_ID);
        return from_id;
    }

    /**
     * @return the to_id
     * @throws java.sql.SQLException
     */
    public int getTo_id() throws SQLException {
        getAtributo(Parametro.TO_ID);
        return to_id;
    }

    /**
     * @return the fecha_enviado
     * @throws java.sql.SQLException
     */
    public Date getFecha_enviado() throws SQLException {
        getAtributo(Parametro.FECHA_ENVIADO);
        return fecha_enviado;
    }

    /**
     * @return the asunto
     * @throws java.sql.SQLException
     */
    public String getAsunto() throws SQLException {
        getAtributo(Parametro.ASUNTO);
        return asunto;
    }

    /**
     * @return the mensaje
     * @throws java.sql.SQLException
     */
    public String getMensaje() throws SQLException {
        getAtributo(Parametro.MENSAJE);
        return mensaje;
    }

    // GETTER BDD
    /**
     *
     * @throws SQLException
     */
    private void getIdBBDD() throws SQLException {
        establecerConexion();
        sql = "Select max(id) set from " + TABLA;
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            this.id = rs.getInt(1);
        }
        conexion.con.close();

    }

    // CONEXION BDD
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
