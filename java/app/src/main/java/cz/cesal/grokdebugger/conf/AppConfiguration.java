package cz.cesal.grokdebugger.conf;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AppConfiguration {

    @Getter
    @Setter
    private static int httpPort = 8080;

    @Getter
    @Setter
    private static String customPatternsDir;

}