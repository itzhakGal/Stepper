package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFlowExecution;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "RerunExecutionServlet", urlPatterns = "/inputForRerunExecution")
public class RerunExecutionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowName = req.getParameter("flowName");

        if (flowName == null) {
            return;
        }

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DTOFlowExecution dtoFlowExecution = systemEngine.readInputsJavaFX(flowName);

        String dtoFlowExecutionToJSON = gson.toJson(dtoFlowExecution);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(dtoFlowExecutionToJSON);
    }
}
