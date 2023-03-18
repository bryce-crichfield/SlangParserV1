package parser;

import tokenizer.Token;
import util.View;

import java.util.ArrayList;
import java.util.List;

public class Program implements Node {
    public List<FunctionDeclaration> functions;
    public List<DataDeclaration> dataTypes;

    Program(List<FunctionDeclaration> functions, List<DataDeclaration> dataTypes) {
        this.functions = functions;
        this.dataTypes = dataTypes;
    }

    public static ParserResult<Program> parse(View<Token> token) {
        // TODO: Right now error messages are not propagated out the tree,
        // TODO: so we can't tell the user what went wrong. We should fix this.

        var remaining = token.clone();

        var functions = new ArrayList<FunctionDeclaration>();
        var data = new ArrayList<DataDeclaration>();

        while (true) {
            var function = FunctionDeclaration.parse(remaining.clone());
            if (function.isOk()) {
                functions.add(function.getValue());
                remaining = function.getRemaining();
                continue;
            }

            var dataDeclaration = DataDeclaration.parse(remaining.clone());
            if (dataDeclaration.isOk()) {

                data.add(dataDeclaration.getValue());
                remaining = dataDeclaration.getRemaining();
                continue;
            }

            break;
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
}
