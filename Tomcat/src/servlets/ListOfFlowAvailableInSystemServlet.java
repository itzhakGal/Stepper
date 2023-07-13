package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RoleImpl;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTOListOfFlowsAvailable;
import utilWebApp.DTORole;
import utilWebApp.DTORoleDataFullInfo;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet(name = "ListOfFlowAvailableInSystemServlet", urlPatterns = "/listOfFlowAvailableForRole")
public class ListOfFlowAvailableInSystemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();
        List<String> listOfAssignedFlows = systemEngine.getListOfFlowsAvailable();
        DTOListOfFlowsAvailable dtoRoleDataFullInfo = new DTOListOfFlowsAvailable(listOfAssignedFlows);

        String dtoRoleDataFullInfoToJson = gson.toJson(dtoRoleDataFullInfo);
        response.getWriter().write(dtoRoleDataFullInfoToJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
