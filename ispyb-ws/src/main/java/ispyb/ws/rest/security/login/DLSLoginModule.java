package ispyb.ws.rest.security.login;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication module specific to the Diamond Light Source site.
 *
 * Configures a login module that uses a CAS service to authenticate and generate a token to use the webservice.
 */
public class DLSLoginModule
{
  private final static String CAS_SERVER_IP = "192.168.33.12";
  private final static String CAS_SERVER_PORT = "8080";
  private final static String CAS_SERVER_ADDRESS = CAS_SERVER_IP + ':' + CAS_SERVER_PORT;
  private final static String CAS_LOGIN_URL = "http://" + CAS_SERVER_ADDRESS + "/cas/v1/tickets";

  static final List<String> ROLES_LIST = new ArrayList<String>()
  {{
    add( "User" );
    add( "Manager" );
  }};

  static final Map<String, String> HEADERS = new HashMap<String, String>()
  {{
    headerMap.put( "accept", "application/json" );
    headerMap.put( "content-type", "application/x-www-form-urlencoded" );
  }};

  public static final List<String> getRoles()
  {
    return ROLES_LIST;
  }

  // Performs CAS authentication and returns a boolean value if the user was successfully logged in or not
  public boolean authenticateUser( String username, String password )
  {
    System.out.printf("Authenticating user[ Uname: %s | Pass: %s ] on CAS server[ %s ]", username,
                                                                                         password,
                                                                                         CAS_SERVER_ADDRESS );

    try
    {
      // Build the POST request and send it
      HttpResponse<String> rsp = Unirest.post( CAS_LOGIN_URL )
        .headers( HEADERS )
        .body( "username=boaty&password=mcboatface" )
        .asString();


      System.out.println("Printing result: ");
      System.out.println( "Body - " + rsp.getBody() );
      System.out.println( "Headers - " + rsp.getHeaders() );
      System.out.println( "RawBody - " + rsp.getRawBody() );
      System.out.println( "Status - " + rsp.getStatus() );
      System.out.println( "StatusText - " + rsp.getStatusText() );


      // Check the returned response for a valid status code and a ticket that has been granted (Starting TGT)
      if(rsp.getStatus() == 201 && rsp.getBody().contains("TGT"))
      {
        return true;
      }
    }
    catch( UnirestException e )
    {
      System.out.println("There was a problem issuing the request: " + e.getMessage();
    }

    return false;
  }
}
