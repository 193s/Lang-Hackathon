package marg.token;

public enum TokenKind {
  EOF,              // End of file
  Space,            // space (space, tab, ...)
  OneLineComment,   // one line comment
  MultiLineComment, // multiline comment
  Reserved,         // Reserved Token
  Identifier,       // Identifier
  IntLiteral,       // Integer Literal
  BoolLiteral,      // true
  Operator,         // Operator Token
  Symbol,           // Symbol Token
}
