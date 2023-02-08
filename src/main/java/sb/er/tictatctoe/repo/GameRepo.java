package sb.er.tictatctoe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.er.tictatctoe.model.Game;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, Long> {
    Optional<Game> findBySessionId(UUID sessionId);

    int deleteByModifiedAtBefore(OffsetDateTime modifiedAt);
}
