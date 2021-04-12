package cn.intotw.springconfig.apollo;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.concurrent.Callable;

public class SpringConfigInterceptor {
    private static final String[] APOLLO_SYSTEM_PROPERTIES_APPEND = {"env","apollo.configService"};
    @RuntimeType
    public static void intercept(@Argument(0) Object args1, @SuperCall Callable<?> callable) throws Exception {

        try {
            initializeSystemProperty((ConfigurableEnvironment) args1);
            callable.call();
        } finally {
            System.out.println("after advice");
        }
    }
    static void initializeSystemProperty(ConfigurableEnvironment environment) {
        for (String propertyName : APOLLO_SYSTEM_PROPERTIES_APPEND) {
            fillSystemPropertyFromEnvironment(environment, propertyName);
        }
    }
    static void fillSystemPropertyFromEnvironment(ConfigurableEnvironment environment, String propertyName) {
        if (System.getProperty(propertyName) != null) {
            return;
        }

        String propertyValue = environment.getProperty(propertyName);

        if (propertyValue==null ||"".equals(propertyValue)) {
            return;
        }

        System.setProperty(propertyName, propertyValue);
    }
}