package de.ceruti.curcuma.keyvaluebinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })

/**
 * FIXME move to api
 *
 */
public @interface KeyValueBindingCreator {

}
