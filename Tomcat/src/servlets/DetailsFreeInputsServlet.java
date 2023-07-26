package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;;
import utils.DTOFlowExecution;
import utils.ServletUtils;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(name = "DetailsFreeInputsServlet", urlPatterns = "/detailsFreeInputs")
public class DetailsFreeInputsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String flowName = req.getParameter("flowName");
        String strContinuation = req.getParameter("strContinuation");
        String userName = req.getParameter("userName");
        Gson gson = new Gson();
        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

        UUID flowIdRerun = systemEngine.updateOptionalExecutionWeb(flowName, strContinuation, userName);

        String inputs = req.getReader().lines().collect(Collectors.joining(" "));
        DTOFlowExecution dtoFlowExecution = new Gson().fromJson(inputs, DTOFlowExecution.class);

        DTOFlowExecution dtoFlowExecutionNew = systemEngine.removeInitialFreeInputFromDTOWeb(dtoFlowExecution); // זה מבטיח שהDTO שנמחקו ממנו חלקים הוא אותו DTO שיש אצל הלקוח?
        dtoFlowExecutionNew.setFlowIdRerun(flowIdRerun);

        String dtoFlowExecutionNewJAVA = gson.toJson(dtoFlowExecutionNew);
        res.getWriter().write(dtoFlowExecutionNewJAVA);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
