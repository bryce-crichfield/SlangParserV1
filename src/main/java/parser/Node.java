package parser;

public interface Node {
    void accept(NodeVisitor visitor);
}
