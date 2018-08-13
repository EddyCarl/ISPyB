package ispyb.ws.rest.mx;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import javax.xml.crypto.Data;

import dls.dto.ScreeningLatticeOutputDTO;
import dls.dto.ScreeningStrategyWedgeDTO;
import dls.model.ScreeningCommentsResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
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
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No data collection records found for the input sessionId" )
    } )
  public Response retrieveDataCollections
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the session to retrieve"
      ) @PathParam( "id" ) int sessionID,
    @ApiParam
      (
        name = "threshold", example = "10",
        value = "Can be used to filter returned results so that only records numOfImages, over the threshold, are shown",
        defaultValue = "1"
      ) @QueryParam( "threshold" ) int threshold

  ) throws Exception
  {
    String methodName = "retrieveDataCollections";
    long id = this.logInit(methodName, logger, sessionID, threshold);

    if(sessionID != 1)
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input sessionId[ " + sessionID + " ] has no data collection records associated with it" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if(threshold != 1)
    {
      Map<String, Object> error = new HashMap<>();
      String errorMsg =  "No data collection records containing more than " + threshold + " images " +
                          "were found for the input sessionId[ " + sessionID + " ]";

      error.put( "error", errorMsg );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    List<Map<String, Object>> dataCollections = new ArrayList<>();
    Map<String, Object> dataCollection = new HashMap<>();

    dataCollection.put("dataCollectionId", 1);
    dataCollection.put("numberOfImages", 1);
    dataCollection.put("RNUM", 1);
    dataCollections.add(dataCollection);

    return Response.ok( dataCollections ).build();
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



  private List<Map<String, Object>> buildDummyDataCollections( int sessionId )
  {
    List<Map<String, Object>> dummyCollections = new ArrayList<>();

    for(int i = 0; i < 5; i++)
    {
      int dcId = i;
      int blSampleId = i;
      int noImgs = new Random().nextInt(11);
      int rnum = i;

      Map<String, Object> dummyCollection = buildDummyDataCollection( dcId, blSampleId, noImgs, sessionId, rnum );
      dummyCollections.add( dummyCollection );
    }

    return dummyCollections;
  }

  private Map<String, Object> buildDummyDataCollection( int dcId, int blSampleId, int numImgs, int sessionId, int rnum )
  {
    Map<String, Object> dataCollection = new HashMap<>();

    dataCollection.put( "dataCollectionId", dcId );
    dataCollection.put( "blSampleId", blSampleId );
    dataCollection.put( "startTime", "2016-04-18 11:00:00" );
    dataCollection.put( "numberOfImages", numImgs );
    dataCollection.put( "sessionId", sessionId );
    dataCollection.put( "exposureTime", "0.1" );
    dataCollection.put( "imagePrefix", "xtal1" );
    dataCollection.put( "wavelength", "0.975" );
    dataCollection.put( "resolution", "1.5" );
    dataCollection.put( "imageDirectory", "/dummy/dls/dir" );
    dataCollection.put( "comments", "dummy comment" );
    dataCollection.put( "axisStart", "0" );
    dataCollection.put( "axisEnd", "0.5" );
    dataCollection.put( "axisRange", "0.5" );
    dataCollection.put( "omegaStart", "0" );
    dataCollection.put( "overlap", "-44.5" );
    dataCollection.put( "beamSizeAtSampleX", "0.08" );
    dataCollection.put( "beamSizeAtSampleY", "0.02" );
    dataCollection.put( "transmission", "5.001" );
    dataCollection.put( "xtalsnapshotfullpath1", "/dummy/dls/snap-path/1" );
    dataCollection.put( "xtalsnapshotfullpath2", "/dummy/dls/snap-path/2" );
    dataCollection.put( "xtalsnapshotfullpath3", "/dummy/dls/snap-path/3" );
    dataCollection.put( "xtalsnapshotfullpath4", "/dummy/dls/snap-path/4" );
    dataCollection.put( "RNUM", rnum );

    return dataCollection;
  }


  /**
   * Used to retrieve a list of Data Collection entries stored in the database where the number of images column
   * in the record has a value larger than that of the set "threshold" value input via the app.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/sessions/{id}/data-collections/details" )
  @ApiOperation
    (
      value = "Retrieves a list of Data Collection entries with expanded details",
      notes = "Returns a list of Data Collection entries that are available to the user currently logged in. The " +
        "returned list will contain data collection entries with more fields of information available",
      tags = { SwaggerTagConstants.DATA_COLLECTION_TAG }, response = DataCollection3VO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No data collection records found for the input sessionId" )
    } )
  public Response retrieveDataCollectionsDetails
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the session to retrieve"
      ) @PathParam( "id" ) int sessionID

  ) throws Exception
  {
    String methodName = "retrieveDataCollectionsDetails";
    long id = this.logInit(methodName, logger, sessionID);

    if( sessionID == 1 || sessionID == 2)
    {
      return Response.ok( buildDummyDataCollections(sessionID) ).build();
    }
    else
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "No data collection entries exist for the input sessionId[" + sessionID + "]" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }
  }





  /**
   * Used to retrieve a screening output lattice for a particular data collection ID and screening output ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{dcId}/screening-output-lattice/{soId}" )
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
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening output records found for the input dataCollectionId" ),
      @ApiResponse( code = 404, message = "No screening output lattice records found for the input screeningOutputId" )
    } )
  public Response retrieveScreeningOutputLattice
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "soId", required = true, example = "5", value = "The ID of the screening output to retrieve"
      ) @PathParam( "soId" ) int screeningOutputId

  ) throws Exception
  {
    String methodName = "retrieveScreeningOutputLattice";
    long id = this.logInit(methodName, logger, dataCollectionId, screeningOutputId );

    // Retrieve the screeningOutput entity using the input screeningOutputId (finding the lattice information also)
    ScreeningOutput3VO screeningOutput3VO = this.getScreeningOutput3Service().findByPk( screeningOutputId, false, true );

    if( screeningOutput3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input screeningOutputId[" + screeningOutputId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any screeningOutputLattice entities attached to the screeningOutput entity
    List<ScreeningOutputLattice3VO> screeningOutputLatticesList = screeningOutput3VO.getScreeningOutputLatticesList();

    if( screeningOutputLatticesList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningOutputId[" + screeningOutputId + "] doesn't have " +
        "any associated screeningOutputLattice records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( screeningOutputLatticesList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningOutputId[" + screeningOutputId + "] doesn't have " +
        "any associated screeningOutputLattice records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildScreeningOutputLatticeResponse( screeningOutputId, screeningOutputLatticesList ) ).build();
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
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening strategy records found for the input dataCollectionId" ),
      @ApiResponse( code = 404, message = "No screening strategy wedge records found for the input screeningStrategyId" )
    } )
  public Response retrieveScreeningStrategyWedge
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "sswId", required = true, example = "5", value = "The ID of the screening strategy to retrieve"
      ) @PathParam( "sswId" ) int screeningStrategyId

  ) throws Exception
  {
    String methodName = "retrieveScreeningStrategyWedge";
    long id = this.logInit(methodName, logger, dataCollectionId, screeningStrategyId );

    // Retrieve the screeningOutput entity using the input screeningOutputId (finding the wedge information also)
    ScreeningStrategy3VO screeningStrategy = this.getScreeningStrategy3Service().findByPk( screeningStrategyId, true );

    if( screeningStrategy == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input screeningStrategyId[" + screeningStrategyId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any screeningStrategyWedge entities attached to the screeningStrategy entity
    List<ScreeningStrategyWedge3VO> screeningStrategyWedgeList = screeningStrategy.getScreeningStrategyWedgesList();

    if( screeningStrategyWedgeList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningStrategyId[" + screeningStrategyId + "] doesn't have " +
        "any associated screeningStrategyWedge records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( screeningStrategyWedgeList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningStrategyId[" + screeningStrategyId + "] doesn't have " +
        "any associated screeningStrategyWedge records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildScreeningStrategyWedgeResponse( screeningStrategyId, screeningStrategyWedgeList ) ).build();
  }


  /**
   * Used to retrieve a screening strategy for a particular data collection ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{dcId}/screening-strategy/{soId}" )
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
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening strategy records found for the input dataCollectionId" ),
      @ApiResponse( code = 404, message = "No screening strategy wedge records found for the input screeningStrategyId" )
    } )
  public Response retrieveScreeningStrategy
  (

    @ApiParam
      (
        name = "dcId", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "dcId" ) int dataCollectionId,

    @ApiParam
      (
        name = "soId", required = true, example = "5", value = "The ID of the screening output to retrieve"
      ) @PathParam( "soId" ) int screenOutputId

    ) throws Exception
  {
    String methodName = "retrieveScreeningStrategy";
    long id = this.logInit(methodName, logger, dataCollectionId, screenOutputId );

    if(dataCollectionId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollection ID[" + dataCollectionId + "] has no screening output records associated" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if(screenOutputId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input screenOutput ID[" + screenOutputId + "] has no screening strategy records associated" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDummyScreeningStrategyData() ).build();
  }


  private List<Map<String, Object>> buildDummyScreeningStrategyData()
  {
    List<Map<String, Object>> dummyScreeningStrategyData = new ArrayList<>();

    for( int i = 0; i < 10; i++ )
    {
      Map<String, Object> dummyScreeningStrategy = new HashMap<>();
      Random rand = new Random();

      dummyScreeningStrategy.put( "screeningOutputId", i );
      dummyScreeningStrategy.put( "screeningStrategyId", "1" );
      dummyScreeningStrategy.put( "phiStart", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "phiEnd", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "rotation", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "exposureTime", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "resolution", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "completeness", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "multiplicity", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "anomalous", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "program", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "rankingResolution", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "transmission", rand.nextInt(i + 40) + 0.5 );
      dummyScreeningStrategy.put( "RNUM", i );

      dummyScreeningStrategyData.add( dummyScreeningStrategy );
    }

    return dummyScreeningStrategyData;
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
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening comments records found for the input dataCollectionId" )
    } )
  public Response retrieveScreeningComments
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the data collection to retrieve"
      ) @PathParam( "id" ) int dataCollectionId

  ) throws Exception
  {
    String methodName = "retrieveScreeningComments";
    long id = this.logInit(methodName, logger, dataCollectionId );

    // Get a dataCollection entity by ID if available
    DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);

    if( dataCollection == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Get a list of screenings using the dataCollectionId
    List<Screening3VO> screenings = this.getScreening3Service().findFiltered( dataCollectionId );

    Map<String, Object> emptyScreeningsError = new HashMap<>();
    String errorMessage = "Unable to find any screening entities for the input dataCollectionId[" + dataCollectionId + "].";
    emptyScreeningsError.put( "error", errorMessage );

    if( screenings == null )
    {
      return Response.status(Response.Status.NOT_FOUND).entity( emptyScreeningsError ).build();
    }
    if( screenings.isEmpty() )
    {
      return Response.status(Response.Status.NOT_FOUND).entity( emptyScreeningsError ).build();
    }

    // Create the response using the data obtained
    return Response.ok( buildScreeningCommentsResponse( dataCollectionId, screenings ) ).build();
  }



  /**
   * Utility method used to build a list of ScreeningLatticeOutputDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the ScreeningOutputLattice3VO entities
   * obtained from the database.
   *
   * @param screeningOutputId - The screeningOutputId to be used in each ScreeningLatticeOutputDTO (Passed in by the user)
   * @param screeningOutputLatticesList - A list of the ScreeningOutputLattice3VO entities retrieved from the database
   *
   * @return List<ScreeningLatticeOutputDTO> - A list of the response objects holding just the relevant data
   */
  private List<ScreeningLatticeOutputDTO>
  buildScreeningOutputLatticeResponse( final int screeningOutputId,
                                       final List<ScreeningOutputLattice3VO> screeningOutputLatticesList )
  {
    List<ScreeningLatticeOutputDTO> screeningLatticeOutputDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( ScreeningOutputLattice3VO screeningOutputLattice : screeningOutputLatticesList )
    {
      ScreeningLatticeOutputDTO screeningLatticeOutputDTO = new ScreeningLatticeOutputDTO();

      screeningLatticeOutputDTO.setScreeningOutputId( screeningOutputId );
      screeningLatticeOutputDTO.setScreeningOutputLatticeId( screeningOutputLattice.getScreeningOutputLatticeId() );
      screeningLatticeOutputDTO.setSpaceGroup( screeningOutputLattice.getSpaceGroup() );
      screeningLatticeOutputDTO.setPointGroup( screeningOutputLattice.getPointGroup() );
      screeningLatticeOutputDTO.setUnitCellA( screeningOutputLattice.getUnitCell_a() );
      screeningLatticeOutputDTO.setUnitCellB( screeningOutputLattice.getUnitCell_b() );
      screeningLatticeOutputDTO.setUnitCellC( screeningOutputLattice.getUnitCell_c() );
      screeningLatticeOutputDTO.setUnitCellAlpha( screeningOutputLattice.getUnitCell_alpha() );
      screeningLatticeOutputDTO.setUnitCellBeta( screeningOutputLattice.getUnitCell_beta() );
      screeningLatticeOutputDTO.setUnitCellGamma( screeningOutputLattice.getUnitCell_gamma() );
      screeningLatticeOutputDTO.setRowNumber( rowNumber++ );

      screeningLatticeOutputDTOList.add( screeningLatticeOutputDTO );
    }

    return screeningLatticeOutputDTOList;
  }


  /**
   * Utility method used to build a list of ScreeningCommentsResponse objects for each of the returned
   * Screening entities in the database. They will hold the relevant screening data and the dataCollectionId.
   *
   * @param dataCollectionId - The dataCollectionId input by the user
   * @param screenings - The list of obtained Screening entities from the database
   *
   * @return List<ScreeningCommentsResponse> - A list of response objects built to hold the relevant data
   */
  private List<ScreeningCommentsResponse> buildScreeningCommentsResponse( final int dataCollectionId,
                                                                          final List<Screening3VO> screenings )
  {
    List<ScreeningCommentsResponse> screeningCommentsResponses = new ArrayList<>();

    /*
     * Loop through the obtained Screening entities and pull the relevant information
     * needed to create the response objects to be sent back to the user
     */
    int rowIdx = 1;
    for( Screening3VO screening : screenings )
    {
      ScreeningCommentsResponse screeningCommentsResponse = new ScreeningCommentsResponse();
      screeningCommentsResponse.setScreeningId( screening.getScreeningId() );
      screeningCommentsResponse.setDataCollectionId( dataCollectionId );
      screeningCommentsResponse.setComments( screening.getComments() );
      screeningCommentsResponse.setShortComments( screening.getShortComments() );
      screeningCommentsResponse.setRowNumber( rowIdx++ );
      screeningCommentsResponses.add( screeningCommentsResponse );
    }

    return screeningCommentsResponses;
  }


  /**
   * Utility method used to build a list of ScreeningStrategyWedgeDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the ScreeningStrategyWedge3VO entities
   * obtained from the database.
   *
   * @param screeningStrategyId - The screeningStrategyId to be used in each ScreeningStrategyWedgeDTO (Passed in by the user)
   * @param screeningStrategyWedgeList - A list of the ScreeningStrategyWedge3VO entities retrieved from the database
   *
   * @return List<ScreeningStrategyWedgeDTO> - A list of the response objects holding just the relevant data
   */
  private List<ScreeningStrategyWedgeDTO>
  buildScreeningStrategyWedgeResponse( final int screeningStrategyId,
                                       final List<ScreeningStrategyWedge3VO> screeningStrategyWedgeList )
  {
    List<ScreeningStrategyWedgeDTO> screeningStrategyWedgeDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( ScreeningStrategyWedge3VO screeningStrategyWedge : screeningStrategyWedgeList )
    {
      ScreeningStrategyWedgeDTO screeningStrategyWedgeDTO = new ScreeningStrategyWedgeDTO();

      screeningStrategyWedgeDTO.setScreeningStrategyWedgeId( screeningStrategyWedge.getScreeningStrategyWedgeId() );
      screeningStrategyWedgeDTO.setScreeningStrategyId( screeningStrategyId );
      screeningStrategyWedgeDTO.setResolution( screeningStrategyWedge.getResolution() );
      screeningStrategyWedgeDTO.setCompleteness( screeningStrategyWedge.getCompleteness() );
      screeningStrategyWedgeDTO.setMultiplicity( screeningStrategyWedge.getMultiplicity() );
      screeningStrategyWedgeDTO.setWedgeNumber( screeningStrategyWedge.getWedgeNumber() );
      screeningStrategyWedgeDTO.setDoseTotal( screeningStrategyWedge.getDoseTotal() );
      screeningStrategyWedgeDTO.setNumberOfImages( screeningStrategyWedge.getNumberOfImages() );
      screeningStrategyWedgeDTO.setRowNumber( rowNumber++ );

      screeningStrategyWedgeDTOList.add( screeningStrategyWedgeDTO );
    }

    return screeningStrategyWedgeDTOList;
  }


  /**
   * Utility method used to get a ScreeningOutput3Service instance in order to obtain ScreeningOutput3VO entities
   * from the database using the methods defined in the service.
   *
   * @return ScreeningOutput3Service - The service containing helper methods to obtain data from the database
   *
   * @throws NamingException
   */
  protected ScreeningOutput3Service getScreeningOutput3Service() throws NamingException {
    return (ScreeningOutput3Service) Ejb3ServiceLocator.getInstance().getLocalService(ScreeningOutput3Service.class);
  }


  /**
   * Utility method used to get a ScreeningStrategy3Service instance in order to obtain ScreeningStrategy3VO entities
   * from the database using the methods defined in the service.
   *
   * @return ScreeningStrategy3Service - The service containing helper methods to obtain data from the database
   *
   * @throws NamingException
   */
  protected ScreeningStrategy3Service getScreeningStrategy3Service() throws NamingException {
    return (ScreeningStrategy3Service) Ejb3ServiceLocator.getInstance().getLocalService(ScreeningStrategy3Service.class);
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
