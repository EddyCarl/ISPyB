package utils;

import ispyb.server.common.services.login.Login3Service;
import ispyb.server.common.vos.login.Login3VO;

import javax.naming.NamingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DLSApiAuthenticationChecker
{
  private final static String API_TOKEN_HEADER = "api_token";

  public static String retrieveToken( final HttpHeaders headers )
  {
    String apiToken = "";

    if( headers != null )
    {

      MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();

      if( headerMap == null )
      {
        System.out.println( " === The header map is null === ");
      }
      if( headerMap.isEmpty() )
      {
        System.out.println( " === The header map is empty === ");
      }

      System.out.println( " === The header map size is: " + headerMap.size() );


      for( Map.Entry<String, List<String>> me : headerMap.entrySet() )
      {
        String key = me.getKey();

        List<String> values = me.getValue();


        System.out.println( "    Key: " + key );
        System.out.println( "     Values size: " + values.size() );

        for( String value : values )
        {
          System.out.println( "             " + value );
        }
      }


      System.out.println(" ---------- Time for the real stuff ------------- ");


      List<String> headerValues = headers.getRequestHeader( API_TOKEN_HEADER );
      if( headerValues != null )
      {
        System.out.println( "headers.getReqHeader for API TOKEN is NOT null ");


        // I think we hit an exception when trying to "get(0)" if the API token hasn't actually been input
        // into the header... Investigate further

        System.out.println("    size of headervalue list: " +  headerValues.size() );

        for( String hv : headerValues )
        {
          System.out.println( "       " + hv );
        }



        if( headers.getRequestHeader( API_TOKEN_HEADER ).get( 0 ) != null )
        {
          apiToken = headers.getRequestHeader( API_TOKEN_HEADER ).get( 0 );
        }
      }
      else
      {
        System.out.println("headers.getreqheader for API token is NULL ");
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
