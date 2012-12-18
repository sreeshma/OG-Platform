/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.blotter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;

import com.google.common.collect.Lists;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
/* package */ class BeanTraverser {

  private final List<BeanVisitorDecorator> _decorators;

  /* package */ BeanTraverser() {
    _decorators = Collections.emptyList();
  }

  /* package */ BeanTraverser(BeanVisitorDecorator... decorators) {
    _decorators = Arrays.asList(decorators);
    // first decorator in the list should be on the outside, need to reverse before wrapping
    Collections.reverse(_decorators);
  }

  /* package */ Object traverse(MetaBean bean, BeanVisitor<?> visitor) {
    BeanVisitor<?> decoratedVisitor = decorate(visitor);
    decoratedVisitor.visitBean(bean);
    List<Failure> failures = Lists.newArrayList();
    for (MetaProperty<?> property : bean.metaPropertyIterable()) {
      Class<?> propertyType = property.propertyType();
      try {
        if (Bean.class.isAssignableFrom(propertyType)) {
          decoratedVisitor.visitBeanProperty(property, this);
        } else if (Set.class.isAssignableFrom(propertyType)) {
          decoratedVisitor.visitSetProperty(property);
        } else if (List.class.isAssignableFrom(propertyType)) {
          decoratedVisitor.visitListProperty(property);
        } else if (Collection.class.isAssignableFrom(propertyType)) {
          decoratedVisitor.visitCollectionProperty(property);
        } else if (Map.class.isAssignableFrom(propertyType)) {
          decoratedVisitor.visitMapProperty(property);
        } else {
          decoratedVisitor.visitProperty(property);
        }
      } catch (Exception e) {
        failures.add(new Failure(e, property));
      }
    }
    if (failures.isEmpty()) {
      return decoratedVisitor.finish();
    } else {
      throw new TraversalException(failures);
    }
  }

  private BeanVisitor<?> decorate(BeanVisitor<?> visitor) {
    BeanVisitor<?> decoratedVisitor = visitor;
    for (BeanVisitorDecorator decorator : _decorators) {
      decoratedVisitor = decorator.decorate(decoratedVisitor);
    }
    return decoratedVisitor;
  }

  /* package */ static final class Failure {

    private final Exception _exception;
    private final MetaProperty<?> _property;

    private Failure(Exception exception, MetaProperty<?> property) {
      ArgumentChecker.notNull(exception, "exception");
      ArgumentChecker.notNull(property, "property");
      _exception = exception;
      _property = property;
    }

    /* package */ Exception getException() {
      return _exception;
    }

    /* package */ MetaProperty<?> getProperty() {
      return _property;
    }
  }

  /* package */ static final class TraversalException extends RuntimeException {

    private final List<Failure> _failures;

    /* package */ TraversalException(List<Failure> failures) {
      super("Bean traversal failed");
      ArgumentChecker.notEmpty(failures, "failures");
      _failures = failures;
    }

    /* package */ List<Failure> getFailures() {
      return _failures;
    }
  }
}