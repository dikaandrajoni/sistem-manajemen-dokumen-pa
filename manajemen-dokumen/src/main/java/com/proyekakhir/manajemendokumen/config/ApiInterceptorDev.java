package com.proyekakhir.manajemendokumen.config;

import com.proyekakhir.manajemendokumen.auth.Secured;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.constant.RoleUser;
import com.proyekakhir.manajemendokumen.exception.ResourceForbiddenException;
import com.proyekakhir.manajemendokumen.exception.ResourceNotFoundException;
import com.proyekakhir.manajemendokumen.exception.UnauthorizedException;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class ApiInterceptorDev implements HandlerInterceptor {

    private CurrentUser currentUser;

    @Autowired
    UserRepository userRepository;

    @Value("${app.mode:final}")
    String mode;

    @Autowired
    public ApiInterceptorDev(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("options")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String userId = "dev".equals(mode) ? request.getHeader("User-ID") : null;
        if (!StringUtils.isAnyEmpty(currentUser.getUserId())) {
            userId = currentUser.getUserId();
        }

        if (StringUtils.isAnyEmpty(userId)) {
            throw UnauthorizedException.create("Unauthorized Access");
        }
        if (!(handler instanceof HandlerMethod)) {
            throw ResourceNotFoundException.create("Invalid url");
        }
        HandlerMethod method = (HandlerMethod) handler;
        Secured anno = method.getMethodAnnotation(Secured.class);
        User user = userRepository.findByEmail(userId);
        boolean allowed = false;
        if (user != null){
            try {
                RoleUser role = RoleUser.valueOf(user.getRole());
                allowed = anno == null || anno.value().length == 0 || Arrays.stream(anno.value()).anyMatch(s -> s.equals(role));
            } catch (Exception e) {
                throw UnauthorizedException.create("Unauthorized");
            }
            currentUser.set(user.getEmail(), user.getRole(), user.getUsername(), user.getName());
        } else {
            throw UnauthorizedException.create("Unauthorized Access");
        }

        if (!allowed) {
            throw ResourceForbiddenException.create("Forbidden Access");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
