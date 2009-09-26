/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.chromattic.core.builder;

import org.chromattic.spi.instrument.Instrumentor;
import org.chromattic.spi.jcr.SessionLifeCycle;
import org.chromattic.core.Domain;
import org.chromattic.core.mapping.TypeMapping;
import org.chromattic.core.mapping.TypeMappingBuilder;
import org.chromattic.api.BuilderException;
import org.chromattic.api.Chromattic;
import org.chromattic.api.ChromatticBuilder;
import org.chromattic.api.format.DefaultObjectFormatter;
import org.chromattic.api.format.ObjectFormatter;
import org.reflext.jlr.JavaLangReflectTypeModel;
import org.reflext.jlr.JavaLangReflectMethodModel;
import org.reflext.core.TypeDomain;
import org.reflext.api.ClassTypeInfo;

import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ChromatticBuilderImpl extends ChromatticBuilder {

  /** . */
  private Instrumentor instrumentor;

  /** . */
  private SessionLifeCycle sessionProvider;

  /** . */
  private ObjectFormatter objectFormatter;

  /** . */
  private boolean stateCacheEnabled;

  /** . */
  private String rootNodePath;

  /** . */
  private Boolean optimizeJCREnabled;

  /** . */
  private boolean optimizeJCRHasPropertyEnabled;

  /** . */
  private boolean optimizeJCRHasNodeEnabled;

  public ChromatticBuilderImpl() {
    setOption(INSTRUMENTOR_CLASSNAME, "org.chromattic.apt.InstrumentorImpl");
    setOption(SESSION_LIFECYCLE_CLASSNAME, "org.chromattic.exo.ExoSessionLifeCycle");
    setOption(OBJECT_FORMATTER_CLASSNAME, DefaultObjectFormatter.class.getName());
    setOption(CACHE_STATE_ENABLED, false);
    setOption(JCR_OPTIMIZE_HAS_PROPERTY_ENABLED, false);
    setOption(JCR_OPTIMIZE_HAS_NODE_ENABLED, false);
    setOption(ROOT_NODE_PATH, "/");
  }

  private <T> T create(OptionInstance<String> optionInstance, Class<T> expectedClass) {
    Option<String> option = optionInstance.getOption();
    String s = optionInstance.getValue();
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Class<?> instrumentorClass = classLoader.loadClass(s);
      if (expectedClass.isAssignableFrom(instrumentorClass)) {
        Class<? extends T> ic2 = instrumentorClass.asSubclass(expectedClass);
        return ic2.newInstance();
      } else {
        throw new BuilderException("Class " + s + " does not implement the " +
          expectedClass.getName() + " interface");
      }
    }
    catch (InstantiationException e) {
      throw new BuilderException("Could not instanciate " + option.getShortName() + " " + s, e);
    }
    catch (IllegalAccessException e) {
      throw new BuilderException("Could not instanciate " + option.getShortName(), e);
    }
    catch (ClassNotFoundException e) {
      throw new BuilderException("Could not load " + option.getShortName() + " class " + s, e);
    }
  }

  protected <T> void configure(OptionInstance<T> optionInstance) {
    if (optionInstance.getOption() == INSTRUMENTOR_CLASSNAME) {
      instrumentor = create((OptionInstance<String>)optionInstance, Instrumentor.class);
    } else if (optionInstance.getOption() == SESSION_LIFECYCLE_CLASSNAME) {
      sessionProvider = create((OptionInstance<String>)optionInstance, SessionLifeCycle.class);
    } else if (optionInstance.getOption() == OBJECT_FORMATTER_CLASSNAME) {
      objectFormatter = create((OptionInstance<String>)optionInstance, ObjectFormatter.class);
    } else if (optionInstance.getOption() == CACHE_STATE_ENABLED) {
      stateCacheEnabled = ((OptionInstance<Boolean>)optionInstance).getValue();
    } else if (optionInstance.getOption() == ROOT_NODE_PATH) {
      rootNodePath = ((OptionInstance<String>)optionInstance).getValue();
    } else if (optionInstance.getOption() == JCR_OPTIMIZE_ENABLED) {
      optimizeJCREnabled = ((OptionInstance<Boolean>)optionInstance).getValue();
    } else if (optionInstance.getOption() == JCR_OPTIMIZE_HAS_PROPERTY_ENABLED) {
      optimizeJCRHasPropertyEnabled = ((OptionInstance<Boolean>)optionInstance).getValue();
    } else if (optionInstance.getOption() == JCR_OPTIMIZE_HAS_NODE_ENABLED) {
      optimizeJCRHasNodeEnabled = ((OptionInstance<Boolean>)optionInstance).getValue();
    }
  }

  protected Chromattic boot() throws Exception {

    // Configure from options
    for (OptionInstance<?> optionInstance : options.values()) {
      configure(optionInstance);
    }

    //
    TypeDomain<Type, Method> typeDomain = new TypeDomain<Type, Method>(new JavaLangReflectTypeModel(), new JavaLangReflectMethodModel());

    //
    Set<TypeMapping> mappings = new HashSet<TypeMapping>();
    for (Class clazz : classes) {
      ClassTypeInfo typeInfo = (ClassTypeInfo)typeDomain.getType(clazz);
      TypeMappingBuilder mappingBuilder = new TypeMappingBuilder(typeInfo);
      TypeMapping mapping = mappingBuilder.build();
      mappings.add(mapping);
    }

    //
    boolean hasPropertyOptimized = optimizeJCRHasPropertyEnabled;
    boolean hasNodeOptimized = optimizeJCRHasNodeEnabled;
    if (optimizeJCREnabled != null) {
      hasPropertyOptimized = optimizeJCREnabled;
      hasNodeOptimized = optimizeJCREnabled;
    }

    // Build domain
    Domain domain = new Domain(mappings, instrumentor, objectFormatter, stateCacheEnabled, hasPropertyOptimized, hasNodeOptimized, rootNodePath);

    //
    return new ChromatticImpl(domain, sessionProvider);
  }
}