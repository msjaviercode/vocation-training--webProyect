/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import BBDD.tables.Usuario;
import Security.Encriptar;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mushi ft. Axel
 */
public class ProfileServletUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se supone que tenemos el objeto de User
        Usuario u = (Usuario) request.getSession().getAttribute("user");
        boolean correct = false;

        if (u != null) {
            try {
                // Si el valor ha cambiado lo modificamos
                String mail = request.getParameter("email");
                if (u.getEmail().equals(mail)) {
                    u.setAtributo(Usuario.Parametro.EMAIL, mail);
                }
                // Si las passw coinciden ponemos la nueva
                String passwordActual = request.getParameter("password");
                if (u.getPassword().equals(Encriptar.encryptSHA1(passwordActual))) {
                    String newPassword = Encriptar.encryptSHA1(request.getParameter("password2"));
                    u.setAtributo(Usuario.Parametro.PASSWORD, newPassword);
                }
                // Siempre que se haya modificado un valor lo actualizamos 
                String country = request.getParameter("country");
                if (!u.getCountry().equals(country)) {
                    u.setAtributo(Usuario.Parametro.COUNTRY, country);
                }
                String address = request.getParameter("address");
                if (u.getFulladdress() == null || !u.getFulladdress().equals(address)) {
                    u.setAtributo(Usuario.Parametro.FULLADDRESS, address);
                }
                String city = request.getParameter("city");
                if (u.getCity() == null || !u.getCity().equals(city)) {
                    u.setAtributo(Usuario.Parametro.CITY, city);
                }
                String st = request.getParameter("st");
                if (!u.getStateorprovince().equals(st)) {
                    u.setAtributo(Usuario.Parametro.STATEORPROVINCE, st);
                }
                String cp = request.getParameter("cp");
                if (u.getCp() == null || !u.getCp().equals(cp)) {
                    u.setAtributo(Usuario.Parametro.CP, cp);
                }
                String image = request.getParameter("profileimage");
                if (u.getProfilesimageurl() == null || !u.getProfilesimageurl().equals(image)) {
                    u.setAtributo(Usuario.Parametro.PROFILEIMAGEURL, image);
                }

                correct = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // Si no hay usuario o se lanza una exception se redirecciona al inicio
        if (correct) {
            response.sendRedirect("userprofile.jsp");
        } else {

            response.sendRedirect("index.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
