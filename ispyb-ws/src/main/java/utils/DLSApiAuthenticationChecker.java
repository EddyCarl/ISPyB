package utils;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.vos.login.Login3VO;

import javax.naming.NamingException;
import javax.ws.rs.core.HttpHeaders;

public class DLSApiAuthenticationChecker
{
  private final static String API_TOKEN_HEADER = "api_token";

  public static String retrieveToken( final HttpHeaders headers )
  {
    String apiToken = "";

    if( headers != null )
    {
      if( headers.getRequestHeader( API_TOKEN_HEADER ) != null )
      {
        if( headers.getRequestHeader( API_TOKEN_HEADER ).get( 0 ) != null )
        {
          apiToken = headers.getRequestHeader( API_TOKEN_HEADER ).get( 0 );
        }
      }
    }

    return apiToken;
  }


  public static boolean userIsAuthenticated( final Login3Service login3Service, final String apiToken )
  {
    // Check the input X-API-Key auth token by attempting to retrieve a session from the DB
    Login3VO login3VO = login3Service.findByToken( apiToken );

    if(login3VO == null)
    {
      return false;
    }

    return true;
  }

}
