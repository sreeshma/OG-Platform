/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.engine;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.position.PositionSource;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.CachingComputationTargetResolver;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.DefaultCachingComputationTargetResolver;
import com.opengamma.engine.DefaultComputationTargetResolver;
import com.opengamma.financial.currency.CurrencyPair;
import com.opengamma.financial.temptarget.ConfigItemTarget;
import com.opengamma.financial.temptarget.ConfigItemTargetResolver;
import com.opengamma.financial.temptarget.TempTarget;
import com.opengamma.financial.temptarget.TempTargetResolver;
import com.opengamma.financial.temptarget.TempTargetSource;

/**
 * Component factory for the target resolver.
 */
@BeanDefinition
public class TargetResolverComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The security source.
   */
  @PropertyDefinition(validate = "notNull")
  private SecuritySource _securitySource;
  /**
   * The position source.
   */
  @PropertyDefinition(validate = "notNull")
  private PositionSource _positionSource;
  /**
   * The temporary targets.
   */
  @PropertyDefinition
  private TempTargetSource _tempTargets;
  /**
   * The cache manager. If set a caching target resolver will be created, omit to not cache.
   */
  @PropertyDefinition
  private CacheManager _cacheManager;
  /**
   * The configuration source.
   */
  @PropertyDefinition
  private ConfigSource _configSource;

  protected ComputationTargetResolver createTargetResolver() {
    final DefaultComputationTargetResolver resolver = new DefaultComputationTargetResolver(getSecuritySource(), getPositionSource());
    initDefaultResolver(resolver);
    return resolver;
  }

  protected void initDefaultResolver(final DefaultComputationTargetResolver resolver) {
    if (getTempTargets() != null) {
      resolver.addResolver(TempTarget.TYPE, new TempTargetResolver(getTempTargets()));
    }
    resolver.addResolver(CurrencyPair.TYPE);
    if (_configSource != null) {
      resolver.addResolver(ConfigItemTarget.TYPE, new ConfigItemTargetResolver(_configSource));
    }
  }

  protected CachingComputationTargetResolver createCachedTargetResolver(final ComputationTargetResolver underlying) {
    return new DefaultCachingComputationTargetResolver(underlying, getCacheManager());
  }

  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {
    ComputationTargetResolver resolver = createTargetResolver();
    if (getCacheManager() != null) {
      resolver = createCachedTargetResolver(resolver);
    }
    repo.registerComponent(new ComponentInfo(ComputationTargetResolver.class, getClassifier()), resolver);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code TargetResolverComponentFactory}.
   * 
   * @return the meta-bean, not null
   */
  public static TargetResolverComponentFactory.Meta meta() {
    return TargetResolverComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(TargetResolverComponentFactory.Meta.INSTANCE);
  }

  @Override
  public TargetResolverComponentFactory.Meta metaBean() {
    return TargetResolverComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431: // classifier
        return getClassifier();
      case -702456965: // securitySource
        return getSecuritySource();
      case -1655657820: // positionSource
        return getPositionSource();
      case 1942609550: // tempTargets
        return getTempTargets();
      case -1452875317: // cacheManager
        return getCacheManager();
      case 195157501: // configSource
        return getConfigSource();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431: // classifier
        setClassifier((String) newValue);
        return;
      case -702456965: // securitySource
        setSecuritySource((SecuritySource) newValue);
        return;
      case -1655657820: // positionSource
        setPositionSource((PositionSource) newValue);
        return;
      case 1942609550: // tempTargets
        setTempTargets((TempTargetSource) newValue);
        return;
      case -1452875317: // cacheManager
        setCacheManager((CacheManager) newValue);
        return;
      case 195157501: // configSource
        setConfigSource((ConfigSource) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_securitySource, "securitySource");
    JodaBeanUtils.notNull(_positionSource, "positionSource");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      TargetResolverComponentFactory other = (TargetResolverComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getSecuritySource(), other.getSecuritySource()) &&
          JodaBeanUtils.equal(getPositionSource(), other.getPositionSource()) &&
          JodaBeanUtils.equal(getTempTargets(), other.getTempTargets()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          JodaBeanUtils.equal(getConfigSource(), other.getConfigSource()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSecuritySource());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPositionSource());
    hash += hash * 31 + JodaBeanUtils.hashCode(getTempTargets());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    hash += hash * 31 + JodaBeanUtils.hashCode(getConfigSource());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * 
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * 
   * @param classifier the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * 
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security source.
   * 
   * @return the value of the property, not null
   */
  public SecuritySource getSecuritySource() {
    return _securitySource;
  }

  /**
   * Sets the security source.
   * 
   * @param securitySource the new value of the property, not null
   */
  public void setSecuritySource(SecuritySource securitySource) {
    JodaBeanUtils.notNull(securitySource, "securitySource");
    this._securitySource = securitySource;
  }

  /**
   * Gets the the {@code securitySource} property.
   * 
   * @return the property, not null
   */
  public final Property<SecuritySource> securitySource() {
    return metaBean().securitySource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position source.
   * 
   * @return the value of the property, not null
   */
  public PositionSource getPositionSource() {
    return _positionSource;
  }

  /**
   * Sets the position source.
   * 
   * @param positionSource the new value of the property, not null
   */
  public void setPositionSource(PositionSource positionSource) {
    JodaBeanUtils.notNull(positionSource, "positionSource");
    this._positionSource = positionSource;
  }

  /**
   * Gets the the {@code positionSource} property.
   * 
   * @return the property, not null
   */
  public final Property<PositionSource> positionSource() {
    return metaBean().positionSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the temporary targets.
   * 
   * @return the value of the property
   */
  public TempTargetSource getTempTargets() {
    return _tempTargets;
  }

  /**
   * Sets the temporary targets.
   * 
   * @param tempTargets the new value of the property
   */
  public void setTempTargets(TempTargetSource tempTargets) {
    this._tempTargets = tempTargets;
  }

  /**
   * Gets the the {@code tempTargets} property.
   * 
   * @return the property, not null
   */
  public final Property<TempTargetSource> tempTargets() {
    return metaBean().tempTargets().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cache manager. If set a caching target resolver will be created, omit to not cache.
   * 
   * @return the value of the property
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cache manager. If set a caching target resolver will be created, omit to not cache.
   * 
   * @param cacheManager the new value of the property
   */
  public void setCacheManager(CacheManager cacheManager) {
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * 
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the configuration source.
   * 
   * @return the value of the property
   */
  public ConfigSource getConfigSource() {
    return _configSource;
  }

  /**
   * Sets the configuration source.
   * 
   * @param configSource the new value of the property
   */
  public void setConfigSource(ConfigSource configSource) {
    this._configSource = configSource;
  }

  /**
   * Gets the the {@code configSource} property.
   * 
   * @return the property, not null
   */
  public final Property<ConfigSource> configSource() {
    return metaBean().configSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code TargetResolverComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", TargetResolverComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code securitySource} property.
     */
    private final MetaProperty<SecuritySource> _securitySource = DirectMetaProperty.ofReadWrite(
        this, "securitySource", TargetResolverComponentFactory.class, SecuritySource.class);
    /**
     * The meta-property for the {@code positionSource} property.
     */
    private final MetaProperty<PositionSource> _positionSource = DirectMetaProperty.ofReadWrite(
        this, "positionSource", TargetResolverComponentFactory.class, PositionSource.class);
    /**
     * The meta-property for the {@code tempTargets} property.
     */
    private final MetaProperty<TempTargetSource> _tempTargets = DirectMetaProperty.ofReadWrite(
        this, "tempTargets", TargetResolverComponentFactory.class, TempTargetSource.class);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", TargetResolverComponentFactory.class, CacheManager.class);
    /**
     * The meta-property for the {@code configSource} property.
     */
    private final MetaProperty<ConfigSource> _configSource = DirectMetaProperty.ofReadWrite(
        this, "configSource", TargetResolverComponentFactory.class, ConfigSource.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "securitySource",
        "positionSource",
        "tempTargets",
        "cacheManager",
        "configSource");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431: // classifier
          return _classifier;
        case -702456965: // securitySource
          return _securitySource;
        case -1655657820: // positionSource
          return _positionSource;
        case 1942609550: // tempTargets
          return _tempTargets;
        case -1452875317: // cacheManager
          return _cacheManager;
        case 195157501: // configSource
          return _configSource;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends TargetResolverComponentFactory> builder() {
      return new DirectBeanBuilder<TargetResolverComponentFactory>(new TargetResolverComponentFactory());
    }

    @Override
    public Class<? extends TargetResolverComponentFactory> beanType() {
      return TargetResolverComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code securitySource} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<SecuritySource> securitySource() {
      return _securitySource;
    }

    /**
     * The meta-property for the {@code positionSource} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<PositionSource> positionSource() {
      return _positionSource;
    }

    /**
     * The meta-property for the {@code tempTargets} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<TempTargetSource> tempTargets() {
      return _tempTargets;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

    /**
     * The meta-property for the {@code configSource} property.
     * 
     * @return the meta-property, not null
     */
    public final MetaProperty<ConfigSource> configSource() {
      return _configSource;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
