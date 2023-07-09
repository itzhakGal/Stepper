package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFlowExecution;
import utils.DTOFullDetailsPastRun;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "FlowExecutionTaskServlet", urlPatterns = "/flowExecutionTask")
public class FlowExecutionTaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowId = req.getParameter("flowId");
        UUID flowIdUUID = UUID.fromString(flowId);

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DTOFullDetailsPastRunWeb executedData = systemEngine.getFlowExecutedDataDTOWeb(flowIdUUID);

        String executedDataToJSON = gson.toJson(executedData);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(executedDataToJSON);
    }
}
