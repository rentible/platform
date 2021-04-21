package hu.lanoga.flatshares.bootstrap;

import hu.lanoga.flatshares.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Only for test purpose
 */
@Slf4j
@Component
@Profile("dev")
public class FlatSharesBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;

    public FlatSharesBootstrap(UserService userService) {
        this.userService = userService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		initUsers();
	}

	private void initUsers() {
//        User user = userService.findByUsername("user");
//        user.setEnabled(true);
//
//        userService.update(user);
//
//		User user2 = userService.findByUsername("user2");
//		user2.setEnabled(true);
//
//		userService.update(user2);
	}
}
