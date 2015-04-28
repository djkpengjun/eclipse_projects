package redis.basic.oauth;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * We won’t be implementing the API or the OAuth interaction itself. Here we’re inter- ested only in the data required
 * for this sort of scenario. We’ll be storing five types of data in Redis: [consumer keys, consumer secrets, request
 * tokens, access tokens, nonces] Assuming: 1) applications (consumers) are identified by their key and secret, of which
 * they have exactly one pair. 2) consumers can have as many request and access tokens as they desire 3) nonces should
 * be unique per consumer/timestamp pair.
 * 
 * @author ppeng
 */
public class oAuthService
{
  public void main( String[] args ) throws Exception
  {
    @SuppressWarnings( "resource" )
    Jedis conn = new Jedis( "localhost" );

    // Global variables
    final String userId = "16";

    /**
     * To start with, consumers must enter their data before they issue a request. The key is the one we’ve stored for
     * the particular consumer when he or she registered with our system:
     */
    final String consumerKey = "/consumers/key:dpf43f3p2l4k3l03";
    /**
     * For security purposes, the consumer secret is never sent—it’s a pre-shared secret since both parts know it
     */
    final String requesetTokenKey = "hh5s93j4hdidpola";
    final String requesetTokenValue = "hdhd0244k9j7ao03";

    final String requestTokenNonceSet = "/nonces/key:dpf43f3p2l4k3l03/timestamp:20110306182600";
    final String accessTokenNonceSet = "/nonces/key:dpf43f3p2l4k3l03/timestamp:20110306182700";
    final String newNonceId = "/nonces/key:dpf43f3p2l4k3l03/timestamp:20110306182800";
    final String requestTokenId = "/request_tokens/key:dpf43f3p2l4k3l03";
    final String authorizationTokenId = "/authorizations/request_token:hh5s93j4hdidpola";
    final String accessTokenId = "/access_tokens/consumer_key:dpf43f3p2l4k3l03/access_token:nnch734d00sl2jdk";

    // Global consumer data properties, identified by secret and key
    final String CONSUMER_PROPERTY_NAME = "name";
    final String CONSUMER_PROPERTY_SECRET = "secret";
    final String CONSUMER_PROPERTY_CRAETION_TIMSTAMP = "created_at";
    final String CONSUMER_PROPERTY_REDIRECT_URL = "redirect_url";

    // Global access data properties, identified by secret and key
    final String ACCESS_TOKEN_PROPERTY_USER_ID = "user_id";
    final String ACCESS_TOKEN_PROPERTY_SECRET = "secret";
    final String ACCESS_TOKEN_PROPERTY_CRAETION_TIMSTAMP = "created_at";

    /////////////////////////////////Predefined application/consumer data///////////////////////////////
    Map<String, String> consumerData = new HashMap<String, String>();
    consumerData.put( CONSUMER_PROPERTY_NAME, "test_application" );
    consumerData.put( CONSUMER_PROPERTY_SECRET, "kd94hf93k423kf44" );
    consumerData.put( CONSUMER_PROPERTY_CRAETION_TIMSTAMP, "201103060000" );
    consumerData.put( CONSUMER_PROPERTY_REDIRECT_URL, "http://www.example.com/oauth_redirect" );
    conn.hmset( consumerKey, consumerData );

    ///////////////////////////////Getting a request token from consumer data/////////////////////
    /**
     * In order to get a request token, consumers send their key, a timestamp, a unique generated nonce a callback url,
     * and a request signature that is a hash of the request path and parameters using the consumer secret. (For
     * security purposes, the consumer secret is never sent—it’s a pre-shared secret since both parts know it).
     */
    // Get consumer data
    consumerData = conn.hgetAll( consumerKey );

    System.out.println( consumerData );

    // Check whether the nonce already exists
    if ( conn.sadd( requestTokenNonceSet, "dji430splmx33448" ) != 0 )
    {
      // The nonce will expire in 30 minutes
      conn.expireAt( requestTokenNonceSet, 1800 );
    }
    else
    {
      // The nounce for the application and timestamp pair already exists then we will refuse to generate a new token
      // attach to replay!!!!

      throw new Exception( "Replay attack" );
    }

    // After validating all the data, we need to create a token to matching secret and return
    conn.hset( requestTokenId, requesetTokenKey, requesetTokenValue );

    //////////////////Redirections and consent///////////////////////////////////////////////////////////
    /**
     * After successfully retrieving the request token, the application should redirect the user to the oAuth API
     * provider
     */

    /**
     * Authenticate the user and authorize the application to access the user’s data Should the user grant his
     * permission, we’ll have to store it.
     */
    conn.set( authorizationTokenId, userId );

    ///Once storing is done, we can redirect the user to the redirect URL we stored:
    conn.hget( consumerKey, CONSUMER_PROPERTY_REDIRECT_URL );

    //////////////////Exchanging the request token for an access token///////////////////////////////////
    /**
     * The access tokens are what the consumers need to authenticate with the API. These are obtained by submitting the
     * consumer key, request token, and secret that were pre- viously fetched and generating an access token.
     */
    //check whether this access token was authorized by a user
    conn.hgetAll( consumerKey );

    /**
     * we need to check whether the consumer key is valid and matches an existing application. a failure to find it
     * would probably mean someone is attempting to reuse a request token which is not allowed by the spec
     */
    conn.get( authorizationTokenId );

    /**
     * The last thing we need to check is which user authorized this application.
     */
    // Check whether the nonce already exists
    if ( conn.sadd( accessTokenNonceSet, "kllo9940pd9333jh" ) != 0 )
    {
      // The nonce will expire in 30 minutes
      conn.expireAt( accessTokenNonceSet, 1800 );
    }
    else
    // A failure in any of the checks implies an invalid request and therefore we shouldn’t generate an access token.
    {
      throw new Exception( "Invalid request" );
    }

    // If everything is OK, we can proceed to fill access token related information
    Map<String, String> accessData = new HashMap<String, String>();
    accessData.put( ACCESS_TOKEN_PROPERTY_SECRET, "pfkkdhi9sl3r4s00" );
    accessData.put( ACCESS_TOKEN_PROPERTY_USER_ID, userId );
    accessData.put( ACCESS_TOKEN_PROPERTY_CRAETION_TIMSTAMP, "20110306182600" );
    conn.hmset( accessTokenId, accessData );

    // delete request token and authorizationTokenId once we get access token
    conn.hdel( requestTokenId, requesetTokenKey );
    conn.del( authorizationTokenId );

    /**
     * Perhaps somewhere in our application we allow users to see which applications have access to their credentials.
     * To facilitate the retrieval of that information,, so let's add it to a hash of client applications
     */
    conn.hset( "/users/user_id:16/applications", "dpf43f3p2l4k3l03", "nnch734d00sl2jdk" );

    /**
     * A follow-up feature would be to allow users to revoke access to these applications. Doing so is trivial: pass
     * application key
     */
    conn.hdel( "/users/user_id:16/applications", "dpf43f3p2l4k3l03" );
    conn.del( accessTokenId );

    /**
     * Our application logic might also define different expiration times for each new token, perhaps even at the user’s
     * request.
     */
    conn.expire( accessTokenId, 86400 );

    // API Access
    /**
     * When the consumer is accessing the API, the process should be really simple: validate the keys, secrets,
     * signatures, and nonce.
     */
    conn.hgetAll( consumerKey );
    conn.hgetAll( accessTokenId ); // Return token and access key
    conn.sadd( newNonceId, "kllo9940pd9333jh" );
    conn.expire( requestTokenNonceSet, 1800 );
  }
}
