/*
 * IntPTI: integer error fixing by proper-type inference
 * Copyright (c) 2017.
 *
 * Open-source component:
 *
 * CPAchecker
 * Copyright (C) 2007-2014  Dirk Beyer
 *
 * Guava: Google Core Libraries for Java
 * Copyright (C) 2010-2006  Google
 *
 *
 */
package org.sosy_lab.cpachecker.cfa.ast.c;

public interface CLeftHandSideVisitor<R, X extends Exception> {

  R visit(CArraySubscriptExpression pIastArraySubscriptExpression) throws X;

  R visit(CFieldReference pIastFieldReference) throws X;

  R visit(CIdExpression pIastIdExpression) throws X;

  R visit(CPointerExpression pointerExpression) throws X;

  R visit(CComplexCastExpression complexCastExpression) throws X;
}
