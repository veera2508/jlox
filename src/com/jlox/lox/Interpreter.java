package com.jlox.lox;

import java.util.Arrays;

import com.jlox.lox.Expr.Binary;
import com.jlox.lox.Expr.Grouping;
import com.jlox.lox.Expr.Literal;
import com.jlox.lox.Expr.Unary;

class Interpreter implements Expr.Visitor<Object> {

  void interpret(Expr expr) {
    try {
      Object value = evaluate(expr);
      System.out.println(stringify(value));
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
  }

  @Override
  public Object visitBinaryExpr(Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);
    try {
      switch (expr.operator.type) {
        case MINUS:
          checkNumberOperand(expr.operator, left, right);
          return (double) left - (double) right;
        case STAR:
          checkNumberOperand(expr.operator, left, right);
          return (double) left * (double) right;
        case SLASH:
          checkNumberOperand(expr.operator, left, right);
          checkDivByZero(expr.operator, right);
          return (double) left / (double) right;
        case GREATER:
          checkNumberOperand(expr.operator, left, right);
          return (double) left > (double) right;
        case LESS:
          checkNumberOperand(expr.operator, left, right);
          return (double) left < (double) right;
        case GREATER_EQUAL:
          checkNumberOperand(expr.operator, left, right);
          return (double) left >= (double) right;
        case LESS_EQUAL:
          checkNumberOperand(expr.operator, left, right);
          return (double) left <= (double) right;
        case EQUAL_EQUAL:
          return isEqual(left, right);
        case BANG_EQUAL:
          return !isEqual(left, right);
        case PLUS:
          if (left instanceof Double && right instanceof Double)
            return (double) left + (double) right;
          else if (left instanceof String && right instanceof String)
            return (String) left + (String) right;

          throw new RuntimeError(expr.operator, "Operands must be both strings or numbers!");
      }
    } catch (ArithmeticException exception) {
      throw new RuntimeError(expr.operator, exception.getMessage());
    }
    return null;
  }

  @Override
  public Object visitGroupingExpr(Grouping expr) {
    return evaluate(expr.expression);
  }

  @Override
  public Object visitLiteralExpr(Literal expr) {
    return expr.value;
  }

  @Override
  public Object visitUnaryExpr(Unary expr) {
    Object right = evaluate(expr.right);
    switch (expr.operator.type) {
      case BANG:
        return !isTruthy(right);
      case MINUS:
        checkNumberOperand(expr.operator, right);
        return -(double) right;
    }

    return null;
  }

  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  private boolean isTruthy(Object object) {
    if (object == null)
      return false;
    if (object instanceof Boolean)
      return (boolean) object;
    return true;
  }

  private boolean isEqual(Object left, Object right) {
    if (left == null && right == null)
      return true;
    if (left == null || right == null)
      return false;
    return left.equals(right);
  }

  private void checkNumberOperand(Token operator, Object... operands) {
    Arrays.stream(operands).forEach(operand -> {
      if (!(operand instanceof Double))
        throw new RuntimeError(operator, "Operand must be a number!");
    });
    return;
  }

  private String stringify(Object object) {
    if (object == null)
      return "nil";

    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }

    return object.toString();
  }

  private void checkDivByZero(Token operator, Object right) {
    if ((double) right == 0) {
      throw new RuntimeError(operator, "Divide by zero!");
    }
    return;
  }
}
