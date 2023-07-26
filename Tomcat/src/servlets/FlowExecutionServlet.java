package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "FlowExecutionServlet", urlPatterns = "/flowExecution")
public class FlowExecutionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowName = req.getParameter("flowName");

        if(flowName == null)
            return;

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DTOFullDetailsPastRunWeb fullDetails = systemEngine.flowActivationAndExecutionWeb(flowName);

        String fullDetailsToJSON = gson.toJson(fullDetails);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(fullDetailsToJSON);
    }
}
