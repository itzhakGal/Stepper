package servlets;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utils.ServletUtils;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "ContinuationServlet", urlPatterns = "/continuation")
public class ContinuationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowUUID = req.getParameter("flowUUID");
        String sourceFlowName = req.getParameter("sourceFlowName");
        String targetFlowName = req.getParameter("targetFlowName");
        String userName = req.getParameter("userName");

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        systemEngine.continuationToOtherFlowWeb(flowUUID, sourceFlowName, targetFlowName, userName);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}

