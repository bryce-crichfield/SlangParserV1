package parser;

import parser.syntax.*;
import parser.syntax.Number;
import tokenizer.TokenKind;

public interface NodeListener {
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
    void enter(Block block);
    void exit(Block block);
    public void enter(Expression expression);
    public void exit(Expression expression);
    public void enter(Application application);
    public void exit(Application application);
    public void enter(Term term);
    public void exit(Term term);
    public void enter(Factor factor);
    public void exit(Factor factor);
    public void enter(parser.syntax.Number number);
    public void exit(Number number);
    public void enter(Identifier identifier);
    public void exit(Identifier identifier);

    public void enter(TokenKind tokenKind);
    public void exit(TokenKind tokenKind);
}
