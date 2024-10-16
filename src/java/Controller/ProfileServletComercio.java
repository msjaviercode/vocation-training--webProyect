/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import BBDD.tables.Comercio;
import Security.Encriptar;
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
public class ProfileServletComercio extends HttpServlet {

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

        // Se supone que tenemos el objeto de Comercio
        Comercio com = (Comercio) request.getSession().getAttribute("comercio");

        boolean correct = false;

        if (com != null) {
            try {
                // Si el valor ha cambiado lo modificamos
                String mail = request.getParameter("email");
                if (com.getEmail().equals(mail)) {
                    com.setAtributo(Comercio.Parametro.EMAIL,mail);
                }
                // Si las passw coinciden ponemos la nueva
                String passwordActual = Encriptar.encryptSHA1(request.getParameter("password"));
                if (com.getPassword().equals(passwordActual)) {
                    String newPassword = Encriptar.encryptSHA1(request.getParameter("password2"));
                    com.setAtributo(Comercio.Parametro.PASSWORD,newPassword);
                }
                // Siempre que se haya modificado un valor lo actualizamos 
                String country = request.getParameter("country");
                if (!com.getCountry().equals(country)) {
                    com.setAtributo(Comercio.Parametro.COUNTRY,country);
                }
                String address = request.getParameter("address");
                if (!com.getFulladdress().equals(address)) {
                    com.setAtributo(Comercio.Parametro.FULLADDRESS,address);
                }
                String city = request.getParameter("city");
                if (!com.getCity().equals(city)) {
                    com.setAtributo(Comercio.Parametro.CITY,city);
                }
                String st = request.getParameter("st");
                if (!com.getStateorprovince().equals(st)) {
                    com.setAtributo(Comercio.Parametro.STATEORPROVINCE,st);
                }
                String cp = request.getParameter("cp");
                if (!com.getCp().equals(cp)) {
                    com.setAtributo(Comercio.Parametro.CP,cp);
                }
                String desc = request.getParameter("desc");
                if (com.getDescription() == null || !com.getDescription().equals(desc)) {
                    com.setAtributo(Comercio.Parametro.DESCRIPTION,desc);
                }
                String image = request.getParameter("profileimage");
                if (com.getProfileimageurl() == null || !com.getProfileimageurl().equals(image)) {
                   com.setAtributo(Comercio.Parametro.PROFILEIMAGEURL,image);
                }

                correct = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // Si no hay comercio o se lanza una exception se redirecciona al inicio
        if (correct) {
            response.sendRedirect("businessprofile.jsp");
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
