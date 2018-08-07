package ispyb.ws.rest.mx;

import dls.dto.EnergyScanDataDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.ws.rest.energyscan.EnergyScanRestWsService;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;
import org.apache.log4j.Logger;
import utils.SwaggerTagConstants;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class EnergyScanRestWebService extends RestWebService
{
  private final static Logger logger = Logger.getLogger(EnergyScanRestWebService.class);

  /**
   * Used to retrieve Energy Scan information for any records relating to the users session.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/sessions/{id}/energy-scans" )
  @ApiOperation
    (
      value = "Retrieves a list of Energy Scan entries",
      notes = "Returns a list of Energy Scan entries for the current logged in user",
      tags = { SwaggerTagConstants.ENERGY_SCAN_TAG }, response = EnergyScan3VO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No energy scan records found for the input sessionId" )
    } )
  public Response retrieveEnergyScanData
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the session to retrieve"
      ) @PathParam( "id" ) int sessionId

  ) throws Exception
  {
    String methodName = "retrieveEnergyScanData";
    long id = this.logInit(methodName, logger, sessionId);

    // Retrieve the session entity using the input sessionId
    Session3VO session = this.getSession3Service().findByPk( sessionId, false, true, false );

    if( session == null )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any energy scan entities attached to the session entity
    List<EnergyScan3VO> energyScans = session.getEnergyScansList();

    if( energyScans == null )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] doesn't have any associated energy scan records" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( energyScans.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] doesn't have any associated energy scan records" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildEnergyScanDataResponse( sessionId, energyScans ) ).build();
  }


  /**
   * Utility method used to build a list of EnergyScanDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the EnergyScan3VO entities
   * obtained from the database.
   *
   * @param sessionId - The sessionId to be used in each EnergyScanDTO (Passed in by the user)
   * @param energyScans - A list of the EnergyScan3VO entities retrieved from the database
   *
   * @return List<EnergyScanDataDTO> - A list of the response objects holding just the relevant data
   */
  private List<EnergyScanDataDTO> buildEnergyScanDataResponse( final int sessionId, List<EnergyScan3VO> energyScans )
  {
    List<EnergyScanDataDTO> energyScanDataDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( EnergyScan3VO energyScan : energyScans )
    {
      EnergyScanDataDTO energyScanDataDTO = new EnergyScanDataDTO();

      energyScanDataDTO.setSessionId( sessionId );
      energyScanDataDTO.setEnergyScanId( energyScan.getEnergyScanId() );
      energyScanDataDTO.setElement( energyScan.getElement() );
      energyScanDataDTO.setStartEnergy( energyScan.getStartEnergy() );
      energyScanDataDTO.setEndEnergy( energyScan.getEndEnergy() );
      energyScanDataDTO.setPeakEnergy( energyScan.getPeakEnergy() );
      energyScanDataDTO.setPeakFPrime( energyScan.getPeakFPrime() );
      energyScanDataDTO.setPeakFDoublePrime( energyScan.getPeakFDoublePrime() );
      energyScanDataDTO.setEdgeEnergy( energyScan.getEdgeEnergy() );
      energyScanDataDTO.setStartTime( energyScan.getStartTime() );
      energyScanDataDTO.setInflectionEnergy( energyScan.getInflectionEnergy() );
      energyScanDataDTO.setInflectionFPrime( energyScan.getInflectionFPrime() );
      energyScanDataDTO.setInflectionFDoublePrime( energyScan.getInflectionFDoublePrime() );
      energyScanDataDTO.setJpegChoochFileFullPath( energyScan.getJpegChoochFileFullPath() );
      energyScanDataDTO.setRowNumber( rowNumber++ );

      energyScanDataDTOList.add( energyScanDataDTO );
    }

    return energyScanDataDTOList;
  }


  /*
   * ---- Legacy endpoints below this point ----
   */
    @Path("{token}/proposal/{proposal}/mx/energyscan/session/{sessionId}/list")
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Produces({ "application/json" })
	public Response getEnergyScanBySessionId(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") int sessionId) throws Exception {

		String methodName = "getEnergyScanBySessionId";
		long id = this.logInit(methodName, logger, token, proposal, sessionId);
		try{
			List<Map<String, Object>> result = getEnergyScanService().getViewBySessionId(this.getProposalId(proposal), sessionId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}

    private EnergyScan3VO getEnergyById(int energyscanId, String proposal) throws NamingException, Exception{
    	List<Map<String, Object>> result = getEnergyScanService().getViewById(this.getProposalId(proposal), energyscanId);
		if (result.size() > 0){
			EnergyScan3Service energyScan3Service = (EnergyScan3Service) Ejb3ServiceLocator.getInstance().getLocalService(EnergyScan3Service.class);
			return energyScan3Service.findByPk(Integer.parseInt(result.get(0).get("energyScanId").toString()));
		}
		return null;
    }
    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/scanfile")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("text/plain")
   	public Response getScanFileFullPath(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {

   		String methodName = "getScanFileFullPath";
   		long start = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{
   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
	   			if (new File(energyScan.getScanFileFullPath()).exists()){
	   				this.logFinish(methodName, start, logger);
	   				return this.downloadFileAsAttachment(energyScan.getScanFileFullPath());
				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, start, logger);
   		}
		return null;
   	}

    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/chooch")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
	@Produces("text/plain")
   	public Response getChooch(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {

   		String methodName = "getChooch";
   		long id = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{

   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
   				if (energyScan.getChoochFileFullPath() != null){
	   				if (new File(energyScan.getChoochFileFullPath()).exists()){
	   					logger.info("Energy scan found " + energyScan.getEnergyScanId());
	   	   				logger.info("File: " + energyScan.getScanFileFullPath());
	   	   			    logger.info("Downloading: " + energyScan.getChoochFileFullPath());
	   	   			    this.logFinish(methodName, id, logger);
	   					return this.downloadFileAsAttachment(energyScan.getChoochFileFullPath());
	   				}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
		return null;
   	}

    @Path("{token}/proposal/{proposal}/mx/energyscan/energyscanId/{energyscanId}/jpegchooch")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("image/png")
   	public Response getjpegchooch(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("energyscanId") int energyscanId) throws Exception {

   		String methodName = "getjpegchooch";
   		long id = this.logInit(methodName, logger, token, proposal, energyscanId);
   		try{

   			EnergyScan3VO energyScan = this.getEnergyById(energyscanId, proposal);
   			if (energyScan != null){
   				if (new File(energyScan.getJpegChoochFileFullPath()).exists()){
   					logger.info("Energy scan found " + energyScan.getEnergyScanId());
   	   				logger.info("File: " + energyScan.getJpegChoochFileFullPath());
   	   			    logger.info("Downloading: " + energyScan.getJpegChoochFileFullPath());
   	   			    this.logFinish(methodName, id, logger);
   					return this.sendImage(energyScan.getJpegChoochFileFullPath());
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
		return null;
   	}


	private EnergyScanRestWsService getEnergyScanService() throws NamingException {
		return (EnergyScanRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(EnergyScanRestWsService.class);
	}



}
