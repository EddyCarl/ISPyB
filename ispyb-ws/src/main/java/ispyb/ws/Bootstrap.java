package ispyb.ws;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.annotations.Api;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;

@Api(value = "/ispyb-ws")
public class Bootstrap extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
//		super.init(config);

		Info info = new Info()
			.title("Some new test title")
			.description("Adding some bloat text for test purposes");

		ServletContext context = config.getServletContext();
		Swagger swagger = new Swagger().info( info );
		swagger.securityDefinition( "api_key",
			new ApiKeyAuthDefinition( "api_key", In.HEADER ) );
		swagger.tag( new Tag()
											.name( "Test-Tag" )
		.description( "All of the test endpoints" ) );

		swagger.tag( new Tag()
		.name( "Another test tag" )
		.description( "More rubbish text" ));

		new SwaggerContextService().withServletConfig( config ).updateSwagger( swagger );


//
//		BeanConfig beanConfig = new BeanConfig();
//		beanConfig.setVersion("5.4.3");
//		beanConfig.setSchemes(new String[] { "http" });
//		//beanConfig.setHost("localhost:8080");
//		beanConfig.setHost("192.168.30.200:8080");
//		beanConfig.setBasePath("/ispyb/ispyb-ws/rest");
//		beanConfig.setResourcePackage("io.swagger.resources");
//		beanConfig.setScan(true);
	}
}
