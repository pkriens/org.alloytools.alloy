package org.alloytools.alloy.model.api;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Represents an Alloy Module
 */
public interface TModule {

    /**
     * The source path of this module. In certain cases a module does not have a
     * path, for example when it is compiled from a string.
     *
     * @return an optional file path to a module
     */
    URI uri();

    /**
     * Get the defined sigs in this module
     *
     * @return a list of sigs
     */
    List<TSig> sigs();


    /**
     * Get any run commands defined in the module
     *
     * @return the list of available run commands
     */
    List<TRun> runs();

    /**
     * Get any check commands defined in the module
     *
     * @return the list of available check commands
     */
    List<TCheck> checks();

    /**
     * Get compiler warnings and errors
     *
     * @return compiler warnings
     */
    List<UserMessage> messages();

    /**
     * Return true if this module has no fatal errors.
     *
     * @return true if no fatal errors
     */
    boolean isValid();

    /**
     * Get the options in the source for the given command. A source option is
     * specified with {@code--option[suffix] option}. The suffix is by default
     * {@code *} which implies all.
     *
     * @param command
     * @return Options given in the source for the given command
     */
    Map<String,Object> options(TCommand command);

    List<TFun> functions();

    List<TPred> preds();

    List<TFact> facts();

    List<TMacro> macros();

    List<String> formalArguments();

    String simpleName();
}
