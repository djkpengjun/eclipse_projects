package redis.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController
{

  @RequestMapping( "/error" )
  public String index()
  {
    return "Greetings from Spring Boot!";
  }

}
