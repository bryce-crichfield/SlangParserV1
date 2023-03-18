package parser.syntax;

public interface Node {
    void accept(NodeVisitor visitor);
}
