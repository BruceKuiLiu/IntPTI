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
package org.sosy_lab.cpachecker.core.phase.fix.util;

import com.google.common.base.Joiner;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.core.bugfix.MutableASTForFix;
import org.sosy_lab.cpachecker.core.bugfix.instance.integer.IntegerFix;
import org.sosy_lab.cpachecker.core.bugfix.instance.integer.IntegerFix.IntegerFixMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class IntegerFixDisplayInfo {

  private final UUID id;
  private final IntegerFix fix;
  private final MutableASTForFix ast;
  private final List<IntegerFixDisplayInfo> children = new ArrayList<>();
  private int startOffset = 0;
  private int endOffset = 0;

  private transient IntegerFixMetaInfo meta;

  private IntegerFixDisplayInfo(UUID pID, IntegerFix pFix, MutableASTForFix pAST,
                                IntegerFixMetaInfo pMeta) {
    id = pID;
    fix = pFix;
    ast = pAST;
    meta = pMeta;
  }

  public static IntegerFixDisplayInfo of(UUID pID, IntegerFix pFix, MutableASTForFix pAST,
                                         IntegerFixMetaInfo pMeta) {
    return new IntegerFixDisplayInfo(pID, pFix, pAST, pMeta);
  }

  public void addChild(IntegerFixDisplayInfo pInfo) {
    children.add(pInfo);
  }

  public void setStartAndEnd(int pStart, int pEnd) {
    startOffset = (pStart >= 0) ? pStart : 0;
    endOffset = (pEnd >= 0) ? pEnd : 0;
  }

  public IASTFileLocation getLocation() {
    return ast.getWrappedNode().getFileLocation();
  }

  public IntegerFixMode getFixMode() {
    return fix.getFixMode();
  }

  public UUID getID() {
    return id;
  }

  public IntegerFix getWrappedFix() {
    return fix;
  }

  public MutableASTForFix getWrappedAST() {
    return ast;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"UUID\":").append("\"").append(id.toString()).append("\"").append(",");
    sb.append("\"mode\":").append("\"").append(fix.getFixMode().getName()).append("\"").append(",");
    CSimpleType targetType = fix.getTargetType();
    assert (targetType != null);
    sb.append("\"type\":").append("\"").append(targetType.toString()).append("\"").append(",");
    // file location info
    IASTFileLocation loc = ast.getWrappedNode().getFileLocation();
    sb.append("\"startLine\":").append(loc.getStartingLineNumber()).append(",");
    sb.append("\"endLine\":").append(loc.getEndingLineNumber()).append(",");
    sb.append("\"startOffset\":").append(startOffset).append(",");
    sb.append("\"endOffset\":").append(endOffset).append(",");
    // add meta-info for showing bug fix details
    // sanity check
    assert (meta != null);
    assert (fix.getFixMode() == meta.getMode());
    // the persistence of meta-info should be under JSON format
    sb.append(meta.toString()).append(",");
    // add children
    sb.append("\"children\":");
    if (children.isEmpty()) {
      sb.append("[]");
    } else {
      List<String> childList = new ArrayList<>(children.size());
      StringBuilder subSb = new StringBuilder();
      subSb.append("[");
      for (IntegerFixDisplayInfo child : children) {
        childList.add(child.toString());
      }
      subSb.append(Joiner.on(',').join(childList));
      subSb.append("]");
      sb.append(subSb.toString());
    }
    sb.append("}");
    return sb.toString();
  }
}
