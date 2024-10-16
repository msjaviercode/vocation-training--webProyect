/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import BBDD.tables.Franquicia;
import BBDD.tables.Franquiciado;
import BBDD.utilities.UserManagement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mushi
 */

public class RegisterServletFranquicia extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String  name,nif,address,email,telefono,surname,password;
        
        name = request.getParameter("name");
        nif = request.getParameter("nif");
        address = request.getParameter("address");
        email = request.getParameter("email");
        telefono = request.getParameter("telefono");
        surname = request.getParameter("surname");
        password = request.getParameter("password");
        
        Franquiciado franquiciado = null;
        franquiciado= UserManagement.registroFranquiciado(name, surname, password, nif, address, email, telefono);
        
         
            
            if (franquiciado !=null){
                response.sendRedirect("index.jsp");
                
             } else {
                 response.sendRedirect("rfranchise.jsp");

            }
            
            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
