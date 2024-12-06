package org.sasha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    // Кастомный запрос для выбора только нужных столбцов
    @Query("SELECT new org.sasha.RecordDTO(u.id, u.login, u.job) FROM Record u")
    List<RecordDTO> findAllUsersWithSelectedFields();
}
