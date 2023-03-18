package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class DeclarationStatement implements Node {
    public Identifier identifier;
    public Optional<TypeSpecifier> typeSpecifier;
    public Expression expression;

    DeclarationStatement(Identifier identifier, Optional<TypeSpecifier> typeSpecifier, Expression expression) {
        this.identifier = identifier;
        this.typeSpecifier = typeSpecifier;
        this.expression = expression;
    }

    public static ParserResult<DeclarationStatement> parse(View<Token> view) {
        var letToken = Parser.token(view.clone(), TokenKind.LET);
        if (letToken.isError()) {
            return ParserResult.error(view, letToken.getMessage());
        }

        var identifier = Identifier.parse(letToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        var typeSpecifier = Parser.optional(letToken.getRemaining(), TypeSpecifier::parse);

        var equals = Parser.token(typeSpecifier.getRemaining(), TokenKind.EQUALS);
        if (equals.isError()) {
            return ParserResult.error(view, equals.getMessage());
        }

        var expression = Expression.parse(equals.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        var semiColon = Parser.token(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semiColon.isError()) {
            return ParserResult.error(view, semiColon.getMessage());
        }

        var id = identifier.getValue();
        var type = typeSpecifier.getValue();
        var declarationStatement = new DeclarationStatement(id, type, expression.getValue());
        return ParserResult.ok(declarationStatement, semiColon.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        typeSpecifier.ifPresent(t -> t.accept(visitor));
        expression.accept(visitor);
        visitor.exit(this);
    }
}

