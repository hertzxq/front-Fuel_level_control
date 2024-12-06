package org.sasha;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/azs-info")
public class AzsInfoController {

    @Autowired
    private AzsInfoRepository azsInfoRepository;

    // Получение всех записей
    @GetMapping
    public ResponseEntity<List<AzsInfo>> getAllAzsInfo() {
        List<AzsInfo> azsInfoList = azsInfoRepository.findAll();
        return ResponseEntity.ok(azsInfoList);
    }

    // Добавление новой записи
    @PostMapping
    public ResponseEntity<AzsInfo> createAzsInfo(@RequestBody AzsInfo azsInfo) {
        AzsInfo savedAzsInfo = azsInfoRepository.save(azsInfo);
        return ResponseEntity.ok(savedAzsInfo);
    }
}
