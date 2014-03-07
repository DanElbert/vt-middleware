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
package org.ldaptive.schema;

import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ldaptive.LdapUtils;

/**
 * Bean for a DIT content rule schema element.
 * <pre>
   DITStructureRuleDescription = LPAREN WSP
     ruleid                     ; rule identifier
     [ SP "NAME" SP qdescrs ]   ; short names (descriptors)
     [ SP "DESC" SP qdstring ]  ; description
     [ SP "OBSOLETE" ]          ; not active
     SP "FORM" SP oid           ; NameForm
     [ SP "SUP" ruleids ]       ; superior rules
     extensions WSP RPAREN      ; extensions
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class DITStructureRule extends AbstractNamedSchemaElement
{

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 1153;

  /** Pattern to match definitions. */
  private static final Pattern DEFINITION_PATTERN = Pattern.compile(
    WSP_REGEX + "\\(" +
      WSP_REGEX + "(\\p{Digit}+)" +
      WSP_REGEX + "(?:NAME (?:'([^']+)'|\\(([^\\)]+)\\)))?" +
      WSP_REGEX + "(?:DESC '([^']+)')?" +
      WSP_REGEX + "(OBSOLETE)?" +
      WSP_REGEX + "(?:FORM (" + NO_WSP_REGEX + "))?" +
      WSP_REGEX + "(?:SUP (?:(" + NO_WSP_REGEX + ")|\\(([^\\)]+)\\)))?" +
      WSP_REGEX + "(?:(X-[^ ]+.*))?" +
      WSP_REGEX + "\\)" + WSP_REGEX);

  /** ID. */
  private final int id;

  /** Name form. */
  private String nameForm;

  /** Superior rules. */
  private int[] superiorRules;


  /**
   * Creates a new DIT structure rule.
   *
   * @param  i  id
   */
  public DITStructureRule(final int i)
  {
    id = i;
  }


  /**
   * Creates a new DIT structure rule.
   *
   * @param  id  id
   * @param  names  names
   * @param  description  description
   * @param  obsolete  obsolete
   * @param  nameForm  name form
   * @param  superiorRules  superior rules
   * @param  extensions  extensions
   */
  // CheckStyle:ParameterNumber|HiddenField OFF
  public DITStructureRule(
    final int id, final String[] names, final String description,
    final boolean obsolete, final String nameForm, final int[] superiorRules,
    final Extensions extensions)
  {
    this(id);
    setNames(names);
    setDescription(description);
    setObsolete(obsolete);
    setNameForm(nameForm);
    setSuperiorRules(superiorRules);
    setExtensions(extensions);
  }
  // CheckStyle:ParameterNumber|HiddenField ON


  /**
   * Returns the id.
   *
   * @return  id
   */
  public int getID()
  {
    return id;
  }


  /**
   * Returns the name form.
   *
   * @return  name form
   */
  public String getNameForm()
  {
    return nameForm;
  }


  /**
   * Sets the name form.
   *
   * @param  s  name form
   */
  public void setNameForm(final String s)
  {
    nameForm = s;
  }


  /**
   * Returns the superior rules.
   *
   * @return  superior rules
   */
  public int[] getSuperiorRules()
  {
    return superiorRules;
  }


  /**
   * Sets the superior rules.
   *
   * @param  i  superior rules
   */
  public void setSuperiorRules(final int[] i)
  {
    superiorRules = i;
  }


  /**
   * Parses the supplied definition string and creates an initialized DIT
   * structure rule.
   *
   * @param  definition  to parse
   *
   * @return  DIT structure rule
   *
   * @throws  ParseException  if the supplied definition is invalid
   */
  public static DITStructureRule parse(final String definition)
    throws ParseException
  {
    final Matcher m = DEFINITION_PATTERN.matcher(definition);
    if (!m.matches()) {
      throw new ParseException(
        "Invalid DIT structure rule definition: " + definition,
        definition.length());
    }

    final DITStructureRule dsrd = new DITStructureRule(
      Integer.parseInt(m.group(1).trim()));

    // CheckStyle:MagicNumber OFF
    // parse names
    if (m.group(2) != null) {
      dsrd.setNames(SchemaUtils.parseDescriptors(m.group(2).trim()));
    } else if (m.group(3) != null) {
      dsrd.setNames(SchemaUtils.parseDescriptors(m.group(3).trim()));
    }

    dsrd.setDescription(m.group(4) != null ? m.group(4).trim() : null);
    dsrd.setObsolete(m.group(5) != null);
    dsrd.setNameForm(m.group(6) != null ? m.group(6).trim() : null);

    // parse superior rules
    if (m.group(7) != null) {
      dsrd.setSuperiorRules(SchemaUtils.parseNumbers(m.group(7).trim()));
    } else if (m.group(8) != null) {
      dsrd.setSuperiorRules(SchemaUtils.parseNumbers(m.group(8).trim()));
    }

    // parse extensions
    if (m.group(9) != null) {
      dsrd.setExtensions(Extensions.parse(m.group(9).trim()));
    }
    return dsrd;
    // CheckStyle:MagicNumber ON
  }


  /** {@inheritDoc} */
  @Override
  public String format()
  {
    final StringBuilder sb = new StringBuilder("( ");
    sb.append(id).append(" ");
    if (getNames() != null && getNames().length > 0) {
      sb.append("NAME ");
      sb.append(SchemaUtils.formatDescriptors(getNames()));
    }
    if (getDescription() != null) {
      sb.append("DESC '").append(getDescription()).append("' ");
    }
    if (isObsolete()) {
      sb.append("OBSOLETE ");
    }
    if (nameForm != null) {
      sb.append("FORM ").append(nameForm).append(" ");
    }
    if (superiorRules != null && superiorRules.length > 0) {
      sb.append("SUP ");
      sb.append(SchemaUtils.formatNumbers(superiorRules));
    }
    if (getExtensions() != null) {
      sb.append(getExtensions().format());
    }
    sb.append(")");
    return sb.toString();
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        id,
        getNames(),
        getDescription(),
        isObsolete(),
        nameForm,
        superiorRules,
        getExtensions());
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::id=%s, names=%s, description=%s, obsolete=%s, " +
          "nameForm=%s, superiorRules=%s, extensions=%s]",
        getClass().getName(),
        hashCode(),
        id,
        Arrays.toString(getNames()),
        getDescription(),
        isObsolete(),
        nameForm,
        Arrays.toString(superiorRules),
        getExtensions());
  }
}
