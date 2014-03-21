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
package org.ldaptive.beans.spring;

import java.util.Calendar;
import org.ldaptive.beans.AbstractLdapEntryMapper;
import org.ldaptive.beans.ClassDescriptor;
import org.ldaptive.io.GeneralizedTimeValueTranscoder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;

/**
 * Uses a {@link SpringClassDescriptor} for ldap entry mapping.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class SpringLdapEntryMapper extends AbstractLdapEntryMapper<Object>
{

  /** Custom converters for the spring conversion service. */
  private Converter<?, ?>[] converters;


  /**
   * Sets additional converters to add to the spring conversion service.
   *
   * @param  c  additional converters
   */
  public void setConverters(final Converter<?, ?>... c)
  {
    converters = c;
  }


  /** {@inheritDoc} */
  @Override
  protected ClassDescriptor getClassDescriptor(final Object object)
  {
    final SpringClassDescriptor descriptor = new SpringClassDescriptor(
      createEvaluationContext(object));
    descriptor.initialize(object.getClass());
    return descriptor;
  }


  /**
   * Creates an evaluation context to use in the spring class descriptor. Adds
   * the default converters from the default conversion service.
   *
   * @param  object  to supply to the evaluation context
   *
   * @return  evalutation context
   */
  protected EvaluationContext createEvaluationContext(final Object object)
  {
    final GenericConversionService conversionService =
      new GenericConversionService();
    DefaultConversionService.addDefaultConverters(conversionService);
    if (converters != null) {
      for (Converter<?, ?> converter : converters) {
        conversionService.addConverter(converter);
      }
    }
    addDefaultConverters(conversionService);
    final StandardEvaluationContext context =
      new StandardEvaluationContext(object);
    context.setTypeConverter(new StandardTypeConverter(conversionService));
    return context;
  }


  /**
   * Adds default converters to the supplied conversion service.
   *
   * @param  service  to add default converters to
   */
  protected void addDefaultConverters(final GenericConversionService service)
  {
    if (!service.canConvert(String.class, Calendar.class)) {
      service.addConverter(new Converter<String, Calendar>()
      {
        @Override
        public Calendar convert(final String s)
        {
          final GeneralizedTimeValueTranscoder transcoder =
            new GeneralizedTimeValueTranscoder();
          return transcoder.decodeStringValue(s);
        }
      });
    }
    if (!service.canConvert(Calendar.class, String.class)) {
      service.addConverter(new Converter<Calendar, String>()
      {
        @Override
        public String convert(final Calendar c)
        {
          final GeneralizedTimeValueTranscoder transcoder = new
            GeneralizedTimeValueTranscoder();
          return transcoder.encodeStringValue(c);
        }
      });
    }
  }
}
