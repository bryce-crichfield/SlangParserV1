package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.List;
import java.util.Optional;

public class DataDeclaration implements Node {
    public Identifier identifier;
    public List<TypedIdentifier> typedIdentifiers;

    DataDeclaration(Identifier identifier, List<TypedIdentifier> typedIdentifiers) {
        this.identifier = identifier;
        this.typedIdentifiers = typedIdentifiers;
    }

    public static ParserResult<DataDeclaration> parse(View<Token> token) {
        var dataToken = Parse.token(token.clone(), TokenKind.DATA);
        if (dataToken.isError()) {
            return ParserResult.error(token, dataToken.getMessage());
        }

        var identifier = Identifier.parse(dataToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        var leftBrace = Parse.token(identifier.getRemaining(), TokenKind.LBRACE);
        if (leftBrace.isError()) {
            return ParserResult.error(token, leftBrace.getMessage());
        }

        var typedIdentifiers = Parse.zeroOrMoreSeparatedBy(
                leftBrace.getRemaining(),
                TypedIdentifier::parse,
                Optional.of(TokenKind.COMMA)
        );

        var rightBrace = Parse.token(typedIdentifiers.getRemaining(), TokenKind.RBRACE);
        if (rightBrace.isError()) {
            return ParserResult.error(token, rightBrace.getMessage());
        }

        var dataDeclaration = new DataDeclaration(identifier.getValue(), typedIdentifiers.getValue());
        return ParserResult.ok(dataDeclaration, rightBrace.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        for (var typedIdentifier : typedIdentifiers) {
            typedIdentifier.accept(visitor);
        }
        visitor.exit(this);
    }
}

