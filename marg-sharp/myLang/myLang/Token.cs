using System;
using System.IO;

namespace MyLang
{
	public struct Token
	{
		static public Token EOF = new Token(TokenKind.EOF, "");
		public Token(TokenKind t, string s)
		{
			String = s;
			kind = t;
		}

		public string String;
		public TokenKind kind;
	}

	public enum TokenKind
	{
		Reserved,
		Identifier,
		Operator,
		Literal,
		EOF,
		Space,
	}
}

