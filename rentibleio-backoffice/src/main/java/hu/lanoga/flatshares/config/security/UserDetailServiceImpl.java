package hu.lanoga.flatshares.config.security;

import hu.lanoga.flatshares.exception.IPBlockedException;
import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.model.UserPrincipal;
import hu.lanoga.flatshares.repository.RoleMapper;
import hu.lanoga.flatshares.service.UserDetailService;
import hu.lanoga.flatshares.service.UserService;
import hu.lanoga.flatshares.util.DateAndTimeUtil;
import hu.lanoga.flatshares.util.SecurityUtil;
import org.owasp.encoder.Encode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserDetailService userDetailService;
    private final RoleMapper roleMapper;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    public UserDetailServiceImpl(UserService userService, UserDetailService userDetailService, RoleMapper roleMapper, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.roleMapper = roleMapper;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        String ip = SecurityUtil.getClientIP(request);
        if (loginAttemptService.isBlocked(ip)) {
            throw new IPBlockedException(Encode.forJava("Ip " + ip + " is blocked. Too much wrong attempts!"));
        } else {
            User user = userService.findByUsername(username);

            if (Objects.nonNull(user)) {
                user.setRoles(roleMapper.findAllByUserId(user.getId()));
                user.setLastLoggedIn(DateAndTimeUtil.now());

                userService.updateWithoutUserDetail(user);

                return new UserPrincipal(user);
            } else {
                throw new UsernameNotFoundException(Encode.forJava(username + " username does not exist"));
            }
        }
    }
}
