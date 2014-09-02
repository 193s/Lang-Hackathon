import lang.lexer.Token;


class TokenSet implements Cloneable {
		static Token[] tokens;
		static int length;
		int offset = 0;
		
		TokenSet(Token[] tokens) {
			this.tokens = tokens;
			length = tokens.length;
		}
		Token getNext() {
			return tokens[offset+1];
		}
		Token get() {
			return tokens[offset];
		}
		Token get(int skip) {
			return tokens[offset + skip];
		}
		
		
		@Override
		public TokenSet clone() {
			TokenSet r;
			try {
				r = (TokenSet) super.clone();
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
		}
		
		TokenSet clone(int skip) {
			TokenSet r;
			try {
				r = (TokenSet) super.clone();
				r.offset += skip;
			}
			catch (CloneNotSupportedException e) {
				throw new RuntimeException();
			}
			return r;
		}
	}