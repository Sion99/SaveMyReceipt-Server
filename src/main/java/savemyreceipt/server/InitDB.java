package savemyreceipt.server;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.domain.Group;
import savemyreceipt.server.domain.User;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit() {

            User user = User.builder()
                .email("kmicety1@gmail.com")
                .password(passwordEncoder.encode("dr0907"))
                .name("신시온")
                .authority(Authority.ROLE_ADMIN)
                .build();

            User user2 = User.builder()
                .email("yelnets99@naver.com")
                .password(passwordEncoder.encode("dr0907"))
                .name("김시온")
                .authority(Authority.ROLE_USER)
                .build();

            Group group = Group.builder()
                .name("포항대흥교회")
                .department("1청년부")
                .description("포항대흥교회 1청년부 디모데입니다.")
                .build();
            em.persist(group);

            user.joinGroup(group);
            user2.joinGroup(group);
            em.persist(user);
            em.persist(user2);
        }
    }
}
