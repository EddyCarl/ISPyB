package ispyb.ws.rest.mx;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import org.apache.log4j.Logger;
import utils.SwaggerTagConstants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Api
@Path("/")
public class AutoProcScalingRestWebService extends MXRestWebService
{
  private final static Logger logger = Logger.getLogger(AutoProcScalingRestWebService.class);


  /**
   * Used to retrieve a set of MX MR Run results based on an input auto-proc-scaling ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/auto-proc-scalings/{id}/mx-mr-runs" )
  @ApiOperation
    (
      value = "Retrieves MX MR Run results",
      notes = "Retrieves MX MR Run results based on the input Auto Proc Scaling ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG },
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No MX MR Runs found for the input autoProcScalingId" )
    } )
  public Response retrieveMxMrRuns
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam( "id" ) int autoProcScalingId

  ) throws Exception
  {
    String methodName = "retrieveMxMrRuns";
    long id = this.logInit(methodName, logger, autoProcScalingId);

    if(autoProcScalingId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      String errorMsg = "The input autoProcScaling ID[ " + autoProcScalingId+ " ] has no MX MR Runs associated with it";
      error.put( "error", errorMsg );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDummyMxMrRunData() ).build();
  }



  private List<Map<String, Object>> buildDummyMxMrRunData()
  {
    List<Map<String, Object>> dummyMxMrRunData = new ArrayList<>();

    for( int i = 0; i < 5; i++ )
    {
      Map<String, Object> dummyMxMrRun = new HashMap<>();

      int startValues = ( i + 5 * 10 );
      int endValues = ( i + 10 * 5 );

      dummyMxMrRun.put( "mxmrrunid", i );
      dummyMxMrRun.put( "success", "Yes" );
      dummyMxMrRun.put( "message", "Dummy Msg with index" + i );
      dummyMxMrRun.put( "pipeline", "pipeline" );
      dummyMxMrRun.put( "rvaluestart", startValues );
      dummyMxMrRun.put( "rvalueend", endValues );
      dummyMxMrRun.put( "rfreevaluestart", startValues  );
      dummyMxMrRun.put( "rfreevalueend", endValues );
      dummyMxMrRun.put( "logfile", "/dls/dummy/path/tologfile" );
      dummyMxMrRun.put( "commandline", "commandline" );
      dummyMxMrRun.put( "RNUM", i );

      dummyMxMrRunData.add( dummyMxMrRun );
    }

    return dummyMxMrRunData;
  }





  /**
   * Used to retrieve a set of auto-proc-scaling statistics where the auto-proc-scaling ID is specified.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/auto-proc-scalings/{id}/statistics" )
  @ApiOperation
    (
      value = "Retrieves auto-proc-scaling statistics",
      notes = "Retrieves auto-proc-scaling statistics based on the input Auto Proc Scaling ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProcScalingStatistics3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No auto-proc-scaling statistics found for the input autoProcScalingId" )
    } )
  public Response retrieveAutoProcScalingStatistics
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam( "id" ) int autoProcScalingId

  ) throws Exception
  {
    String methodName = "retrieveAutoProcScalingStatistics";
    long id = this.logInit(methodName, logger, autoProcScalingId);

    if(autoProcScalingId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      String errorMsg = "The input autoProcScaling ID[ " + autoProcScalingId+ " ] has no statistics associated with it";
      error.put( "error", errorMsg );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDummyAutoProcStatisticsData() ).build();
  }


  private List<Map<String, Object>> buildDummyAutoProcStatisticsData()
  {
    List<Map<String, Object>> dummyAutoProcStatisticsData = new ArrayList<>();

    for( int i = 0; i < 5; i++ )
    {
      Map<String, Object> dummyAutoProcStatistic = new HashMap<>();

      double resolutionLimitBase = 1.45;
      Random rand = new Random();

      dummyAutoProcStatistic.put( "autoProcScalingId", "1" );
      dummyAutoProcStatistic.put( "autoProcScalingStatisticsId", i );
      dummyAutoProcStatistic.put( "scalingStatisticsType", "outershell" );
      dummyAutoProcStatistic.put( "resolutionLimitLow", resolutionLimitBase + i );
      dummyAutoProcStatistic.put( "resolutionLimitHigh", resolutionLimitBase + ( i * 5 ) );
      dummyAutoProcStatistic.put( "rMerge", "3.0" );
      dummyAutoProcStatistic.put( "meanIOverSigI", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "completeness", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "nTotalUniqueObservations", rand.nextInt(20));
      dummyAutoProcStatistic.put( "multiplicity", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "anomalousCompleteness", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "anomalousMultiplicity", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "ccHalf", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "ccAnomalous", rand.nextInt(20) );
      dummyAutoProcStatistic.put( "RNUM", i );

      dummyAutoProcStatisticsData.add( dummyAutoProcStatistic );
    }

    return dummyAutoProcStatisticsData;
  }



  /**
   * Used to retrieve the data for a specific Auto Proc instance based on the input auto-proc ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/auto-proc/{id}" )
  @ApiOperation
    (
      value = "Retrieves an auto-proc instance",
      notes = "Retrieves an auto-proc instance based on the input Auto Proc ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProc3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "An Auto Proc instance could not be found for the input ID" )
    } )
  public Response retrieveAutoProc
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc instance to retrieve"
      ) @PathParam( "id" ) int autoProcId

  ) throws Exception
  {
    String methodName = "retrieveAutoProc";
    long id = this.logInit(methodName, logger, autoProcId);

    if(autoProcId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProc ID[" + autoProcId + "] does not exist." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDummyAutoProcData() ).build();
  }



  private List<Map<String, Object>> buildDummyAutoProcData()
  {
    List<Map<String, Object>> dummyAutoProcData = new ArrayList<>();

    for( int i = 0; i < 5; i++ )
    {
      Map<String, Object> dummyAutoProc = new HashMap<>();
      Random rand = new Random();

      dummyAutoProc.put( "autoProcId", "1" );
      dummyAutoProc.put( "spaceGroup", i );
      dummyAutoProc.put( "refinedCell_a", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "refinedCell_b", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "refinedCell_c", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "refinedCell_alpha", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "refinedCell_beta", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "refinedCell_gamma", rand.nextInt(i + 40) + 0.5 );
      dummyAutoProc.put( "RNUM", i );

      dummyAutoProcData.add( dummyAutoProc );
    }

    return dummyAutoProcData;
  }

}
