package sb.er.tictatctoe.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sb.er.tictatctoe.Endpoints;
import sb.er.tictatctoe.controller.IGameController;
import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.exception.GameRuntimeException;
import sb.er.tictatctoe.model.Turn.Player;
import sb.er.tictatctoe.service.IGameService;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static sb.er.tictatctoe.Constants.GAME_SESSION_ATTRIBUTE_NAME;

@Slf4j
@RestController
@RequestMapping(Endpoints.GAME)
@RequiredArgsConstructor
public class GameController implements IGameController {
    private final IGameService gameService;
    private final HttpSession session;

    @GetMapping
    @Override
    public GameDto get() {
        UUID sessionId = getSessionId();

        log.info("Get requested, session id = {}", sessionId);
        return gameService.get(sessionId);
    }

    @PostMapping
    @Override
    public GameDto create(@RequestParam(name = "first") Player first) {
        UUID sessionId = UUID.randomUUID();
        session.setAttribute(GAME_SESSION_ATTRIBUTE_NAME, sessionId);

        log.info("Create requested, session id = {}", sessionId);
        return gameService.create(sessionId, first);
    }

    @PutMapping
    @Override
    public GameDto move(@RequestBody TurnDto turnDto) {
        UUID sessionId = getSessionId();

        log.info("Move requested, session id = {}", sessionId);
        return gameService.doMove(sessionId, turnDto);
    }

    @DeleteMapping
    @Override
    public GameDto undo() {
        UUID sessionId = getSessionId();

        log.info("Undo requested, session id = {}", sessionId);
        return gameService.undo(sessionId);
    }

    private UUID getSessionId() {
        Object attribute = session.getAttribute(GAME_SESSION_ATTRIBUTE_NAME);

        if (attribute == null) {
            throw new GameRuntimeException();
        }

        return (UUID) attribute;
    }
}
