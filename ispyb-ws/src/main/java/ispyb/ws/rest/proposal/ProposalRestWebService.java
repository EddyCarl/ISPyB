package ispyb.ws.rest.proposal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.apache.log4j.Logger;

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
import utils.SwaggerTagConstants;


@Api( tags = SwaggerTagConstants.LEGACY_TAG )
@Path("/")
public class ProposalRestWebService extends MXRestWebService
{
  private final static Logger logger = Logger.getLogger(ProposalRestWebService.class);

  /**
   * Used to retrieve a full list of proposals stored in the database, that are available to the user currently
   * logged into the system.
   *
   * @return  Response  - Returns a relevant HTTP response
   */
  @GET
  @Path( "/proposals" )
  @ApiOperation
  (
    value = "Retrieve a list of proposals",
    notes = "Returns a list of proposals that are available to the user currently logged in.",
    tags = { SwaggerTagConstants.PROPOSAL_TAG }, response = Proposal3VO.class, responseContainer = "List",
    authorizations = @Authorization( "basicAuth" )
  )
  @Produces({ "application/json" })
  @ApiResponses
  ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
  } )
  public Response retrieveProposals() throws Exception
  {
    return null;
  }


  /**
   * Used to retrieve the information related to a specific proposal stored in the database, based on the input
   * proposal ID in the endpoint (if it is available to the user currently logged into the system.)
   *
   * @param   proposalID  - Input proposalID used to retrieve information for
   *
   * @return  Response    - Returns a relevant HTTP response
   */
  @GET
  @Path( "/proposals/{id}" )
  @ApiOperation
  (
    value = "Retrieve the information of a proposal",
    notes = "Obtain the information relating to a specific proposal (based on the input ID) " +
            "if it is available to the user currently logged in.",
    tags = { PROPOSAL_TAG }, response = Proposal3VO.class,
    authorizations = @Authorization( "basicAuth" )
  )
  @Produces({ "application/json" })
  @ApiResponses
  ( {
      @ApiResponse( code = 200, message = "Ok" ),
      @ApiResponse( code = 400, message = "Some error" )
  } )
  public Response retrieveProposalById
  (

    @ApiParam
    (
      name = "id", value = "The ID of the proposal to retrieve", required = true, example = "131"
    ) @PathParam( "id" ) int proposalID

  ) throws Exception
  {
    System.out.println("The proposalID input is: " + proposalID);
    return null;
  }


//  @RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
//  @GET
//  @Path("/proposals")
//  @Produces({ "application/json" })
//  public Response getSimpleProposals() throws Exception {
//    String methodName = "getSimpleProposals";
//    long id = this.logInit(methodName, logger);
//    try {
//      List<Map<String, Object>> proposals = this.getProposalsFromTokenNoAuth();
//      this.logFinish(methodName, id, logger);
//      return this.sendResponse(proposals);
//    } catch (Exception e) {
//      return this.logError(methodName, e, id, logger);
//    }
//  }
//

  /*
   * ------ Legacy endpoints below this point ----
   */

  @RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
  @GET
  @Path("{token}/proposal/list")
  @Produces({ "application/json" })
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
  public Response getProposalInfos(@PathParam("token") String token, @PathParam("proposal") String proposal)
        throws Exception {
    String methodName = "getProposalInfos";
    long id = this.logInit(methodName, logger, token, proposal);

    try {
      ArrayList<HashMap<String, List<?>>> multiple = new ArrayList<HashMap<String, List<?>>>();
      HashMap<String, List<?>> results = new HashMap<String, List<?>>();

      if (proposal == null || proposal.isEmpty()) {
        List<Map<String, Object>> proposals = this.getProposalsFromToken(token);
        results.put("proposal", proposals);
      } else {
        int proposalId = this.getProposalId(proposal);
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
    }  else {
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
