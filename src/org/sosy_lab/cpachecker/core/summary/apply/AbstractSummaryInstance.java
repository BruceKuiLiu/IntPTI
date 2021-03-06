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
package org.sosy_lab.cpachecker.core.summary.apply;

import org.sosy_lab.cpachecker.core.summary.manage.SummaryInstance;
import org.sosy_lab.cpachecker.cpa.shape.constraint.SymbolicExpression;

abstract public class AbstractSummaryInstance implements SummaryInstance {
  // TODO
  public static final SymbolicExpression TRUE_EXPRESSION = null;
  public static final SymbolicExpression FALSE_EXPRESSION = null;
}
