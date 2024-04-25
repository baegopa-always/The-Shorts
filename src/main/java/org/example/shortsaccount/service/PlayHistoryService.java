package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.dto.AddPlayHistoryRequest;
import org.example.shortsaccount.repository.PlayHistoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlayHistoryService {
    private final PlayHistoryRepository playHistoryRepository;

    public PlayHistory save(AddPlayHistoryRequest request) {
        return playHistoryRepository.save(request.toEntity());
    }
}
