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
package org.sosy_lab.cpachecker.cpa.invariants.formula;


public class IsLinearVisitor<T>
    implements ParameterizedNumeralFormulaVisitor<T, Variable<T>, Boolean> {

  private final ContainsVarVisitor<T> containsVarVisitor = new ContainsVarVisitor<>();

  @Override
  public Boolean visit(Add<T> pAdd, Variable<T> pParameter) {
    return pAdd.getSummand1().accept(this, pParameter) && pAdd.getSummand2()
        .accept(this, pParameter);
  }

  @Override
  public Boolean visit(BinaryAnd<T> pAnd, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(BinaryNot<T> pNot, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(BinaryOr<T> pOr, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(BinaryXor<T> pXor, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Constant<T> pConstant, Variable<T> pParameter) {
    return true;
  }

  @Override
  public Boolean visit(Divide<T> pDivide, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Exclusion<T> pExclusion, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Modulo<T> pModulo, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Multiply<T> pMultiply, Variable<T> pParameter) {
    return !pMultiply.accept(containsVarVisitor, pParameter.getMemoryLocation())
        && (pMultiply.getOperand1() instanceof Constant || pMultiply
        .getOperand2() instanceof Constant);
  }

  @Override
  public Boolean visit(ShiftLeft<T> pShiftLeft, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(ShiftRight<T> pShiftRight, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Union<T> pUnion, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Variable<T> pVariable, Variable<T> pParameter) {
    return true;
  }

  @Override
  public Boolean visit(IfThenElse<T> pIfThenElse, Variable<T> pParameter) {
    return false;
  }

  @Override
  public Boolean visit(Cast<T> pCast, Variable<T> pParameter) {
    return false;
  }

}
