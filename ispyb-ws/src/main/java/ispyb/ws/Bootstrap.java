package ispyb.ws;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.annotations.Api;
import io.swagger.jaxrs.config.BeanConfig;

@Api(value = "/ispyb-ws")
public class Bootstrap extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("5.4.3");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/ispyb-ws");
		beanConfig.setResourcePackage("io.swagger.resources");
		beanConfig.setScan(true);
	}
}