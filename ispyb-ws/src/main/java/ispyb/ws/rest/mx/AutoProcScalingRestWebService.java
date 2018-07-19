package ispyb.ws.rest.mx;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import utils.SwaggerTagConstants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api
@Path("/")
public class AutoProcScalingRestWebService extends MXRestWebService
{


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
      responseContainer = "List", authorizations = @Authorization( "basicAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveMxMrRuns
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam( "id" ) int autoProcScalingId

  ) throws Exception
  {
    return null;
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
      responseContainer = "List", authorizations = @Authorization( "basicAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveAutoProcScalingStatistics
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-scaling to retrieve"
      ) @PathParam( "id" ) int autoProcScalingId

  ) throws Exception
  {
    return null;
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
      responseContainer = "List", authorizations = @Authorization( "basicAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveAutoProc
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc instance to retrieve"
      ) @PathParam( "id" ) int autoProcId

  ) throws Exception
  {
    return null;
  }








}
