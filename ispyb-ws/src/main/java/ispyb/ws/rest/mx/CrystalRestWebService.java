package ispyb.ws.rest.mx;

import dls.model.CrystalSnapshotPaths;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import org.apache.log4j.Logger;
import utils.SwaggerTagConstants;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class CrystalRestWebService extends MXRestWebService
{
  private final static Logger logger = Logger.getLogger(CrystalRestWebService.class);

  /**
   * Used to retrieve the crystal snapshot image paths for a given data collection ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{id}/crystal-snapshot-paths" )
  @ApiOperation
    (
      value = "Retrieve the crystal snapshot image paths.",
      notes = "Retrieve the crystal snapshot image paths for a particular data-collection specified by the input ID.",
      tags = { SwaggerTagConstants.CRYSTAL_SNAPSHOT_TAG },
      response = CrystalSnapshotPaths.class, authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No Crystal Snapshot Path records found for the input dataCollectionId" )
    } )
  public Response retrieveCrystalSnapshotPaths
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The Id of the data-collection to retrieve"
      ) @PathParam( "id" ) int dataCollectionId

  ) throws Exception
  {
    String methodName = "retrieveCrystalSnapshotPaths";
    long id = this.logInit(methodName, logger, dataCollectionId );

    // * CE * Need to check the auth token here before getting anything...
    //        The DataCollectionId must belong to a users sessions.

    // Get a dataCollection entity by ID if available
    DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);

    if( dataCollection == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollectionId[" + dataCollectionId + "] could not be found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Create the response using the snapshot paths from the obtained dataCollection entity
    return Response.ok( buildCrystalSnapshotPathResponse( dataCollection ) ).build();
  }







  /**
   * Used to retrieve the crystal snapshot images for a given data collection ID.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/data-collections/{id}/crystal-snapshot-images" )
  @ApiOperation
    (
      value = "Retrieve the crystal snapshot images.",
      notes = "Retrieve the crystal snapshot images for a particular data-collection specified by the input ID.",
      tags = { SwaggerTagConstants.CRYSTAL_SNAPSHOT_TAG }, response = Crystal3VO.class,
      responseContainer = "List", authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" ),
      @ApiResponse( code = 404, message = "No Crystal Snapshot Images found for the input dataCollectionId" )
    } )
  public Response retrieveCrystalSnapshotImages
  (

    @ApiParam
      (
        name = "id", required = true, example = "12", value = "The ID of the data-collection to retrieve"
      ) @PathParam( "id" ) int dataCollectionId

  ) throws Exception
  {
    String methodName = "retrieveCrystalSnapshotImages";
    long id = this.logInit(methodName, logger, dataCollectionId );

    if(dataCollectionId != 1)
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "The input dataCollection ID[" + dataCollectionId + "] has no crystal snapshot images associated" );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    return Response.ok( buildDummyCrystalSnapshotImageData() ).build();
  }


  private List<Map<String, Object>> buildDummyCrystalSnapshotImageData()
  {
    List<Map<String, Object>> dummyCrystalSnapshotImageData = new ArrayList<>();

    for( int i = 0; i < 10; i++ )
    {
      Map<String, Object> dummyCrystalSnapshotImage = new HashMap<>();
      Random rand = new Random();

      dummyCrystalSnapshotImage.put( "imageId", i );
      dummyCrystalSnapshotImage.put( "imageNumber", i );
      dummyCrystalSnapshotImage.put( "fileName", "/dls/dummy/crystal/image/" + i + ".jpg" );
      dummyCrystalSnapshotImage.put( "temperature", rand.nextInt( ( i + 20 ) ) );
      dummyCrystalSnapshotImage.put( "measuredIntensity", rand.nextInt( ( i + 20 ) ) );
      dummyCrystalSnapshotImage.put( "synchrotronCurrent", rand.nextInt( ( i + 20 ) ) );
      dummyCrystalSnapshotImage.put( "comments", "Dummy Comment " + i );
      dummyCrystalSnapshotImage.put( "RNUM", i );

      dummyCrystalSnapshotImageData.add( dummyCrystalSnapshotImage );
    }

    return dummyCrystalSnapshotImageData;
  }



  /**
   * Utility method used to build a CrystalSnapshotPaths object which will hold the relevant
   * data that is taken from the DataCollection entity obtained from the database.
   *
   * @param dataCollection - The obtained dataCollection entity from the database
   *
   * @return CrystalSnapshotPaths crystalSnapshotPaths - The response object built to hold the data
   */
  private CrystalSnapshotPaths buildCrystalSnapshotPathResponse( final DataCollection3VO dataCollection )
  {
    CrystalSnapshotPaths crystalSnapshotPaths = new CrystalSnapshotPaths();
    crystalSnapshotPaths.setCrystalSnapshotFullPathOne( dataCollection.getXtalSnapshotFullPath1() );
    crystalSnapshotPaths.setCrystalSnapshotFullPathTwo( dataCollection.getXtalSnapshotFullPath2() );
    crystalSnapshotPaths.setCrystalSnapshotFullPathThree( dataCollection.getXtalSnapshotFullPath3() );
    crystalSnapshotPaths.setCrystalSnapshotFullPathFour( dataCollection.getXtalSnapshotFullPath4() );
    crystalSnapshotPaths.setRowNumber( 1 ); // Only returning a single entry (Not sure if this ought to be changed?)
    return crystalSnapshotPaths;
  }


  /*
   * ---- Legacy endpoints below this point ----
   */
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/datacollection/{dataCollectionId}/pdb/download")
	@Produces("text/plain")
	public Response downloadPdbByDataCollectionId(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "downloadPdbByDataCollectionId";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			String filePath = this.getDataCollection3Service().findPdbFullPath(dataCollectionId);
			this.logFinish(methodName, start, logger);
			if (filePath != null){
				if (new File(filePath).exists()){
					return this.downloadFileAsAttachment(filePath);
				}
			}
			throw new Exception("File " + filePath + " does not exit");
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}


	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/{crystalId}/get")
	@Produces({ "application/json" })
	public Response getCrystalById(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("crystalId") int crystalId) {

		String methodName = "getCrystalById";
		long start = this.logInit(methodName, logger, token, crystalId);
		try {
			return this.sendResponse(this.getCrystal3Service().findByPk(crystalId, true));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/list")
	@Produces({ "application/json" })
	public Response getCrystalList(@PathParam("token") String token,@PathParam("proposal") String proposal) {

		String methodName = "getCrystalById";
		long start = this.logInit(methodName, logger, token, proposal);
		try {
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));
			return this.sendResponse(crystals);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/crystal/geometryclass/{spacegroup}/list")
	@Produces({ "application/json" })
	public Response getGeometryClassBySpaceGroup(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("spacegroup") String spaceGroup) {

		String methodName = "getGeometryClassBySpaceGroup";
		long start = this.logInit(methodName, logger, token, spaceGroup);
		try {
			List<SpaceGroup3VO> res = this.getSpaceGroup3Service().findBySpaceGroupShortName(spaceGroup);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(res);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/crystal/proteinid/{proteinId}/save")
	@Produces({ "application/json" })
  @Consumes("multipart/form-data")
	public Response saveCrystalForm(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("proteinId") Integer proteinId,
			@FormParam("crystalId") String stringCrystalId,
			@FormParam("name") String name,
			@FormParam("spaceGroup") String spaceGroup,
			@FormParam("cellA") Double cellA,
			@FormParam("cellB") Double cellB,
			@FormParam("cellC") Double cellC,
			@FormParam("cellAlpha") Double cellAlpha,
			@FormParam("cellBeta") Double cellBeta,
			@FormParam("cellGamma") Double cellGamma,
			@FormParam("comments") String comments

			) {

		String methodName = "saveCrystalForm";
		long start = this.logInit(methodName, logger, token, spaceGroup);
		try {
			List<Crystal3VO> crystalForms = this.getCrystal3Service().findByProposalId(this.getProposalId(proposal));

			Crystal3VO crystal = new Crystal3VO();

			Integer crystalId = null;
			try{
				crystalId = Integer.valueOf(stringCrystalId);
			}
			catch(Exception e){
				crystalId = null;
			}

			if (crystalId != null){
				/** Update **/
				for (Crystal3VO crystal3vo : crystalForms) {
					if (crystal3vo.getCrystalId().equals(crystalId)){
						crystal = crystal3vo;
					}
				}
			}

			if (proteinId == null){
				throw new Exception("ProteinId must not be null");
			}
			else{
				crystal.setComments(comments);
				crystal.setName(name);
				crystal.setSpaceGroup(spaceGroup);
				crystal.setCellA(cellA);
				crystal.setCellB(cellB);
				crystal.setCellC(cellC);
				crystal.setCellAlpha(cellAlpha);
				crystal.setCellBeta(cellBeta);
				crystal.setCellGamma(cellGamma);

				/** We only update if the protein is in the list of the proteins of the proposal **/
				List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(this.getProposalId(proposal));
				for (Protein3VO protein3vo : proteins) {
					if (protein3vo.getProteinId().equals(proteinId)){
						crystal.setProteinVO(protein3vo);
						/** Merge that creates and updates **/
						logger.info("Updating crystal form");
						crystal = this.getCrystal3Service().update(crystal);
						this.logFinish(methodName, start, logger);
						return this.sendResponse(crystal);
					}
				}

			}


		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}


}
