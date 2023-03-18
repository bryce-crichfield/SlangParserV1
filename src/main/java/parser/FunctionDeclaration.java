package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class FunctionDeclaration implements Node {
    public Identifier identifier;
    public Parameters parameters;
    public Identifier returnType;
    public Block block;


    FunctionDeclaration(Identifier identifier, Parameters parameters, Identifier type, Block block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.returnType = type;
        this.block = block;
    }

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

        var colon = Parse.token(parameters.getRemaining(), TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(token, colon.getMessage());
        }

        var type = Identifier.parse(colon.getRemaining());
        if (type.isError()) {
            return ParserResult.error(token, type.getMessage());
        }

        var block = Block.parse(type.getRemaining());
        if (block.isError()) {
            return ParserResult.error(token, block.getMessage());
        }

        var declaration = new FunctionDeclaration(
                identifier.getValue(),
                parameters.getValue(),
                type.getValue(),
                block.getValue()
        );
        return ParserResult.ok(declaration, block.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        parameters.accept(visitor);
        returnType.accept(visitor);
        block.accept(visitor);
        visitor.exit(this);
    }
}