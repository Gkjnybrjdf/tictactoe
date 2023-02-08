package sb.er.tictatctoe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sb.er.tictatctoe.service.IGameCleanScheduleService;
import sb.er.tictatctoe.service.IGameService;

import java.time.OffsetDateTime;

import static sb.er.tictatctoe.Constants.DAYS_WITHOUT_UPDATE_LIMIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameCleanScheduleService implements IGameCleanScheduleService {
    private final IGameService gameService;

    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void schedule() {
        log.info("Clean started");
        int removed = gameService.removeOld(OffsetDateTime.now().minusDays(DAYS_WITHOUT_UPDATE_LIMIT));
        log.info("Clean ended, removed = {}", removed);
    }
}
