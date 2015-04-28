package redis.basic.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RankSearchService
{

  Jedis conn;

  public static final Set<String> STOP_WORDS = new HashSet<>( Arrays.asList( new String[] { "the", "of", "to", "and",
                                                                                           "a", "in", "is", "it",
                                                                                           "you", "that" } ) );

  String getIdForDocument( String fileName )
  {
    String docId = conn.hget( "documents", fileName );
    if ( docId != null )
    {
      return docId;
    }
    docId = String.valueOf( conn.incr( "next_document_id" ) );
    conn.hset( "documents", fileName, docId );
    conn.hset( "filenames", docId, fileName );
    return docId;
  }

  void addWord( String word, String docId )
  {
    conn.zincrby( "word:".concat( word ), 1, docId );
  }

  List<String> search( String[] terms )
  {
    List<String> indexSet = new ArrayList<>();
    for ( String term : terms )
    {
      if ( !STOP_WORDS.contains( term ) )
        indexSet.add( "word:".concat( term ) );
    }

    Transaction tx = conn.multi();
    tx.zinterstore( "result", indexSet.toArray( new String[ 0 ] ) );
    tx.exec();

    Set<String> docs = conn.zrevrangeByScore( "result", 9999, 0 );
    return conn.hmget( "filenames", docs.toArray( new String[ 0 ] ) );
  }

  void index( String[] files )
  {
    for ( int i = 0; i < files.length; i++ )
    {
      for ( String word : files[ i ].split( "," ) )
      {
        String fileName = "file".concat( String.valueOf( i ) );
        if ( STOP_WORDS.contains( word ) )
          continue;
        addWord( word, getIdForDocument( fileName ) );
      }
    }
  }

  public void main( Jedis conn )
  {
    this.conn = conn;

    String file0 = "Peter,Becky,Leaf,Bin";
    String file1 = "Becky,Leaf,Bin,Rain,Bin,Becky";
    String file2 = "Leaf,Bin,Feifei";

    String[] files = new String[] { file0, file1, file2 };
    // Clear cache
    conn.del( "result word:Peter word:Becky word:Bin word:Leaf word:Feifei word:Rain".split( " " ) );

    index( files );

    // Search
    System.out.println( search( "Peter is Becky".split( " " ) ) );
    System.out.println( search( "Bin is Becky".split( " " ) ) );
    System.out.println( search( "Bin".split( " " ) ) );
    System.out.println( search( "Feifei is Bin".split( " " ) ) );

  }
}