/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.Message;

/**
 *
 * @author francis
 */
public class ServletMessage extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AjouterMessage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AjouterMessage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
         String id = (String) request.getParameter("id");

        ServletContext contexte = getServletContext();
        Map<String, ArrayList<Message>> mapMessage = (Map<String, ArrayList<Message>>) contexte.getAttribute("mapMessage");

        response.addHeader("Refresh", "3");
        
        if (mapMessage != null) {
            ArrayList<Message> listMessage = mapMessage.get(id);

            PrintWriter out = response.getWriter();

            if (listMessage != null) {
                for (Message listMessage1 : listMessage) {
                    out.println("<p>" + listMessage1.toString() + "</p>");
                }
            }
        }
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
        HttpSession session = request.getSession();

        String psd = (String) session.getAttribute("pseudo");
        String msg = request.getParameter("message");

        String id = (String) request.getParameter("id");
        
        if (msg.length() != 0) {
            ServletContext contexte = getServletContext();
            HashMap<String, ArrayList<Message>> mapMessage = (HashMap<String, ArrayList<Message>>) (Map<String, ArrayList<Message>>) contexte.getAttribute("mapMessage");
           
            ArrayList<Message> listMessage = mapMessage.get(id);
            
            if(listMessage == null)
                listMessage = new ArrayList<>();
            
            Message message = new Message(psd, msg);
            
            listMessage.add(message);
            
            mapMessage.put(id, listMessage);
            
            long lastModified = System.currentTimeMillis();
            
            response.addHeader("Refresh", "3");
            response.sendRedirect("document.jsp?id=" + id);
        }
    }

    @Override
    public void init() {
        HashMap<String, ArrayList<ServletMessage>> mapMessage = new HashMap<>();
        ServletContext contexte = getServletContext();
        contexte.setAttribute("mapMessage", mapMessage);
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
