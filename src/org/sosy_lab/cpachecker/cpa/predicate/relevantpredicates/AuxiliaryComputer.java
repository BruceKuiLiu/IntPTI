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
package org.sosy_lab.cpachecker.cpa.predicate.relevantpredicates;

import org.sosy_lab.cpachecker.cfa.blocks.Block;
import org.sosy_lab.cpachecker.cfa.blocks.ReferencedVariable;
import org.sosy_lab.cpachecker.util.predicates.AbstractionPredicate;
import org.sosy_lab.cpachecker.util.predicates.smt.FormulaManagerView;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;


/**
 * Computes set of irrelevant predicates of a block by identifying the variables that a auxiliary to
 * the block.
 */
public class AuxiliaryComputer extends AbstractRelevantPredicatesComputer<Collection<String>> {

  public AuxiliaryComputer(FormulaManagerView pFmgr) {
    super(pFmgr);
  }

  @Override
  protected Collection<String> precompute(
      Block pContext,
      Collection<AbstractionPredicate> pPredicates) {
    // compute relevant variables
    Deque<ReferencedVariable> waitlist = new ArrayDeque<>();

    for (ReferencedVariable var : pContext.getReferencedVariables()) {
      if (var.occursInCondition()) {
        // var is important for branching
        waitlist.add(var);
      } else if (!var.getInfluencingVariables().isEmpty() && occursInPredicate(var, pPredicates)) {
        // var is important, because it is assigned in current block.
        waitlist.add(var);
      }
    }

    Set<String> relevantVars = new HashSet<>();

    // get transitive closure over relevant vars.
    // note: at this point, all relevant vars are in the waitlist, but will be copied into it later.
    while (!waitlist.isEmpty()) {
      ReferencedVariable var = waitlist.pop();
      if (!relevantVars.add(var.getName())) {
        // important: here each var is copied into relevant vars.
        continue;
      }
      waitlist.addAll(var.getInfluencingVariables());
    }

    return relevantVars;
  }

  private boolean occursInPredicate(
      ReferencedVariable pVar,
      Collection<AbstractionPredicate> pPredicates) {
    for (AbstractionPredicate predicate : pPredicates) {
      if (predicate.getSymbolicAtom().toString().contains(pVar.getName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean isRelevant(
      Collection<String> relevantVariables,
      AbstractionPredicate predicate) {
    String predicateString = predicate.getSymbolicAtom().toString();

    for (String var : relevantVariables) {
      if (predicateString.contains(var)) {
        //var occurs in the predicate, so better trace it
        //TODO: contains is a quite rough approximation; for example "foo <= 5" also contains "f", although the variable f does in fact not occur in the predicate.
        return true;
      }
    }
    return false;
  }
}
