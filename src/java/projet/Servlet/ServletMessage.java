/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Servlet;

import java.io.IOException;
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
 * Servlet qui permet à des personnes travaillant sur le même document de
 * chatter
 *
 * @author Francis
 * @author Guillaume
 */
public class ServletMessage extends HttpServlet {

    /**
     * doPost qui permet de récupèrer un message, de le traiter et de l'ajouter
     * à l'ArrayList correspondante
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @author Francis
     * @author Guillaume
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

            if (listMessage == null) {
                listMessage = new ArrayList<>();
            }

            Message message = new Message(psd, msg);

            listMessage.add(message);

            mapMessage.put(id, listMessage);

        }

        response.sendRedirect("document?id=" + id);
    }
}
