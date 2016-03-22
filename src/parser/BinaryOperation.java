package parser;

// Used by BinaryExpression to denote which operation it uses
public enum BinaryOperation {
    // relop
    LessThanOrEqualTo,
    LessThan,
    GreaterThan,
    GreaterThanOrEqualTo,
    Equal,
    NotEqual,

    // addop
    Plus,
    Minus,

    // mulop
    Times,
    Over,

    // assign
    Assign
}
