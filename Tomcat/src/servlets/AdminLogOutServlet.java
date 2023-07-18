package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AdminLogOutServlet", urlPatterns = "/adminLogOut")
public class AdminLogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        if (!ServletUtils.isAdminExists()) {  // האדמין לא קיים במערכת
            String errorMessage = "Admin Not Exists.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        }
        else {  // האדמין קיים במערכת ולכן נאתחל אותו מחדש ל- FALSE
            ServletUtils.setAdminExists(false);
            String message = "Admin Log out.";
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().print(message);
        }
    }
}

