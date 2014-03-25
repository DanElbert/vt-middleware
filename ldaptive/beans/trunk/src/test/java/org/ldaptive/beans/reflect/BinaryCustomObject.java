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
import java.util.LinkedHashSet;
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
public class BinaryCustomObject implements CustomObject
{

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 21;

  // CheckStyle:JavadocVariable OFF
  // CheckStyle:DeclarationOrder OFF
  private byte[] type1;
  protected byte[] type2;
  private byte[] type3;
  private Collection<byte[]> typeCol1;
  protected Collection<byte[]> typeCol2;
  private Set<byte[]> typeSet1;
  protected Set<byte[]> typeSet2;
  private List<byte[]> typeList1;
  protected List<byte[]> typeList2;
  // CheckStyle:DeclarationOrder ON
  // CheckStyle:JavadocVariable ON


  // CheckStyle:JavadocMethod OFF
  // CheckStyle:LeftCurly OFF
  public byte[] getType1() { return type1; }
  public void setType1(final byte[] t) { type1 = t; }
  public void writeType2(final byte[] t) { type2 = t; }
  public byte[] getType3() { return type3; }
  public void setType3(final byte[] t) { type3 = t; }
  public Collection<byte[]> getTypeCol1() { return typeCol1; }
  public void setTypeCol1(final Collection<byte[]> c) { typeCol1 = c; }
  public void writeTypeCol2(final Collection<byte[]> c) { typeCol2 = c; }
  public Set<byte[]> getTypeSet1() { return typeSet1; }
  public void setTypeSet1(final Set<byte[]> s) { typeSet1 = s; }
  public void writeTypeSet2(final Set<byte[]> s) { typeSet2 = s; }
  public List<byte[]> getTypeList1() { return typeList1; }
  public void setTypeList1(final List<byte[]> l) { typeList1 = l; }
  public void writeTypeList2(final List<byte[]> l) { typeList2 = l; }
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
      "typeCol1=%s, typeCol2=%s, " +
      "typeSet1=%s, typeSet2=%s, " +
      "typeList1=%s, typeList2=%s]",
      getClass().getSimpleName(),
      hashCode(),
      Arrays.toString(type1),
      Arrays.toString(type2),
      Arrays.toString(type3),
      toString(typeCol1),
      toString(typeCol2),
      toString(typeSet1),
      toString(typeSet2),
      toString(typeList1),
      toString(typeList2));
  }


  /**
   * Returns a string representation of the supplied collection.
   *
   * @param  c  collection to represent as a string
   *
   * @return  collection as a string
   */
  private String toString(final Collection<byte[]> c)
  {
    String s = null;
    if (c != null) {
      s = "";
      for (byte[] t : c) {
        s += Arrays.toString((byte[]) t);
      }
    }
    return s;
  }


  /** Test class for the default ldap entry mapper. */
  @Entry(
    dn = "cn=Binary Entry,ou=people,dc=ldaptive,dc=org",
    attributes = {
      @Attribute(name = "customname1", values = "QEFCQw==", binary = true),
      @Attribute(
        name = "customname2",
        values = {"REVGRw==", "SElQUQ=="},
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "type1", property = "type1", binary = true),
      @Attribute(name = "type2", property = "type2", binary = true),
      @Attribute(name = "binarythree", property = "type3", binary = true),
      @Attribute(
        name = "typeCol1",
        property = "typeCol1",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeCol2",
        property = "typeCol2",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "typeSet1", property = "typeSet1", binary = true),
      @Attribute(name = "typeSet2", property = "typeSet2", binary = true),
      @Attribute(
        name = "typeList1",
        property = "typeList1",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeList2",
        property = "typeList2",
        binary = true,
        sortBehavior = SortBehavior.ORDERED)
      }
  )
  public static class Default extends BinaryCustomObject {}


  /** Test class for the spring ldap entry mapper. */
  @Entry(
    dn = "cn=Binary Entry,ou=people,dc=ldaptive,dc=org",
    attributes = {
      @Attribute(name = "customname1", values = "QEFCQw==", binary = true),
      @Attribute(
        name = "customname2",
        values = {"REVGRw==", "SElQUQ=="},
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "type1", property = "type1", binary = true),
      @Attribute(name = "type2", property = "type2", binary = true),
      @Attribute(name = "binarythree", property = "type3", binary = true),
      @Attribute(
        name = "typeCol1",
        property = "typeCol1",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeCol2",
        property = "typeCol2",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(name = "typeSet1", property = "typeSet1", binary = true),
      @Attribute(name = "typeSet2", property = "typeSet2", binary = true),
      @Attribute(
        name = "typeList1",
        property = "typeList1",
        binary = true,
        sortBehavior = SortBehavior.ORDERED),
      @Attribute(
        name = "typeList2",
        property = "typeList2",
        binary = true,
        sortBehavior = SortBehavior.ORDERED)
      }
  )
  public static class Spring extends BinaryCustomObject
  {
    // CheckStyle:JavadocMethod OFF
    // CheckStyle:LeftCurly OFF
    public byte[] getType2() { return type2; }
    public void setType2(final byte[] t) { type2 = t; }
    public Collection<byte[]> getTypeCol2() { return typeCol2; }
    public void setTypeCol2(final Collection<byte[]> c) { typeCol2 = c; }
    public Set<byte[]> getTypeSet2() { return typeSet2; }
    public void setTypeSet2(final Set<byte[]> s) { typeSet2 = s; }
    public List<byte[]> getTypeList2() { return typeList2; }
    public void setTypeList2(final List<byte[]> l) { typeList2 = l; }
    // CheckStyle:LeftCurly ON
    // CheckStyle:JavadocMethod ON
  }


  /**
   * Creates a binary custom object for testing.
   *
   * @param  <T>  type of binary custom object
   * @param  type  of binary custom object
   *
   * @return  instance of binary custom object
   */
  public static <T extends BinaryCustomObject> T createCustomObject(
    final Class<T> type)
  {
    // CheckStyle:MagicNumber OFF
    final Set<byte[]> s1 = new LinkedHashSet<byte[]>();
    s1.add(new byte[]{0x22});
    s1.add(new byte[]{0x23});

    final T o1;
    try {
      o1 = type.newInstance();
    } catch (InstantiationException e) {
      throw new IllegalStateException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    o1.setType1(new byte[]{0x01});
    o1.writeType2(new byte[]{0x02});
    o1.setType3(new byte[]{0x03});
    o1.setTypeCol1(Arrays.asList(new byte[]{0x20}, new byte[]{0x21}));
    o1.writeTypeCol2(Arrays.asList(new byte[]{0x20}, new byte[]{0x21}));
    o1.setTypeSet1(s1);
    o1.writeTypeSet2(s1);
    o1.setTypeList1(Arrays.asList(new byte[]{0x24}, new byte[]{0x25}));
    o1.writeTypeList2(Arrays.asList(new byte[]{0x24}, new byte[]{0x25}));

    return o1;
    // CheckStyle:MagicNumber ON
  }


  /**
   * Creates an ldap entry containing binary data.
   *
   * @return  ldap entry
   */
  public static LdapEntry createLdapEntry()
  {
    // CheckStyle:MagicNumber OFF
    final LdapAttribute typeCol1 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeCol1.setName("typeCol1");
    typeCol1.addBinaryValue(new byte[] {0x20}, new byte[] {0x21});
    final LdapAttribute typeCol2 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeCol2.setName("typeCol2");
    typeCol2.addBinaryValue(new byte[] {0x20}, new byte[] {0x21});

    final LdapAttribute typeSet1 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeSet1.setName("typeSet1");
    typeSet1.addBinaryValue(new byte[] {0x22}, new byte[] {0x23});
    final LdapAttribute typeSet2 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeSet2.setName("typeSet2");
    typeSet2.addBinaryValue(new byte[] {0x22}, new byte[] {0x23});

    final LdapAttribute typeList1 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeList1.setName("typeList1");
    typeList1.addBinaryValue(new byte[] {0x24}, new byte[] {0x25});
    final LdapAttribute typeList2 = new LdapAttribute(
      SortBehavior.ORDERED, true);
    typeList2.setName("typeList2");
    typeList2.addBinaryValue(new byte[] {0x24}, new byte[] {0x25});

    final LdapEntry entry = new LdapEntry();
    entry.setDn("cn=Binary Entry,ou=people,dc=ldaptive,dc=org");
    entry.addAttribute(
      new LdapAttribute(
        "customname1",
        new byte[] {0x40, 0x41, 0x42, 0x43}),
      new LdapAttribute(
        "customname2",
        new byte[] {0x44, 0x45, 0x46, 0x47},
        new byte[] {0x48, 0x49, 0x50, 0x51}),
      new LdapAttribute("type1", new byte[] {0x01}),
      new LdapAttribute("type2", new byte[] {0x02}),
      new LdapAttribute("binarythree", new byte[] {0x03}),
      typeCol1,
      typeCol2,
      typeSet1,
      typeSet2,
      typeList1,
      typeList2);
    return entry;
    // CheckStyle:MagicNumber ON
  }
}
