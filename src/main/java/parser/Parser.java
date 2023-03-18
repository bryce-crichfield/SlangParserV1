package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.Tuple;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// ---------------------------------------------------------------------------------------------------------------------
public class Parser {
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Token> token(View<Token> view, TokenKind kind) {
        var token = view.peek(0);
        if (token.isEmpty()) {
            return ParserResult.error(view, "Unexpected end of input");
        }
        if (token.get().kind() != kind) {
            return ParserResult.error(view, "Expected " + kind + " but got " + token.get().kind());
        }
        return ParserResult.ok(token.get(), view.pop());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> zeroOrMoreSeparatedBy(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser,
            Optional<TokenKind> separator
    ) {
        var accumulator = new ArrayList<A>();
        var remaining = view.clone();

        while (true) {
            var result = parser.apply(remaining);
            if (result.isError()) {
                return ParserResult.ok(accumulator, remaining);
            }
            accumulator.add(result.getValue());
            remaining = result.getRemaining();

            if (separator.isPresent()) {
                var separatorResult = token(remaining, separator.get());
                if (separatorResult.isError()) {
                    return ParserResult.ok(accumulator, remaining);
                }
                remaining = separatorResult.getRemaining();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> zeroOrMore(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser
    ) {
        var result = zeroOrMoreSeparatedBy(view, parser, Optional.empty());
        if (result.isError()) {
            return ParserResult.ok(new ArrayList<>(), view);
        }
        return ParserResult.ok(result.getValue(), result.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> oneOrMoreSeparatedBy(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser,
            Optional<TokenKind> separator
    ) {
        var accumulator = new ArrayList<A>();
        var remaining = view.clone();

        var result = parser.apply(remaining);
        if (result.isError()) {
            return ParserResult.error(view, result.getMessage());
        }

        accumulator.add(result.getValue());

        while (separator.isPresent()) {
            var separatorResult = token(result.getRemaining(), separator.get());
            if (separatorResult.isError()) {
                return ParserResult.ok(accumulator, result.getRemaining());
            }

            result = parser.apply(separatorResult.getRemaining());
            if (result.isError()) {
                return ParserResult.error(view, result.getMessage());
            }

            accumulator.add(result.getValue());
        }

        return ParserResult.ok(accumulator, result.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> oneOrMore(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser
    ) {
        return oneOrMoreSeparatedBy(view, parser, Optional.empty());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Tuple<Identifier, Identifier>> parseTypedIdentifier(View<Token> token) {
        var identifier = Identifier.parse(token.clone());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        var colon = token(identifier.getRemaining(), TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(token, colon.getMessage());
        }

        var type = Identifier.parse(colon.getRemaining());
        if (type.isError()) {
            return ParserResult.error(token, type.getMessage());
        }

        var typedIdentifier = Tuple.of(identifier.getValue(), type.getValue());
        return ParserResult.ok(typedIdentifier, type.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<Optional<A>> optional(
            View<Token> view,
            Function<View<Token>, ParserResult<A>> parser
    ) {
        var result = parser.apply(view.clone());
        if (result.isError()) {
            return ParserResult.ok(Optional.empty(), view);
        }
        return ParserResult.ok(Optional.of(result.getValue()), result.getRemaining());
    }
}
// ---------------------------------------------------------------------------------------------------------------------