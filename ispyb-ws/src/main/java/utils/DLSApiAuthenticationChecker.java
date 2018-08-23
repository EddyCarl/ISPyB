package utils;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import javax.naming.NamingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class DLSApiAuthenticationChecker
{
  private final static String API_TOKEN_HEADER = "api_token";


  /**
   * Utility method used to get a Login3Service instance in order to obtain Login3VO entities
   * from the database using the methods defined in the service.
   *
   * @return Login3Service - The service containing helper methods to obtain data from the database.
   */
  private static Login3Service getLogin3Service()
  {
    try
    {
      return ( Login3Service ) Ejb3ServiceLocator.getInstance().getLocalService( Login3Service.class );
    }
    catch( NamingException ne )
    {
      return null;
    }
  }


  /**
   * Used by the webservice methods to ensure that the authorisation token input in the header (or lack thereof)
   * can be used to validate whether or not the user is authorised to use the endpoints.
   *
   * @param headers - The input context headers from the request which may (or may not) contain the API token.
   *
   * @return boolean - A boolean response depending on whether or not the user has been authorised.
   */
  public static boolean validAuthenticationToken( final HttpHeaders headers )
  {
    // If the API token has not been provided in the headers then the user cannot be authorised
    if( headers == null || headers.getRequestHeader( API_TOKEN_HEADER ).isEmpty() )
    {
      return false;
    }

    // Retrieve the API token from the input headers
    String apiToken = headers.getRequestHeader( API_TOKEN_HEADER ).get( 0 );

    // Retrieve the login service that will be used to access the database
    if( getLogin3Service() == null )
    {
      return false;
    }

    // Attempt to find a login instance using the input API token to determine whether the user is authorised
    if( getLogin3Service().findByToken( apiToken ) == null )
    {
      return false;
    }

    return true;
  }


  /**
   * Used by the webservice classes if the authentication attempt fails to return a simple error response that
   * can be sent back by the API to inform the user that they are unauthorised.
   *
   * @return Response - The error response with a message explaining to the user that they are unauthorised.
   */
  public static Response getUnauthorisedResponse()
  {
    Map<String, Object> errorEntity = new HashMap<>();

    errorEntity.put( "error", "You are unauthorised to use this endpoint. " +
                              "Please login to the system to obtain an authentication token first." );

    return Response.status( Response.Status.UNAUTHORIZED ).entity( errorEntity ).build();
  }






  //  public static boolean userIsAuthenticated( final Login3Service login3Service, final String apiToken )
//  {
//    // Check the input X-API-Key auth token by attempting to retrieve a session from the DB
//    Login3VO login3VO = login3Service.findByToken( apiToken );
//
//    if(login3VO == null)
//    {
//      return false;
//    }
//
//    return true;
//  }

  //
  //  String apiToken = DLSApiAuthenticationChecker.retrieveToken( headers );
  //
  //    if( !DLSApiAuthenticationChecker.userIsAuthenticated( this.getLogin3Service(), apiToken ) )
  //  {
  //    Map<String, Object> error = new HashMap<>();
  //    error.put( "error", "You are not authorised to use this endpoint" );
  //    return Response.status(Response.Status.UNAUTHORIZED).entity( error ).build();
  //  }


  //  if( !DLSApiAuthenticationChecker.validAuthenticationToken( headers ) )
  //  {
  //    return DLSApiAuthenticationChecker.getUnauthorisedResponse();
  //  }
}
