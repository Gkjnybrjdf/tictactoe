package sb.er.tictatctoe.model;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "t_row")
    @Basic(optional = false)
    private Integer row;

    @Column(name = "t_column")
    @Basic(optional = false)
    private Integer column;

    @Basic(optional = false)
    private Player player;

    @Basic(optional = false)
    private OffsetDateTime createdAt;

    @Getter
    @RequiredArgsConstructor
    public enum Player {
        PERSON('X'), AI('O');

        private final Character icon;
    }
}
