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
package org.sosy_lab.cpachecker.cpa.cfapath;

import org.sosy_lab.cpachecker.core.defaults.FlatLatticeDomain;

public class CFAPathDomain extends FlatLatticeDomain {

  private static final CFAPathDomain sDomainInstance = new CFAPathDomain();

  public static CFAPathDomain getInstance() {
    return sDomainInstance;
  }

  public CFAPathDomain() {
    super(CFAPathTopState.getInstance());
  }

}