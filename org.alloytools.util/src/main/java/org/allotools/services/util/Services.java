package org.allotools.services.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Abstracts away the use of the service loader because I do not like how it is
 * tied to statics and classes. By abstracting it we can later insert other
 * mechanisms.
 */
public class Services {

    /**
     * Return a list of services from the service loader.
     * @param <S>
     * @param serviceType
     * @return a list of services
     */
    public static <S> List<S> getServices(Class<S> serviceType) {
        List<S> result = new ArrayList<>();
        for (S s : ServiceLoader.load(serviceType)) {
            result.add(s);
        }
        return result;
    }

    /**
     * Return a Supplier for a list of services. This can be used in initialisation
     * and not delay it.
     *
     * @param <S>
     * @param serviceType
     * @return a supplier of {@link #getServices(Class)}
     */

    public static <S> Supplier<List<S>> getDeferredServices(Class<S> serviceType) {
        return new Supplier<List<S>>() {
            final List<S> list = new ArrayList<>();

            @Override
            public List<S> get() {
                if (list.isEmpty())
                    list.addAll(getServices(serviceType));
                return list;
            }
        };
    }


}
