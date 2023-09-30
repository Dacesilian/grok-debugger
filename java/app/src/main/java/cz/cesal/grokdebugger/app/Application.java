package cz.cesal.grokdebugger.app;

import cz.cesal.grokdebugger.conf.AppConfigurationUtil;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"cz.cesal.grokdebugger"})
public class Application {

    private static Logger LOGGER;

    @Autowired
    private ApplicationContext ctx;

    public static void main(String[] args) {
        AppConfigurationUtil.loadFromProperties(System.getProperties());

        System.out.println("   _____           _    _____       _                                 ");
        System.out.println("  / ____|         | |  |  __ \\     | |                                ");
        System.out.println(" | |  __ _ __ ___ | | _| |  | | ___| |__  _   _  __ _  __ _  ___ _ __ ");
        System.out.println(" | | |_ | '__/ _ \\| |/ / |  | |/ _ \\ '_ \\| | | |/ _` |/ _` |/ _ \\ '__|");
        System.out.println(" | |__| | | | (_) |   <| |__| |  __/ |_) | |_| | (_| | (_| |  __/ |   ");
        System.out.println("  \\_____|_|  \\___/|_|\\_\\_____/ \\___|_.__/ \\__,_|\\__, |\\__, |\\___|_|   ");
        System.out.println("                                                 __/ | __/ |          ");
        System.out.println("                                                |___/ |___/           ");

        System.setProperty("logcoreApp", "worker");

        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    protected void postConstruct() {
        Application.LOGGER = LogManager.getLogger(Application.class);
        LOGGER.info("Start class postConstruct");
    }
}