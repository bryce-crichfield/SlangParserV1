package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataDeclaration implements Node {
    public Identifier identifier;
    public List<DeclarationStatement> declarations;

    DataDeclaration(Identifier identifier, List<DeclarationStatement> declarations) {
        this.identifier = identifier;
        this.declarations = declarations;
    }

    public static ParserResult<DataDeclaration> parse(View<Token> token) {
        var dataToken = Parser.token(token.clone(), TokenKind.DATA);
        if (dataToken.isError()) {
            return ParserResult.error(token, dataToken.getMessage());
        }

        var identifier = Identifier.parse(dataToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        var leftBrace = Parser.token(identifier.getRemaining(), TokenKind.LBRACE);
        if (leftBrace.isError()) {
            return ParserResult.error(token, leftBrace.getMessage());
        }

        var declarations = Parser.zeroOrMoreSeparatedBy(
                leftBrace.getRemaining(),
                DeclarationStatement::parse,
                Optional.empty()
        );
        var declarationStatements = new ArrayList<DeclarationStatement>();
        if (declarations.isOk()) {
            declarationStatements.addAll(declarations.getValue());
        }

        var rightBrace = Parser.token(declarations.getRemaining(), TokenKind.RBRACE);
        if (rightBrace.isError()) {
            return ParserResult.error(token, rightBrace.getMessage());
        }

        var dataDeclaration = new DataDeclaration(identifier.getValue(), declarationStatements);
        return ParserResult.ok(dataDeclaration, rightBrace.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        for (var declaration : declarations) {
            declaration.accept(visitor);
        }
        visitor.exit(this);
    }
}

