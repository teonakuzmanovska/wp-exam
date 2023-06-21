package mk.ukim.finki.wp.exam.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; // inject-itame userdetailsservis - interfejs od spring security, treba da se implementira

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO: If you are implementing the security requirements, remove this following line
//        web.ignoring().antMatchers("/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("ADMIN")
//                .authenticated() // ova ne treba posho za anyRequest sakame da ima dozvola samo admin
                .and()
                .formLogin() //formLogin se dobiva na /login
//                .loginPage("/login").permitAll() // site mozhat da pristapat na login stranata - nemame login page, go trgame
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/products", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true) // na logout da se ischisti avtentikacijata
                .invalidateHttpSession(true) // po logout da se invalidizira sesijata
                .deleteCookies("JSESSIONID") // da se izbrishe kolacheto
                .logoutSuccessUrl("/"); // po logout odvedeni sme na "/"
//                .and()
//                .exceptionHandling().accessDeniedPage("/access_denied"); // ova ne se bara

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(this.userDetailsService);

    }

}
