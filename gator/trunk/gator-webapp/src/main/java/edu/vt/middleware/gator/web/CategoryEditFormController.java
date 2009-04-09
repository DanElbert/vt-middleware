/*
  $Id$

  Copyright (C) 2008 Virginia Tech, Marvin S. Addison.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Marvin S. Addison
  Email:   serac@vt.edu
  Version: $Revision$
  Updated: $Date$
 */
package edu.vt.middleware.gator.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import edu.vt.middleware.gator.AppenderConfig;
import edu.vt.middleware.gator.CategoryConfig;
import edu.vt.middleware.gator.ProjectConfig;
import edu.vt.middleware.gator.util.RequestParamExtractor;

/**
 * Handles edits to logging category configuration.
 *
 * @author Marvin S. Addison
 *
 */
public class CategoryEditFormController extends BaseFormController
{
  /** {@inheritDoc} */
  @Override
  protected Object formBackingObject(final HttpServletRequest request)
      throws Exception
  {
    final ProjectConfig project = configManager.findProject(
      RequestParamExtractor.getProjectName(request));
    if (project == null) {
      throw new IllegalArgumentException("Project not found.");
    }
    final CategoryConfig category = project.getCategory(
      RequestParamExtractor.getCategoryId(request));
    CategoryWrapper wrapper = null;
    if (category == null) {
      final CategoryConfig newCategory = new CategoryConfig();
      newCategory.setProject(project);
      wrapper = new CategoryWrapper(newCategory);
    } else {
      wrapper = new CategoryWrapper(category);
    }
    return wrapper;
  }


  /** {@inheritDoc} */
  @Override
  protected Map<String, Object> referenceData(final HttpServletRequest request)
      throws Exception
  {
    final Map<String, Object> refData = new HashMap<String, Object>();
    final ProjectConfig project = configManager.findProject(
      RequestParamExtractor.getProjectName(request));
    refData.put("project", project);
    refData.put("availableAppenders", project.getAppenders());
    refData.put("logLevels", CategoryConfig.LOG_LEVELS);
    return refData;
  }


  /** {@inheritDoc} */
  @Override
  protected ModelAndView onSubmit(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object command, final BindException errors)
      throws Exception
  {
    final CategoryWrapper wrapper = (CategoryWrapper) command;
    final CategoryConfig category = wrapper.getCategory();
    final ProjectConfig project = category.getProject();
    category.getAppenders().clear();
    for (int id : wrapper.getAppenderIds()) {
      final AppenderConfig appender = project.getAppender(id);
      if (appender == null) {
        throw new IllegalArgumentException(String.format(
            "Appender ID=%s does not exist in project.", id));
      }
      category.getAppenders().add(appender);
    }
    if (!configManager.exists(category)) {
      project.addCategory(category);
    }
    configManager.save(project);
    return new ModelAndView(
        ControllerHelper.filterViewName(getSuccessView(), project));
  }


  /**
  Wrapper class for {@link CategoryConfig} that exposes additional attributes
   * needed for binding to forms.
   *
   * @author Marvin S. Addison
   *
   */
  public static class CategoryWrapper
  {
    private CategoryConfig category;
    
    private int[] appenderIds;


    /**
     * Creates a new wrapper around the given category configuration.
     * @param wrapped Category configuration to wrap.
     */
    public CategoryWrapper(final CategoryConfig wrapped)
    {
      setCategory(wrapped);
      final int[] ids = new int[wrapped.getAppenders().size()];
      int i = 0;
      for (AppenderConfig appender : wrapped.getAppenders()) {
        ids[i++] = appender.getId();
      }
      setAppenderIds(ids);
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryConfig category)
    {
      this.category = category;
    }

    /**
     * @return the category
     */
    public CategoryConfig getCategory()
    {
      return category;
    }

    /**
     * Gets the IDs of all appenders this category sends logging events to.
     * @return Array of IDs of associated appenders.
     */
    public int[] getAppenderIds()
    {
      return appenderIds;
    }

    /**
     * Sets the IDs of all appenders this category sends logging events to.
     * @param ids Array of IDs of associated appenders.
     */
    public void setAppenderIds(final int[] ids)
    {
      appenderIds = ids;
    }
  }
}
