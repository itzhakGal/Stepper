package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utils.ServletUtils;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "FlowDefinitionRefresher", urlPatterns = "/flowDefinitionRefresher")
public class FlowDefinitionRefresherServlet extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

                DTOListFlowsDetails listFlowsDetails = systemEngine.readFlowsDetails();
                String listFlowsDetailsJSON = gson.toJson(listFlowsDetails);
                out.println(listFlowsDetailsJSON);
                out.flush();
            }
        }

}
