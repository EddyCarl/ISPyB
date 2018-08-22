package ispyb.ws.rest.mx;

import dls.dto.AutoProcDTO;
import dls.dto.AutoProcScalingStatisticsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProcScaling3Service;
import ispyb.server.mx.services.autoproc.MxMrRun3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScaling3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.MxMrRun3VO;
import org.apache.log4j.Logger;
import utils.SwaggerTagConstants;

import javax.naming.NamingException;
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
  private final static Logger logger = Logger.getLogger( AutoProcScalingRestWebService.class );


  /**
   * Used to retrieve a set of MX MR Run results based on an input auto-proc-scaling ID.
   *
   * @return Response  - Returns a relevant HTTP response
   */
  @GET
  @Path("/auto-proc-scalings/{id}/mx-mr-runs")
  @ApiOperation
    (
      value = "Retrieves MX MR Run results",
      notes = "Retrieves MX MR Run results based on the input Auto Proc Scaling ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG },
      responseContainer = "List", authorizations = @Authorization("apiKeyAuth")
    )
  @Produces({ "application/json" })
  @ApiResponses
    ({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Some error"),
      @ApiResponse(code = 404, message = "No MX MR Runs found for the input autoProcScalingId")
    })
  public Response retrieveMxMrRuns
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam("id") int autoProcScalingId

  ) throws Exception
  {
    String methodName = "retrieveMxMrRuns";
    long id = this.logInit( methodName, logger, autoProcScalingId );

    // Get an autoProcScaling entity by ID if available
    AutoProcScaling3VO autoProcScaling3VO = this.getAutoProcScaling3Service().findByPk( autoProcScalingId );

    if( autoProcScaling3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcScalingId[" + autoProcScalingId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }





    List<MxMrRun3VO> mxMrRun3VOList = this.getMxMrRun3Service().findAll();

    if( mxMrRun3VOList == null )
    {
      System.out.println( "--- MxMrRun3VO List is null ---");
      return Response.status( Response.Status.NOT_FOUND ).entity( "MxMrRun3VO List is null" ).build();
    }

    System.out.println( "List size: " + mxMrRun3VOList.size() );

    if( !mxMrRun3VOList.isEmpty() )
    {
      printListResults( mxMrRun3VOList );
    }
    else
    {
      System.out.println( "--- MxMrRun3VO List is empty ---");
      return Response.status( Response.Status.NOT_FOUND ).entity( "MxMrRun3VO List is empty" ).build();
    }





    System.out.println( " *** Attempt to find using the autoProcScalingId : " + autoProcScalingId );

    List<MxMrRun3VO> mxMrRun3VOListByAPSId = this.getMxMrRun3Service().findByAutoProcScalingId( autoProcScalingId );


    if( mxMrRun3VOListByAPSId == null )
    {
      System.out.println( "--- MxMrRun3VO List is null ---");
      return Response.status( Response.Status.NOT_FOUND ).entity( "MxMrRun3VO List is null" ).build();
    }

    System.out.println( "List size: " + mxMrRun3VOListByAPSId.size() );

    if( !mxMrRun3VOListByAPSId.isEmpty() )
    {
      printListResults( mxMrRun3VOListByAPSId );
    }
    else
    {
      System.out.println( "--- MxMrRun3VO List is empty ---");
      return Response.status( Response.Status.NOT_FOUND ).entity( "MxMrRun3VO List is empty" ).build();
    }







    return Response.ok( buildDummyMxMrRunData() ).build();
  }




  private void printListResults( List<MxMrRun3VO> runsList )
  {
    for( MxMrRun3VO mxMrRun3VO : runsList )
    {
      System.out.println( "mxMrRun3VO.getMxMRRunId() : " + mxMrRun3VO.getMxMRRunId() );
      System.out.println( "mxMrRun3VO.getAutoProcScalingVO() : " + mxMrRun3VO.getAutoProcScalingVO() );
      System.out.println( "mxMrRun3VO.getMessage() : " + mxMrRun3VO.getMessage() );
      System.out.println( "mxMrRun3VO.getCommandLine() : " + mxMrRun3VO.getCommandLine() );
      System.out.println( "mxMrRun3VO.getSuccess() : " + mxMrRun3VO.getSuccess() );
      System.out.println( "mxMrRun3VO.getrFreeValueEnd() : " + mxMrRun3VO.getrFreeValueEnd() );
      System.out.println( "mxMrRun3VO.getrFreeValueStart() : " + mxMrRun3VO.getrFreeValueStart() );
    }
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
      dummyMxMrRun.put( "rfreevaluestart", startValues );
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
   * @return Response  - Returns a relevant HTTP response
   */
  @GET
  @Path("/auto-proc-scalings/{id}/statistics")
  @ApiOperation
    (
      value = "Retrieves auto-proc-scaling statistics",
      notes = "Retrieves auto-proc-scaling statistics based on the input Auto Proc Scaling ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProcScalingStatisticsDTO.class,
      responseContainer = "List", authorizations = @Authorization("apiKeyAuth")
    )
  @Produces({ "application/json" })
  @ApiResponses
    ({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Some error"),
      @ApiResponse(code = 404, message = "No auto-proc-scaling statistics found for the input autoProcScalingId")
    })
  public Response retrieveAutoProcScalingStatistics
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam("id") int autoProcScalingId

  ) throws Exception
  {
    String methodName = "retrieveAutoProcScalingStatistics";
    long id = this.logInit( methodName, logger, autoProcScalingId );

    // Get an autoProcScaling entity by ID if available
    AutoProcScaling3VO autoProcScaling3VO = this.getAutoProcScaling3Service().findByPk( autoProcScalingId );

    if( autoProcScaling3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcScalingId[" + autoProcScalingId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Get the autoProc entity from the autoProcScaling entity
    AutoProc3VO autoProc3VO = autoProcScaling3VO.getAutoProcVO();

    if( autoProc3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcScalingId[" + autoProcScalingId + "] has no associated AutoProc record." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Get a list of autoProcScalingStatistics using the autoProcId
    List<AutoProcScalingStatistics3VO> autoProcScalingStatisticsList =
            this.getAutoProcScalingStatistics3Service().findByAutoProcId( autoProc3VO.getAutoProcId(), null );

    String errorMsg = "Failed to find any AutoProcScalingStatistic records for the " +
      "input autoProcScalingId[" + autoProcScalingId + "]";

    if( autoProcScalingStatisticsList == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", errorMsg );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( autoProcScalingStatisticsList.isEmpty() )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", errorMsg );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildAutoProcScalingStatisticsResponse( autoProcScalingStatisticsList ) ).build();
  }


  /**
   * Used to retrieve the data for a specific Auto Proc instance based on the input auto-proc ID.
   *
   * @return Response  - Returns a relevant HTTP response
   */
  @GET
  @Path("/auto-proc/{id}")
  @ApiOperation
    (
      value = "Retrieves an auto-proc instance",
      notes = "Retrieves an auto-proc instance based on the input Auto Proc ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProcDTO.class,
      authorizations = @Authorization("apiKeyAuth")
    )
  @Produces({ "application/json" })
  @ApiResponses
    ({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Some error"),
      @ApiResponse(code = 404, message = "An Auto Proc instance could not be found for the input ID")
    })
  public Response retrieveAutoProc
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc instance to retrieve"
      ) @PathParam("id") int autoProcId

  ) throws Exception
  {
    String methodName = "retrieveAutoProc";
    long id = this.logInit( methodName, logger, autoProcId );

    // Retrieve the autoProc entity using the input autoProcId
    AutoProc3VO autoProc = this.getAutoProc3Service().findByPk( autoProcId );

    if( autoProc == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcId[" + autoProcId + "] could not be found in the database" );
      return Response.status( Response.Status.NOT_FOUND ).entity( error ).build();
    }

    return Response.ok( buildAutoProcResponse( autoProc ) ).build();
  }



  /**
   * Utility method used to build a list of AutoProcScalingStatisticsDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the AutoProcScalingStatistics3VO entities
   * obtained from the database.
   *
   * @param autoProcScalingStatisticsList - A list of the AutoProcScalingStatistics3VO entities retrieved from the database
   *
   * @return List<AutoProcScalingStatisticsDTO> - A list of the response objects holding just the relevant data
   */
  private List<AutoProcScalingStatisticsDTO>
  buildAutoProcScalingStatisticsResponse( final List<AutoProcScalingStatistics3VO> autoProcScalingStatisticsList )
  {
    List<AutoProcScalingStatisticsDTO> autoProcScalingStatisticsDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( AutoProcScalingStatistics3VO autoProcScalingStatistics : autoProcScalingStatisticsList )
    {
      AutoProcScalingStatisticsDTO autoProcScalingStatisticsDTO = new AutoProcScalingStatisticsDTO();

      autoProcScalingStatisticsDTO.setAutoProcScalingId( autoProcScalingStatistics.getAutoProcScalingVOId() );
      autoProcScalingStatisticsDTO.setAutoProcScalingStatisticsId( autoProcScalingStatistics.getAutoProcScalingStatisticsId() );
      autoProcScalingStatisticsDTO.setScalingStatisticsType( autoProcScalingStatistics.getScalingStatisticsType() );
      autoProcScalingStatisticsDTO.setResolutionLimitLow( autoProcScalingStatistics.getResolutionLimitLow() );
      autoProcScalingStatisticsDTO.setResolutionLimitHigh( autoProcScalingStatistics.getResolutionLimitHigh() );
      autoProcScalingStatisticsDTO.setrMerge( autoProcScalingStatistics.getRmerge() );
      autoProcScalingStatisticsDTO.setMeanIoverSigI( autoProcScalingStatistics.getMeanIoverSigI() );
      autoProcScalingStatisticsDTO.setCompleteness( autoProcScalingStatistics.getCompleteness() );
      autoProcScalingStatisticsDTO.setnTotalUniqueObservations( autoProcScalingStatistics.getnTotalUniqueObservations() );
      autoProcScalingStatisticsDTO.setMultiplicity( autoProcScalingStatistics.getMultiplicity() );
      autoProcScalingStatisticsDTO.setAnomalousCompleteness( autoProcScalingStatistics.getAnomalousCompleteness() );
      autoProcScalingStatisticsDTO.setAnomalousMultiplicity( autoProcScalingStatistics.getAnomalousMultiplicity() );
      autoProcScalingStatisticsDTO.setCcHalf( autoProcScalingStatistics.getCcHalf() );
      autoProcScalingStatisticsDTO.setCcAno( autoProcScalingStatistics.getCcAno() );
      autoProcScalingStatisticsDTO.setRowNumber( rowNumber++ );

      autoProcScalingStatisticsDTOList.add( autoProcScalingStatisticsDTO );
    }

    return autoProcScalingStatisticsDTOList;
  }


  /**
   * Utility method used to build an AutoProcDTO object which holds the relevant data
   * required for the response. The object is populated with data retrieved from the AutoProc3VO entity
   * obtained from the database.
   *
   * @param autoProc - An AutoProc3VO entity retrieved from the database
   *
   * @return AutoProcDTO - A response object holding just the relevant data
   */
  private AutoProcDTO buildAutoProcResponse( final AutoProc3VO autoProc )
  {
    AutoProcDTO autoProcDTO = new AutoProcDTO();

    autoProcDTO.setAutoProcId( autoProc.getAutoProcId() );
    autoProcDTO.setSpaceGroup( autoProc.getSpaceGroup() );
    autoProcDTO.setRefinedCellA( autoProc.getRefinedCellA() );
    autoProcDTO.setRefinedCellB( autoProc.getRefinedCellB() );
    autoProcDTO.setRefinedCellC( autoProc.getRefinedCellC() );
    autoProcDTO.setRefinedCellAlpha( autoProc.getRefinedCellAlpha() );
    autoProcDTO.setRefinedCellBeta( autoProc.getRefinedCellBeta() );
    autoProcDTO.setRefinedCellGamma( autoProc.getRefinedCellGamma() );
    autoProcDTO.setRowNumber( 1 );

    return autoProcDTO;
  }


  /**
   * Utility method used to get an AutoProcScaling3Service instance in order to obtain AutoProcScaling3VO entities
   * from the database using the methods defined in the service.
   *
   * @return AutoProcScaling3Service - The service containing helper methods to obtain data from the database
   *
   * @throws NamingException - Thrown when the service cannot be found
   */
  protected AutoProcScaling3Service getAutoProcScaling3Service() throws NamingException
  {
    return (AutoProcScaling3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcScaling3Service.class);
  }


  /**
   * Utility method used to get a MxMrRun3Service instance in order to obtain MxMrRun3VO entities
   * from the database using the methods defined in the service.
   *
   * @return MxMrRun3Service - The service containing helper methods to obtain data from the database
   *
   * @throws NamingException - Thrown when the service cannot be found
   */
  protected MxMrRun3Service getMxMrRun3Service() throws NamingException
  {
    return (MxMrRun3Service) Ejb3ServiceLocator.getInstance().getLocalService(MxMrRun3Service.class);
  }
}
