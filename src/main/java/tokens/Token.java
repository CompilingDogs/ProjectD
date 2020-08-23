package tokens;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String token;
    private Integer line;
    private Integer column;

}
