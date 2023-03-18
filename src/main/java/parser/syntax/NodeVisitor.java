package parser.syntax;

import tokenizer.Token;

public interface NodeVisitor {
    void enter(Program program);
    void exit(Program program);
    void enter(FunctionDeclaration function);
    void exit(FunctionDeclaration function);
    void enter(Parameter parameter);
    void exit(Parameter parameter);
    void enter(DataDeclaration dataType);
    void exit(DataDeclaration dataType);
    void enter(Statement statement);
    void exit(Statement statement);
    void enter(IfStatement ifStatement);
    void exit(IfStatement ifStatement);
    void enter(WhileStatement whileStatement);
    void exit(WhileStatement whileStatement);
    void enter(AssignmentStatement assignmentStatement);
    void exit(AssignmentStatement assignmentStatement);
    void enter(DeclarationStatement declarationStatement);
    void exit(DeclarationStatement declarationStatement);
    void enter(Expression expression);
    void exit(Expression expression);
    void enter(Application application);
    void exit(Application application);
    void enter(Term term);
    void exit(Term term);
    void enter(Factor factor);
    void exit(Factor factor);
    void enter(Block block);
    void exit(Block block);
    void enter(Accessor accessor);
    void exit(Accessor accessor);
    void enter(Identifier identifier);
    void exit(Identifier identifier);
    void enter(Number number);
    void exit(Number number);
}
