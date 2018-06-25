package ispyb.ws;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.config.Scanner;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import ispyb.ws.rest.mx.AutoprocintegrationRestWebService;
import ispyb.ws.rest.mx.CrystalRestWebService;
import ispyb.ws.rest.mx.EnergyScanRestWebService;
import ispyb.ws.rest.mx.ImageWebService;
import ispyb.ws.rest.mx.PhasingRestWebService;
import ispyb.ws.rest.mx.ProteinRestWebService;
import ispyb.ws.rest.mx.SampleRestWebService;
import ispyb.ws.rest.mx.WorkflowRestWebService;
import ispyb.ws.rest.mx.XFEFluorescenceSpectrumRestWebService;
import ispyb.ws.rest.proposal.DewarRestWebService;
import ispyb.ws.rest.proposal.ProposalRestWebService;
import ispyb.ws.rest.proposal.SessionRestWebService;
import ispyb.ws.rest.proposal.ShippingRestWebService;
import ispyb.ws.rest.saxs.BufferRestWebService;
import ispyb.ws.rest.saxs.DataCollectionRestWebService;
import ispyb.ws.rest.saxs.ExperimentRestWebService;
import ispyb.ws.rest.saxs.FrameRestWebService;
import ispyb.ws.rest.saxs.MacromoleculeRestWebService;
import ispyb.ws.rest.saxs.MeasurementRestWebService;
import ispyb.ws.rest.saxs.ModelingRestWebService;
import ispyb.ws.rest.saxs.SaxsRestWebService;
import ispyb.ws.rest.saxs.SpecimenRestWebService;
import ispyb.ws.rest.saxs.StatsRestWebService;
import ispyb.ws.rest.saxs.StockSolutionRestWebService;
import ispyb.ws.rest.saxs.SubtractionRestWebService;
import ispyb.ws.rest.security.AuthenticationRestWebService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Api(value = "/ispyb-ws")
//@ApplicationPath("rest")
public class Bootstrap extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
//
//	@Override
//  @Path( "/ispyb-ws" )
//	public void init(ServletConfig config) throws ServletException {
////		super.init(config);
//
//		Info info = new Info()
//			.title("Some new test title")
//			.description("Adding some bloat text for test purposes")
//      .version( "5.4.3" );
//
//		ServletContext context = config.getServletContext();
//		Swagger swagger = new Swagger().info( info );
//		swagger.securityDefinition( "api_key",
//			new ApiKeyAuthDefinition( "api_key", In.HEADER ) );
//		swagger.tag( new Tag()
//											.name( "Test-Tag" )
//		.description( "All of the test endpoints" ) );
//
//		swagger.tag( new Tag()
//		.name( "Another test tag" )
//		.description( "More rubbish text" ));
//
//    swagger.setSchemes( Collections.singletonList( Scheme.HTTP ) );
//    swagger.setHost("192.168.30.200:8080");
//    swagger.setBasePath("/ispyb/ispyb-ws/rest");
////    swagger.setResourcePackage("io.swagger.resources");
////    swagger.setScan(true);
//
//    Scanner scanner = new Scanner()
//    {
//      @Override
//      public Set<Class<?>> classes()
//      {
//        return getClasses();
//      }
//
//
//      @Override
//      public boolean getPrettyPrint()
//      {
//        return false;
//      }
//
//
//      @Override
//      public void setPrettyPrint( boolean b )
//      {
//
//      }
//    };
//
//		new SwaggerContextService().withServletConfig( config ).updateSwagger( swagger ).setScanner( scanner );
//
//
//
////		BeanConfig beanConfig = new BeanConfig();
////		beanConfig.setVersion("5.4.3");
////		beanConfig.setSchemes(new String[] { "http" });
////		//beanConfig.setHost("localhost:8080");
////		beanConfig.setHost("192.168.30.200:8080");
////		beanConfig.setBasePath("/ispyb/ispyb-ws/rest");
////		beanConfig.setResourcePackage("io.swagger.resources");
////		beanConfig.setScan(true);
//	}
//
//
////  @Override
//  public Set<Class<?>> getClasses() {
//    Set<Class<?>> resources = new HashSet<Class<?>>();
//    /** MX **/
//    resources.add(AutoprocintegrationRestWebService.class);
//    resources.add(CrystalRestWebService.class);
//    resources.add(DataCollectionRestWebService.class);
//    resources.add(EnergyScanRestWebService.class);
//    resources.add(ImageWebService.class);
//    resources.add(PhasingRestWebService.class);
//    resources.add(ProteinRestWebService.class);
//    resources.add(SampleRestWebService.class);
//    resources.add(WorkflowRestWebService.class);
//    resources.add(XFEFluorescenceSpectrumRestWebService.class);
//
//    /** SAXS **/
//    resources.add(BufferRestWebService.class);
//    resources.add(DataCollectionRestWebService.class);
//    resources.add(ExperimentRestWebService.class);
//    resources.add(FrameRestWebService.class);
//    resources.add(MacromoleculeRestWebService.class);
//    resources.add(MeasurementRestWebService.class);
//    resources.add(ModelingRestWebService.class);
//    resources.add(SaxsRestWebService.class);
//    resources.add(SpecimenRestWebService.class);
//    resources.add(StatsRestWebService.class);
//    resources.add(StockSolutionRestWebService.class);
//    resources.add(SubtractionRestWebService.class);
//
//    /** PROPOSAL **/
//    resources.add(DewarRestWebService.class);
//    resources.add(ProposalRestWebService.class);
//    resources.add(SessionRestWebService.class);
//    resources.add(ShippingRestWebService.class);
//
//    /** AUTHENTICATION **/
//    resources.add(AuthenticationRestWebService.class);
//
//    resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
//    resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
//    return resources;
//  }


}
