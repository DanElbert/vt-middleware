/*
  $Id$

  Copyright (C) 2003-2014 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.beans.reflect;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapUtils;
import org.ldaptive.SortBehavior;
import org.ldaptive.beans.Attribute;
import org.ldaptive.beans.Entry;

/**
 * Class for testing bean annotations.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class BooleanCustomObject implements CustomObject
{

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 51;

  // CheckStyle:JavadocVariable OFF
  // CheckStyle:DeclarationOrder OFF
  private boolean type1;
  protected boolean type2;
  private boolean type3;
  private boolean[] typeArray1;
  protected boolean[] typeArray2;
  private Collection<Boolean> typeCol1;
  protected Collection<Boolean> typeCol2;
  private Set<Boolean> typeSet1;
  protected Set<Boolean> typeSet2;
  private List<Boolean> typeList1;
  protected List<Boolean> typeList2;
  // CheckStyle:DeclarationOrder ON
  // CheckStyle:JavadocVariable ON


  // CheckStyle:JavadocMethod OFF
  // CheckStyle:LeftCurly OFF
  public boolean getType1() { return type1; }
  public void setType1(final boolean t) { type1 = t; }
  public void writeType2(final boolean t) { type2 = t; }
  public boolean getType3() { return type3; }
  public void setType3(final boolean t) { type3 = t; }
  public boolean[] getTypeArray1() { return typeArray1; }
  public void setTypeArray1(final boolean[] t) { typeArray1 = t; }
  public void writeTypeArray2(final boolean[] t) { typeArray2 = t; }
  public Collection<Boolean> getTypeCol1() { return typeCol1; }
  public void setTypeCol1(final Collection<Boolean> c) { typeCol1 = c; }
  public void writeTypeCol2(final Collection<Boolean> c) { typeCol2 = c; }
  public Set<Boolean> getTypeSet1() { return typeSet1; }
  public void setTypeSet1(final Set<Boolean> s) { typeSet1 = s; }
  public void writeTypeSet2(final Set<Boolean> s) { typeSet2 = s; }
  public List<Boolean> getTypeList1() { return typeList1; }
  public void setTypeList1(final List<Boolean> l) { typeList1 = l; }
  public void writeTypeList2(final List<Boolean> l) { typeList2 = l; }
  // CheckStyle:LeftCurly ON
  // CheckStyle:JavadocMethod ON


  /** {@inheritDoc} */
  @Override
  public void initialize() {}


  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o)
  {
    return LdapUtils.areEqual(this, o);
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        type1,
        type2,
        type3,
        typeArray1,
        typeArray2,
        typeCol1 != null ? Collections.unmodifiableCollection(typeCol1) : null,
        typeCol2 != null ? Collections.unmodifiableCollection(typeCol2) : null,
        typeSet1,
        typeSet2,
        typeList1,
        typeList2);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return String.format(
      "[%s@%d::" +
        "type1=%s, type2=%s, type3=%s, " +
        "typeArray1=%s, typeArray2=%s, " +
        "typeCol1=%s, typeCol2=%s, " +
        "typeSet1=%s, typeSet2=%s, " +
        "typeList1=%s, typeList2=%s]",
      getClass().getSimpleName(),
      hashCode(),
      type1,
      type2,
      type3,
      Arrays.toString(typeArray1),
      Arrays.toString(typeArray2),
      typeCol1,
      typeCol2,
      typeSet1,
      typeSet2,
      typeList1,
      typeList2);
  }


  /** Test class for the default ldap entry mapper. */
  @Entry(
    dn = "cn=Boolean Entry,ou=people,dc=ldaptive,dc=org",
    attributes = {
      @Attribute(name = "type1", property = "type1"),
      @Attribute(name = "type2", property = "type2"),
      @Attribute(name = "booleanthree", property = "type3"),
      @Attribute(
        name = "typeArray1",
        property = "typeArray1",
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeArray2",
        property = "typeArray2",
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "typeCol1", property = "typeCol1"),
      @Attribute(name = "typeCol2", property = "typeCol2"),
      @Attribute(name = "typeSet1", property = "typeSet1"),
      @Attribute(name = "typeSet2", property = "typeSet2"),
      @Attribute(name = "typeList1", property = "typeList1"),
      @Attribute(name = "typeList2", property = "typeList2")
      }
  )
  public static class Default extends BooleanCustomObject {}


  /** Test class for the spring ldap entry mapper. */
  @Entry(
    dn = "cn=Boolean Entry,ou=people,dc=ldaptive,dc=org",
    attributes = {
      @Attribute(name = "type1", property = "type1"),
      @Attribute(name = "type2", property = "type2"),
      @Attribute(name = "booleanthree", property = "type3"),
      @Attribute(
        name = "typeArray1",
        property = "typeArray1",
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeArray2",
        property = "typeArray2",
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "typeCol1", property = "typeCol1"),
      @Attribute(name = "typeCol2", property = "typeCol2"),
      @Attribute(name = "typeSet1", property = "typeSet1"),
      @Attribute(name = "typeSet2", property = "typeSet2"),
      @Attribute(name = "typeList1", property = "typeList1"),
      @Attribute(name = "typeList2", property = "typeList2")
      }
  )
  public static class Spring extends BooleanCustomObject
  {
    // CheckStyle:JavadocMethod OFF
    // CheckStyle:LeftCurly OFF
    public boolean getType2() { return type2; }
    public void setType2(final boolean t) { type2 = t; }
    public boolean[] getTypeArray2() { return typeArray2; }
    public void setTypeArray2(final boolean[] t) { typeArray2 = t; }
    public Collection<Boolean> getTypeCol2() { return typeCol2; }
    public void setTypeCol2(final Collection<Boolean> c) { typeCol2 = c; }
    public Set<Boolean> getTypeSet2() { return typeSet2; }
    public void setTypeSet2(final Set<Boolean> s) { typeSet2 = s; }
    public List<Boolean> getTypeList2() { return typeList2; }
    public void setTypeList2(final List<Boolean> l) { typeList2 = l; }
    // CheckStyle:LeftCurly ON
    // CheckStyle:JavadocMethod ON
  }

  /**
   * Creates a boolean custom object for testing.
   *
   * @param  <T>  type of boolean custom object
   * @param  type  of boolean custom object
   *
   * @return  instance of boolean custom object
   */
  public static <T extends BooleanCustomObject> T createCustomObject(
    final Class<T> type)
  {
    final Set<Boolean> s1 = new HashSet<Boolean>();
    s1.add(true);
    s1.add(false);

    final T o1;
    try {
      o1 = type.newInstance();
    } catch (InstantiationException e) {
      throw new IllegalStateException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    o1.setType1(true);
    o1.writeType2(false);
    o1.setType3(true);
    o1.setTypeArray1(new boolean[]{false, true});
    o1.writeTypeArray2(new boolean[]{false, true});
    o1.setTypeCol1(Arrays.asList(true, false));
    o1.writeTypeCol2(Arrays.asList(false, true));
    o1.setTypeSet1(s1);
    o1.writeTypeSet2(s1);
    o1.setTypeList1(Arrays.asList(false, true));
    o1.writeTypeList2(Arrays.asList(true, false));

    return o1;
  }


  /**
   * Creates an ldap entry containing boolean based string values.
   *
   * @return  ldap entry
   */
  public static LdapEntry createLdapEntry()
  {
    final LdapAttribute typeArray1 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeArray1.setName("typeArray1");
    typeArray1.addStringValue("false", "true");
    final LdapAttribute typeArray2 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeArray2.setName("typeArray2");
    typeArray2.addStringValue("false", "true");

    final LdapAttribute typeCol1 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeCol1.setName("typeCol1");
    typeCol1.addStringValue("true", "false");
    final LdapAttribute typeCol2 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeCol2.setName("typeCol2");
    typeCol2.addStringValue("false", "true");

    final LdapAttribute typeSet1 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeSet1.setName("typeSet1");
    typeSet1.addStringValue("true", "false");
    final LdapAttribute typeSet2 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeSet2.setName("typeSet2");
    typeSet2.addStringValue("true", "false");

    final LdapAttribute typeList1 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeList1.setName("typeList1");
    typeList1.addStringValue("false", "true");
    final LdapAttribute typeList2 = new LdapAttribute(
      SortBehavior.ORDERED);
    typeList2.setName("typeList2");
    typeList2.addStringValue("true", "false");

    final LdapEntry entry = new LdapEntry();
    entry.setDn("cn=Boolean Entry,ou=people,dc=ldaptive,dc=org");
    entry.addAttribute(
      new LdapAttribute("type1", "true"),
      new LdapAttribute("type2", "false"),
      new LdapAttribute("booleanthree", "true"),
      typeArray1,
      typeArray2,
      typeCol1,
      typeCol2,
      typeSet1,
      typeSet2,
      typeList1,
      typeList2);
    return entry;
  }
}
