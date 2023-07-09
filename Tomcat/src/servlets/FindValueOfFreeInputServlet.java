package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTODataInFlowExecution;
import utils.ServletUtils;


import java.io.IOException;

// אופציה יום שלישי
/*@WebServlet(name = "FindValueOfFreeInputServlet", urlPatterns = "/findValueOfFreeInput")
public class FindValueOfFreeInputServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String flowName = req.getParameter("flowName");
        String finalInputName = req.getParameter("finalInputName");

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();

        DataInFlowExecution freeInputDataInFlow = systemEngine.findValueOfFreeInput(flowName, finalInputName);

        DTODataInFlowExecution dataInFlowExecution = new DTODataInFlowExecution(freeInputDataInFlow);
        String dataInFlowExecutionJSON = gson.toJson(dataInFlowExecution);
        res.getWriter().write(dataInFlowExecutionJSON);
    }
}*/

// אופציה יום רביעי
/*@WebServlet(name = "FindValueOfFreeInputServlet", urlPatterns = "/findValueOfFreeInput")
public class FindValueOfFreeInputServlet extends HttpServlet {


    Gson gson = new Gson();
    @Override
    public void init() throws ServletException {
        super.init();
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String flowName = req.getParameter("flowName");
        String finalInputName = req.getParameter("finalInputName");

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

        DataInFlowExecution freeInputDataInFlow = systemEngine.findValueOfFreeInput(flowName, finalInputName);

        String dataInFlowExecutionJSON = gson.toJson(freeInputDataInFlow);
        res.getWriter().write(dataInFlowExecutionJSON);
    }
}*/


// אופציה יום רביעי לפי מה שעבד בלילה בלי בעיות
@WebServlet(name = "FindValueOfFreeInputServlet", urlPatterns = "/findValueOfFreeInput")
public class FindValueOfFreeInputServlet extends HttpServlet {

    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String flowName = req.getParameter("flowName");
        String finalInputName = req.getParameter("finalInputName");

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

        DataInFlowExecution freeInputDataInFlow = systemEngine.findValueOfFreeInput(flowName, finalInputName);

        DTODataInFlowExecution dataInFlowExecution = new DTODataInFlowExecution(freeInputDataInFlow);
        String dataInFlowExecutionJSON = gson.toJson(dataInFlowExecution);
        res.getWriter().write(dataInFlowExecutionJSON);
    }
}
