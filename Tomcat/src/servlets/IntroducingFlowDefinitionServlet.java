package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFlowDefinition;
import utils.ServletUtils;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
@WebServlet(name = "IntroducingFlowDefinitionServlet", urlPatterns = "/introducing_Flow_Definition")
public class IntroducingFlowDefinitionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();
        String flowName = req.getParameter("flowName");

        if (flowName == null) {
            return;
        }

        DTOFlowDefinition dtoFlowDefinition = systemEngine.introducingFlowDefinitionJavaFX(flowName);

        String dtoFlowDefinitionToJSON = gson.toJson(dtoFlowDefinition);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(dtoFlowDefinitionToJSON);
    }
}