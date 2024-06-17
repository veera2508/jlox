package com.jlox.lox;

import com.jlox.lox.Expr.Binary;
import com.jlox.lox.Expr.Grouping;
import com.jlox.lox.Expr.Literal;
import com.jlox.lox.Expr.Unary;

//TODO Reverse Polish Notation

class AstPrinter implements Expr.Visitor<String> {

  String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Binary expr) {
    return paranthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Grouping expr) {
    return paranthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Literal expr) {
    if (expr.value == null)
      return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Unary expr) {
    return paranthesize(expr.operator.lexeme, expr.right);
  }

  private String paranthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);
    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");

    return builder.toString();
  }
}
