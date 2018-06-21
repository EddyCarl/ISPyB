package ispyb.ws.rest.proposal;

import io.swagger.annotations.ApiParam;
import ispyb.server.common.services.ws.rest.session.SessionService;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.ws.rest.RestWebService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;

import io.swagger.annotations.Api;

@Api
@Path("/")
public class SessionRestWebService extends RestWebService {
	private final static Logger logger = Logger.getLogger(SessionRestWebService.class);

	@GET
	@Path("sessions")
	@Produces({ "application/json" })
	public Response getSessionInformation(@QueryParam("id") List<Integer> sessionIDs,
																				@Context HttpHeaders headers,
																				@ApiParam(value = "Authorisation token for the session", required = true)
																					@HeaderParam("Auth-Token") String authToken )
	{
		String methodName = "getSessionInformation";
		System.out.println("Called " + methodName + " with sessionIDs:");

		for(Integer i : sessionIDs)
		{
			System.out.println("~~~ ID: " + i + " ~~~");
		}

		System.out.println("-------------------------------");
		System.out.println("Auth Token: " + authToken);
		System.out.println("-------------------------------");


    System.out.println("Checking that the auth token is valid");
    try
    {
      Login3VO login3VO = this.getLogin3Service().findByToken( authToken );
      System.out.println("login3vo.getusername(): " + login3VO.getUsername());
      System.out.println("login3vo.getauthorised(): " + login3VO.getAuthorized());
      System.out.println("login3vo.getsiteid(): " + login3VO.getSiteId());
    }
    catch( NamingException e )
    {
      System.out.println("Caught namingexception ");
      e.printStackTrace();
    }


    long id = this.logInit(methodName, logger, sessionIDs);

		try
		{
			System.out.println("Calling the service method here... ");
			List<Map<String, Object>> result = getSessionService().getTestSessionInfo(sessionIDs);
			this.logFinish(methodName, id, logger);
			return sendResponse(result);
		}
		catch (Exception e)
		{
			return this.logError(methodName, e, id, logger);
		}

	}

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
