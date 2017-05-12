/*
 * Tsmart-BD: The static analysis component of Tsmart platform
 *
 * Copyright (C) 2013-2017  Tsinghua University
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


import org.sosy_lab.cpachecker.cfa.ast.ALeftHandSide;

public interface CLeftHandSide extends CExpression, ALeftHandSide {

  public <R, X extends Exception> R accept(CLeftHandSideVisitor<R, X> v) throws X;
}