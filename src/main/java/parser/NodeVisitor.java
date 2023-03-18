package parser;

public interface NodeVisitor {
    void enter(Program program);

    void exit(Program program);

    void enter(FunctionDeclaration function);

    void exit(FunctionDeclaration function);

    void enter(Parameters parameters);

    void exit(Parameters parameters);

    void enter(DataDeclaration dataType);

    void exit(DataDeclaration dataType);

    void enter(Statement statement);

    void exit(Statement statement);

    void enter(ReturnStatement returnStatement);

    void exit(ReturnStatement returnStatement);

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

    void enter(FunctionCall functionCall);

    void exit(FunctionCall functionCall);

    void enter(Term term);

    void exit(Term term);

    void enter(Factor factor);

    void exit(Factor factor);

    void enter(Block block);

    void exit(Block block);

    void enter(TypeSpecifier typeSpecifier);

    void exit(TypeSpecifier typeSpecifier);

    void enter(CompositeIdentifier compositeIdentifier);

    void exit(CompositeIdentifier compositeIdentifier);

    void enter(Identifier identifier);

    void exit(Identifier identifier);

    void enter(Number number);

    void exit(Number number);

    void enter(TypedIdentifier typedIdentifier);

    void exit(TypedIdentifier typedIdentifier);

    void enter(ForStatement forStatement);

    void exit(ForStatement forStatement);
}
