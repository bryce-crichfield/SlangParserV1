package parser.syntax;

import tokenizer.TokenKind;

public class NodeVisitor {
    NodeListener listener;

    public NodeVisitor(NodeListener listener) {
        this.listener = listener;
    }

    public void visit(Expression expression) {
        listener.enter(expression);
        if (expression.expression() != null) {
            visit(expression.expression());
        }
        if (expression.operator() != null) {
            visit(expression.operator());
        }
        if (expression.term() != null) {
            // THIS SHOULD NEVER HAPPEN
            visit(expression.term());
        }
        listener.exit(expression);
    }

    public void visit(Term term) {
        listener.enter(term);
        if (term.term() != null) {
            visit(term.term());
        }
        if (term.operator() != null) {
            visit(term.operator());
        }
        if (term.factor() != null) {
            // THIS SHOULD NEVER HAPPEN
            visit(term.factor());
        }
        listener.exit(term);
    }

    public void visit(Factor factor) {
        listener.enter(factor);
        if (factor.expression() != null) {
            visit(factor.expression());
        } else if (factor.number() != null) {
            visit(factor.number());
        } else if (factor.identifier() != null) {
            visit(factor.identifier());
        }
        listener.exit(factor);
    }

    public void visit(Number number) {
        listener.enter(number);
        listener.exit(number);
    }

    public void visit(Identifier identifier) {
        listener.enter(identifier);
        listener.exit(identifier);
    }

    public void visit(TokenKind tokenKind) {
        listener.enter(tokenKind);
        listener.exit(tokenKind);
    }
}
