package parser.syntax;

public record WhileStatement(Expression condition, Block block) implements Node {
    public static WhileStatement of(Expression condition, Block block) {
        return new WhileStatement(condition, block);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        condition.accept(visitor);
        block.accept(visitor);
        visitor.exit(this);
    }
}

