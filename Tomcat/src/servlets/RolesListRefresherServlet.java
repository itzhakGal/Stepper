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
            //RolesManager rolesManager = ServletUtils.getRolesManager(getServletContext());

            SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
            RolesManager rolesManager = systemEngine.getRolesManager();

            Map<String, DTORole> roles = rolesManager.getRoles();
            //String rolesStr = new Gson().toJson(roles, new TypeToken<Map<String, DTORole>>(){}.getType());
            String rolesJson = new Gson().toJson(roles);
            out.println(rolesJson);
            out.flush();
        }
    }
}
