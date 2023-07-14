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

@WebServlet(name = "AvailableFlowsServlet", urlPatterns = "/available_flows")
public class AvailableFlowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DTOListFlowsDetails listFlowsDetails = systemEngine.readFlowsDetails();
        String listFlowsDetailsJSON = gson.toJson(listFlowsDetails);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(listFlowsDetailsJSON);
    }
}
