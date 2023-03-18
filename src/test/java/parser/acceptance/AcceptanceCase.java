package parser.acceptance;

import parser.ParserResult;
import tokenizer.Token;
import tokenizer.Tokenizer;
import util.View;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

record AcceptanceCase<A>(String input, Function<View<Token>, ParserResult<A>> parser) {

}
