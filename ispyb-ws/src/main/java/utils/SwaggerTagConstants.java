package utils;


/**
 * Utility class created to hold all of the tags that will be used to organise
 * the endpoints on the Swagger UI page.
 *
 * The older endpoints (prior to the Synchlink functionality merge) will be categorised
 * under the "Legacy Endpoints" tag.
 */
public class SwaggerTagConstants
{
  // All endpoints will fall under the Legacy tag unless otherwise specified
  final public static String LEGACY_TAG = "Legacy Endpoints";

  // The following tags will be used for any newer Synchlink based endpoints
  final public static String PROPOSAL_TAG = "Proposal";

  final public static String SESSION_TAG = "Session";

  final public static String DATA_COLLECTION_TAG = "Data Collection";

  final public static String ENERGY_SCAN_TAG = "Energy Scan";
}
