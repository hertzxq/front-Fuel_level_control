package org.sasha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AzsInfoRepository extends JpaRepository<AzsInfo, Long> {
}
