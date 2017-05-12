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
package org.sosy_lab.cpachecker.pcc.strategy.partialcertificate;

import com.google.common.collect.Lists;

import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.cfa.model.FunctionEntryNode;
import org.sosy_lab.cpachecker.cfa.model.FunctionExitNode;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.ConfigurableProgramAnalysis;
import org.sosy_lab.cpachecker.core.interfaces.Precision;
import org.sosy_lab.cpachecker.cpa.arg.ARGCPA;
import org.sosy_lab.cpachecker.cpa.arg.ARGState;
import org.sosy_lab.cpachecker.cpa.predicate.PredicateAbstractState;
import org.sosy_lab.cpachecker.exceptions.CPAException;
import org.sosy_lab.cpachecker.util.AbstractStates;
import org.sosy_lab.cpachecker.util.CPAs;
import org.sosy_lab.cpachecker.util.globalinfo.GlobalInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ARGBasedPartialReachedSetConstructionAlgorithm extends
                                                            MonotoneTransferFunctionARGBasedPartialReachedSetConstructionAlgorithm {

  private ConfigurableProgramAnalysis cpa;

  public ARGBasedPartialReachedSetConstructionAlgorithm(final boolean pReturnARGStatesInsteadOfWrappedStates) {
    super(pReturnARGStatesInsteadOfWrappedStates, false);
  }

  @Override
  protected NodeSelectionARGPass getARGPass(final Precision pRootPrecision, final ARGState pRoot)
      throws InvalidConfigurationException {
    if (!GlobalInfo.getInstance().getCPA().isPresent()) {
      throw new InvalidConfigurationException("No CPA specified.");
    } else {
      ARGCPA cpa = CPAs.retrieveCPA(GlobalInfo.getInstance().getCPA().get(), ARGCPA.class);
      if (cpa == null) {
        throw new InvalidConfigurationException("Require ARGCPA");
      }
      this.cpa = cpa.getWrappedCPAs().get(0);
    }
    return new ExtendedNodeSelectionARGPass(pRootPrecision, pRoot);
  }

  private class ExtendedNodeSelectionARGPass extends NodeSelectionARGPass {

    private final Precision precision;
    private final boolean handlePredicateStates;

    public ExtendedNodeSelectionARGPass(final Precision pRootPrecision, final ARGState pRoot) {
      super(pRoot);
      precision = pRootPrecision;
      handlePredicateStates =
          AbstractStates.extractStateByType(pRoot, PredicateAbstractState.class) != null;
    }

    @Override
    protected boolean isToAdd(final ARGState pNode) {
      boolean isToAdd = super.isToAdd(pNode);
      if (!isToAdd && !pNode.isCovered()) {
        if (handlePredicateStates) {
          CFANode loc = AbstractStates.extractLocation(pNode);
          isToAdd = isPredicateAbstractionState(pNode)
              || loc.getNumEnteringEdges() > 0 && !(loc instanceof FunctionEntryNode
              || loc instanceof FunctionExitNode);
        } else {
          for (ARGState parent : pNode.getParents()) {
            if (!isTransferSuccessor(parent, pNode)) {
              isToAdd = true;
            }
            break;
          }
        }
      }
      return isToAdd;
    }

    private boolean isTransferSuccessor(ARGState pPredecessor, ARGState pChild) {
      CFAEdge edge = pPredecessor.getEdgeToChild(pChild);
      try {
        Collection<AbstractState> successors;
        if (edge == null) {
          successors =
              new ArrayList<>(cpa.getTransferRelation().getAbstractSuccessors(
                  pPredecessor.getWrappedState(), Lists.<AbstractState>newArrayList(), precision));
        } else {
          successors =
              new ArrayList<>(
                  cpa.getTransferRelation().getAbstractSuccessorsForEdge(
                      pPredecessor.getWrappedState(), Lists.<AbstractState>newArrayList(),
                      precision, edge));
        }
        // check if child is the successor computed by transfer relation
        if (successors.contains(pChild.getWrappedState())) {
          return true;
        }
        // check if check only failed because it is not the same object
        if (!cpa.getStopOperator().stop(pChild.getWrappedState(), successors, precision)) {
          return false;
        }
        Collection<AbstractState> childCollection = Collections.singleton(pChild.getWrappedState());
        for (AbstractState state : successors) {
          if (cpa.getStopOperator().stop(state, childCollection, precision)) {
            return true;
          }
        }
      } catch (InterruptedException | CPAException e) {
      }
      return false;
    }

    private boolean isPredicateAbstractionState(ARGState pChild) {
      return PredicateAbstractState.getPredicateState(pChild).isAbstractionState();
    }

  }

}