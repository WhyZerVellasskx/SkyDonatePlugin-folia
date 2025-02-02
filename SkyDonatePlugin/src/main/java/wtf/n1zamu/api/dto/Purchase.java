package wtf.n1zamu.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Purchase {
    private String username;
    private String productId;
    private List<String> usedCommands;
    private Map<String, String> placeholders;
}
