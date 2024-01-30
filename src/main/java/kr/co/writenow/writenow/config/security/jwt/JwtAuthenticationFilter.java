package kr.co.writenow.writenow.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider provider;
  private final UserDetailsService userDetailsService;

  private final static String HEADER_AUTHORIZATION = "Authorization";
  private final static String TOKEN_PREFIX = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader(HEADER_AUTHORIZATION);
    final String jwt = getAccessToken(authHeader);

    if(StringUtils.hasText(authHeader) && StringUtils.hasText(jwt)){
      final String userId = provider.getUserId(jwt);
      UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
      if(provider.isValidToken(jwt, userDetails.getUsername())){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
      }
    }else{
      log.warn("유효한 JWT토큰이 존재하지 않음. uri: {}", request.getRequestURI());
    }
    filterChain.doFilter(request, response);
  }

  private String getAccessToken(String authHeader) {
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      return authHeader.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
