package parser.acceptance;

import parser.ParserResult;
import tokenizer.Token;
import util.View;

import java.util.function.Function;

record AcceptanceCase<A>(String input, Function<View<Token>, ParserResult<A>> parser) {

}
