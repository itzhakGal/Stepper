package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTORole;
import utils.DTOStatistics;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


    @WebServlet(name = "StatisticsDataServlet", urlPatterns = "/statisticsData")
    public class StatisticsDataServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

            try (PrintWriter out = res.getWriter()) {
                SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
                DTOStatistics statistics = systemEngine.readStatistics();

                String rolesJson = new Gson().toJson(statistics);
                out.println(rolesJson);
                out.flush();
            }
        }
    }

