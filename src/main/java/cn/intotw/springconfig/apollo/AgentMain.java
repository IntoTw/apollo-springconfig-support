package cn.intotw.springconfig.apollo;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;
import static net.bytebuddy.matcher.ElementMatchers.takesNoArguments;

/**
 * Created by Chenxiang
 *
 * @generator: IntelliJ IDEA
 * @description: agent启动类
 * @project: apollo-springconfig-support
 * @package: PACKAGE_NAME
 * @date: 2021年04月09日 09时58分
 */
public class AgentMain {

    public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException {
        System.out.println("进入premain");
        System.out.println("isRedefineClassesSupported: " + inst.isRedefineClassesSupported());

        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer"))
                .transform((builder, type, loader, module) -> builder
                        .method(named("postProcessEnvironment"))
                        .intercept(MethodDelegation.to(SpringConfigInterceptor.class)))
                .installOn(inst);
    }

}
