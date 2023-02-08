package sb.er.tictatctoe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sb.er.tictatctoe.model.Game;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private Game.GameState gameState;
    private List<List<Character>> turns;
}
