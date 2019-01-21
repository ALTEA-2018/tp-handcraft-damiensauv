import com.miage.altea.servlet.Controller;
import com.miage.altea.servlet.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("########## Getting request for " + req.getRequestURI());

        String uri = req.getRequestURI();
        Method m = this.getMappingForUri(uri);

        if (m == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "no mapping found for request uri " + uri);
            return;
        }

        try {
            Object o = m.getDeclaringClass().getDeclaredConstructor().newInstance();

            String invoke;

            Map<String, String[]> queryString = req.getParameterMap();
            if (queryString.size() > 0)
                invoke = (String) m.invoke(o, queryString);
            else
                invoke = (String) m.invoke(o);


            resp.getWriter().print(invoke);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "exception when calling method someThrowingMethod : some exception message");
        }


        // todo

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(HelloController.class);
    }

    protected void registerController(Class controllerClass) {
        System.out.println("########## Analysing class " + controllerClass.getName());

        Annotation[] declaredAnnotations = controllerClass.getDeclaredAnnotations();

        if (controllerClass.getAnnotation(Controller.class) instanceof Controller) {
            Method[] methods = controllerClass.getMethods();
            for (Method m : methods) {
                this.registerMethod(m);
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    protected void registerMethod(Method method) {
        System.out.println("##########  Registering method " + method.getName());
        Annotation[] declaredAnnotations = method.getAnnotations();

        if (method.getReturnType().equals(void.class))
            return;

        for (Annotation a : declaredAnnotations) {
            if (a instanceof RequestMapping) {
                RequestMapping tmp = (RequestMapping) a;
                String uri = ((RequestMapping) a).uri();
                this.uriMappings.put(uri, method);
            }
        }

    }

    protected Map<String, Method> getMappings() {
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri) {
        return this.uriMappings.get(uri);
    }
}
