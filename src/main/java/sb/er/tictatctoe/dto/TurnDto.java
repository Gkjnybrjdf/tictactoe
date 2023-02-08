package sb.er.tictatctoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TurnDto {
    private int row;
    private int column;
}
