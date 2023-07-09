package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOListFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListFlowsExecutionServlet", urlPatterns = "/listFlowsExecution")
public class ListFlowsExecutionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();
        DTOListFullDetailsPastRunWeb dtoListFullDetailsPastRun = new DTOListFullDetailsPastRunWeb();

        List<DTOFullDetailsPastRunWeb> flowsExecutedList = systemEngine.getFlowsExecutedDataDTOHistoryWeb();
        dtoListFullDetailsPastRun.setDtoListFullDetailsPastRun(flowsExecutedList);

        String dtoListFullDetailsPastRunJSON = gson.toJson(dtoListFullDetailsPastRun);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(dtoListFullDetailsPastRunJSON);
    }
}
