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
package org.sosy_lab.cpachecker.core.interfaces;

public interface AbstractWrapperState extends AbstractState {

  /**
   * Retrieve all wrapped abstract states contained directly in this object.
   *
   * @return A non-empty list of abstract states.
   */
  public Iterable<AbstractState> getWrappedStates();

}
