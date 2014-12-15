package marg.token;

public enum TokenKind {
  EOF,              // End of file
  EOL,              // End of Line
  Space,            // space (space, tab, ...)
  Comment,          // comment
  Reserved,         // Reserved Token
  Identifier,       // Identifier
  IntLiteral,       // Integer Literal
  BoolLiteral,      // true / false
  Operator,         // Operator Token
  Symbol,           // Symbol Token
}
