package parser.syntax;

import tokenizer.TokenKind;

public interface NodeListener {
    public void enter(Expression expression);
    public void exit(Expression expression);
    public void enter(Term term);
    public void exit(Term term);
    public void enter(Factor factor);
    public void exit(Factor factor);
    public void enter(Number number);
    public void exit(Number number);
    public void enter(Identifier identifier);
    public void exit(Identifier identifier);

    public void enter(TokenKind tokenKind);
    public void exit(TokenKind tokenKind);
}
