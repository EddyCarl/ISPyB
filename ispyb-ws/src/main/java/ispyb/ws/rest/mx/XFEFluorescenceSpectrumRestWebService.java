package ispyb.ws.rest.mx;

import dls.dto.XFEFluorescenceSpectrumDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.ws.rest.xfefluorescencespectrum.XFEFluorescenSpectrumRestWsService;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.ws.rest.RestWebService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import io.swagger.annotations.Api;
import utils.DLSApiAuthenticationChecker;
import utils.SwaggerTagConstants;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class XFEFluorescenceSpectrumRestWebService extends RestWebService
{
  private final static Logger logger = Logger.getLogger(XFEFluorescenceSpectrumRestWebService.class);


  /**
   * Used to retrieve the fluorescence spectrum data for a particular user's session.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/sessions/{id}/fluorescence-spectrum" )
  @ApiOperation
    (
      value = "Retrieves fluorescence spectrum data",
      notes = "Returns a list of fluorescence spectrum data for a particular session",
      tags = { SwaggerTagConstants.FLUORESCENCE_SPEC_TAG }, response = XFEFluorescenceSpectrum3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 401, message = "You are not authorised to use this endpoint" ),
      @ApiResponse( code = 404, message = "No fluorescence spectrum records found for the input sessionId" )
    } )
  public Response retrieveFluorescenceSpectrumData
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the session to retrieve"
      ) @PathParam( "id" ) int sessionId, @Context HttpHeaders headers

    ) throws Exception
  {
    String methodName = "retrieveFluorescenceSpectrumData";
    long id = this.logInit(methodName, logger, sessionId);

    String apiToken = DLSApiAuthenticationChecker.retrieveToken( headers );

    if( !DLSApiAuthenticationChecker.userIsAuthenticated( this.getLogin3Service(), apiToken ) )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "You are not authorised to use this endpoint" );
      return Response.status(Response.Status.UNAUTHORIZED).entity( error ).build();
    }

    // Retrieve the session entity using the input sessionId
    Session3VO session = this.getSession3Service().findByPk( sessionId, false, false, true );

    if( session == null )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any XFESpectrum entities attached to the session entity
    List<XFEFluorescenceSpectrum3VO> xfefSpectrumList = session.getXfeSpectrumsList();

    if( xfefSpectrumList == null )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] doesn't have any associated XFE Spectrum records" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( xfefSpectrumList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] doesn't have any associated XFE Spectrum records" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildXFEFluorescenceSpectrumResponse( sessionId, xfefSpectrumList ) ).build();
  }


  /**
   * Utility method used to build a list of XFEFluorescenceSpectrumDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the XFEFluorescenceSpectrum3VO
   * entities obtained from the database.
   *
   * @param sessionId - The sessionId to be used in each XFEFluorescenceSpectrumDTO (Passed in by the user)
   * @param xfefSpectrumList - A list of the XFEFluorescenceSpectrum3VO entities retrieved from the database
   *
   * @return List<XFEFluorescenceSpectrumDTO> - A list of the response objects holding just the relevant data
   */
  private List<XFEFluorescenceSpectrumDTO>
                            buildXFEFluorescenceSpectrumResponse( final int sessionId,
                                                                  List<XFEFluorescenceSpectrum3VO> xfefSpectrumList )
  {
    List<XFEFluorescenceSpectrumDTO> xfefSpectrumDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( XFEFluorescenceSpectrum3VO xfefSpectrum : xfefSpectrumList )
    {
      XFEFluorescenceSpectrumDTO xfeFluorescenceSpectrumDTO = new XFEFluorescenceSpectrumDTO();

      xfeFluorescenceSpectrumDTO.setSessionId( sessionId );
      xfeFluorescenceSpectrumDTO.setExposureTime( xfefSpectrum.getExposureTime() );
      xfeFluorescenceSpectrumDTO.setStartTime( xfefSpectrum.getStartTime() );
      xfeFluorescenceSpectrumDTO.setJpegScanFileFullPath( xfefSpectrum.getJpegScanFileFullPath() );
      xfeFluorescenceSpectrumDTO.setEnergy( xfefSpectrum.getEnergy() );
      xfeFluorescenceSpectrumDTO.setRowNumber( rowNumber++ );

      xfefSpectrumDTOList.add( xfeFluorescenceSpectrumDTO );
    }

    return xfefSpectrumDTOList;
  }


  /*
   * ---- Legacy endpoints below this point ----
   */
  @Path("{token}/proposal/{proposal}/mx/xrfscan/session/{sessionId}/list")
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
			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewBySessionId(this.getProposalId(proposal), sessionId);
			this.logFinish(methodName, id, logger);
			return sendResponse(result );
		}
		catch(Exception e){
			return this.logError(methodName, e, id, logger);
		}
	}

    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/image/{imageType}/get")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("image/png")
   	public Response getImage(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId,
   			@PathParam("imageType") String imageType) throws Exception {

   		String methodName = "getImage";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey(imageType)){
   						String imageFilePath = result.get(0).get(imageType).toString();
   						if (imageFilePath != null){
   							if (new File(imageFilePath).exists()){
   								this.logFinish(methodName, id, logger);
   								return this.sendImage(imageFilePath);
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}

    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/file/{imageType}/get")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("text/plain")
   	public Response getFile(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId,
   			@PathParam("imageType") String imageType) throws Exception {

   		String methodName = "getFile";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey(imageType)){
   						String imageFilePath = result.get(0).get(imageType).toString();
   						if (imageFilePath != null){
   							if (new File(imageFilePath).exists()){
   								this.logFinish(methodName, id, logger);
   								return this.sendImage(imageFilePath);
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}

    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/csv")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("text/plain")
   	public Response getCSVFile(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId) throws Exception {

   		String methodName = "getCSVFile";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey("annotatedPymcaXfeSpectrum")){
   						String imageFilePath = result.get(0).get("annotatedPymcaXfeSpectrum").toString();
   						if (imageFilePath != null){
   							/** Converting to csv **/
   							imageFilePath = imageFilePath.replace(".html", "_peaks.csv");
   							if (new File(imageFilePath).exists()){
   								this.logFinish(methodName, id, logger);
   								return this.sendResponse(new String(Files.readAllBytes(Paths.get(imageFilePath))));
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}

    @Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/file/list")
   	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
   	@GET
   	@Produces("text/plain")
   	public Response getFiles(
   			@PathParam("token") String token,
   			@PathParam("proposal") String proposal,
   			@PathParam("xrfscanId") int xrfscanId) throws Exception {

   		String methodName = "getFiles";
   		long id = this.logInit(methodName, logger, token, proposal, xrfscanId);
   		try{
   			List<Map<String, Object>> result = getXFEFluorescenSpectrumService().getViewById(this.getProposalId(proposal), xrfscanId);
   			if (result != null){
   				if (result.size()> 0){
   					if (result.get(0).containsKey("annotatedPymcaXfeSpectrum")){
   						String imageFilePath = result.get(0).get("annotatedPymcaXfeSpectrum").toString();
   						if (imageFilePath != null){
   							/** Converting to csv **/
   							File[] files = null;
   							if (new File(imageFilePath).exists()){
   								String parent = new File(imageFilePath).getParent();
   								if (new File(parent).exists()){
   									System.out.println(parent);
   									files = new File(parent).listFiles();
   								}
   								/** Cenverting abstract file to filePaths **/
   								List<String> filePaths = new ArrayList<String>();
   								for (File file : files) {
									filePaths.add(file.getName());
								}
   								this.logFinish(methodName, id, logger);
   								return this.sendResponse(filePaths);
   							}
   						}
   					}
   				}
   			}
   		}
   		catch(Exception e){
   			return this.logError(methodName, e, id, logger);
   		}
   		return null;
   	}

	private XFEFluorescenSpectrumRestWsService getXFEFluorescenSpectrumService() throws NamingException {
		return (XFEFluorescenSpectrumRestWsService) Ejb3ServiceLocator.getInstance().getLocalService(XFEFluorescenSpectrumRestWsService.class);
	}



}
