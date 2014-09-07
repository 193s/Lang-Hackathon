using System;
using System.Text.RegularExpressions;
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

            Token[] ls = Lexer.tokenize(s);
            foreach (Token t in ls) Console.WriteLine("[{0}]\n", s);
        }
    }

    public class Token
    {
        public Token(String s)
        {
            String = s;
        }

        public string String { get; protected set; }
    }
    public class ReservedToken : Token
    {
        public ReservedToken(string s) : base (s)
        {}
    }
    public class IdentiferToken : Token
    {
        public IdentiferToken(string s) : base (s)
        {}
    }
    public class OperatorToken : Token
    {
        public OperatorToken(string s) : base (s)
        {}
    }
    public class LiteralToken : Token
    {
        public LiteralToken(string s) : base (s)
        {
            Value = int.Parse(s);
        }
        public int Value { get; private set; }
    }

    public class Lexer
    {
        public static Token[] tokenize(string s)
        {

            return null;
        }
    }
}

