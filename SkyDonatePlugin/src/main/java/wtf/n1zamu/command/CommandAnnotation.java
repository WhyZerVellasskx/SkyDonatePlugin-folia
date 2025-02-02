package wtf.n1zamu.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {
    String getName();

    String getUsage();

    int getNeedArgs();
}
