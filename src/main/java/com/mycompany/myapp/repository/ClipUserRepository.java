package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ClipUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClipUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClipUserRepository extends JpaRepository<ClipUser, Long> {}
