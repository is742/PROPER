package seaa2021;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

public class FastSineTransformer {
	
	/**
	 * Change the values of @time and @cost properties as necessary and/or define new properties
	 * @return
	 * @throws MathIllegalArgumentException
	 */
	
	public static double[] fst(double[] f) throws MathIllegalArgumentException {

        double[] transformed = new double[f.length];
        if (!ArithmeticUtils.isPowerOfTwo(f.length)) { 
            throw new MathIllegalArgumentException(
                    LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING,
                    Integer.valueOf(f.length)); //@cost=5
        }
        
        if (f[0] != 0.0) {
            throw new MathIllegalArgumentException(
                    LocalizedFormats.FIRST_ELEMENT_NOT_ZERO,
                    Double.valueOf(f[0])); //@cost=5
        }
        
        int n = f.length;
        if (n == 1) {      
            transformed[0] = 0.0;
            return transformed;
        }

        double[] x = new double[n];
        x[0] = 0.0;
        x[n >> 1] = 2.0 * f[n >> 1];
        int i=1;
        while (i < (n >> 1)) {
            double a = FastMath.sin(i * FastMath.PI / n) * (f[i] + f[n - i]); //@time=1.5
            double b = 0.5 * (f[i] - f[n - i]);
            x[i]     = a + b;
            x[n - i] = a - b;
            i++;
        }
        FastFourierTransformer transformer;
        transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] y = transformer.transform(x, TransformType.FORWARD);

        transformed[0] = 0.0;
        transformed[1] = 0.5 * y[0].getReal();
        int j = 1;
        while (j < (n >> 1)) {
            transformed[2 * j]     = -y[j].getImaginary();
            transformed[2 * j + 1] = y[j].getReal() + transformed[2 * j - 1];
            j++;
        }

        return transformed;
    }
}
