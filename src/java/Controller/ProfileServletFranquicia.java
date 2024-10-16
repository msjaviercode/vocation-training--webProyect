/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import BBDD.tables.Franquicia;
import BBDD.tables.Franquiciado;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mushi
 */
public class ProfileServletFranquicia extends HttpServlet {

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

        // Con el objeto de Franquiciado recuperaremos de algun modo (en el futuro) la franquicia 
        Franquiciado franquiciado = (Franquiciado) request.getSession().getAttribute("franquiciado");

        boolean correct = false;

        if (franquiciado != null) {
            try {
                // TODO recuperar id de la franquicia ? 
                // Por ahora probamos con la franquicia 1
                Franquicia f = new Franquicia(1);

                // Siempre que se haya modificado un valor lo actualizamos 
                String image = request.getParameter("profileimage");
                if (f.getProfileimageurl() == null || !f.getProfileimageurl().equals(image)) {
                    f.setProfileimageurl(image);
                }
                String bg = request.getParameter("background");
                if (f.getBackground() == null || !f.getBackground().equals(bg)) {
                    f.setProfileimageurl(bg);
                }
                String address = request.getParameter("address");
                if (!f.getFulladdress().equals(address)) {
                    f.setFulladdress(address);
                }
                String cp = request.getParameter("cp");
                if (!f.getCp().equals(cp)) {
                    f.setCp(cp);
                }
                String st = request.getParameter("st");
                if (!f.getStateorprovince().equals(st)) {
                    f.setStateorprovince(st);
                }
                String city = request.getParameter("city");
                if (!f.getCity().equals(city)) {
                    f.setCity(city);
                }
                String country = request.getParameter("country");
                if (!f.getCountry().equals(country)) {
                    f.setCountry(country);
                }

                correct = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // Si no hay franquiciado o se lanza una exception se redirecciona al inicio
        if (correct) {
            response.sendRedirect("franchiseprofile.jsp");
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
