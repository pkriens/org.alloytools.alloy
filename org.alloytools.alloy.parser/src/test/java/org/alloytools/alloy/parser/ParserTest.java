package org.alloytools.alloy.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alloytools.alloy.parser.AlloyParser.NumberContext;
import org.alloytools.alloy.parser.AlloyParser.QnameContext;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

public class ParserTest {
	final static boolean unicode = true;

	@Test
	public void testComments() {
		test("""
				-- this is a comment

				// this is also a comment with a string in it "hello"
				// this is a comment with a \r
				\t\t
				/**
				   a long comment
				   * /
				*/
				""", "", AlloyParser::alloyFile);
	}

	@Test
	public void testWhiteSpace() {
		test("a+b", "(a + b)", AlloyParser::value);
		test("a + b", "(a + b)", AlloyParser::value);
		test("a    +     b", "(a + b)", AlloyParser::value);
		test("a\t\t\t+\t\t   b", "(a + b)", AlloyParser::value);
		test("a\n\r\n +\n\r\n  b\n\r\n", "(a + b)", AlloyParser::value);
	}

	@Test
	public void testQname() {
		test("abc", "abc", AlloyParser::qname);
		test("a\"b\"c", "a\"b\"c", AlloyParser::qname);
		test("a/_/___/a___", "a/_/___/a___", AlloyParser::qname);
		test("abc/def/ghi", "abc/def/ghi", AlloyParser::qname);
		test("this/def/ghi", "this/def/ghi", AlloyParser::qname);
		test("seq/def/ghi", "seq/def/ghi", AlloyParser::qname);

		if (unicode) {
			test("aé", "aé", AlloyParser::qname);
			test("Δ", "Δ", AlloyParser::qname);
			test("Δ/Δ", "Δ/Δ", AlloyParser::qname);
			test("شᚴ", "شᚴ", AlloyParser::qname);
		}
	}

	@Test
	public void testPrecedence() {

		test("a", "a", AlloyParser::value);
		// same level
		test("*^~a", "(* (^ (~ a)))", AlloyParser::value);
		test("^*~a", "(^ (* (~ a)))", AlloyParser::value);

		// [~^*] binds higher than prime
		test("~a'", "((~ a) ')", AlloyParser::value);
		test("^a'", "((^ a) ')", AlloyParser::value);
		test("*a'", "((* a) ')", AlloyParser::value);

		// prime > join
		test("a.b'", "(a . (b '))", AlloyParser::value);
		test("a'.b'", "((a ') . (b '))", AlloyParser::value);

		// join ==
		test("a.b.c.d", "(((a . b) . c) . d)", AlloyParser::value);

		// join > box
		test("a.b.c[d]", "(((a . b) . c) [ d ])", AlloyParser::value);

		// box ==
		test("c[d[e]]", "(c [ (d [ e ]) ])", AlloyParser::value);

		// box > restriction
		test("a <: c[d]", "(a <: (c [ d ]))", AlloyParser::value);
		test("a :> c[d]", "(a :> (c [ d ]))", AlloyParser::value);

		// restriction ==
		test("a :> b <: c", "((a :> b) <: c)", AlloyParser::value);
		test("a <: b :> c", "((a <: b) :> c)", AlloyParser::value);

		// restriction > arrow
		test("a->b :> c", "(a -> (b :> c))", AlloyParser::value);
		test("a->b :> c->d", "((a -> (b :> c)) -> d)", AlloyParser::value);

		// arrow ==
		test("a some->set b->c->d", "(((a some -> set b) -> c) -> d)", AlloyParser::value);

		// arrow > intersection
		test("a some->set b & c", "((a some -> set b) & c)", AlloyParser::value);
		test("a some -> set b & c->d", "((a some -> set b) & (c -> d))", AlloyParser::value);

		// intersection ==
		test("a & c & d", "((a & c) & d)", AlloyParser::value);

		// intersection > override
		test("a & c ++ d", "((a & c) ++ d)", AlloyParser::value);
		test("a ++ c & d", "(a ++ (c & d))", AlloyParser::value);

		// override ==
		test("a ++ b ++ c", "((a ++ b) ++ c)", AlloyParser::value);

		// override > cardinality
		test("# a ++ b", "(# (a ++ b))", AlloyParser::value);

		// cardinality ==
		test("# # a", "(# (# a))", AlloyParser::value);

		// cardinality > union/difference
		test("# a + b", "((# a) + b)", AlloyParser::value);
		test("# a - b", "((# a) - b)", AlloyParser::value);

		// union diff ==
		test("a + b - c - d", "(((a + b) - c) - d)", AlloyParser::value);
	}

	@Test
	public void testDecl() {
		test("a : disj one A", "((a) : disj one A)", AlloyParser::decl);

	}

	@Test
	public void testBox() {
//		test("a.c[d]", "(((a . b) . c) [ d ])", AlloyParser::value);

	}

	
	@Test
	public void testValue() {

		testValue("a.b.c.d", "(((a . b) . c) . d)");

		testValue("this/a", "this/a");
		testValue("a/b/c", "a/b/c");
		testValue("@a", "(@ a)");
		testValue("A", "A");

		testValue("univ", "univ");
		testValue("iden", "iden");
		testValue("a'", "(a ')");

		testValue("a & univ + b", "((a & univ) + b)");
		testValue("a + univ & b", "(a + (univ & b))");
		testValue("~1+*2-^3", "(((~ 1) + (* 2)) - (^ 3))");
		testValue("a+b-3", "((a + b) - 3)");
		testValue("1-2+3", "((1 - 2) + 3)");

		testValue("1-2++3", "(1 - (2 ++ 3))");
		testValue("1++2+3", "((1 ++ 2) + 3)");

		testValue("1-^2", "(1 - (^ 2))");
		testValue("^2-1", "((^ 2) - 1)");
		testValue("^2.1-1", "(((^ 2) . 1) - 1)");

		testValue("^2-1", "((^ 2) - 1)");

		testValue("~^*foo", "(~ (^ (* foo)))");
		testValue("*^~foo", "(* (^ (~ foo)))");
		testValue("a.b[c]", "((a . b) [ c ])");
		testValue("a[b].c", "((a [ b ]) . c)");
		testValue("^*foo", "(^ (* foo))");
		testValue("*^foo", "(* (^ foo))");
		testValue("-1", "-1");
		testValue("1-2-3", "((1 - 2) - 3)");

		testValue("1--1 -- comment", "(1 - -1)");
	}

	@Test
	public void testFormula() {

		// formulas

		test("a > b", "(a > b)", AlloyParser::formula);
		test("a + b > c", "((a + b) > c)", AlloyParser::formula);

		test("a not > c", "(a not > c)", AlloyParser::formula);

		test("always a > 1", "(always (a > 1))", AlloyParser::formula);
		test("always a>b c<d", "((always (a > b)) (c < d))", AlloyParser::formula);

		test("a + b > d + e -> f :> g", "((a + b) > (d + (e -> (f :> g))))", AlloyParser::formula);

		test("a + b > d + e -> f :> g", "((a + b) > (d + (e -> (f :> g))))", AlloyParser::formula);

		test("a ++ c & d", "(a ++ (c & d))", AlloyParser::value);

		// [.[]] binds lower than prime
		test("a.b'", "(a . (b '))", AlloyParser::value);

		test("^a'", "((^ a) ')", AlloyParser::value);
		test("*a'", "((* a) ')", AlloyParser::value);

		test("~a+b", "((~ a) + b)", AlloyParser::value);
		test("a+~b", "(a + (~ b))", AlloyParser::value);
		test("some a no b", "(((some a) (no b)))", AlloyParser::expr);

		// testFormula("p => q => r", "(p => (q => r))");
		// testFormula("1>2 => 1>2 => 1>2", "((1 > 2) => ((1 > 2) => (1 > 2)))");
		testFormula("some a->b && no b", "((some (a -> b)) && (no b))");
		testFormula("3 > 0 or let a=1, b=2 | a > b", "((3 > 0) or ((let a = 1 , b = 2 (| (a > b)))))");
		testFormula("let a=1, b=2 | a > b", "((let a = 1 , b = 2 (| (a > b))))");
		testFormula("1 - 2 > 3 + 4", "((1 - 2) > (3 + 4))");
		testFormula("1 > 2 3 < 5", "((1 > 2) (3 < 5))");
		testFormula("1 > 2 && 3 < 5", "((1 > 2) && (3 < 5))");
		testFormula("1 > 2 or 3 < 5 and 6 > 9", "((1 > 2) or ((3 < 5) and (6 > 9)))");
	}

	void testValue(String source, String expected) {
		test(source, expected, AlloyParser::value);
	}

	void testFormula(String source, String expected) {
		test(source, expected, AlloyParser::formula);
	}

	void testMacro(String source, String expected) {
		test(source, expected, AlloyParser::expr);
	}

	void test(String source, String expected, Function<AlloyParser, ? extends ParseTree> getter, String... ignores) {
		CodePointCharStream in = CharStreams.fromString(source);
		AtomicInteger found = new AtomicInteger(0);
		AlloyLexer al = new AlloyLexer(in);
		al.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
				for (String ignore : ignores) {
					boolean optional = ignore.startsWith("-");
					if (optional)
						ignore = ignore.substring(1);

					Pattern p = Pattern.compile(ignore, Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(msg);
					if (m.find()) {
						if (!optional)
							found.incrementAndGet();
						return;
					}
				}
			}
		});
		CommonTokenStream tokens = new CommonTokenStream(al);
		AlloyParser parser = new AlloyParser(tokens);
		parser.addParseListener(new AlloyBaseListener());
		parser.setTrace(true);
		ParseTree tree = getter.apply(parser);
		if (found.get() > 0)
			return;

		assertThat(tokens.LA(1)).describedAs("not at EOF").isEqualTo(AlloyParser.EOF);
		assertThat(toSimpleSExpr(tree)).isEqualTo(expected);

	}

	public static String toSimpleSExpr(ParseTree tree) {
		// If the node is a terminal, return its text.
		if (tree instanceof TerminalNode) {
			return tree.getText();
		}
		if (tree instanceof AlloyParser.QnameValueContext qnv) {
			QnameContext qname = qnv.qname();
			return qname.getText();
		}
		if (tree instanceof AlloyParser.NumberValueContext qnv) {
			NumberContext qname = qnv.number();
			return qname.getText();
		}

		if (tree.getChildCount() == 0) {
			return tree.getText();
		}

		if (tree.getChildCount() == 1 && tree.getChild(0) instanceof TerminalNode) {
			return tree.getChild(0).getText();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < tree.getChildCount(); i++) {
			sb.append(toSimpleSExpr(tree.getChild(i)));
			if (i < tree.getChildCount() - 1) {
				sb.append(" ");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
