package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Clips;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Clips entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClipsRepository extends JpaRepository<Clips, Long> {}
