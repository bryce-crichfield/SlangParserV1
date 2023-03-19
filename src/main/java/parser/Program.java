package parser;

import tokenizer.Token;
import util.Either;
import util.View;

import java.util.ArrayList;
import java.util.List;

public class Program implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public List<FunctionDeclaration> functions;
    public List<DataDeclaration> dataTypes;
    // -----------------------------------------------------------------------------------------------------------------

    Program(List<FunctionDeclaration> functions, List<DataDeclaration> dataTypes) {
        this.functions = functions;
        this.dataTypes = dataTypes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var function : functions) {
            function.accept(visitor);
        }

        for (var dataType : dataTypes) {
            dataType.accept(visitor);
        }
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Program> parse(View<Token> token) {
        // TODO: Right now error messages are not propagated out the tree,
        // TODO: so we can't tell the user what went wrong. We should fix this.

        var remaining = token.clone();

        var functions = new ArrayList<FunctionDeclaration>();
        var data = new ArrayList<DataDeclaration>();

        while (true) {
            var topDeclaration = parseTopDeclaration(remaining);
            if (topDeclaration.isNeither()) {
                break;
            }

            if (topDeclaration.isLeft()) {
                var functionResult = topDeclaration.getLeft();
                if (functionResult.isError()) {
                    return ParserResult.error(remaining, functionResult.getMessage());
                }

                functions.add(functionResult.getValue());
                remaining = functionResult.getRemaining();
            } else {
                var dataResult = topDeclaration.getRight();
                if (dataResult.isError()) {
                    return ParserResult.error(remaining, dataResult.getMessage());
                }

                data.add(dataResult.getValue());
                remaining = dataResult.getRemaining();
            }
        }

        if (!remaining.isEmpty()) {
            var message = new StringBuilder();
            message.append("Unexpected token: ");
            remaining.forEach(t -> message.append("\n").append(t.toString()));
            return ParserResult.error(token, message.toString());
        } else {
            var program = new Program(functions, data);
            return ParserResult.ok(program, remaining);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    private static Either<ParserResult<FunctionDeclaration>, ParserResult<DataDeclaration>> parseTopDeclaration(View<Token> view) {
        var function = FunctionDeclaration.parse(view);
        if (function.isError()) {
            var data = DataDeclaration.parse(view);
            if (data.isError()) {
                return Either.neither();
            } else {
                return Either.right(data);
            }
        } else {
            return Either.left(function);
        }
    }
}
