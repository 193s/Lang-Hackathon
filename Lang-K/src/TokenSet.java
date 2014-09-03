import lang.lexer.Token;


public class TokenSet implements Cloneable {
		Token[] tokens;
		int length;
		int offset = 0;
		
		TokenSet(Token[] tokens) {
			this.tokens = tokens;
			length = tokens.length;
		}
		Token next() {
			return tokens[offset++];
		}
		Token getNext() {
			return tokens[offset+1];
		}
		Token get() {
			return tokens[offset];
		}
		
		boolean isEOF() {
			return offset >= length;
		}
		
		boolean isName() {
			return isEOF()? false : get() instanceof Token.Name;
		}
		
		Token.Name readName() {
			return isName()? (Token.Name) next() : null;
		}
		
		
		boolean isNumber() {
			return isEOF()? false : get() instanceof Token.Num;
		}
		
		Token.Num readNumber() {
			return isNumber()? (Token.Num) next() : null;
		}
		
		
		boolean isOperator() {
			return isEOF()? false : get() instanceof Token.Operator;
		}
		
		Token.Operator readOperator() {
			return isOperator()? (Token.Operator) next() : null;
		}
		
		boolean isReserved() {
			return isEOF()? false : get() instanceof Token.Reserved;
		}
		
		Token.Reserved readReserved() {
			return isReserved()? (Token.Reserved) next() : null;
		}
		
//		@Override
//		public TokenSet clone() {
//			TokenSet r;
//			try {
//				r = (TokenSet) super.clone();
//			}
//			catch (CloneNotSupportedException e) {
//				throw new RuntimeException();
//			}
//			return r;
//		}
//		
//		TokenSet clone(int skip) {
//			TokenSet r;
//			try {
//				r = (TokenSet) super.clone();
//				r.offset += skip;
//			}
//			catch (CloneNotSupportedException e) {
//				throw new RuntimeException();
//			}
//			return r;
//		}
	}