package parser.syntax;

public record WhileStatement(Expression condition, Block block) implements Node {
    public static WhileStatement of(Expression condition, Block block) {
        return new WhileStatement(condition, block);
    }
}

