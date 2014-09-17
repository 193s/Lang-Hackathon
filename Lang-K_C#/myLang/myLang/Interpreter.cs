using System;
using System.Collections.Generic;

namespace MyLang
{
	public class Interpreter 
	{
		static void Main()
		{
			Console.WriteLine("Interpreter:");
			string input, s = "";
			do {
				input = Console.ReadLine();
				s += input + '\n';
			}
			while (input != "");

			IEnumerable<Token> ls = Lexer.Tokenize(s);
			Console.WriteLine ("---SUCCEED TOKENIZING---");
			foreach (Token t in ls) Console.WriteLine("[{0}] : {1}", t.kind, t.String);
		}
	}
}
