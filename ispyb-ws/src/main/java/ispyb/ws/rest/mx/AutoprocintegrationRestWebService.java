package ispyb.ws.rest.mx;

import dls.dto.AutoProcIntegrationDTO;
import dls.dto.AutoProcIntegrationScalingDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.common.util.HashMapToZip;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProcScalingHasInt3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.utils.reader.AutoProcProgramaAttachmentFileReader;
import ispyb.server.mx.services.ws.rest.autoprocessingintegration.AutoProcessingIntegrationService;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingHasInt3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;
import utils.SwaggerTagConstants;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class AutoprocintegrationRestWebService extends MXRestWebService
{
  private final static Logger logger = Logger.getLogger(AutoprocintegrationRestWebService.class);
  private static final String NOT_ALLOWED = "You don't have access to this resource";


  /**
   * Used to retrieve the auto processing results from the Auto Proc Integration tables for a particular
   * data collection (input by data-collection id).
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{id}/auto-processing-results" )
  @ApiOperation
    (
      value = "Retrieves auto processing results",
      notes = "Returns a list of auto processing results for a particular data collection",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProcIntegration3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No auto processing results found for the input dataCollectionId" )
    } )
  public Response retrieveAutoProcessingResults
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the data-collection to retrieve"
      ) @PathParam( "id" ) int dataCollectionId

  ) throws Exception
  {
    String methodName = "retrieveAutoProcessingResults";
    long id = this.logInit(methodName, logger, dataCollectionId);

    // Retrieve the data collection entity using the input dataCollectionId
    DataCollection3VO dataCollection = this.getDataCollection3Service().findByDataCollectionId( dataCollectionId );

    if( dataCollection == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any autoProcIntegration entities attached to the data collection entity
    List<AutoProcIntegration3VO> autoProcIntegrationList = dataCollection.getAutoProcIntegrationsList();

    if( autoProcIntegrationList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input dataCollectionId[" + dataCollectionId + "] doesn't have " +
                            "any associated autoProcIntegration records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( autoProcIntegrationList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input dataCollectionId[" + dataCollectionId + "] doesn't have " +
        "any associated autoProcIntegration records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildAutoProcIntegrationResponse( dataCollectionId, autoProcIntegrationList ) ).build();
  }


  /**
   * Used to retrieve a particular auto-proc-integration based on the input ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/auto-proc-integrations/{id}/" )
  @ApiOperation
    (
      value = "Retrieves auto proc integration results based on the input ID",
      notes = "Returns an auto proc integration based on the input ID",
      tags = { SwaggerTagConstants.AUTO_PROC_TAG }, response = AutoProcIntegration3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No auto proc scaling records found for the input autoProcIntId" )
    } )
  public Response retrieveAutoProcIndexScalingSuccess
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the auto-proc-integration to retrieve"
      ) @PathParam( "id" ) int autoProcIntegrationId

  ) throws Exception
  {
    String methodName = "retrieveAutoProcIntegrationScalingSuccess";
    long id = this.logInit(methodName, logger, autoProcIntegrationId);

    // Retrieve autoProcScalingHasInt entities using the input autoProcIntegrationId
    List<AutoProcScalingHasInt3VO> autoProcScalingHasIntList =
                this.getAutoProcScalingHasInt3Service().findFiltered( autoProcIntegrationId );

    if( autoProcScalingHasIntList == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcIntegrationId[" + autoProcIntegrationId + "] " +
        "could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( autoProcScalingHasIntList.isEmpty() )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input autoProcIntegrationId[" + autoProcIntegrationId + "] " +
        "could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildAutoProcIntegrationScalingResponse( autoProcScalingHasIntList ) ).build();
  }


  /**
   * Utility method used to build a list of AutoProcIntegrationDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the AutoProcIntegration3VO entities
   * obtained from the database.
   *
   * @param dataCollectionId - The dataCollectionId to be used in each AutoProcIntegrationDTO (Passed in by the user)
   * @param autoProcIntegrations - A list of the AutoProcIntegration3VO entities retrieved from the database
   *
   * @return List<AutoProcIntegrationDTO> - A list of the response objects holding just the relevant data
   */
  private List<AutoProcIntegrationDTO>
                            buildAutoProcIntegrationResponse( final int dataCollectionId,
                                                              final List<AutoProcIntegration3VO> autoProcIntegrations )
  {
    List<AutoProcIntegrationDTO> autoProcIntegrationDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( AutoProcIntegration3VO autoProcIntegration : autoProcIntegrations )
    {
      AutoProcIntegrationDTO autoProcIntegrationDTO = new AutoProcIntegrationDTO();

      autoProcIntegrationDTO.setAutoProcIntegrationId( autoProcIntegration.getAutoProcIntegrationId() );
      autoProcIntegrationDTO.setDataCollectionId( dataCollectionId );
      autoProcIntegrationDTO.setAutoProcProgramId( autoProcIntegration.getAutoProcProgramVOId() );
      autoProcIntegrationDTO.setCellA( autoProcIntegration.getCellA() );
      autoProcIntegrationDTO.setCellB( autoProcIntegration.getCellB() );
      autoProcIntegrationDTO.setCellC( autoProcIntegration.getCellC() );
      autoProcIntegrationDTO.setCellAlpha( autoProcIntegration.getCellAlpha() );
      autoProcIntegrationDTO.setCellBeta( autoProcIntegration.getCellBeta() );
      autoProcIntegrationDTO.setCellGamma( autoProcIntegration.getCellGamma() );
      autoProcIntegrationDTO.setRowNumber( rowNumber++ );

      autoProcIntegrationDTOList.add( autoProcIntegrationDTO );
    }

    return autoProcIntegrationDTOList;
  }


  /**
   * Utility method used to build a list of AutoProcIntegrationScalingDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the AutoProcScalingHasInt3VO entities
   * obtained from the database.
   *
   * @param autoProcScalingHasIntList - A list of the AutoProcScalingHasInt3VO entities retrieved from the database
   *
   * @return List<AutoProcIntegrationScalingDTO> - A list of the response objects holding just the relevant data
   */
  private List<AutoProcIntegrationScalingDTO> buildAutoProcIntegrationScalingResponse(
                                            final List<AutoProcScalingHasInt3VO> autoProcScalingHasIntList )
  {
    List<AutoProcIntegrationScalingDTO> autoProcIntScalingDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( AutoProcScalingHasInt3VO autoProcScalingHasInt : autoProcScalingHasIntList )
    {
      AutoProcIntegrationScalingDTO autoProcIntegrationScalingDTO = new AutoProcIntegrationScalingDTO();

      autoProcIntegrationScalingDTO.setAutoProcIntegrationId( autoProcScalingHasInt.getAutoProcIntegrationVOId() );
      autoProcIntegrationScalingDTO.setAutoProcScalingId( autoProcScalingHasInt.getAutoProcScalingVOId() );
      autoProcIntegrationScalingDTO.setRowNumber( rowNumber++ );

      autoProcIntScalingDTOList.add( autoProcIntegrationScalingDTO );
    }

    return autoProcIntScalingDTOList;
  }


  /**
   * Utility method used to get a AutoProcScalingHasInt3Service instance in order to obtain AutoProcScalingHasInt3VO
   * entities from the database using the methods defined in the service.
   *
   * @return AutoProcScalingHasInt3Service - The service containing helper methods to obtain data from the database
   *
   * @throws NamingException
   */
  protected AutoProcScalingHasInt3Service getAutoProcScalingHasInt3Service() throws NamingException {
    return (AutoProcScalingHasInt3Service) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcScalingHasInt3Service.class);
  }


  /*
   * ---- Legacy endpoints below this point ----
   */

  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/plot")
	@Produces({ "application/json" })
	public Response getXScalePlotByAutoProcProgramId(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScalePlotByAutoProcProgramId";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(autoProcIntegrationListId);
			HashMap<Integer, List<HashMap<String, Object>>> result = new HashMap<Integer, List<HashMap<String, Object>>>();
			for (Integer id : ids) {
				AutoProcIntegration3VO autoProcIntegration3VO = this.getAutoProcIntegration3Service().findByPk(id);
				List<AutoProcProgramAttachment3VO> xscaleAttachmentList = this.getAutoProcProgramAttachment3Service()
						.findXScale(autoProcIntegration3VO.getAutoProcProgramVOId());

				List<HashMap<String, Object>> attachmentData = new ArrayList<HashMap<String, Object>>();
				for (AutoProcProgramAttachment3VO autoProcProgramAttachment3VO : xscaleAttachmentList) {
					HashMap<String, Object> data = AutoProcProgramaAttachmentFileReader
							.readAttachment(autoProcProgramAttachment3VO);
					attachmentData.add(data);
				}
				result.put(id, attachmentData);
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	protected AutoProcessingIntegrationService getAutoprocessingServiceBean() throws NamingException {
		return (AutoProcessingIntegrationService) Ejb3ServiceLocator.getInstance().getLocalService(AutoProcessingIntegrationService.class);
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/datacollection/{dataCollectionIdList}/view")
	@Produces({ "application/json" })
	public Response getByDatacollectionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal, @PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getByDatacollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {
			List<Integer> dataCollectionIds = this.parseToInteger(dataCollectionIdList);
			List<List<Map<String, Object>>> result = new ArrayList<List<Map<String,Object>>>();
			for (Integer dataCollectionId : dataCollectionIds) {
				result.add(this.getAutoprocessingServiceBean().getViewByDataCollectionId(this.getProposalId(proposal), dataCollectionId));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/**
	 * AutoProcProgramAttachment has not AutoProcProgramId mapped in the EJB object
	 * so it is necessary to keep separately the possible list of ids in order
	 * to identify in the client the list of files linked to a sinble autoProcProgram
	 *
	 * So, if autoprocattachmentids contains n different ids then the response will be an n-array with the list of files for each id
	 **/
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoprocprogramid/{autoprocattachmentids}/list")
	@Produces({ "application/json" })
	public Response getAttachments(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoprocattachmentids") String autoprocattachmentids) {

		String methodName = "getAttachments";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> ids = this.parseToInteger(autoprocattachmentids);
			List<List<AutoProcProgramAttachment3VO>> list = new ArrayList<List<AutoProcProgramAttachment3VO>>();
			for (Integer id : ids) {
				AutoProcProgram3VO autoProcProgram3VO = this.getAutoProcProgram3Service().findByPk(id, true);
				list.add(autoProcProgram3VO.getAttachmentListVOs());
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(list);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}




	private boolean checkProposalByAutoProcProgramId(int proposalId, int autoProcProgramId) throws NamingException, Exception{
		return this.getSession3Service().findByAutoProcProgramId(autoProcProgramId).getProposalVOId().equals(proposalId);
	}


	/**
	 * AutoProcProgramAttachment has not AutoProcProgramId mapped in the EJB object
	 * so it is necessary to keep separately the possible list of ids in order
	 * to identify in the client the list of files linked to a sinble autoProcProgram
	 *
	 * So, if autoprocattachmentids contains n different ids then the response will be an n-array with the list of files for each id
	 **/
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Produces("text/plain")
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/attachment/autoprocprogramid/{autoprocattachmentids}/download")
	public Response downloadAttachments(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("autoprocattachmentids") String autoprocattachmentids,
			@QueryParam("forceFilename") String forceFilename) {

		String methodName = "downloadAttachments";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Integer> autoProcProgramIds = this.parseToInteger(autoprocattachmentids);
			List<List<AutoProcProgramAttachment3VO>> list = new ArrayList<List<AutoProcProgramAttachment3VO>>();
			HashMap<String, String> filePaths = new HashMap<String, String>();
			String filename = "download.zip";

			for (Integer autoProcProgramId : autoProcProgramIds) {
				/** Check that id correspond to the proposal **/
				if (!this.checkProposalByAutoProcProgramId(this.getProposalId(proposal), autoProcProgramId)){
					throw new Exception(NOT_ALLOWED);
				}


				AutoProcProgram3VO autoProcProgram3VO = this.getAutoProcProgram3Service().findByPk(autoProcProgramId, true);

				/** Prefix for the name of the file and the internal structure if many results are retrieved **/
				String prefix = String.format("%s_%s", autoProcProgram3VO.getProcessingPrograms(), autoProcProgram3VO.getAutoProcProgramId());

				list.add(autoProcProgram3VO.getAttachmentListVOs());
				ArrayList<AutoProcProgramAttachment3VO> listAttachments = autoProcProgram3VO.getAttachmentListVOs();
				for (AutoProcProgramAttachment3VO auto : listAttachments) {
					String filePath = auto.getFilePath() + "/" + auto.getFileName();
					if (new File(filePath).exists()){
						if (new File(filePath).isFile()){
							if (autoProcProgramIds.size() > 1){
								String zipNameFile = prefix + "/" + auto.getFileName();
								filePaths.put(zipNameFile, filePath);
							}
							else{
								filePaths.put(auto.getFileName(), filePath);
							}
						}
					}
				}

				/** If it is a single result then filename is the name of the program and the ID **/
				if (autoProcProgramIds.size() == 1){
					filename = prefix + ".zip";
				}

				/** If forceFilename is filled then it will be used as filename **/
				if (forceFilename != null){
					if (forceFilename.length() > 0){
						filename = forceFilename;
					}
				}

			}
			this.logFinish(methodName, start, logger);
			return this.downloadFile(HashMapToZip.doZip(filePaths), filename);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}



	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/completeness")
	@Produces("text/plain")
	public Response getXScaleCompleteness(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleCompleteness";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(this.parseToInteger(autoProcIntegrationListId)).parseCompleteness();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/cc2")
	@Produces("text/plain")
	public Response getXScaleCC2(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleCC2";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parsecc2();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/isigma")
	@Produces("text/plain")
	public Response getXScaleISigma(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleISigma";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseISigma();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/rfactor")
	@Produces("text/plain")
	public Response getXScaleRfactor(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleRfactor";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseRfactor();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/sigmaano")
	@Produces("text/plain")
	public Response getXScaleSigmaAno(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleSigmaAno";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseSigmaAno();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/anomcorr")
	@Produces("text/plain")
	public Response getXScaleAnomCorr(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleAnomCorr";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseAnomCorrection();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/{autoProcIntegrationListId}/xscale/wilson")
	@Produces("text/plain")
	public Response getXScaleWilson(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcIntegrationListId") String autoProcIntegrationListId) {

		String methodName = "getXScaleWilson";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			String result = this.getAutoprocessingParserByAutoProcIntegrationListId(
					this.parseToInteger(autoProcIntegrationListId)).parseWilson();
			this.logFinish(methodName, start, logger);
			return this.sendResponse(result);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	private boolean checkProposalByAutoProcProgramAttachmentId(int proposalId, int autoProcProgramAttachmentId) throws NamingException, Exception{
		return this.getSession3Service().findByAutoProcProgramAttachmentId(autoProcProgramAttachmentId).getProposalVOId().equals(proposalId);
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/download")
	@Produces({ "application/json" })
	public Response downloadAutoProcAttachment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {

		String methodName = "downloadAutoProcAttachment";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			/** Checking that attachment is linked to the proposal **/
			if (this.checkProposalByAutoProcProgramAttachmentId(this.getProposalId(proposal), autoProcAttachmentId)){
				AutoProcProgramAttachment3VO attachment = this.getAutoProcProgramAttachment3Service().findByPk(autoProcAttachmentId);
				this.logFinish(methodName, start, logger);
				File file = new File(attachment.getFilePath() + "/" + attachment.getFileName());
				this.logFinish(methodName, start, logger);
				return this.downloadFileAsAttachment(file.getAbsolutePath());
			}
			else{
				throw new Exception(NOT_ALLOWED);
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/autoprocintegration/autoprocattachmentid/{autoProcAttachmentId}/get")
	@Produces("text/plain")
	public Response getAutoProcAttachment(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("autoProcAttachmentId") int autoProcAttachmentId) {

		String methodName = "getAutoProcAttachment";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			/** Checking that attachment is linked to the proposal **/
			if (this.checkProposalByAutoProcProgramAttachmentId(this.getProposalId(proposal), autoProcAttachmentId)){
				AutoProcProgramAttachment3VO attachment = this.getAutoProcProgramAttachment3Service().findByPk(autoProcAttachmentId);
				File file = new File(attachment.getFilePath() + "/" + attachment.getFileName());
				this.logFinish(methodName, start, logger);
				return this.downloadFile(file.getAbsolutePath());
			}
			else{
				throw new Exception(NOT_ALLOWED);
			}
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}


}
