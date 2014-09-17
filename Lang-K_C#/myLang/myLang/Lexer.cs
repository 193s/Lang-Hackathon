using System;
using System.Collections.Generic;
using System.Collections;
using System.Text.RegularExpressions;
using MyLang;

namespace MyLang
{
	public static class Lexer
	{
		private static Dictionary<TokenKind, string> rules;
		private static Regex whitespace = new Regex (@"\s");

		static Lexer ()
		{
			rules = new Dictionary<TokenKind, string> ()
			{
				{ TokenKind.Reserved,	@"if|else|while" },
				{ TokenKind.Identifier,	@"[a-zA-Z_]+" },
//				{ TokenKind.Space,		@"[ ]+" },
				{ TokenKind.Operator,	@"[\-\+*\/\=\(\)]" },
				{ TokenKind.Literal,	@"\d+" },
			};
		}

		public static IEnumerable<Token> Tokenize (string s)
		{
			Console.WriteLine ("---START TOKENIZING---");
			int offset = 0;
			List<Token> list = new List<Token> ();
			while (true) {
				Token t = GetToken (s, offset);
				if (t.kind == TokenKind.EOF) {
					Console.WriteLine ("-EOF-");
					break;
				}

				Console.WriteLine ("Token : " + t.String + "\tkind: " + t.kind);
				offset += t.String.Length;
				if (t.kind != TokenKind.Space)
					list.Add (t);
			}
			return list;
		}

		private static Token GetToken (string s, int offset)
		{
			Console.WriteLine ("offset : {0}", offset);
			if (offset >= s.Length)
				return Token.EOF;
			string str = s.Substring (offset);
			Console.WriteLine ("str : {0}", str);

			if (whitespace.IsMatch(str [0].ToString())) {
				return new Token (TokenKind.Space, " ");
			}

			foreach (KeyValuePair<TokenKind, string> rule in rules) {
				Regex r = new Regex (rule.Value);
				if (r.IsMatch (str))
					return new Token(rule.Key, r.Match(str).Value);
			}
			return Token.EOF;
		}
	}
}
