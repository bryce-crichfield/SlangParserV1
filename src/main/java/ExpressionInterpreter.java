import parser.*;
import parser.Number;
import tokenizer.TokenKind;

import java.util.Stack;

public class ExpressionInterpreter extends NodeVisitorAdapter {

    @Override
    public void defaultEnter(Node node) {

    }

    @Override
    public void defaultExit(Node node) {

    }
    Stack<Double> stack = new Stack<>();


    @Override
    public void exit(Number number) {
        stack.push(number.value);
    }

    @Override
    public void exit(Term term) {
        term.operator.ifPresent(tokenKind -> {
            var right = stack.pop();
            var left = stack.pop();
            switch (tokenKind) {
                case STAR -> stack.push(left * right);
                case SLASH -> stack.push(left / right);
            }
        });
    }

    @Override
    public void exit(Expression expression) {
        expression.operator.ifPresent(tokenKind -> {
            var right = stack.pop();
            var left = stack.pop();
            switch (tokenKind) {
                case PLUS -> stack.push(left + right);
                case MINUS -> stack.push(left - right);
            }
        });
    }
}
