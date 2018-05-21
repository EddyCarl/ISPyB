package ispyb.ws.rest.security.login;

import ispyb.server.security.EmployeeVO;
import ispyb.server.security.LdapConnection;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;

import org.jboss.security.SubjectSecurityManager;

/**
 * Authentication module specific to the Diamond Light Source site.
 *
 * Configures a login module that uses a CAS service to authenticate and generate a token to use the webservice.
 */
public class DLSLoginModule
{
  private final static String AUTH_TYPE = "simple";
//  private final static String SERVER_URL = "ldap://ldap.esrf.fr:389/";
  private final static String SERVER_URL = "http://192.168.33.12:8080/cas";

  /**
   * Creates a set of environment variable properties to return that allow the DLSLoginModule to be configured.
   *
   * (Realistically, this should probably be done via a config file perhaps rather than hardcoded in the codebase?)
   *
   * @param username
   *        - The login username
   *
   * @param password
   *        - The login password
   *
   * @return Properties props
   *        - A set of environment variables to configure the DLSLoginModule
   */
  private Properties getConnectionProperties(final String username, final String password)
  {
    Properties props = new Properties();

    props.put( "java.naming.provider.url", SERVER_URL );
    props.put("java.naming.security.authentication", AUTH_TYPE);





//    env.put( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
//    env.put( "principalDNPrefix", principalDNPrefix );
//    env.put( "java.naming.security.principal", "uid=" + username + ",ou=People,dc=esrf,dc=fr" );
//    env.put( "groupAttributeID", groupAttributeID );
//    env.put( "groupCtxDN", groupCtxDN );
//    env.put( "principalDNSuffix", principalDNSuffix );
//    env.put( "allowEmptyPasswords", "false" );
//    env.put( "groupUniqueMember", groupUniqueMemberName );
//    env.put( "jboss.security.security_domain", "ispyb" );
//    env.put( "java.naming.provider.url", server );
//    env.put( "java.naming.security.credentials", password );



    return props;
  }

  protected static Properties getConnectionProperties( String username, String password )
  {
    private static String groupUniqueMemberName = "uniqueMember";
    private static String principalDNSuffix = ",ou=People,dc=esrf,dc=fr";
    private static String groupCtxDN = "ou=Pxwebgroups,dc=esrf,dc=fr";
    private static String principalDNPrefix = "uid=";
    private static String groupAttributeID = "cn";
    private static String server = "ldap://ldap.esrf.fr:389/";


    Properties env = new Properties();
    env.put( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
    env.put( "principalDNPrefix", principalDNPrefix );
    env.put( "java.naming.security.principal", "uid=" + username + ",ou=People,dc=esrf,dc=fr" );
    env.put( "groupAttributeID", groupAttributeID );
    env.put( "groupCtxDN", groupCtxDN );
    env.put( "principalDNSuffix", principalDNSuffix );
    env.put( "allowEmptyPasswords", "false" );
    env.put( "groupUniqueMember", groupUniqueMemberName );
    env.put( "jboss.security.security_domain", "ispyb" );
    env.put( "java.naming.provider.url", server );
    env.put( "java.naming.security.authentication", "simple" );
    env.put( "java.naming.security.credentials", password );

    System.out.printf( "			Returning the ESRFLoginModule properties correctly\n" );

    return env;
  }


  protected static String getFilter( String username )
  {
    String userDN = principalDNPrefix + username + principalDNSuffix;
    return new StringBuffer().append( "(&" )
      .append( "(objectClass=groupOfUniqueNames)" )
      .append( "(" + groupUniqueMemberName + "=" )
      .append( userDN )
      .append( ")" ).append( ")" ).toString();
  }


  public static List<String> authenticate( String username, String password )
  {
    List<String> rolesList = new ArrayList<String>();
    rolesList.add( "User" );
    rolesList.add( "Manager" );
    return rolesList;
  }

  //	public static List<String> authenticate(String username, String password)
  //			throws Exception {
  //
  //		System.out.printf("		ESRFLoginModule.authenticate() :: Username[ %s ], Pass[ %s ]\n", username, password);
  //		List<String> myRoles = new ArrayList<String>();
  //
  //		if (!password.isEmpty()){
  //			System.out.printf("			Attempt to create InitialLdapContext\n");
  //			InitialLdapContext ctx = new InitialLdapContext(getConnectionProperties(username, password), null);
  //
  //			// Set up search constraints
  //			SearchControls cons = new SearchControls();
  //			cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
  //			// Search
  //			NamingEnumeration<SearchResult> answer = ctx.search(groupCtxDN, getFilter(username),cons);
  //
  //			System.out.println(answer);
  //
  //			while (answer.hasMore()) {
  //				SearchResult sr = answer.next();
  //				System.out.println(sr);
  //				Attributes attrs = sr.getAttributes();
  //				Attribute roles = attrs.get(groupAttributeID);
  //				for (int r = 0; r < roles.size(); r++) {
  //					Object value = roles.get(r);
  //					String roleName = null;
  //					roleName = value.toString();
  //					// fill roles array
  //					if (roleName != null) {
  //						myRoles.add(roleName);
  //					}
  //				}
  //			}
  //			/** Any validated user is in role User */
  //			if (myRoles.size() == 0){
  //				myRoles.add("User");
  //			}
  //		}
  //		else{
  //			throw new Exception("Empty passwords are not allowed");
  //		}
  //		return myRoles;
  //	}
}
