package org.sasha;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository RecordRepository, RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<RecordDTO> getAllUsers() {
        return recordRepository.findAllUsersWithSelectedFields();
    }
}
