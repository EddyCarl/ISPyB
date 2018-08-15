package ispyb.ws.rest.proposal;

import dls.dto.SessionListDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import ispyb.server.common.services.ws.rest.session.SessionService;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

import io.swagger.annotations.Api;
import utils.SwaggerTagConstants;

@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class SessionRestWebService extends RestWebService
{
  private final static Logger logger = Logger.getLogger(SessionRestWebService.class);


  /**
   * Used to retrieve a full list of sessions stored in the database, that are available to the user currently
   * logged into the system.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/sessions" )
  @ApiOperation
    (
      value = "Retrieve a list of sessions",
      notes = "Returns a list of sessions that are available to the user currently logged in.",
      tags = { SwaggerTagConstants.SESSION_TAG }, response = SessionListDTO.class, responseContainer = "List",
      authorizations = @Authorization( "apiKeyAuth" )
    )
  @Produces({ "application/json" })
  @ApiResponses
    ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
    } )
  public Response retrieveSessions() throws Exception
  {
    String methodName = "retrieveSessions";
    long id = this.logInit(methodName, logger );

    // * CE * Need to check the auth token here before getting anything...
    //        The DataCollectionId must belong to a users sessions.

    // Retrieve a list of sessions if possible
    List<Session3VO> sessions = this.getSession3Service().findAllOrderedByDate( false, false, false );

    if( sessions == null )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "There were no sessions found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    if( sessions.isEmpty() )
    {
      Map<String, Object> error = new HashMap<>();
      error.put( "error", "There were no sessions found in the database." );
      return Response.status(Response.Status.NOT_FOUND).entity( error ).build();
    }

    // Create the response using the snapshot paths from the obtained dataCollection entity
    return Response.ok( buildSessionListDTOs( sessions ) ).build();
  }


  /**
   * Utility method used to build a list of SessionListDTO objects which will hold the relevant
   * data that is taken from the Session3VO entities retrieved from the database.
   *
   * @param sessions - The obtained list of Session3VO entities from the database
   *
   * @return List<SessionListDTO> - A list of the response objects to hold the data
   */
  private List<SessionListDTO> buildSessionListDTOs( List<Session3VO> sessions )
  {
    List<SessionListDTO> SessionListDTOs = new ArrayList<>();

    int rowNumber = 1;
    for( Session3VO session3VO : sessions )
    {
      SessionListDTO SessionListDTO = new SessionListDTO();

      SessionListDTO.setSessionId( session3VO.getSessionId() );
      SessionListDTO.setProposalId( session3VO.getProposalVOId() );
      SessionListDTO.setStartDate( session3VO.getStartDate() );
      SessionListDTO.setBeamlineName( session3VO.getBeamlineName() );
      SessionListDTO.setBeamLineOperator( session3VO.getBeamlineOperator() );
      SessionListDTO.setProjectCode( session3VO.getProjectCode() );
      SessionListDTO.setVisitNumber( session3VO.getVisit_number() );
      SessionListDTO.setRowNumber( rowNumber++ );

      SessionListDTOs.add( SessionListDTO );
    }

    return SessionListDTOs;
  }


  /*
   * ---- Legacy endpoints below this point ----
   */
  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
  @POST
  @Path("{token}/proposal/{proposal}/mx/session/{sessionId}/comments/save")
  @Produces("image/png")
  @Consumes("multipart/form-data")
  public Response saveSessionComments(
      @PathParam("token") String token,
      @PathParam("proposal") String proposal,
      @PathParam("sessionId") int sessionId,
      @FormParam("comments") String comments) {

    String methodName = "saveSessionComments";
    long id = this.logInit(methodName, logger, token, proposal, sessionId, comments);

    try {
      Session3VO session = this.getSession3Service().findByPk(sessionId, false, false, false);
      session.setComments(comments);
      this.getSession3Service().update(session);
    } catch (Exception e) {
      e.printStackTrace();
      return this.logError(methodName, e, id, logger);
    }
    return this.sendResponse(true);
  }

  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
  @GET
  @GZIP
  @Path("{token}/proposal/{proposal}/session/list")
  @Produces({ "application/json" })
  public Response getSessionByProposalId(@PathParam("token") String token, @PathParam("proposal") String proposal) throws Exception {
    String methodName = "getSessionList";
    long id = this.logInit(methodName, logger, token, proposal);
    try {
      List<Map<String, Object>> result = getSessionService().getSessionViewByProposalId(this.getProposalId(proposal));
      this.logFinish(methodName, id, logger);
      return sendResponse(result);
    } catch (Exception e) {
      return this.logError(methodName, e, id, logger);
    }
  }

  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
  @GET
  @GZIP
  @Path("{token}/proposal/{proposal}/session/sessionId/{sessionId}/list")
  @Produces({ "application/json" })
  public Response getSessionById(@PathParam("token") String token, @PathParam("proposal") String proposal,
      @PathParam("sessionId") int sessionId) throws Exception {
    String methodName = "getSessionById";
    long id = this.logInit(methodName, logger, token, proposal, sessionId);
    try {
      List<Map<String, Object>> result = getSessionService().getSessionViewBySessionId(this.getProposalId(proposal), sessionId);
      this.logFinish(methodName, id, logger);
      return sendResponse(result);
    } catch (Exception e) {
      return this.logError(methodName, e, id, logger);
    }
  }

  /**
   * Returns the session list that will take place between start and end date
   *
   * @param token
   * @param proposal name of the proposal
   * @param start format is YYYYMMDD
   * @param end format is YYYYMMDD
   * @return
   * @throws Exception
   */
  @RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
  @GET
  @GZIP
  @Path("{token}/proposal/{proposal}/session/date/{startdate}/{enddate}/list")
  @Produces({ "application/json" })
  public Response getSessionByDate(@PathParam("token") String token, @PathParam("proposal") String proposal,
      @PathParam("startdate") String start,
      @PathParam("enddate") String end) throws Exception {
    String methodName = "getSessionByDate";
    long id = this.logInit(methodName, logger, token, proposal, start, end);
    try {
      List<Map<String, Object>> result = getSessionService().getSessionViewByProposalAndDates(this.getProposalId(proposal), start, end);
      this.logFinish(methodName, id, logger);
      return sendResponse(result);
    } catch (Exception e) {
      return this.logError(methodName, e, id, logger);
    }
  }


  private SessionService getSessionService() throws NamingException {
    return (SessionService) Ejb3ServiceLocator.getInstance().getLocalService(SessionService.class);
  }

  @RolesAllowed({ "Manager", "Localcontact" })
  @GET
  @GZIP
  @Path("{token}/proposal/session/date/{startdate}/{enddate}/list")
  @Produces({ "application/json" })
  public Response getSessionsByDate(
      @PathParam("token") String token,
      @PathParam("startdate") String start,
      @PathParam("enddate") String end) throws Exception {
    String methodName = "getSessionsByDate";
    long id = this.logInit(methodName, logger, token, start, end);
    try {
      List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
      Login3VO login = this.getLogin3Service().findByToken(token);
      if (login.isManager()){
        result = getSessionService().getSessionViewByDates(start, end);
      }
      else{
        result = getSessionService().getSessionViewByDates(start, end, login.getSiteId());

      }
      this.logFinish(methodName, id, logger);
      return sendResponse(result);
    } catch (Exception e) {
      return this.logError(methodName, e, id, logger);
    }
  }

  @RolesAllowed({ "Manager", "Localcontact" })
  @GET
  @GZIP
  @Path("{token}/proposal/session/beamlineoperator/{beamlineOperator}/list")
  @Produces({ "application/json" })
  public Response getSessionsByBeamlineOperator(
      @PathParam("token") String token,
      @PathParam("beamlineOperator") String beamlineOperator) throws Exception {
    String methodName = "getSessionsByBeamlineOperator";
    long id = this.logInit(methodName, logger, token, beamlineOperator);
    List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

    try {
      Login3VO login = this.getLogin3Service().findByToken(token);
      if (login.isManager() || login.getSiteId() == null){
        // logged user is manager
        result = getSessionService().getSessionViewByBeamlineOperator(beamlineOperator);
      } else {
        //check if the localcontact is the beamlineOperator
        String surname = this.getPerson3Service().findBySiteId(login.getSiteId()).getFamilyName();
        if (beamlineOperator.contains(surname)) {
          result = getSessionService().getSessionViewByBeamlineOperator(beamlineOperator);
        } else {
          Exception e = new Exception ("Unauthorized " + surname + " to view " + beamlineOperator);
          throw e;
        }
      }

      this.logFinish(methodName, id, logger);
      return sendResponse(result);
    } catch (Exception e) {
      return this.logError(methodName, e, id, logger);
    }
  }

}
