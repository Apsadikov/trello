package ru.itis.trello.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.itis.trello.security.details.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private PersistentTokenRepository persistentTokenRepository;
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(@Qualifier(value = "customUserDetailsService") UserDetailsServiceImpl userDetailsService,
                             BCryptPasswordEncoder bCryptPasswordEncoder, PersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/chat").authenticated();

        http.formLogin()
                .loginPage("/sign-in")
                .usernameParameter("email")
                .defaultSuccessUrl("/")
                .failureUrl("/sign-in?error=true")
                .loginProcessingUrl("/sign-in")
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me").tokenRepository(persistentTokenRepository);

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/sign-in")
                .deleteCookies("JSESSIONID", "remember-me")
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
