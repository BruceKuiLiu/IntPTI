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
package org.sosy_lab.cpachecker.util.collections.map;

import org.sosy_lab.common.collect.PathCopyingPersistentTreeMap;
import org.sosy_lab.common.collect.PersistentMap;

import java.util.Map.Entry;

public final class PersistentMaps {

  private PersistentMaps() {
  }

  public static <K extends Comparable<? super K>, V> PersistentMap<K, V> copy
      (PersistentMap<K, V> pFrom) {
    PersistentMap<K, V> newMap = PathCopyingPersistentTreeMap.of();
    for (Entry<K, V> fromEntry : pFrom.entrySet()) {
      newMap = newMap.putAndCopy(fromEntry.getKey(), fromEntry.getValue());
    }
    return newMap;
  }

}