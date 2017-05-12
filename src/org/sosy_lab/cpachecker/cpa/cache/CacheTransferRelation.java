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
package org.sosy_lab.cpachecker.cpa.cache;

import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.core.defaults.SingleEdgeTransferRelation;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.Precision;
import org.sosy_lab.cpachecker.core.interfaces.TransferRelation;
import org.sosy_lab.cpachecker.exceptions.CPATransferException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheTransferRelation extends SingleEdgeTransferRelation {

  private final TransferRelation mCachedTransferRelation;
  //private Map<CFAEdge, Map<AbstractState, Map<Precision, Collection<? extends AbstractState>>>> mSuccessorsCache;
  private Map<Precision, Map<CFAEdge, Map<AbstractState, Collection<? extends AbstractState>>>>
      mSuccessorsCache;

  //private int lCacheMisses = 0;
  //private int lCacheHits = 0;


  //private Set<CFAEdge> mHitEdges;


  public CacheTransferRelation(TransferRelation pCachedTransferRelation) {
    mCachedTransferRelation = pCachedTransferRelation;
    //mSuccessorsCache = new HashMap<CFAEdge, Map<AbstractState, Map<Precision, Collection<? extends AbstractState>>>>();
    mSuccessorsCache = new HashMap<>();

    //mHitEdges = new HashSet<>();
  }

  @Override
  public Collection<? extends AbstractState> getAbstractSuccessorsForEdge(
      AbstractState pElement,
      List<AbstractState> otherStates,
      Precision pPrecision,
      CFAEdge pCfaEdge)
      throws CPATransferException, InterruptedException {

    /*Map<AbstractState, Map<Precision, Collection<? extends AbstractState>>> lLevel1Cache = mSuccessorsCache.get(pCfaEdge);

    if (lLevel1Cache == null) {
      lLevel1Cache = new HashMap<AbstractState, Map<Precision, Collection<? extends AbstractState>>>();
      mSuccessorsCache.put(pCfaEdge, lLevel1Cache);
    }

    Map<Precision, Collection<? extends AbstractState>> lLevel2Cache = lLevel1Cache.get(pElement);

    if (lLevel2Cache == null) {
      lLevel2Cache = new HashMap<>();
      lLevel1Cache.put(pElement, lLevel2Cache);
    }

    Collection<? extends AbstractState> lSuccessors = lLevel2Cache.get(pPrecision);

    if (lSuccessors == null) {
      lSuccessors = mCachedTransferRelation.getAbstractSuccessors(pElement, pPrecision, pCfaEdge);
      lLevel2Cache.put(pPrecision, lSuccessors);

      lCacheMisses++;
    } else {
      lCacheHits++;
    }

    return lSuccessors;*/

    Map<CFAEdge, Map<AbstractState, Collection<? extends AbstractState>>> lLevel1Cache =
        mSuccessorsCache.get(pPrecision);

    if (lLevel1Cache == null) {
      lLevel1Cache = new HashMap<>();
      mSuccessorsCache.put(pPrecision, lLevel1Cache);
    }

    Map<AbstractState, Collection<? extends AbstractState>> lLevel2Cache =
        lLevel1Cache.get(pCfaEdge);

    if (lLevel2Cache == null) {
      lLevel2Cache = new HashMap<>();
      lLevel1Cache.put(pCfaEdge, lLevel2Cache);
    }

    Collection<? extends AbstractState> lSuccessors = lLevel2Cache.get(pElement);

    if (lSuccessors == null) {
      lSuccessors = mCachedTransferRelation.getAbstractSuccessorsForEdge(pElement, otherStates,
          pPrecision, pCfaEdge);
      lLevel2Cache.put(pElement, lSuccessors);

      //lCacheMisses++;
    } else {
      //lCacheHits++;
    }

    return lSuccessors;
  }

  @Override
  public Collection<? extends AbstractState> strengthen(
      AbstractState pElement, List<AbstractState> pOtherElements,
      CFAEdge pCfaEdge, Precision pPrecision) throws CPATransferException, InterruptedException {

    // TODO implement caching

    return mCachedTransferRelation.strengthen(pElement, pOtherElements, pCfaEdge, pPrecision);
  }

}