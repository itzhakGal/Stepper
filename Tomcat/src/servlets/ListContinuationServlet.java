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
import utilsDesktopApp.DTOListContinuationFlowName;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "ListContinuationServlet", urlPatterns = "/listContinuationFlowName")
public class ListContinuationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowName = req.getParameter("flowName");
        String flowUUID = req.getParameter("flowUUID");
        UUID uuidFlowId = UUID.fromString(flowUUID);

        if (flowName == null) {
            return;
        }

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DTOListContinuationFlowName listContinuationFlowName = systemEngine.setListContinuationFlowNameWeb(flowName, uuidFlowId);

        String listContinuationFlowNameToJSON = gson.toJson(listContinuationFlowName);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(listContinuationFlowNameToJSON);
    }
}
