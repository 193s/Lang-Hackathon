package marg.token;


public enum OperatorSign {
    ExclamationMark	('!'),
    QuestionMark	('?'),
    Percent			('%'),
    Ampersand		('&'),
    Asterisk		('*'),
    Plus			('+'),
    Minus			('-'),
    Comma			(','),
    Dot				('.'),
    Slash			('/'),
    Colon			(':'),
    LessThanSign	('<'),
    MoreThanSign	('>'),
    Equal			('='),
    AtSign			('@'),
    Caret			('^'),
    BackQuote		('`'),
    VerticalBar		('|'),
    Tilde			('~'),
    ;

    public char character;
    private OperatorSign(char c) {
        character = c;
    }
}
