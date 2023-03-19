package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class FunctionDeclaration implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Identifier identifier;
    public Parameters parameters;
    public Optional<TypeSpecifier> returnType;
    public Block block;

    // -----------------------------------------------------------------------------------------------------------------
    FunctionDeclaration(Identifier identifier, Parameters parameters, Optional<TypeSpecifier> type, Block block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.returnType = type;
        this.block = block;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        parameters.accept(visitor);
        returnType.ifPresent(type -> type.accept(visitor));
        block.accept(visitor);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<FunctionDeclaration> parse(View<Token> token) {
        var fnToken = Parse.token(token.clone(), TokenKind.FN);
        if (fnToken.isError()) {
            return ParserResult.error(token, fnToken.getMessage());
        }

        var identifier = Identifier.parse(fnToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        var parameters = Parameters.parse(identifier.getRemaining());
        if (parameters.isError()) {
            return ParserResult.error(token, parameters.getMessage());
        }

        var typeSpecifier = Parse.optional(parameters.getRemaining(), TypeSpecifier::parse);


        var block = Block.parse(typeSpecifier.getRemaining());
        if (block.isError()) {
            return ParserResult.error(token, block.getMessage());
        }

        var declaration = new FunctionDeclaration(
                identifier.getValue(),
                parameters.getValue(),
                typeSpecifier.getValue(),
                block.getValue()
        );
        return ParserResult.ok(declaration, block.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}