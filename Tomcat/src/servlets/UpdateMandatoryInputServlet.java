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
@WebServlet(name = "UpdateMandatoryInputServlet", urlPatterns = "/updateMandatoryInput")
public class UpdateMandatoryInputServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String type = req.getParameter("type");
        String value = req.getParameter("value");
        String labelValue = req.getParameter("labelValue");

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

        if (type.equals("Integer")) {
            try {
                // Try to parse the value as an integer
                int intValue = Integer.parseInt(value);
                systemEngine.updateMandatoryInput(labelValue, intValue);
            } catch (NumberFormatException e) {
                //createTitleFreeInputs("The input value is not an int", "-fx-font-weight: bold; -fx-font-size: 12", row);

            }
        } else  if (type.equals("EnumeratorData"))
        {
            systemEngine.updateMandatoryInputEnumerator(labelValue, value);
        }else {
            systemEngine.updateMandatoryInput(labelValue, value);
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
