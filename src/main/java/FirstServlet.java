import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirstServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var writer = resp.getWriter();
        writer.println("Hello !");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        System.out.println("Initialisation de la servlet");
    }
}
