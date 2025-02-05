package demo;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class UiApplication {

  @GetMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
      return "forward:/";
  }

  @RequestMapping("/user")
  @ResponseBody
  public Map<String, String> user(Principal user) {
    return Collections.singletonMap("name", user.getName());
  }

  public static void main(String[] args) {
    SpringApplication.run(UiApplication.class, args);
  }

  @Configuration
 // @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http
        .httpBasic().and()
        .authorizeRequests()
          .antMatchers("/index.html", "/app.html", "/").permitAll()
          .anyRequest().hasRole("USER");
      // @formatter:on
    }
  }

}
