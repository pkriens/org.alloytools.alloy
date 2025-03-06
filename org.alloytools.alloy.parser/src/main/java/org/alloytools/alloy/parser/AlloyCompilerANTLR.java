package org.alloytools.alloy.parser;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alloytools.alloy.builder.api.FormulaBuilder;
import org.alloytools.alloy.builder.api.ModuleBuilder;
import org.alloytools.alloy.builder.api.ModuleBuilder.CommandDecl;
import org.alloytools.alloy.builder.api.ModuleBuilder.CommandType;
import org.alloytools.alloy.builder.api.ModuleBuilder.Decl;
import org.alloytools.alloy.builder.api.ModuleBuilder.KeyValue;
import org.alloytools.alloy.builder.api.ModuleBuilder.TypeScope;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.builder.api.ValueBuilder;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.Qualifier;
import org.alloytools.alloy.model.api.UserMessage;
import org.alloytools.alloy.model.api.UserMessage.Severity;
import org.alloytools.alloy.parser.AlloyParser.AndFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.ArrowValueContext;
import org.alloytools.alloy.parser.AlloyParser.AssertDeclContext;
import org.alloytools.alloy.parser.AlloyParser.AtnameValueContext;
import org.alloytools.alloy.parser.AlloyParser.BinaryFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.BlockFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.CallValueContext;
import org.alloytools.alloy.parser.AlloyParser.CardinalityValueContext;
import org.alloytools.alloy.parser.AlloyParser.CommandDeclContext;
import org.alloytools.alloy.parser.AlloyParser.ComparisonFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.ComprehensionValueContext;
import org.alloytools.alloy.parser.AlloyParser.ConstantContext;
import org.alloytools.alloy.parser.AlloyParser.ConstantValueContext;
import org.alloytools.alloy.parser.AlloyParser.DeclContext;
import org.alloytools.alloy.parser.AlloyParser.DotJoinValueContext;
import org.alloytools.alloy.parser.AlloyParser.EnumDeclContext;
import org.alloytools.alloy.parser.AlloyParser.FactDeclContext;
import org.alloytools.alloy.parser.AlloyParser.FormulaContext;
import org.alloytools.alloy.parser.AlloyParser.FunDeclContext;
import org.alloytools.alloy.parser.AlloyParser.IffFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.ImportDeclContext;
import org.alloytools.alloy.parser.AlloyParser.IntersectionValueContext;
import org.alloytools.alloy.parser.AlloyParser.LabelCommandDeclContext;
import org.alloytools.alloy.parser.AlloyParser.LetContext;
import org.alloytools.alloy.parser.AlloyParser.MacroDeclContext;
import org.alloytools.alloy.parser.AlloyParser.MetaValueContext;
import org.alloytools.alloy.parser.AlloyParser.ModuleDeclContext;
import org.alloytools.alloy.parser.AlloyParser.MultiplicityFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.NameContext;
import org.alloytools.alloy.parser.AlloyParser.NumberContext;
import org.alloytools.alloy.parser.AlloyParser.NumberValueContext;
import org.alloytools.alloy.parser.AlloyParser.OrFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.ParenthesisFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.ParenthesisValueContext;
import org.alloytools.alloy.parser.AlloyParser.PredDeclContext;
import org.alloytools.alloy.parser.AlloyParser.PrimeValueContext;
import org.alloytools.alloy.parser.AlloyParser.PrimitiveValueContext;
import org.alloytools.alloy.parser.AlloyParser.QnameContext;
import org.alloytools.alloy.parser.AlloyParser.QnameValueContext;
import org.alloytools.alloy.parser.AlloyParser.QuantificationContext;
import org.alloytools.alloy.parser.AlloyParser.RelationOverrideValueContext;
import org.alloytools.alloy.parser.AlloyParser.RestrictionValueContext;
import org.alloytools.alloy.parser.AlloyParser.SigDeclContext;
import org.alloytools.alloy.parser.AlloyParser.SumValueContext;
import org.alloytools.alloy.parser.AlloyParser.TypescopeContext;
import org.alloytools.alloy.parser.AlloyParser.UnaryFormulaContext;
import org.alloytools.alloy.parser.AlloyParser.UnaryOpValueContext;
import org.alloytools.alloy.parser.AlloyParser.UnionDifferenceValueContext;
import org.alloytools.alloy.parser.AlloyParser.ValueContext;
import org.alloytools.alloy.parser.AlloyParser.VarDeclContext;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import aQute.lib.converter.TypeReference;

public class AlloyCompilerANTLR {

	class ErrorListener implements ANTLRErrorListener {

		final ModuleBuilder mb;

		ErrorListener(ModuleBuilder moduleBuilder) {
			this.mb = moduleBuilder;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int pos, String msg,
				RecognitionException e) {
			mb.add(new UserMessage(Severity.ERROR, null, msg));
		}

		@Override
		public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
				BitSet ambigAlts, ATNConfigSet configs) {
			mb.add(new UserMessage(Severity.DEBUG, null, "ambiguity"));
		}

		@Override
		public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
				BitSet conflictingAlts, ATNConfigSet configs) {
			mb.add(new UserMessage(Severity.DEBUG, null, "reportAttemptingFullContext"));
		}

		@Override
		public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
				ATNConfigSet configs) {
			mb.add(new UserMessage(Severity.DEBUG, null, "reportContextSensitivity"));
		}
	}

	class AlloyBaseListenerPlus extends AlloyBaseListener {
		final Map<ParseTree, Object> data = new IdentityHashMap<>();
		private final Map<ParseTree, ValueBuilder> values = new IdentityHashMap<>();
		private final Map<ParseTree, FormulaBuilder> formulas = new IdentityHashMap<>();
		private final Map<ParseTree, Decl> decls = new IdentityHashMap<>();

		@SuppressWarnings("unchecked")
		<T> Pos<T> get(Object context, Class<T> type) {
			Object object = data.get(context);
			assert object != null;
			return (Pos<T>) type.cast(object);
		}

		@SuppressWarnings("unchecked")
		<T> Pos<T> get(Object context, TypeReference<T> typeRef) {
			Object object = data.get(context);
			assert object != null;
			return (Pos<T>) object;
		}

		Pos<String> get(Object context) {
			return get(context, String.class);
		}

		<T> List<Pos<T>> get(Collection<? extends ParseTree> context, Class<T> memberType) {
			return context.stream().map(ctx -> get(ctx, memberType)).toList();
		}

		<T> Pos<T> set(ParseTree context, T value) {
			Interval sourceInterval = context.getSourceInterval();
			Pos<T> pos = new Pos<T>(value, sourceInterval.a, sourceInterval.b);
			data.put(context, pos);
			return pos;
		}

		Pos<Boolean> toPos(Token nd) {
			if (nd == null)
				return new Pos<>(false, 0, 0);
			else
				return new Pos<Boolean>(true, nd.getStartIndex(), nd.getStopIndex());
		}

		void putValue(ParseTree ctx, ValueBuilder result) {
			putValue(ctx, result);
		}

		ValueBuilder getValue(ParseTree ctx) {
			return values.get(ctx);
		}

		void putFormula(ParseTree ctx, FormulaBuilder result) {
			formulas.put(ctx, result);
		}

		FormulaBuilder getFormula(ParseTree ctx) {
			return formulas.get(ctx);
		}

		void putDecl(ParseTree ctx, Decl result) {
			decls.put(ctx, result);
		}

		Decl getDecl(ParseTree ctx) {
			return decls.get(ctx);
		}

		List<Decl> getDecls(List<? extends ParseTree> ctx) {
			return ctx.stream().map(this::getDecl).toList();
		}
	}

	class AlloyToModule extends AlloyBaseListenerPlus {
		final ModuleBuilder mb;

		AlloyToModule(ModuleBuilder moduleBuilder) {
			this.mb = moduleBuilder;
		}

		/**
		 * MODULE
		 */
		@Override
		public void exitModuleDecl(ModuleDeclContext ctx) {
			Pos<String> moduleName = get(ctx.qname());
			List<Pos<String>> formalArguments = get(ctx.names().name(), String.class);
			mb.module(moduleName, formalArguments);
		}

		/**
		 * OPEN
		 */
		@Override
		public void exitImportDecl(ImportDeclContext ctx) {
			Pos<String> qname = get(ctx.qname());
			List<Pos<String>> typeReferences = get(ctx.qnames().qname(), String.class);
			Pos<String> as = get(ctx.name(), String.class);
			mb.open(qname, typeReferences, as);
		}

		/**
		 * MACRO
		 */
		@Override
		public void exitMacroDecl(MacroDeclContext ctx) {
			Pos<String> name = get(ctx.name());
			List<Pos<String>> formalArguments = get(ctx.names().name(), String.class);
			ValueBuilder expr = getValue(ctx.expr());
			mb.macro(name, formalArguments, expr);
		}

		/**
		 * SIG
		 */

		@Override
		public void exitSigDecl(SigDeclContext ctx) {
			List<Pos<Qualifier>> qualifiers = get(ctx.qualifier(), Qualifier.class);
			List<Pos<String>> sigNames = get(ctx.names().name(), String.class);

			List<Pos<String>> ins = null;
			Pos<String> extend = null;

			if (ctx.extend != null) {
				extend = get(ctx.extend);
			} else {
				ins = get(ctx.qname(),String.class);
			}

			List<Pos<Decl>> fields = get(ctx.varDecl(), Decl.class);
			FormulaBuilder block = getFormula(ctx.block());

			for (Pos<String> sig : sigNames) {
				mb.sig(qualifiers, sig, extend, ins, fields, block);
			}
		}

		/**
		 * ENUM
		 */

		@Override
		public void exitEnumDecl(EnumDeclContext ctx) {

			Set<Pos<Qualifier>> qualifiers = ctx.p != null
					? Set.of(new Pos<>(Qualifier.PRIVATE, ctx.p.getStartIndex(), ctx.p.getStopIndex()))
					: Collections.emptySet();

			Pos<String> name = get(ctx.name());
			List<Pos<String>> members = get(ctx.names().name(), String.class);
			mb.enum_(qualifiers, name, members);
		}

		/**
		 * FACT
		 */

		@Override
		public void exitFactDecl(FactDeclContext ctx) {
			Pos<String> name = get(ctx.name());
			FormulaBuilder block = getFormula(ctx.block());
			mb.fact(name, block);
		}

		/**
		 * PRED
		 */

		@Override
		public void exitPredDecl(PredDeclContext ctx) {
			Pos<String> pred = get(ctx.name());
			List<Decl> formalArguments = getDecls(ctx.arguments().decl());
			FormulaBuilder block = getFormula(ctx.block());
			mb.pred(pred, formalArguments, block);
		}

		/**
		 * FUN
		 */

		@Override
		public void exitFunDecl(FunDeclContext ctx) {
			Pos<String> pred = get(ctx.name());
			List<Decl> formalArguments = getDecls(ctx.arguments().decl());
			Pos<Multiplicity> multiplicity = get(ctx.multiplicity(), Multiplicity.class);
			ValueBuilder domain = getValue(ctx.value(0));
			ValueBuilder body = getValue(ctx.value(1));
			mb.fun(pred, formalArguments, multiplicity, domain, body);
		}

		/**
		 * ASSERT
		 */

		@Override
		public void exitAssertDecl(AssertDeclContext ctx) {
			Pos<String> pred = get(ctx.name());
			FormulaBuilder block = getFormula(ctx.block());
			mb.assert_(pred, block);
		}

		/**
		 * COMMAND
		 */

		@Override
		public void exitCommandDecl(CommandDeclContext ctx) {
			CommandType command = ctx.run() != null ? CommandType.RUN : CommandType.CHECK;
			Pos<String> label = get(ctx.name());
			Pos<String> predicate = get(ctx.qname());
			FormulaBuilder body = getFormula(ctx.block());
			List<Pos<TypeScope>> scopes = get(ctx.scope().typescope(), TypeScope.class);
			Pos<Integer> expect = get(ctx.number(), Integer.class);
			Pos<CommandDecl> implies = get(ctx.commandDecl(), CommandDecl.class);
			set(ctx, new CommandDecl(label, new Pos<>(command, 0, 0), predicate, body, scopes, expect, implies));
		}
		
		@Override
		public void enterLabelCommandDecl(LabelCommandDeclContext ctx) {
			Pos<CommandDecl> cmd = get(ctx.commandDecl(), CommandDecl.class);
			if ( ctx.getChildCount() == 2) {
				Pos<String> label = set(ctx.label(),ctx.label().name().getText());
				if ( cmd.get().label().isPresent())
					throw new IllegalArgumentException("label already set in command declaration");
				cmd = cmd.replace(cmd.get().label(label));
			}
			set(ctx,cmd);
		}

		/**
		 * VALUE
		 */

		/**
		 * ~ ^ *
		 */

		@Override
		public void exitUnaryOpValue(UnaryOpValueContext ctx) {
			String op = ctx.getChild(0).getText();
			ValueBuilder value = getValue(ctx.value());
			ValueBuilder result = switch (op) {
			case "~" -> mb.transpose(value);
			case "^" -> mb.transitiveClosure(value);
			case "*" -> mb.reflexiveTransitiveClosure(value);
			default -> throw new IllegalArgumentException("TODO");
			};
			putValue(ctx, result);
		}

		/**
		 * PRIME
		 */
		@Override
		public void exitPrimeValue(PrimeValueContext ctx) {
			ValueBuilder value = getValue(ctx.value());
			ValueBuilder result = mb.prime(value);
			putValue(ctx, result);
		}

		/**
		 * JOIN
		 */

		@Override
		public void exitDotJoinValue(DotJoinValueContext ctx) {
			List<ValueContext> contexts = ctx.value();
			ValueContext removed = contexts.remove(0);
			ValueBuilder left = getValue(removed);
			List<ValueBuilder> arguments = contexts.stream().map(this::getValue).toList();
			ValueBuilder result = mb.boxJoin(left, arguments);
			putValue(ctx, result);
		}

		@Override
		public void exitCallValue(CallValueContext ctx) {
			String name = ctx.qname().getText();
			List<ValueContext> contexts = ctx.value();
			List<ValueBuilder> arguments = contexts.stream().map(this::getValue).toList();
			ValueBuilder result = mb.call(name, arguments);
			putValue(ctx, result);
		}

		@Override
		public void exitRestrictionValue(RestrictionValueContext ctx) {
			valueOp(ctx, ctx.value(0), ctx.getChild(1).getText(), ctx.value(1));
		}

		@Override
		public void exitIntersectionValue(IntersectionValueContext ctx) {
			valueOp(ctx, ctx.value(0), ctx.getChild(1).getText(), ctx.value(1));
		}

		@Override
		public void exitRelationOverrideValue(RelationOverrideValueContext ctx) {
			valueOp(ctx, ctx.value(0), ctx.getChild(1).getText(), ctx.value(1));
		}

		@Override
		public void exitCardinalityValue(CardinalityValueContext ctx) {
			valueOp(ctx, ctx.value(), ctx.getChild(0).getText(), null);
		}

		@Override
		public void exitUnionDifferenceValue(UnionDifferenceValueContext ctx) {
			valueOp(ctx, ctx.value(0), ctx.getChild(1).getText(), ctx.value(1));
		}

		@Override
		public void exitComprehensionValue(ComprehensionValueContext ctx) {
			List<Decl> decls = getDecls(ctx.decl());
			FormulaBuilder block = getFormula(ctx.block());
			if (block == null)
				block = getFormula(ctx.bar());
			ValueBuilder result = mb.comprehension(decls, block);
			putValue(ctx, result);
		}

		@Override
		public void exitPrimitiveValue(PrimitiveValueContext ctx) {
			valueOp(ctx, ctx.value(0), ctx.getChild(1).getText(), ctx.value(1));
		}

		@Override
		public void exitSumValue(SumValueContext ctx) {
			List<Pos<Decl>> decl = get(ctx.decl(), Decl.class);
			ValueBuilder value = getValue(ctx.value());
			ValueBuilder result = mb.sum(decl.stream().map(p -> p.get()).toList(), value);
			putValue(ctx, result);
		}

		@Override
		public void exitParenthesisValue(ParenthesisValueContext ctx) {
			set(ctx, ctx.value());
		}

		@Override
		public void exitAtnameValue(AtnameValueContext ctx) {
			Pos<String> name = get(ctx.name(), String.class);
			ValueBuilder result = mb.atname(name);
			putValue(ctx, result);
		}

		@Override
		public void exitMetaValue(MetaValueContext ctx) {
			Pos<String> qname = get(ctx.qname(), String.class);
			ValueBuilder result = mb.metaReference(qname);
			putValue(ctx, result);
		}

		@Override
		public void exitQnameValue(QnameValueContext ctx) {
			Pos<String> qname = get(ctx.qname(), String.class);
			ValueBuilder result = mb.qname(qname);
			putValue(ctx, result);
		}

		@Override
		public void exitNumberValue(NumberValueContext ctx) {
			Pos<Integer> number = get(ctx.number(), Integer.class);
			ValueBuilder result = mb.number(number.get());
			putValue(ctx, result);
		}

		@Override
		public void exitConstantValue(ConstantValueContext ctx) {
			Pos<String> constant = get(ctx.constant());
			valueOp(ctx, null, constant.get(), null);
		}

		@Override
		public void exitArrowValue(ArrowValueContext ctx) {
			ValueBuilder left = getValue(ctx.value(0));
			Pos<Multiplicity> leftMultiplicity = get(ctx.multiplicity(0), Multiplicity.class);
			ValueBuilder right = getValue(ctx.value(1));
			Pos<Multiplicity> rightMultiplicity = get(ctx.multiplicity(1), Multiplicity.class);
			ValueBuilder result = mb.arrow(left, leftMultiplicity, rightMultiplicity, right);
			putValue(ctx, result);
		}

		@Override
		public void exitConstant(ConstantContext ctx) {
			String text = ctx.getText();
			set(ctx, text);
		}

		void valueOp(ParseTree ctx, ValueContext l, String op, ValueContext r) {
			ValueBuilder left = getValue(l);
			ValueBuilder right = getValue(r);

			ValueBuilder result = switch (op) {
			case "~" -> mb.transpose(left);
			case "^" -> mb.transitiveClosure(left);
			case "*" -> mb.reflexiveTransitiveClosure(left);
			case "." -> mb.dot(left, right);
			case "<:" -> mb.range(left, right);
			case ":>" -> mb.domain(left, right);
			case "&" -> mb.intersection(left, right);
			case "++" -> mb.override(left, right);
			case "#" -> mb.cardinality(left);
			case "+" -> mb.union(left, right);
			case "-" -> mb.difference(left, right);
			case "none" -> mb.none();
			case "univ" -> mb.univ();
			case "iden" -> mb.iden();
			case "this" -> mb.this_();
			default -> mb.primitive(op, left, right);
			};
			putValue(ctx, result);
		}

		/**
		 * FORMULA
		 */

		@Override
		public void exitMultiplicityFormula(MultiplicityFormulaContext ctx) {
			String mult = ctx.getChild(0).getText();
			ValueBuilder value = getValue(ctx.value());
			FormulaBuilder formula = switch (mult) {
			case "no" -> mb.no(value);
			case "lone" -> mb.lone(value);
			case "one" -> mb.one(value);
			case "set" -> mb.set(value);
			case "some" -> mb.some(value);
			default -> throw new IllegalArgumentException();
			};
			putFormula(ctx, formula);
		}

		@Override
		public void exitComparisonFormula(ComparisonFormulaContext ctx) {
			ValueBuilder left = getValue(ctx.value(0));
			ValueBuilder right = getValue(ctx.value(1));
			String op = ctx.getChild(1).getText();
			boolean not = false;
			if (op.equals("!") || op.equals("not")) {
				not = true;
				op = ctx.getChild(1).getText();
			}
			FormulaBuilder result = switch (op) {
			case "in" -> mb.in(left, right, not);
			case "=" -> mb.equal(left, right, not);
			case "<" -> mb.less(left, right, not);
			case ">" -> mb.greater(left, right, not);
			case "=<" -> mb.lessOrEqual(left, right, not);
			case ">=" -> mb.greaterOrEqual(left, right, not);
			default -> throw new IllegalArgumentException();
			};
			putFormula(ctx, result);
		}

		@Override
		public void exitUnaryFormula(UnaryFormulaContext ctx) {
			FormulaBuilder formula = getFormula(ctx.formula());
			String op = ctx.getChild(0).getText();
			FormulaBuilder result = switch (op) {
			case "!", "not" -> mb.not(formula);
			case "always" -> mb.always(formula);
			case "eventually" -> mb.always(formula);
			case "after" -> mb.always(formula);
			case "before" -> mb.always(formula);
			case "historically" -> mb.always(formula);
			case "once" -> mb.always(formula);
			default -> throw new IllegalArgumentException();
			};
			putFormula(ctx, result);
		}

		@Override
		public void exitBinaryFormula(BinaryFormulaContext ctx) {
			binaryFormula(ctx, ctx.formula(0), ctx.getChild(1).getText(), ctx.formula(1));
		}

		@Override
		public void exitAndFormula(AndFormulaContext ctx) {
			binaryFormula(ctx, ctx.formula(0), "and", ctx.formula(1));
		}

		@Override
		public void exitIffFormula(IffFormulaContext ctx) {
			binaryFormula(ctx, ctx.formula(0), ctx.getChild(1).getText(), ctx.formula(1));
		}

		@Override
		public void exitOrFormula(OrFormulaContext ctx) {
			binaryFormula(ctx, ctx.formula(0), "or", ctx.formula(1));
		}

		@Override
		public void exitLet(LetContext ctx) {
			List<NameContext> names = ctx.name();
			List<ValueContext> values = ctx.value();
			assert names.size() == values.size();
			List<KeyValue> keyValues = new ArrayList<>();
			for (int i = 0; i < names.size(); i++) {
				Pos<String> name = get(names.get(i));
				ValueBuilder value = getValue(names.get(i));
				KeyValue kv = new KeyValue(name, value);
				keyValues.add(kv);
			}
			FormulaBuilder block = getFormula(ctx.block());
			if (block == null)
				block = getFormula(ctx.block());

			FormulaBuilder result = mb.let(keyValues, block);
			putFormula(ctx, result);
		}

		@Override
		public void exitQuantification(QuantificationContext ctx) {
			String quantification = ctx.getChild(0).getText();
			List<Decl> decls = getDecls(ctx.decl());
			FormulaBuilder block = getFormula(ctx.block());
			if ( block == null)
				block = getFormula(ctx.block());
			
			FormulaBuilder result = switch(quantification) {
			case "all" -> mb.all(decls, block);
			case "no" -> mb.no(decls, block);
			case "lone" -> mb.lone(decls, block);
			case "one" -> mb.one(decls, block);
			case "some" -> mb.some(decls, block);
			default -> throw new IllegalArgumentException();
			};
			putFormula(ctx, result);
		}

		void binaryFormula(ParseTree ctx, FormulaContext left, String op, FormulaContext right) {
			FormulaBuilder l = getFormula(left);
			FormulaBuilder r = getFormula(right);
			FormulaBuilder result = switch (op) {
			case "releases" -> mb.releases(l, r);
			case "since" -> mb.since(l, r);
			case "triggered" -> mb.triggered(l, r);
			case "iff", "<=>" -> mb.iff(l, r);
			case "and", "&&" -> mb.and(l, r);
			case "or", "||" -> mb.or(l, r);
			default -> throw new IllegalArgumentException();
			};
			putFormula(ctx, result);
		}
		
		@Override
		public void exitParenthesisFormula(ParenthesisFormulaContext ctx) {
			FormulaBuilder l = getFormula(ctx.formula());
			putFormula(ctx,l);
		}

		@Override
		public void exitBlockFormula(BlockFormulaContext ctx) {
			FormulaBuilder l = getFormula(ctx.block().formula());
			putFormula(ctx,l);
		}
		
		/**
		 * TYPESCOPE
		 */
		@Override
		public void exitTypescope(TypescopeContext ctx) {
			boolean exactly = get(ctx.exactly()).isPresent();
			Pos<Integer> start = get(ctx.number(0), Integer.class);
			Pos<Integer> end = get(ctx.number(1), Integer.class);
			Pos<Integer> step = get(ctx.number(2), Integer.class);
			Pos<String> typeReference = get(ctx.qname());
			set(ctx, new TypeScope(exactly, start, end, step, typeReference));
		}

		/**
		 * helpers
		 */

		@Override
		public void exitNumber(NumberContext ctx) {
			String number = ctx.NUMBER().getText();
			String sign = ctx.getStart().getText();

			int value;
			if (number.startsWith("0x")) {
				value = Integer.parseInt(number, 16);
			} else if (number.startsWith("0x")) {
				value = Integer.parseInt(number, 2);
			} else
				value = Integer.parseInt(sign);

			if (sign.equals("-"))
				value = -value;
			set(ctx, value);
		}

		@Override
		public void exitDecl(DeclContext ctx) {
			Pos<String> disjNames = get(ctx.disj(0));
			List<Pos<String>> fieldNames = get(ctx.names().name(), String.class);
			Pos<String> disjValues = get(ctx.disj(1));
			Pos<Multiplicity> multiplicity = get(ctx.multiplicity(), Multiplicity.class);
			ValueBuilder value = get(ctx.value(), ValueBuilder.class).get();
			Decl result = new Decl(null, disjNames, fieldNames, disjValues, multiplicity, value);
			putDecl(ctx, result);
		}

		@Override
		public void exitVarDecl(VarDeclContext ctx) {
			Pos<String> var = get(ctx.var(), String.class);
			Decl declaration = get(ctx.decl(), Decl.class).get();
			putDecl(ctx, declaration.var(var));
		}

		@Override
		public void exitName(NameContext ctx) {
			set(ctx, ctx.getText());
		}

		@Override
		public void exitQname(QnameContext ctx) {
			set(ctx, ctx.getText());
		}

	}

	public void compile(String source, Map<String, Object> options, ModuleBuilder moduleBuilder) {
		AlloyToModule bridge = new AlloyToModule(moduleBuilder);
		ErrorListener listener = new ErrorListener(moduleBuilder);
		CodePointCharStream in = CharStreams.fromString(source);
		AlloyLexer al = new AlloyLexer(in);
		al.addErrorListener(listener);
		CommonTokenStream tokens = new CommonTokenStream(al);
		AlloyParser parser = new AlloyParser(tokens);
		parser.addErrorListener(listener);
		parser.setTrace(true);
		parser.addParseListener(bridge);
		parser.alloyFile();
	}
}
