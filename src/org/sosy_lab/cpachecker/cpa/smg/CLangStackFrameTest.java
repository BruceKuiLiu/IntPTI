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
package org.sosy_lab.cpachecker.cpa.smg;

import com.google.common.collect.ImmutableList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CParameterDeclaration;
import org.sosy_lab.cpachecker.cfa.types.MachineModel;
import org.sosy_lab.cpachecker.cfa.types.c.CFunctionType;
import org.sosy_lab.cpachecker.cfa.types.c.CNumericTypes;
import org.sosy_lab.cpachecker.cpa.smg.objects.SMGObject;
import org.sosy_lab.cpachecker.cpa.smg.objects.SMGRegion;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


public class CLangStackFrameTest {
  static private final CFunctionType functionType =
      CFunctionType.functionTypeWithReturnType(CNumericTypes.UNSIGNED_LONG_INT);
  static private final CFunctionDeclaration functionDeclaration =
      new CFunctionDeclaration(FileLocation.DUMMY, functionType, "foo",
          ImmutableList.<CParameterDeclaration>of());
  static private final MachineModel usedMachineModel = MachineModel.LINUX64;
  private CLangStackFrame sf;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {

    sf = new CLangStackFrame(functionDeclaration, usedMachineModel);
  }

  @Test
  public void CLangStackFrameConstructorTest() {

    // Normal constructor
    Map<String, SMGRegion> variables = sf.getVariables();
    Assert.assertEquals("CLangStackFrame contains no variables after creation",
        variables.size(), 0);
    Assert.assertFalse(sf.containsVariable("foo"));

    // Copy constructor
    CLangStackFrame sf_copy = new CLangStackFrame(sf);
    variables = sf_copy.getVariables();
    Assert.assertEquals("Empty CLangStackFrame contains no variables after copying",
        variables.size(), 0);
    Assert.assertFalse(sf_copy.containsVariable("foo"));
  }

  @Test
  public void CLangStackFrameAddVariableTest() {
    sf.addStackVariable("fooVar", new SMGRegion(8, "fooVarObject"));
    Assert.assertTrue("Added variable is present", sf.containsVariable("fooVar"));

    Map<String, SMGRegion> variables = sf.getVariables();
    Assert.assertEquals("Variables set is nonempty after variable addition",
        variables.size(), 1);
    SMGObject smg_object = variables.get("fooVar");
    Assert.assertEquals("Added variable present in variable map", smg_object.getLabel(),
        "fooVarObject");
    Assert.assertEquals("Added variable present in variable map", smg_object.getSize(), 8);

    smg_object = null;
    smg_object = sf.getVariable("fooVar");
    Assert
        .assertEquals("Correct variable is returned: label", smg_object.getLabel(), "fooVarObject");
    Assert.assertEquals("Correct variable is returned: size", smg_object.getSize(), 8);
  }

  @Test
  public void CLangFrameGetObjectsTest() {
    Set<SMGObject> objects = sf.getAllObjects();
    // Test that there is an return value object at
    Assert.assertEquals(1, objects.size());

    sf.addStackVariable("fooVar", new SMGRegion(8, "fooVarObject"));
    objects = sf.getAllObjects();
    Assert.assertEquals(2, objects.size());
  }

  //TODO: Test void functions
  @Test
  public void CLangFrameReturnValueTest() {
    SMGObject retval = sf.getReturnObject();
    Assert.assertEquals(usedMachineModel.getSizeof(CNumericTypes.UNSIGNED_LONG_INT),
        retval.getSize());
  }

  @Test(expected = IllegalArgumentException.class)
  public void CLangStackFrameAddVariableTwiceTest() {
    sf.addStackVariable("fooVar", new SMGRegion(8, "fooVarObject"));
    sf.addStackVariable("fooVar", new SMGRegion(16, "newFooVarObject"));
  }

  @Test(expected = NoSuchElementException.class)
  public void CLangStackFrameMissingVariableTest() {
    Assert.assertFalse("Non-added variable is not present", sf.containsVariable("fooVaz"));

    sf.getVariable("fooVaz");
  }

  @Test
  public void CLangStackFrameFunctionTest() {
    CFunctionDeclaration fd = sf.getFunctionDeclaration();
    Assert.assertNotNull(fd);
    Assert.assertEquals("Correct function is returned", "foo", fd.getName());
  }
}