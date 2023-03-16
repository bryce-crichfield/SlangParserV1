package parser;

import parser.syntax.Number;
import parser.syntax.*;
import tokenizer.TokenKind;

public class NodeVisitor {
    NodeListener listener;

    public NodeVisitor(NodeListener listener) {
        this.listener = listener;
    }

    public void visit(Program program) {
        listener.enter(program);
        for (FunctionDeclaration function : program.functions()) {
            visit(function);
        }
        for (DataDeclaration dataType : program.dataTypes()) {
            visit(dataType);
        }
        listener.exit(program);
    }

    public void visit(FunctionDeclaration function) {
        listener.enter(function);
        visit(function.identifier());
        for (Parameter parameter : function.parameters()) {
            visit(parameter);
        }
        visit(function.block());
        listener.exit(function);
    }

    public void visit(Parameter parameter) {
        listener.enter(parameter);
        visit(parameter.identifier());
        visit(parameter.type());
        listener.exit(parameter);
    }

    public void visit(DataDeclaration dataType) {
        listener.enter(dataType);
        visit(dataType.identifier());
        for (DeclarationStatement declarations : dataType.declarations()) {
            visit(declarations);
        }
        listener.exit(dataType);
    }

    public void visit(Statement statement) {
        listener.enter(statement);
        if (statement.ifStatement() != null) {
            visit(statement.ifStatement());
        } else if (statement.expression() != null) {
            visit(statement.expression());
        } else if (statement.whileStatement() != null) {
            visit(statement.whileStatement());
        } else if (statement.assignmentStatement() != null) {
            visit(statement.assignmentStatement());
        } else if (statement.declarationStatement() != null) {
            visit(statement.declarationStatement());
        } else {
            throw new RuntimeException("Invalid statement");
        }
        listener.exit(statement);
    }

    public void visit(IfStatement ifStatement) {
        listener.enter(ifStatement);
        visit(ifStatement.condition());
        visit(ifStatement.thenBlock());
        if (ifStatement.elseBlock() != null) {
            visit(ifStatement.elseBlock());
        }
        listener.exit(ifStatement);
    }

    public void visit(WhileStatement whileStatement) {
        listener.enter(whileStatement);
        visit(whileStatement.condition());
        visit(whileStatement.block());
        listener.exit(whileStatement);
    }

    public void visit(AssignmentStatement assignmentStatement) {
        listener.enter(assignmentStatement);
        visit(assignmentStatement.identifier());
        visit(assignmentStatement.expression());
        listener.exit(assignmentStatement);
    }

    public void visit(DeclarationStatement declarationStatement) {
        listener.enter(declarationStatement);
        visit(declarationStatement.identifier());
        visit(declarationStatement.expression());
        listener.exit(declarationStatement);
    }

    public void visit(Block block) {
        listener.enter(block);
        for (Statement statement : block.statements()) {
            visit(statement);
        }
        listener.exit(block);
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
        if (expression.application() != null) {
            visit(expression.application());
        }
        listener.exit(expression);
    }

    public void visit(Application application) {
        listener.enter(application);
        for (Expression expression : application.expressions()) {
            visit(expression);
        }
        listener.exit(application);
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
