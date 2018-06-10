package ca.uwaterloo.crysp.touchclassifier;

/*************************************************************************
source: http://introcs.cs.princeton.edu/java/97data/Complex.java.html
Copyright � 2000�2011, Robert Sedgewick and Kevin Wayne
*************************************************************************/

public class ComplexNumbers {

    private final double re;   // the real part
    private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public ComplexNumbers(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude and angle/phase/argument
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi

    // return a new Complex object whose value is (this + b)
    public ComplexNumbers plus(ComplexNumbers b) {
        ComplexNumbers a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new ComplexNumbers(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public ComplexNumbers minus(ComplexNumbers b) {
        ComplexNumbers a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new ComplexNumbers(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public ComplexNumbers times(ComplexNumbers b) {
        ComplexNumbers a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new ComplexNumbers(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public ComplexNumbers times(double alpha) {
        return new ComplexNumbers(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public ComplexNumbers conjugate() {  return new ComplexNumbers(re, -im); }

    // return a new Complex object whose value is the reciprocal of this
    public ComplexNumbers reciprocal() {
        double scale = re*re + im*im;
        return new ComplexNumbers(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() { return re; }
    public double im() { return im; }

    // return a / b
    public ComplexNumbers divides(ComplexNumbers b) {
        ComplexNumbers a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public ComplexNumbers exp() {
        return new ComplexNumbers(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public ComplexNumbers sin() {
        return new ComplexNumbers(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public ComplexNumbers cos() {
        return new ComplexNumbers(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public ComplexNumbers tan() {
        return sin().divides(cos());
    }



    // a static version of plus
    public static ComplexNumbers plus(ComplexNumbers a, ComplexNumbers b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        ComplexNumbers sum = new ComplexNumbers(real, imag);
        return sum;
    }



    /* sample client for testing
    public static void main(String[] args) {
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(-3.0, 4.0);

        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("Re(a)        = " + a.re());
        System.out.println("Im(a)        = " + a.im());
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
        System.out.println("a / b        = " + a.divides(b));
        System.out.println("(a / b) * b  = " + a.divides(b).times(b));
        System.out.println("conj(a)      = " + a.conjugate());
        System.out.println("|a|          = " + a.abs());
        System.out.println("tan(a)       = " + a.tan());
    }
	*/
}
