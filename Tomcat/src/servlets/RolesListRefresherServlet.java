package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTORole;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@WebServlet(name = "RolesListRefresherServlet", urlPatterns = "/rolesListRefresher")
public class RolesListRefresherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try (PrintWriter out = res.getWriter()) {
            RolesManager rolesManager = ServletUtils.getRolesManager(getServletContext());
            Map<String, DTORole> roles = rolesManager.getRoles();
            String rolesStr = new Gson().toJson(roles, new TypeToken<Map<String, DTORole>>(){}.getType());
            out.println(rolesStr);
            out.flush();
        }
    }
}
