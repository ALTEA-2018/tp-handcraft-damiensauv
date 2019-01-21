import com.miage.altea.servlet.Controller;
import com.miage.altea.servlet.RequestMapping;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DispatcherServletTest {

    @Test
    void registerController_throwsIllegalArgumentException_forNonControllerClasses() {
        var servlet = new DispatcherServlet();

        assertThrows(IllegalArgumentException.class,
                () -> servlet.registerController(String.class));
        assertThrows(IllegalArgumentException.class,
                () -> servlet.registerController(SomeEmptyClass.class));
    }

    @Test
    void registerController_doesNotRegisters_nonAnnotatedMethods() {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClassWithAMethod.class);

        assertTrue(servlet.getMappings().isEmpty());
    }

    @Test
    void registerController_doesNotRegisters_voidReturningMethods() {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClassWithAVoidMethod.class);

        assertTrue(servlet.getMappings().isEmpty());
    }

    @Test
    void registerController_shouldRegisterCorrectyMethods() {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClass.class);
        servlet.registerController(SomeOtherControllerClass.class);

        assertEquals("someGoodMethod",
                servlet.getMappingForUri("/test").getName());
        assertEquals("someOtherNiceMethod",
                servlet.getMappingForUri("/otherTest").getName());
    }

    @Test
    void registerHelloController_shouldWorkCorrectly() {
        var servlet = new DispatcherServlet();
        servlet.registerController(HelloController.class);

        assertEquals("sayHello", servlet.getMappingForUri("/hello").getName());
        assertEquals("sayGoodBye", servlet.getMappingForUri("/bye").getName());
        assertEquals("explode", servlet.getMappingForUri("/boum").getName());
    }

    @Test
    void doGet_shouldReturn404_whenNotMethodIsFound() throws IOException {
        var servlet = new DispatcherServlet();

        var req = mock(HttpServletRequest.class);
        var resp = mock(HttpServletResponse.class);
        when(req.getRequestURI()).thenReturn("/test");

        servlet.doGet(req, resp);

        verify(resp).sendError(404, "no mapping found for request uri /test");
    }

    @Test
    void doGet_shouldReturn500WithMessage_whenMethodThrowsException() throws IOException {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClass.class);

        var req = mock(HttpServletRequest.class);
        var resp = mock(HttpServletResponse.class);
        when(req.getRequestURI()).thenReturn("/test-throwing");

        servlet.doGet(req, resp);

        verify(resp).sendError(500,
                "exception when calling method someThrowingMethod : some exception message");
    }

    @Test
    void doGet_shouldReturnAResult_whenMethodSucceeds() throws IOException {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClass.class);

        var req = mock(HttpServletRequest.class);
        var resp = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);

        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getRequestURI()).thenReturn("/test");

        servlet.doGet(req, resp);

        verify(printWriter).print("\"Hello\"");
    }

    @Test
    void doGet_shouldReturnAResult_whenMethodWithParametersSucceeds() throws IOException {
        var servlet = new DispatcherServlet();

        servlet.registerController(SomeControllerClass.class);

        var req = mock(HttpServletRequest.class);
        var resp = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);

        when(req.getRequestURI()).thenReturn("/test-with-params");
        when(req.getParameterMap()).thenReturn(Map.of("id", new String[]{"12"}));
        when(resp.getWriter()).thenReturn(printWriter);

        servlet.doGet(req, resp);

        verify(printWriter).print("\"12\"");
    }

    @Test
    void doGet_shouldReturnAResult_forHelloController() throws IOException {
        var servlet = new DispatcherServlet();
        servlet.registerController(HelloController.class);

        var req = mock(HttpServletRequest.class);
        var resp = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);

        when(req.getRequestURI()).thenReturn("/hello");
        when(resp.getWriter()).thenReturn(printWriter);

        servlet.doGet(req, resp);

        verify(printWriter).print("\"Hello World !\"");
    }
}


class SomeEmptyClass {
}


@Controller
class SomeControllerClassWithAMethod {
    public String myMethod() {
        return "test";
    }
}

@Controller
class SomeControllerClassWithAVoidMethod {
    @RequestMapping(uri = "/test")
    public void myMethod() {
    }
}

@Controller
class SomeControllerClass {
    @RequestMapping(uri = "/test")
    public String someGoodMethod() {
        return "Hello";
    }

    @RequestMapping(uri = "/test-throwing")
    public String someThrowingMethod() {
        throw new RuntimeException("some exception message");
    }

    @RequestMapping(uri = "/test-with-params")
    public String someThrowingMethod(Map<String, String[]> params) {
        return params.get("id")[0];
    }
}

@Controller
class SomeOtherControllerClass {
    @RequestMapping(uri = "/otherTest")
    public String someOtherNiceMethod() {
        return "Hello again";
    }
}
