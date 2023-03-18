package parser.syntax;

public record IfStatement(Expression condition, Block thenBlock, Block elseBlock) implements Node {
    public static IfStatement of(Expression condition, Block thenBlock, Block elseBlock) {
        return new IfStatement(condition, thenBlock, elseBlock);
    }

    public static IfStatement of(Expression condition, Block thenBlock) {
        return new IfStatement(condition, thenBlock, null);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        condition.accept(visitor);
        thenBlock.accept(visitor);
        if (elseBlock != null) {
            elseBlock.accept(visitor);
        }
        visitor.exit(this);
    }
}
