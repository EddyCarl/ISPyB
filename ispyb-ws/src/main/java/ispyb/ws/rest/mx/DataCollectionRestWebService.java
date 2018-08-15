package ispyb.ws.rest.mx;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

import dls.dto.DataCollectionDTO;
import dls.dto.DetailedDataCollectionDTO;
import dls.dto.ScreeningCommentsDTO;
import dls.dto.ScreeningLatticeOutputDTO;
import dls.dto.ScreeningStrategyDTO;
import dls.dto.ScreeningStrategyWedgeDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
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
      tags = { SwaggerTagConstants.DATA_COLLECTION_TAG }, response = DataCollectionDTO.class, responseContainer = "List",
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
      ) @PathParam( "id" ) int sessionId,
    @ApiParam
      (
        name = "threshold", example = "10",
        value = "Can be used to filter returned results so that only records numOfImages, over the threshold, are shown",
        defaultValue = "1"
      ) @QueryParam( "threshold" ) int threshold

  ) throws Exception
  {
    String methodName = "retrieveDataCollections";
    long id = this.logInit(methodName, logger, sessionId, threshold);

    // Retrieve the session entity using the input sessionId (finding the dataCollectionGroup entities also)
    Session3VO session3VO = this.getSession3Service().findByPk( sessionId, true, false, false );

    if( session3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any dataCollectionGroup entities attached to the session entity
    List<DataCollectionGroup3VO> dataCollectionGroupsList = session3VO.getDataCollectionGroupsList();

    if( dataCollectionGroupsList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input sessionId[" + sessionId + "] doesn't have " +
        "any associated dataCollectionGroup records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( dataCollectionGroupsList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input sessionId[" + sessionId + "] doesn't have " +
        "any associated dataCollectionGroup records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDataCollectionResponse( dataCollectionGroupsList ) ).build();
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
      tags = { SwaggerTagConstants.DATA_COLLECTION_TAG }, response = DetailedDataCollectionDTO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
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
      ) @PathParam( "id" ) int sessionId

  ) throws Exception
  {
    String methodName = "retrieveDataCollectionsDetails";
    long id = this.logInit(methodName, logger, sessionId);

    // Retrieve the session entity using the input sessionId (finding the dataCollectionGroup entities also)
    Session3VO session3VO = this.getSession3Service().findByPk( sessionId, true, false, false );

    if( session3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input sessionId[" + sessionId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any dataCollectionGroup entities attached to the session entity
    List<DataCollectionGroup3VO> dataCollectionGroupsList = session3VO.getDataCollectionGroupsList();

    if( dataCollectionGroupsList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input sessionId[" + sessionId + "] doesn't have " +
        "any associated dataCollectionGroup records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( dataCollectionGroupsList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input sessionId[" + sessionId + "] doesn't have " +
        "any associated dataCollectionGroup records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDetailedDataCollectionResponse( sessionId, dataCollectionGroupsList ) ).build();
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
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningLatticeOutputDTO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening outpu t records found for the input dataCollectionId" ),
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
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningStrategyWedgeDTO.class,
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
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningStrategyDTO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No screening strategy records found for the input dataCollectionId" )
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
      ) @PathParam( "soId" ) int screeningOutputId

    ) throws Exception
  {
    String methodName = "retrieveScreeningStrategy";
    long id = this.logInit(methodName, logger, dataCollectionId, screeningOutputId );

    // Retrieve the screeningOutput entity using the input screeningOutputId (finding the lattice information also)
    ScreeningOutput3VO screeningOutput3VO = this.getScreeningOutput3Service().findByPk( screeningOutputId, true, false );

    if( screeningOutput3VO == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input screeningOutputId[" + screeningOutputId + "] could not be found in the database" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Retrieve any screeningStrategy entities attached to the screeningOutput entity
    List<ScreeningStrategy3VO> screeningStrategyList = screeningOutput3VO.getScreeningStrategysList();

    if( screeningStrategyList == null )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningOutputId[" + screeningOutputId + "] doesn't have " +
        "any associated screeningStrategy records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( screeningStrategyList.isEmpty() )
    {
      Map<String, String> error = new HashMap<>();
      String errorMessage = "The input screeningOutputId[" + screeningOutputId + "] doesn't have " +
        "any associated screeningStrategy records";
      error.put( "error", errorMessage );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildScreeningStrategyResponse( screeningOutputId, screeningStrategyList ) ).build();
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
      tags = { SwaggerTagConstants.SCREENING_TAG }, response = ScreeningCommentsDTO.class, responseContainer = "List",
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

    System.out.println( " --- Retrieve screening comments --- ");
    System.out.println( "Input dataCollectionId : " + dataCollectionId );


    // Get a dataCollection entity by ID if available
    DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);

    if( dataCollection == null )
    {
      System.out.println( "The retrieved dataCollection was null ");
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    System.out.println( "The retrieved dataCollection was non-null. Progress to obotain screenings.");


    DataCollectionGroup3VO dataCollectionGroup3VO = dataCollection.getDataCollectionGroupVO();

    if( dataCollectionGroup3VO == null )
    {
      System.out.println( "The retrieved dataCollectionGroup3VO was null ");
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    System.out.println( "The retrieved dataCollectionGroup3VO was non-null. Progress to obotain screenings.");


    List<Screening3VO> screenings = dataCollectionGroup3VO.getScreeningsList();

    if( screenings == null )
    {
      System.out.println( "The retrieved screenings was null ");
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( screenings.isEmpty() )
    {
      System.out.println( "The retrieved screenings was null ");
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    System.out.println( "The retrieved screenings was non-null. Size: " + screenings.size() );

    for( Screening3VO screening3VO : screenings )
    {
      System.out.println(" === New screening details === ");
      System.out.println( screening3VO.getScreeningId() );
      System.out.println( screening3VO.getComments() );
      System.out.println( screening3VO.getShortComments() );
    }

    return Response.status(Response.Status.NOT_FOUND).entity( "SCREENING DUMMMY" ).build();


//    // Get a list of screenings using the dataCollectionId
//    List<Screening3VO> screenings = this.getScreening3Service().findFiltered( dataCollectionId );
//
//    Map<String, Object> emptyScreeningsError = new HashMap<>();
//    String errorMessage = "Unable to find any screening entities for the input dataCollectionId[" + dataCollectionId + "].";
//    emptyScreeningsError.put( "error", errorMessage );
//
//    if( screenings == null )
//    {
//      return Response.status(Response.Status.NOT_FOUND).entity( emptyScreeningsError ).build();
//    }
//    if( screenings.isEmpty() )
//    {
//      return Response.status(Response.Status.NOT_FOUND).entity( emptyScreeningsError ).build();
//    }
//
//    // Create the response using the data obtained
//    return Response.ok( buildScreeningCommentsResponse( dataCollectionId, screenings ) ).build();
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
   * Utility method used to build a list of ScreeningCommentsDTO objects for each of the returned
   * Screening entities in the database. They will hold the relevant screening data and the dataCollectionId.
   *
   * @param dataCollectionId - The dataCollectionId input by the user
   * @param screenings - The list of obtained Screening entities from the database
   *
   * @return List<ScreeningCommentsDTO> - A list of response objects built to hold the relevant data
   */
  private List<ScreeningCommentsDTO> buildScreeningCommentsResponse( final int dataCollectionId,
                                                                     final List<Screening3VO> screenings )
  {
    List<ScreeningCommentsDTO> screeningCommentsResponses = new ArrayList<>();

    /*
     * Loop through the obtained Screening entities and pull the relevant information
     * needed to create the response objects to be sent back to the user
     */
    int rowIdx = 1;
    for( Screening3VO screening : screenings )
    {
      ScreeningCommentsDTO screeningCommentsResponse = new ScreeningCommentsDTO();
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
   * Utility method used to build a list of ScreeningStrategyDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the ScreeningStrategy3VO entities
   * obtained from the database.
   *
   * @param screeningOutputId - The screeningOutputId to be used in each ScreeningStrategyDTO (Passed in by the user)
   * @param screeningStrategyList - A list of the ScreeningStrategy3VO entities retrieved from the database
   *
   * @return List<ScreeningStrategyDTO> - A list of the response objects holding just the relevant data
   */
  private List<ScreeningStrategyDTO>
  buildScreeningStrategyResponse( final int screeningOutputId,
                                       final List<ScreeningStrategy3VO> screeningStrategyList )
  {
    List<ScreeningStrategyDTO> screeningStrategyDTOList = new ArrayList<>();

    int rowNumber = 1;
    for( ScreeningStrategy3VO screeningStrategy : screeningStrategyList )
    {
      ScreeningStrategyDTO screeningStrategyDTO = new ScreeningStrategyDTO();

      screeningStrategyDTO.setScreeningOutputId( screeningOutputId );
      screeningStrategyDTO.setScreeningStrategyId( screeningStrategy.getScreeningStrategyId() );
      screeningStrategyDTO.setPhiStart( screeningStrategy.getPhiStart() );
      screeningStrategyDTO.setPhiEnd( screeningStrategy.getPhiEnd() );
      screeningStrategyDTO.setRotation( screeningStrategy.getRotation() );
      screeningStrategyDTO.setExposureTime( screeningStrategy.getExposureTime() );
      screeningStrategyDTO.setResolution( screeningStrategy.getResolution() );
      screeningStrategyDTO.setCompleteness( screeningStrategy.getCompleteness() );
      screeningStrategyDTO.setMultiplicity( screeningStrategy.getMultiplicity() );
      screeningStrategyDTO.setAnomalous( screeningStrategy.getAnomalous() );
      screeningStrategyDTO.setProgram( screeningStrategy.getProgram() );
      screeningStrategyDTO.setRankingResolution( screeningStrategy.getRankingResolution() );
      screeningStrategyDTO.setTransmission( screeningStrategy.getTransmission() );
      screeningStrategyDTO.setRowNumber( rowNumber++ );

      screeningStrategyDTOList.add( screeningStrategyDTO );
    }

    return screeningStrategyDTOList;
  }


  /**
   * Utility method used to build a list of DataCollectionDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the DataCollection3VO entities
   * obtained from the database (which in turn are retrieved from the DataCollectionGroup3VO entities).
   *
   * @param dataCollectionGroupList - A list of the DataCollectionGroup3VO entities retrieved from the database
   *
   * @return List<DataCollectionDTO> - A list of the response objects holding just the relevant data
   */
  private List<DataCollectionDTO>
  buildDataCollectionResponse( final List<DataCollectionGroup3VO> dataCollectionGroupList )
  {
    List<DataCollectionDTO> dataCollectionDTOList = new ArrayList<>();

    for( DataCollectionGroup3VO dataCollectionGroup3VO : dataCollectionGroupList )
    {
      List<DataCollection3VO> dataCollection3VOList = dataCollectionGroup3VO.getDataCollectionsList();

      if( dataCollection3VOList != null )
      {
        int rowNumber = 1;
        for( DataCollection3VO dataCollection : dataCollection3VOList )
        {
          DataCollectionDTO dataCollectionDTO = new DataCollectionDTO();

          dataCollectionDTO.setDataCollectionId( dataCollection.getDataCollectionId() );
          dataCollectionDTO.setNumberOfImages( dataCollection.getNumberOfImages() );
          dataCollectionDTO.setRowNumber( rowNumber++ );

          dataCollectionDTOList.add( dataCollectionDTO );
        }
      }
    }

    return dataCollectionDTOList;
  }


  /**
   * Utility method used to build a list of DetailedDataCollectionDTO objects which hold the relevant data
   * required for the response. Each object is populated with data retrieved from the DataCollection3VO entities
   * obtained from the database (which in turn are retrieved from the DataCollectionGroup3VO entities).
   *
   * @param dataCollectionGroupList - A list of the DataCollectionGroup3VO entities retrieved from the database
   *
   * @return List<DetailedDataCollectionDTO> - A list of the response objects holding just the relevant data
   */
  private List<DetailedDataCollectionDTO>
  buildDetailedDataCollectionResponse( final int sessionId,
                                       final List<DataCollectionGroup3VO> dataCollectionGroupList )
  {
    List<DetailedDataCollectionDTO> detailedDataCollectionDTOList = new ArrayList<>();

    for( DataCollectionGroup3VO dataCollectionGroup3VO : dataCollectionGroupList )
    {
      List<DataCollection3VO> dataCollection3VOList = dataCollectionGroup3VO.getDataCollectionsList();

      if( dataCollection3VOList != null )
      {
        int rowNumber = 1;
        for( DataCollection3VO dataCollection : dataCollection3VOList )
        {
          DetailedDataCollectionDTO detailedDataCollectionDTO = new DetailedDataCollectionDTO();

          detailedDataCollectionDTO.setDataCollectionId( dataCollection.getDataCollectionId() );
          detailedDataCollectionDTO.setBlSampleId( getBlSampleId( dataCollection ) );
          detailedDataCollectionDTO.setStartTime( dataCollection.getStartTime() );
          detailedDataCollectionDTO.setNumberOfImages( dataCollection.getNumberOfImages() );
          detailedDataCollectionDTO.setSessionId( sessionId );
          detailedDataCollectionDTO.setExposureTime( dataCollection.getExposureTime() );
          detailedDataCollectionDTO.setImagePrefix( dataCollection.getImagePrefix() );
          detailedDataCollectionDTO.setWavelength( dataCollection.getWavelength() );
          detailedDataCollectionDTO.setResolution( dataCollection.getResolution() );
          detailedDataCollectionDTO.setImageDirectory( dataCollection.getImageDirectory() );
          detailedDataCollectionDTO.setComments( dataCollection.getComments() );
          detailedDataCollectionDTO.setAxisStart( dataCollection.getAxisStart() );
          detailedDataCollectionDTO.setAxisEnd( dataCollection.getAxisEnd() );
          detailedDataCollectionDTO.setAxisRange( dataCollection.getAxisRange() );
          detailedDataCollectionDTO.setOmegaStart( dataCollection.getOmegaStart() );
          detailedDataCollectionDTO.setOverlap( dataCollection.getOverlap() );
          detailedDataCollectionDTO.setBeamSizeAtSampleX( dataCollection.getBeamSizeAtSampleX() );
          detailedDataCollectionDTO.setBeamSizeAtSampleY( dataCollection.getBeamSizeAtSampleY() );
          detailedDataCollectionDTO.setTransmission( dataCollection.getTransmission() );
          detailedDataCollectionDTO.setXtalSnapshotFullPath1( dataCollection.getXtalSnapshotFullPath1() );
          detailedDataCollectionDTO.setXtalSnapshotFullPath2( dataCollection.getXtalSnapshotFullPath2() );
          detailedDataCollectionDTO.setXtalSnapshotFullPath3( dataCollection.getXtalSnapshotFullPath3() );
          detailedDataCollectionDTO.setXtalSnapshotFullPath4( dataCollection.getXtalSnapshotFullPath4() );
          detailedDataCollectionDTO.setRowNumber( rowNumber++ );

          detailedDataCollectionDTOList.add( detailedDataCollectionDTO );
        }
      }
    }

    return detailedDataCollectionDTOList;
  }


  /**
   * Utility method used to return the id of a BLSample entity. The BLSample is nested within a BLSubSample which is
   * again nested within a DataCollection. A null value is returned if there is an issue obtaining the BLSampleId.
   *
   * @param dataCollection3VO - The DataCollection holding the BLSubSample (which holds a BLSample)
   *
   * @return blSampleId - An integer ID representing the blSample
   */
  private Integer getBlSampleId( final DataCollection3VO dataCollection3VO )
  {
    BLSubSample3VO blSubSample3VO = dataCollection3VO.getBlSubSampleVO();
    if( blSubSample3VO == null )
    {
      return null;
    }

    BLSample3VO blSample3VO = blSubSample3VO.getBlSampleVO();
    if( blSample3VO == null )
    {
      return null;
    }

    return blSample3VO.getBlSampleId();
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
