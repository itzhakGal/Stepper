package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFlowExecution;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "FlowExecutionResultServlet", urlPatterns = "/flowExecutionResult")
public class FlowExecutionResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowUUID = req.getParameter("flowUUID");

        if(flowUUID.equals(""))
            return;

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        FlowExecutionResult result = systemEngine.getExecutedFlowsMap().get(UUID.fromString(flowUUID)).getFlowExecutionResult();

        String resultToJSON = gson.toJson(result);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(resultToJSON);
    }
}
