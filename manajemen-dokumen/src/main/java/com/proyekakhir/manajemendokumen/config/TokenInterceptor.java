package com.proyekakhir.manajemendokumen.config;

import com.proyekakhir.manajemendokumen.auth.Secured;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.constant.RoleUser;
import com.proyekakhir.manajemendokumen.exception.UnauthorizedException;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import com.proyekakhir.manajemendokumen.user.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.proyekakhir.manajemendokumen.util.Constants.HEADER_STRING;
import static com.proyekakhir.manajemendokumen.util.Constants.TOKEN_PREFIX;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    Logger logger = Logger.getLogger(getClass().getName());

    CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    public TokenInterceptor(CurrentUser currentUser, Environment environment) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
                User user = userService.findByUsername(username);

                if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(authToken, user))) {
                    HandlerMethod method = (HandlerMethod) handler;
                    Secured anno = method.getMethodAnnotation(Secured.class);

                    boolean allowed = false;
                    try {
                        RoleUser role = RoleUser.valueOf(user.getRole());
                        allowed = anno == null || anno.value().length == 0 || Arrays.stream(anno.value()).anyMatch(s -> s.equals(role));
                    } catch (Exception e) {
                        throw UnauthorizedException.create("Unauthorized");
                    }

                    if (!allowed) {
                        throw UnauthorizedException.create("Unauthorized");
                    }
                    currentUser.set(user.getEmail(), user.getRole(), user.getUsername(), user.getName());

                } else {
                    throw new UnauthorizedException("Unauthorized");
                }
            } catch (Exception e){
                throw new UnauthorizedException("Unauthorized");
            }
        } else {
            logger.warning("couldn't find bearer string, will ignore the header");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
