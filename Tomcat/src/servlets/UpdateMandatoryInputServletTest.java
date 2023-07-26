package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOMandatoryInputsWeb;
import utilWebApp.ListDTOMandatoryInputsWeb;
import utils.DTOFlowExecution;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(name = "UpdateMandatoryInputServletTest", urlPatterns = "/updateMandatory")
public class UpdateMandatoryInputServletTest extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

        String mandatory = req.getReader().lines().collect(Collectors.joining(" "));
        ListDTOMandatoryInputsWeb listDTOMandatoryInputsWebList = new Gson().fromJson(mandatory, ListDTOMandatoryInputsWeb.class);
        List<DTOMandatoryInputsWeb> listDTOMandatoryInputsWeb = listDTOMandatoryInputsWebList.getListDTOMandatoryInputsWeb();

        for(DTOMandatoryInputsWeb dtoMandatoryInputsWeb : listDTOMandatoryInputsWeb)
        {
            if(!dtoMandatoryInputsWeb.getValue().isEmpty()) {
                if (dtoMandatoryInputsWeb.getType().equals("Integer")) {
                    try {
                        // Try to parse the value as an integer
                        int intValue = Integer.parseInt(dtoMandatoryInputsWeb.getValue());
                        systemEngine.updateMandatoryInput(dtoMandatoryInputsWeb.getLabelValue(), intValue);
                    } catch (NumberFormatException e) {
                        //createTitleFreeInputs("The input value is not an int", "-fx-font-weight: bold; -fx-font-size: 12", row);
                    }
                }
                else  if (dtoMandatoryInputsWeb.getType().equals("EnumeratorData"))
                {
                    systemEngine.updateMandatoryInputEnumerator(dtoMandatoryInputsWeb.getLabelValue(), dtoMandatoryInputsWeb.getValue());
                }else  if (dtoMandatoryInputsWeb.getType().equals("JsonData"))
                {
                    systemEngine.updateMandatoryInputJson(dtoMandatoryInputsWeb.getLabelValue(), dtoMandatoryInputsWeb.getValue());
                }
                else {
                    systemEngine.updateMandatoryInput(dtoMandatoryInputsWeb.getLabelValue(), dtoMandatoryInputsWeb.getValue());
                }
            }
        }

        res.setStatus(HttpServletResponse.SC_OK);
    }

}
