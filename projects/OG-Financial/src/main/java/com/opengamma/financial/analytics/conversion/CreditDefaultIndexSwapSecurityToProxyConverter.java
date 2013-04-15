/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import com.opengamma.analytics.financial.credit.creditdefaultswap.definition.vanilla.CreditDefaultSwapDefinition;
import com.opengamma.financial.security.FinancialSecurityVisitorAdapter;
import com.opengamma.financial.security.cds.CreditDefaultSwapIndexDefinitionSecurity;

/**
 * 
 */
public class CreditDefaultIndexSwapSecurityToProxyConverter extends FinancialSecurityVisitorAdapter<CreditDefaultSwapDefinition> {

  @Override
  public CreditDefaultSwapDefinition visitCreditDefaultSwapIndexDefinitionSecurity(final CreditDefaultSwapIndexDefinitionSecurity security) {
    return null;
  }
}