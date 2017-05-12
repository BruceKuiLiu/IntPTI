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
package org.sosy_lab.cpachecker.cpa.pointer2.util;

import org.sosy_lab.cpachecker.util.states.MemoryLocation;


public enum LocationSetBot implements LocationSet {

  INSTANCE;

  @Override
  public boolean mayPointTo(MemoryLocation pTarget) {
    return false;
  }

  @Override
  public LocationSet addElement(MemoryLocation pTarget) {
    return ExplicitLocationSet.from(pTarget);
  }

  @Override
  public LocationSet removeElement(MemoryLocation pTarget) {
    return this;
  }

  @Override
  public LocationSet addElements(Iterable<MemoryLocation> pTargets) {
    return ExplicitLocationSet.from(pTargets);
  }

  @Override
  public boolean isBot() {
    return true;
  }

  @Override
  public boolean isTop() {
    return false;
  }

  @Override
  public LocationSet addElements(LocationSet pElements) {
    return pElements;
  }

  @Override
  public boolean containsAll(LocationSet pElements) {
    return pElements.isBot();
  }

  @Override
  public String toString() {
    return Character.toString('\u22A5');

  }

}