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
package org.sosy_lab.cpachecker.cfa.postprocessing.global.singleloop;

import com.google.common.base.Preconditions;

import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.util.CFAUtils;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Instances of this class are the heads of the loops produced by the single
 * loop transformation. They provide additional information relevant to the
 * transformation and to handling its consequences.
 */
public class SingleLoopHead extends CFANode {

  /**
   * The program counter value assignment edges leading to the loop head.
   */
  private final Map<Integer, ProgramCounterValueAssignmentEdge> enteringPCValueAssignmentEdges =
      new HashMap<>();

  /**
   * Creates a new loop head with line number 0 and an artificial function name.
   */
  public SingleLoopHead() {
    super(CFASingleLoopTransformation.ARTIFICIAL_PROGRAM_COUNTER_FUNCTION_NAME);
  }

  @Override
  public void addEnteringEdge(CFAEdge pEnteringEdge) {
    if (pEnteringEdge instanceof ProgramCounterValueAssignmentEdge) {
      ProgramCounterValueAssignmentEdge edge = (ProgramCounterValueAssignmentEdge) pEnteringEdge;
      int pcValue = edge.getProgramCounterValue();
      Preconditions.checkArgument(!enteringPCValueAssignmentEdges.containsKey(pcValue),
          "All entering program counter value assignment edges must be unique.");
      enteringPCValueAssignmentEdges.put(pcValue, edge);
    } else {
      assert false;
    }
    super.addEnteringEdge(pEnteringEdge);
  }

  @Override
  public void removeEnteringEdge(CFAEdge pEnteringEdge) {
    if (pEnteringEdge instanceof ProgramCounterValueAssignmentEdge
        && CFAUtils.enteringEdges(this).contains(pEnteringEdge)) {
      ProgramCounterValueAssignmentEdge edge = (ProgramCounterValueAssignmentEdge) pEnteringEdge;
      enteringPCValueAssignmentEdges.remove(edge.getProgramCounterValue());
    }
    super.removeEnteringEdge(pEnteringEdge);
  }

  /**
   * Gets the entering assignment edge with the given program counter value.
   *
   * @param pPCValue the value assigned to the program counter.
   * @return the entering assignment edge with the given program counter value or {@code null} if no
   * such edge exists.
   */
  public
  @Nullable
  ProgramCounterValueAssignmentEdge getEnteringAssignmentEdge(int pPCValue) {
    return enteringPCValueAssignmentEdges.get(pPCValue);
  }

  /**
   * Gets the names of functions the loop head is entered from.
   *
   * @return the names of functions the loop head is entered from.
   */
  public Set<String> getEnteringFunctionNames() {
    Set<String> results = new HashSet<>();
    Set<CFANode> visited = new HashSet<>();
    Queue<CFANode> waitlist = new ArrayDeque<>();
    waitlist.offer(this);
    while (!waitlist.isEmpty()) {
      CFANode current = waitlist.poll();
      if (visited.add(current)) {
        if (current.getFunctionName()
            .equals(CFASingleLoopTransformation.ARTIFICIAL_PROGRAM_COUNTER_FUNCTION_NAME)) {
          waitlist.addAll(CFAUtils.allPredecessorsOf(current).toList());
        } else {
          results.add(current.getFunctionName());
        }
      }
    }
    return results;
  }

  /**
   * Gets all program counter values.
   *
   * @return all program counter values.
   */
  public Collection<Integer> getProgramCounterValues() {
    return Collections.unmodifiableSet(enteringPCValueAssignmentEdges.keySet());
  }

}