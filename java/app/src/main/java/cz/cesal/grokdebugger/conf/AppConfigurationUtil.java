package cz.cesal.grokdebugger.conf;

import org.springframework.core.env.AbstractEnvironment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

public class AppConfigurationUtil {

    private AppConfigurationUtil() {
    }

    public static void loadFromProperties(Properties properties) {
        checkEncoding();
        fillValues();
    }

    private static void fillValues() {
        System.out.println("Setting configuration values");

        Field[] declaredFields = AppConfiguration.class.getDeclaredFields();
        List<Field> staticFields = new ArrayList<>();
        for (Field f : declaredFields) {
            try {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                    Class t = f.getType();

                    if (System.getProperty(f.getName()) != null) {
                        String value = System.getProperty(f.getName());
                        value = value.replaceAll("^\"|\"$", "");
                        f.setAccessible(true);
                        System.out.println("Setting " + f.getName() + " = " + value);

                        if (t.getSimpleName().equalsIgnoreCase("int") || t.getSimpleName().equalsIgnoreCase("Integer")) {
                            f.set(null, Integer.valueOf(value).intValue());

                        } else if (t.getSimpleName().equalsIgnoreCase("String")) {
                            f.set(null, value);

                        } else if (t.getSimpleName().equalsIgnoreCase("boolean")) {
                            f.set(null, Boolean.valueOf(value));

                        } else {
                            System.err.println("Unknown type (" + t.getSimpleName() + ")");
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        initRunningProfile();
        setTimezone();

        prepareParameters();

        System.out.println("");
        System.out.println("Listing all filled configuration properties:");
        System.out.println("");
        for (Field f : declaredFields) {
            try {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    if (f.get(null) != null) {
                        System.out.println(f.getName() + " (" + f.getType().getSimpleName() + ") = " + f.get(null));
                        if (!f.getType().getSimpleName().equalsIgnoreCase("List")) {
                            try {
                                System.setProperty(f.getName(), String.valueOf(f.get(null)));
                            } catch (Exception e) {
                                System.err.println("Cannot set System property " + f.getName());
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("");
    }

    private static void prepareParameters() {
        // HTTP bind
        System.setProperty("server.port", String.valueOf(AppConfiguration.getHttpPort()));
        System.out.println("HTTP server port = " + System.getProperty("server.port"));
        System.out.println("");
    }

    private static void initRunningProfile() {
        String profileProp = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if (profileProp == null || profileProp.isEmpty()) {
            System.out.println("-D" + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME + " param is not set, setting default 'dev' profile");
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
        }
        System.out.println("Active profile = '" + System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME) + "'");
        System.out.println("");
    }

    private static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Prague"));
        System.out.println("Setting Java time zone to " + TimeZone.getDefault().getDisplayName());
    }

    private static void checkEncoding() {
        String fe = "file.encoding";
        String v = "UTF-8";
        if (System.getProperty(fe) == null) {
            System.out.println("");
            System.out.println("-------------------------------------");
            System.out.println("[WARNING] Property " + fe + " is NULL, set it manually to " + v);
            System.out.println("-------------------------------------");
            System.out.println("");
        } else if (!System.getProperty(fe).equalsIgnoreCase("UTF-8")) {
            System.out.println("");
            System.out.println("-------------------------------------");
            System.out.println("[WARNING] Property " + fe + " has wrong value " + System.getProperty(fe) + ", set it manually to " + v);
            System.out.println("-------------------------------------");
            System.out.println("");
        } else {
            System.out.println("Property " + fe + " has right value " + System.getProperty(fe));
            System.out.println("");
        }
    }

}