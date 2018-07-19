package ispyb.ws.rest.mx;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

import io.swagger.annotations.Api;
import ispyb.common.util.export.ExiPdfRtfExporter;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import utils.SwaggerTagConstants;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class DataCollectionRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionRestWebService.class);


  /**
   * Used to retrieve a list of Data Collection entries stored in the database where the number of images column
   * in the record has a value larger than that of the set "threshold" value input via the app.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/sessions/{id}/data-collections" )
  @ApiOperation
    (
      value = "Retrieves a list of Data Collection entries",
      notes = "Returns a list of Data Collection entries that are available to the user currently logged in. The " +
        "returned list will only contain entries that have a number of images greater than the input " +
        "\"threshold\" query parameter.",
      tags = { SwaggerTagConstants.DATA_COLLECTION_TAG }, response = DataCollection3VO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveDataCollectionList
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the session to retrieve"
      ) @PathParam( "id" ) int sessionID,
    @ApiParam
      (
        name = "threshold", example = "10",
        value = "Can be used to filter returned results so that only records numOfImages, over the threshold, are shown"
      ) @QueryParam( "threshold" ) int threshold

  ) throws Exception
  {
    return null;
  }


  /*
   *  **** NOTE ****
   *  The data collection method above is currently in place to serve the "Find Data Collection" SQL query which
   *  simply returns a single row (Limit of 1) that contains just the Data Collection ID and the numOfImages based
   *  upon the threshold value input via query param.
   *
   *  The SQL query after this, "Get Data Collection Params for a given session" can use the same endpoint "technically",
   *  however, this query is limited to 100 rows. This query also returns a lot more fields than the previous SQL query,
   *  however, the numOfImages field is still one of them. So if we can ignore the row limit somehow, the same endpoint
   *  and method can be used for both SQL statements with just the optional additional "threshold" parameter being
   *  added if the user wishes to limit the returned rows by the numOfImages.
   */



  /**
   * Used to retrieve a screening output lattice for a particular data collection ID and screening output ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{dcId}/screening-output-lattice/{solId}" )
  @ApiOperation
    (
      value = "Retrieves a screening output lattice for a particular data collection",
      notes = "Retrieves a screening output lattice for a particular data collection",
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningOutputLattice3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveScreeningOutputLattice
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "solId", required = true, example = "5", value = "The ID of the screening output lattice to retrieve"
      ) @PathParam( "solId" ) int screenOutputLatticeId

  ) throws Exception
  {
    return null;
  }



  /**
   * Used to retrieve a screening strategy wedge for a particular data collection ID and screening strategy ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{dcId}/screening-strategy-wedge/{sswId}" )
  @ApiOperation
    (
      value = "Retrieves a screening strategy wedge for a particular data collection",
      notes = "Retrieves a screening strategy wedge for a particular data collection",
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningStrategyWedge3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveScreeningStrategyWedge
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "sswId", required = true, example = "5", value = "The ID of the screening strategy wedge to retrieve"
      ) @PathParam( "sswId" ) int screenStratWedgeId

  ) throws Exception
  {
    return null;
  }



  /**
   * Used to retrieve a screening strategy for a particular data collection ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{dcId}/screening-strategy/{ssId}" )
  @ApiOperation
    (
      value = "Retrieves a screening strategy for a particular data collection",
      notes = "Retrieves a screening strategy that is associated with the input data collection ID",
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningStrategy3VO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveScreeningStrategy
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "ssId", required = true, example = "5", value = "The ID of the screening strategy to retrieve"
      ) @PathParam( "ssId" ) int screenStratId

    ) throws Exception
  {
    return null;
  }




  /**
   * Used to retrieve a the comments on screenings for a particular data-collection ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{id}/screening-comments" )
  @ApiOperation
    (
      value = "Retrieves all comments on screenings for a particular data collection",
      notes = "Retrieves the short and long comments that have been added to all screenings that are associated with " +
              "the input data collection ID",
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = Screening3VO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveScreeningComments
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "id" ) int sessionID

  ) throws Exception
  {
    return null;
  }





  /*
   * ---- Legacy endpoints below this point ----
   */

  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionById(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getDataCollectionById";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {

			List<Integer> ids = this.parseToInteger(dataCollectionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				int propId = this.getProposalId(proposal);
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByDataCollectionId(
						propId, id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}



	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/wilson")
	@Produces("image/png")
	public Response getWilsonPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getWilsonPlot";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getBestWilsonPlotPath());
			}

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/qualityindicatorplot")
	@Produces("image/png")
	public Response getQualityIndicatorsPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getImageQualityIndicatorsPlotPath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@FormParam("comments") String comments) {

		String methodName = "saveDataCollectionComments";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId, comments);

		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			dataCollection.setComments(comments);
			this.getDataCollection3Service().update(dataCollection);

		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse(true);
	}




	@Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/qualityindicatorcsv")
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Produces("text/plain")
	public Response getCSVFile(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getQualityIndicatorsCSV";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				if (dataCollection.getImageQualityIndicatorsCSVPath() != null) {
					/** Converting to csv **/
					if (new File(dataCollection.getImageQualityIndicatorsCSVPath()).exists()) {
						this.logFinish(methodName, id, logger);
						return this.sendResponse(new String(
								Files.readAllBytes(Paths.get(dataCollection.getImageQualityIndicatorsCSVPath()))));
					}
				}

			}

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return null;

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/crystalsnaphot/{id}/get")
	@Produces("image/png")
	public Response getCrystalSnapshot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("id") int id) {

//		String methodName = "getCrystalSnapshot";
//		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId, id);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
//			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				if (id == 1) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath1());
				}
				if (id == 2) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath2());
				}
				if (id == 3) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath3());
				}
				if (id == 4) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath4());
				}
			}

		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
			e.printStackTrace();
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionIdList}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("sessionIdList") String sessionIdList) {

		String methodName = "getDataCollectionNativelyBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionIdList);
		try {
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(
						this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsAnalysisReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionAnalysisReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getAnalysisPdfRtf(sessionId, proposal, nbRows, false);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getAnalysisPdfRtf(sessionId, proposal, nbRows, true);
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}


	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflowstep/{workflowstepId}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByWorkflowStepId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowstepId") String workflowstepId) {

		String methodName = "getViewDataCollectionByWorkflowStepId";
		long start = this.logInit(methodName, logger, token, proposal, workflowstepId);
		try {
			List<Integer> ids = this.parseToInteger(workflowstepId);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByWorkflowId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/datacollectiongroupid/{datacollectiongroupids}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("datacollectiongroupids") String datacollectiongroupids) {

		String methodName = "getViewDataCollectionByWorkflowStepId";
		long start = this.logInit(methodName, logger, token, proposal, datacollectiongroupids);
		try {
			List<Integer> ids = this.parseToInteger(datacollectiongroupids);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getDataCollectionByDataCollectionGroupId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflow/{workflowIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionsByWorkflowId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowIdList") String workflowIdList) {

		String methodName = "getDataCollectionsByWorkflowId";
		long start = this.logInit(methodName, logger, token, proposal, workflowIdList);
		try {
			List<Integer> ids = this.parseToInteger(workflowIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getViewDataCollectionsByWorkflowId(
						this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}


	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@GZIP
	@Path("{token}/proposal/{proposal}/mx/datacollection/protein_acronym/{protein_acronyms}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByProteinAcronym(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("protein_acronyms") String proteinAcronyms) {

		String methodName = "getViewDataCollectionByProteinAcronym";
		long start = this.logInit(methodName, logger, token, proposal, proteinAcronyms);
		try {
			List<String> acronyms = this.parseToString(proteinAcronyms);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (String acronym : acronyms) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByProteinAcronym(
						this.getProposalId(proposal), acronym));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	private byte [] getPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf) throws NamingException, Exception {

		Integer id = new Integer(sessionId);

		List<Map<String, Object>> dataCollections =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(this.getProposalId(proposal), id);

		Integer nbRowsMax = dataCollections.size();

		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = new Integer(nbRows);
		}

		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, nbRowsMax);

		byte [] byteToExport = pdf.exportDataCollectionReport(isRtf).toByteArray();

		return byteToExport;
	}

	private byte [] getAnalysisPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf) throws NamingException, Exception {

		Integer id = new Integer(sessionId);

		List<Map<String, Object>> dataCollections =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(this.getProposalId(proposal), id);

		Integer nbRowsMax = dataCollections.size();

		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = new Integer(nbRows);
		}

		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, nbRowsMax);

		byte [] byteToExport = pdf.exportDataCollectionAnalysisReport(isRtf).toByteArray();

		return byteToExport;
	}
}
