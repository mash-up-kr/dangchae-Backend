package kr.mashup.udada.user.domain;

import kr.mashup.udada.user.common.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndVendor(String username, Vendor vendor);
    Optional<User> findByUsername(String username);
}
