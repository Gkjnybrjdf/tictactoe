package sb.er.tictatctoe.model;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private GameState state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turn> turns;

    @Basic(optional = false)
    private OffsetDateTime modifiedAt;

    @PrePersist
    @PreUpdate
    private void updateModifiedAt() {
        modifiedAt = OffsetDateTime.now();
    }

    public enum GameState {
        AI_WIN, PERSON_WIN, IN_PROGRESS;

        public static GameState ofPlayer(Turn.Player winner) {
            switch (winner) {
                case PERSON:
                    return PERSON_WIN;
                case AI:
                    return AI_WIN;
            }

            throw new IllegalArgumentException();
        }
    }
}
