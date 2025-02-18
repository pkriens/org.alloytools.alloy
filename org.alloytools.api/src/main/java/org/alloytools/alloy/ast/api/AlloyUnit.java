package org.alloytools.alloy.ast.api;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public record AlloyUnit(String comment, Map<String,OptionDecl> options, ModuleDecl module, ImportDecl[] imports, Map<String,SigDecl> sigs, Map<String,FactDecl> facts, Map<String,FunDecl> functions, Map<String,AssertDecl> asserts, Map<String,CommandDecl> commands) {

    public class Pos<T> implements Supplier<T> {

        final T    value;
        final File file;
        final int  line;
        final int  column;

        public Pos(T value, File file, int line, int column) {
            this.column = column;
            this.line = line;
            this.file = file;
            this.value = value;
        }


        boolean isPresent() {
            return value != null;
        }

        @Override
        public T get() {
            if (value != null)
                return value;

            throw new IllegalArgumentException("not set");
        }
    }

    public record ModuleDecl(Pos<String> qualName, String[] formalArguments) {
    }

    public record ImportDecl(String qualName, String as, String[] formalArguments) {
    }

    public enum Qualifier {
                           VAR,
                           ABSTRACT,
                           PRIVATE,
                           LONE,
                           ONE,
                           SOME
    };


    public record FieldDecl(Pos<Boolean> var) {
    }

    public record Block() {
    }

    public record SigDecl(Pos<String> name, Set<Pos<Qualifier>> qualifers, Pos<String> extend, Set<Pos<String>> in, Map<String,FieldDecl> fields, Block constraints) {
    }

}
