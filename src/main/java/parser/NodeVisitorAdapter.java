package parser;

import java.lang.reflect.Type;

public abstract class NodeVisitorAdapter implements NodeVisitor {

    public abstract void defaultEnter(Node node);

    public abstract void defaultExit(Node node);

    @Override
    public void enter(Program program) {
        defaultEnter(program);
    }

    @Override
    public void exit(Program program) {
        defaultExit(program);
    }

    @Override
    public void enter(FunctionDeclaration function) {
        defaultEnter(function);
    }

    @Override
    public void exit(FunctionDeclaration function) {
        defaultExit(function);
    }

    @Override
    public void enter(Parameter parameter) {
        defaultEnter(parameter);
    }

    @Override
    public void exit(Parameter parameter) {
        defaultExit(parameter);
    }

    @Override
    public void enter(DataDeclaration dataType) {
        defaultEnter(dataType);
    }

    @Override
    public void exit(DataDeclaration dataType) {
        defaultExit(dataType);
    }

    @Override
    public void enter(Statement statement) {
        defaultEnter(statement);
    }

    @Override
    public void exit(Statement statement) {
        defaultExit(statement);
    }

    @Override
    public void enter(ReturnStatement returnStatement) {
        defaultEnter(returnStatement);
    }

    @Override
    public void exit(ReturnStatement returnStatement) {
        defaultExit(returnStatement);
    }

    @Override
    public void enter(IfStatement ifStatement) {
        defaultEnter(ifStatement);
    }

    @Override
    public void exit(IfStatement ifStatement) {
        defaultExit(ifStatement);
    }

    @Override
    public void enter(WhileStatement whileStatement) {
        defaultEnter(whileStatement);
    }

    @Override
    public void exit(WhileStatement whileStatement) {
        defaultExit(whileStatement);
    }

    @Override
    public void enter(AssignmentStatement assignmentStatement) {
        defaultEnter(assignmentStatement);
    }

    @Override
    public void exit(AssignmentStatement assignmentStatement) {
        defaultExit(assignmentStatement);
    }

    @Override
    public void enter(DeclarationStatement declarationStatement) {
        defaultEnter(declarationStatement);
    }

    @Override
    public void exit(DeclarationStatement declarationStatement) {
        defaultExit(declarationStatement);
    }

    @Override
    public void enter(Expression expression) {
        defaultEnter(expression);
    }

    @Override
    public void exit(Expression expression) {
        defaultExit(expression);
    }

    @Override
    public void enter(FunctionCall functionCall) {
        defaultEnter(functionCall);
    }

    @Override
    public void exit(FunctionCall functionCall) {
        defaultExit(functionCall);
    }

    @Override
    public void enter(Term term) {
        defaultEnter(term);
    }

    @Override
    public void exit(Term term) {
        defaultExit(term);
    }

    @Override
    public void enter(Factor factor) {
        defaultEnter(factor);
    }

    @Override
    public void exit(Factor factor) {
        defaultExit(factor);
    }

    @Override
    public void enter(Block block) {
        defaultEnter(block);
    }

    @Override
    public void exit(Block block) {
        defaultExit(block);
    }

    @Override
    public void enter(TypeSpecifier typeSpecifier) {
        defaultEnter(typeSpecifier);
    }

    @Override
    public void exit(TypeSpecifier typeSpecifier) {
        defaultExit(typeSpecifier);
    }

    @Override
    public void enter(Accessor accessor) {
        defaultEnter(accessor);
    }

    @Override
    public void exit(Accessor accessor) {
        defaultExit(accessor);
    }

    @Override
    public void enter(Identifier identifier) {
        defaultEnter(identifier);
    }

    @Override
    public void exit(Identifier identifier) {
        defaultExit(identifier);
    }

    @Override
    public void enter(Number number) {
        defaultEnter(number);
    }

    @Override
    public void exit(Number number) {
        defaultExit(number);
    }
}
