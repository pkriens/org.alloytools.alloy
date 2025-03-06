package org.alloytools.alloy.classic.provider;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alloytools.alloy.core.api.SourceResolver;
import org.alloytools.alloy.core.api.ToolChain;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.parser.AlloyCompilerANTLR;

public class PardinusToolchain implements ToolChain{
    final String id;
    final String name;
	final String description;

    PardinusToolchain(String id, String name, String description){
        this.description = description;
		this.id = id;
        this.name = name;

    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }


    @Override
    public Set<String> tags() {
        return Collections.emptySet();
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public List<OptionDescription< ? >> optionsDescription() {
        return Collections.emptyList();
    }

    @Override
    public TModule compile(URI path, Map<String,Object> options, SourceResolver... resolvers) {
        return null;
    }

    @Override
    public TModule compile(String source, URI path, Map<String,Object> options, SourceResolver... resolvers) {
        AlloyCompilerANTLR.compile(source, options, new PardinusModuleBuilder());
    }

}
