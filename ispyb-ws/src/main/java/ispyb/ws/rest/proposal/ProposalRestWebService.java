package ispyb.ws.rest.proposal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import io.swagger.annotations.Api;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.smis.UpdateFromSMIS;
import ispyb.ws.rest.mx.MXRestWebService;

@Api
@Path("/")
public class ProposalRestWebService extends MXRestWebService{

	private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);


//  @ApiOperation( value = "Retrieve a list of sessions from the database",
//    authorizations = @Authorization( value = "apiKeyAuth")
//  )
//  public Response getSessions(@QueryParam("id") List<Integer> sessionIDs,
//                              @Context HttpHeaders headers,
//                              @ApiParam(value = "Authorisation token for the session", required = true)
//                              @HeaderParam("api_token") String authToken )
//  {

  @RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("/proposals")
	@Produces({ "application/json" })
	@ApiOperation( value = "Return list of proposals", httpMethod="GET",
    authorizations = {@Authorization(value="basicAuth" ), @Authorization( value = "apiKeyAuth" ), @Authorization( value = "oAuth")} )
	public Response getSimpleProposals() throws Exception {
		String methodName = "getSimpleProposals";
		long id = this.logInit(methodName, logger);
		try {
			List<Map<String, Object>> proposals = this.getProposalsFromTokenNoAuth();
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposals);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}



	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/list")
	@Produces({ "application/json" })
	@ApiOperation( value = "Return list of proposals again", httpMethod="GET", authorizations = {@Authorization(value="basicAuth")})

	public Response getProposals(@PathParam("token") String token) throws Exception {
		String methodName = "getProposals";
		long id = this.logInit(methodName, logger, token);
		try {
			List<Map<String, Object>> proposals = this.getProposalsFromToken(token);
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposals);
		} catch (Exception e) {
			return this.logError("getProposals", e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/info/get")
	@Produces({ "application/json" })
	public Response getProposaInfos(@PathParam("token") String token, @PathParam("proposal") String proposal)
				throws Exception {
		String methodName = "getProposaInfos";
		long id = this.logInit(methodName, logger, token, proposal);

		System.out.printf("		*CE* - getProposalInfo called with token[ %s ] and proposal [ %s ]\n", token, proposal);

		try {
			ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();

			if (proposal == null || proposal.isEmpty()) {
				System.out.printf("		getProposalInfo - Proporsal == null || proposal.isEmpty - Getting results from token\n");
				List<Map<String, Object>> proposals = this.getProposalsFromToken(token);
				results.put("proposal", proposals);
			} else {
        System.out.printf("			getProposalInfo - Proposal[ %s ]\n", proposal);

        int proposalId = this.getProposalId(proposal);

				// This method worked ... Passing in the ID as the parameter (Which is what I'd expect...)
//				int proposalId = Integer.parseInt(proposal);
        System.out.printf("			getProposalInfo - Proposal ID[ %d ]\n", proposalId);


        List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(proposalId);
				List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

				List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
						proposalId);
				List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
				List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
				proposals.add(this.getProposal3Service().findProposalById(proposalId));

				System.out.printf("			proposals added by now - Check size: %d\n", proposals.size());

				List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(proposalId);
				List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(proposalId);

				List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
				results.put("proposal", proposals);
				results.put("crystals", crystals);
				results.put("plateTypes", plateTypes);
				results.put("macromolecules", macromolecules);
				results.put("buffers", buffers);
				results.put("stockSolutions", stockSolutions);
				results.put("labcontacts", labContacts);
				results.put("proteins", proteins);

			}

			multiple.add(results);
			this.logFinish(methodName, id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}


	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/session/{sessionId}/list")
	@Produces({ "application/json" })
	public Response getProposalsBySessionId(
			@PathParam("token") String token,
			@PathParam("sessionId") int sessionId) throws Exception {
		String methodName = "getProposalsBySessionId";
		long id = this.logInit(methodName, logger, token);
		try {
			Session3VO session = this.getSession3Service().findByPk(sessionId, false, false, false);
			List<Map<String, Object>> proposal = this.getProposal3Service().findProposalById(session.getProposalVOId());
			this.logFinish(methodName, id, logger);
			return this.sendResponse(proposal);
		} catch (AccessDeniedException e) {
			return this.sendError(methodName + " unauthorized user");
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}


	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/technique/{technique}/get")
	@Produces({ "application/json" })
	public Response listProposal(@PathParam("token") String token,
				     @PathParam("proposal") String login,
				     @PathParam("technique") String technique)
			throws Exception {
		//TODO remove this method if above getProposaInfos is sufficient
		long id = this.logInit("listProposal", logger, token, login);
		try {
			ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
			int proposalId = this.getProposalId(login);
			HashMap<String, List<?>> results = new HashMap<String, List<?>>();

			List<Macromolecule3VO> macromolecules = this.getSaxsProposal3Service().findMacromoleculesByProposalId(proposalId);
			List<Buffer3VO> buffers = this.getSaxsProposal3Service().findBuffersByProposalId(proposalId);

			List<StockSolution3VO> stockSolutions = this.getSaxsProposal3Service().findStockSolutionsByProposalId(
					proposalId);
			List<Platetype3VO> plateTypes = this.getPlateType3Service().findAll();
			List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
			proposals.add(this.getProposal3Service().findProposalById(proposalId));

			List<Protein3VO> proteins = this.getProtein3Service().findByProposalId(proposalId);
			List<Crystal3VO> crystals = this.getCrystal3Service().findByProposalId(proposalId);

			List<LabContact3VO> labContacts = this.getLabContact3Service().findFiltered(proposalId, null);
			results.put("proposal", proposals);
			results.put("crystals", crystals);
			results.put("plateTypes", plateTypes);
			results.put("macromolecules", macromolecules);
			results.put("buffers", buffers);
			results.put("stockSolutions", stockSolutions);
			results.put("labcontacts", labContacts);
			results.put("proteins", proteins);

			multiple.add(results);
			this.logFinish("listProposal", id, logger);

			return this.sendResponse(multiple);

		} catch (Exception e) {
			return this.logError("listProposal", e, id, logger);
		}
	}


	private List<Map<String, Object>> getProposalsFromTokenNoAuth () throws Exception {
		List<Map<String, Object>> proposals = new ArrayList<Map<String,Object>>();
		return this.getProposal3Service().findProposals();
	}


	private List<Map<String, Object>> getProposalsFromToken (String token) throws Exception {
		Login3VO login3VO = this.getLogin3Service().findByToken(token);
		List<Map<String, Object>> proposals = new ArrayList<Map<String,Object>>();

		if (login3VO != null){
			if (login3VO.isValid()){

				if (login3VO.isLocalContact() || login3VO.isManager()){
					proposals = this.getProposal3Service().findProposals();
				}
				else{
					proposals = this.getProposal3Service().findProposals(login3VO.getUsername());
				}
			}
		}	else {
			throw new Exception("Token is not valid");
		}
		return (proposals);

	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/update")
	@Produces({ "application/json" })
	public Response updateProposal(@PathParam("token") String token, @PathParam("proposal") String proposal)
			throws Exception {

		long id = this.logInit("updateProposal", logger, token, proposal);
		int proposalId = this.getProposalId(proposal);
		try {
			logger.info("Updating " + proposal + ":" + proposalId);
			UpdateFromSMIS.updateProposalFromSMIS(proposalId);
			this.logFinish("updateProposal", id, logger);
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("Status", "Done");
			return this.sendResponse(response);
		}
		catch(Exception e){
			return this.logError("updateProposal", e, id, logger);
		}
	}

}
