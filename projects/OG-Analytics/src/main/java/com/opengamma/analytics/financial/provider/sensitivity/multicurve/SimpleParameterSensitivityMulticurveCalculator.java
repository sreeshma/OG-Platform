/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.sensitivity.multicurve;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.provider.description.interestrate.MulticurveProviderInterface;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.util.tuple.DoublesPair;

/**
 * For an instrument, computes the sensitivity of a value (often the present value or a par spread) to the parameters used in the curve.
 * The meaning of "parameters" will depend of the way the curve is stored (interpolated yield, function parameters, etc.).
 * The return format is ParameterSensitivity object.
 */
public class SimpleParameterSensitivityMulticurveCalculator extends AbstractSimpleParameterSensitivityMulticurveCalculator {

  /**
   * Constructor
   * @param curveSensitivityCalculator The curve sensitivity calculator.
   */
  public SimpleParameterSensitivityMulticurveCalculator(InstrumentDerivativeVisitor<MulticurveProviderInterface, MulticurveSensitivity> curveSensitivityCalculator) {
    super(curveSensitivityCalculator);
  }

  /**
   * Computes the sensitivity with respect to the parameters from the point sensitivities to the continuously compounded rate.
   * @param sensitivity The point sensitivity.
   * @param multicurves The multi-curve provider. Not null.
   * @param curvesSet The set of curves for which the sensitivity will be computed. Not null.
   * @return The sensitivity (as a ParameterSensitivity).
   */
  @Override
  public SimpleParameterSensitivity pointToParameterSensitivity(final MulticurveSensitivity sensitivity, final MulticurveProviderInterface multicurves, final Set<String> curvesSet) {
    SimpleParameterSensitivity result = new SimpleParameterSensitivity();
    // YieldAndDiscount
    Map<String, List<DoublesPair>> sensitivityDsc = sensitivity.getYieldDiscountingSensitivities();
    for (final String name : sensitivityDsc.keySet()) {
      if (curvesSet.contains(name)) {
        result = result.plus(name, new DoubleMatrix1D(multicurves.parameterSensitivity(name, sensitivityDsc.get(name))));
      }
    }
    // Forward
    Map<String, List<ForwardSensitivity>> sensitivityFwd = sensitivity.getForwardSensitivities();
    for (final String name : sensitivityFwd.keySet()) {
      if (curvesSet.contains(name)) {
        result = result.plus(name, new DoubleMatrix1D(multicurves.parameterForwardSensitivity(name, sensitivityFwd.get(name))));
      }
    }
    return result;
  }
}