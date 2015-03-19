package Mathtype;

public class Complex implements Cloneable{
	public static final Complex ZERO=new Complex(0,0);
	public static final Complex NaN=new Complex(Double.NaN, Double.NaN);
	public static final Complex Infinity=new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	private double real;
	private double imagin;
	/**
	 * create a complex number whose real part is d and imagin part is e
	 * */
	public Complex(double d,double e){
		this.real=d;
		this.imagin=e;
	}
	public Complex(){
		this(0,0);
	}
	public double Re(){
		return this.real;
	}
	public double Im(){
		return this.imagin;
	}
	public void setRe(float x){
		this.real=x;
	}
	public void setIm(float y){
		this.imagin=y;
	}
	public String toString(){
		if(this.real!=0&&this.imagin>0) return Double.toString(this.real)+"+"+Double.toString(this.imagin)+"*i";
		if(this.real!=0&&this.imagin<0) return Double.toString(this.real)+Double.toString(this.imagin)+"*i";
		if(this.real!=0&&this.imagin==0) return Double.toString(this.real);
		if(this.real==0&&this.imagin!=0) return Double.toString(this.imagin)+"*i";
		if(this.equals(ZERO)) return "0.0";
		return "NaN";
	}
	public boolean equals(Complex a){
		return (this.real==a.real)&&(this.imagin==a.imagin);
	}
	public static Complex cplus(Complex a,Complex b){
		return new Complex(a.Re()+b.Re(),a.imagin+b.imagin);
	}
	public static Complex cminus(Complex a,Complex b){
		return new Complex(a.Re()-b.Re(),a.imagin-b.imagin);
	}
	public static Complex cmultiply(Complex a,Complex b){
		return new Complex(a.real*b.real-a.imagin*b.imagin,a.real*b.imagin+a.imagin*b.real);
	}
	public static Complex cdivide(Complex a,Complex b){
		double r=b.real*b.real+b.imagin*b.imagin;
		return new Complex((a.real*b.real+a.imagin*b.imagin)/r,(a.imagin*b.real-a.real*b.imagin)/r);
	}
	public static Complex cpow(Complex a,Complex b){
		if(a.equals(ZERO)) return ZERO;
		else if(a.imagin==0&&b.imagin==0&&b.real%1==0){
			return new Complex((float) Math.pow(a.real, b.real),0);
		}
		else if(a.imagin==0&&b.real%1==0){
			double r=Math.pow(a.real, b.real);
			return new Complex(r*Math.cos(Math.log(a.real)*b.imagin), r*Math.sin(Math.log(a.real)*b.imagin));
		}
		else if(a.real==0&&b.real%1==0&&a.imagin>=0){
			double r=Math.exp(-Math.PI*b.imagin/2)*Math.pow(a.imagin, b.real);
			double theta=Math.PI*b.real/2+Math.log(a.imagin)*b.imagin;
			return new Complex(r*Math.cos(theta),r*Math.sin(theta));
		}
		else{
			double r=Math.sqrt(a.real*a.real+a.imagin*a.imagin);
			double theta=Math.acos(a.real/r);
			double r2=Math.pow(r, b.real)*Math.exp(-b.imagin*theta);
			double theta2=b.imagin*Math.log(r)+theta*b.real;
			return new Complex(r2*Math.cos(theta2),r2*Math.sin(theta2));
		}
	}
	public static Complex cexp(Complex a){
		double r=Math.exp(a.real);
		return new Complex(r*Math.cos(a.imagin),r*Math.sin(a.imagin));
	}
	public static Complex csqrt(Complex a){
		if(a.imagin==0) return (a.real>=0)?(new Complex(Math.sqrt(a.real),0)):(new Complex(0,Math.sqrt(-a.real)));
		else {
			double x=Math.sqrt((a.real+Math.sqrt(a.real*a.real+a.imagin*a.imagin))/2);
			double y=a.imagin/(2*x);
			return new Complex(x,y);
		}
	}
	public static Complex csin(Complex a){
		return new Complex(Math.sin(a.real)*Math.cosh(a.imagin),Math.cos(a.real)*Math.sinh(a.imagin));
	}
	public static Complex ccos(Complex a){
		return new Complex(Math.cos(a.real)*Math.cosh(a.imagin),-Math.sin(a.real)*Math.sinh(a.imagin));
	}
	public static Complex ctan(Complex a){
		return cdivide(csin(a), ccos(a));
	}
	public static Complex ccot(Complex a){
		return cdivide(new Complex(1,0), ctan(a));
	}
	public static Complex csec(Complex a){
		return cdivide(new Complex(1, 0), ccos(a));
	}
	public static Complex ccsc(Complex a){
		return cdivide(new Complex(1, 0), csin(a));
	}
	public static Complex clog(Complex a){
		if(a.equals(ZERO)) return Infinity;
		else if(a.imagin==0&&a.real>0) return new Complex(Math.log(a.real), 0);
		else{
			double r=Math.sqrt(a.real*a.real+a.imagin*a.imagin);
			return new Complex(Math.log(r), Math.atan2(a.imagin, a.real));
		}
	}
	public static Complex carcsin(Complex a){
		if(a.imagin==0&&a.real>=-1&&a.real<=1) return new Complex(Math.asin(a.real), 0);
		else{
			Complex b=Complex.cmultiply(a, new Complex(0, 1));
			return Complex.cmultiply(new Complex(0, -1), Complex.carsinh(b));
		}
	}
	public static Complex carccos(Complex a){
		if(a.imagin==0&&a.real>=-1&&a.real<=1) return new Complex(Math.acos(a.real), 0);
		else{
			return Complex.cmultiply(new Complex(0, -1), Complex.carcosh(a));
		}
	}
	public static Complex carctan(Complex a){
		if(a.imagin==0&&a.real>=-1&&a.real<=1) return new Complex(Math.atan(a.real), 0);
		else{
			Complex b=Complex.cmultiply(a, new Complex(0, 1));
			return Complex.cmultiply(new Complex(0, -1), Complex.cartanh(b));
		}
	}
	public static Complex ccosh(Complex a){
		return new Complex((Math.exp(a.real)+Math.exp(-a.real))*Math.cos(a.imagin)/2,(Math.exp(a.real)-Math.exp(-a.real))*Math.sin(a.imagin)/2);
	}
	public static Complex csinh(Complex a){
		return new Complex((Math.exp(a.real)-Math.exp(-a.real))*Math.cos(a.imagin)/2,(Math.exp(a.real)+Math.exp(-a.real))*Math.sin(a.imagin)/2);
	}
	public static Complex ctanh(Complex a){
		return cdivide(csinh(a), ccosh(a));
	}
	public static Complex carsinh(Complex a){
		if(a.imagin==0) return new Complex(Math.log(a.real+Math.sqrt(a.real*a.real+1)), 0);
		else{
			Complex i=new Complex(1, 0);
			return clog(cplus(a, csqrt(cplus(cmultiply(a, a), i))));
		}
	}
	public static Complex carcosh(Complex a){
		if(a.imagin==0&&a.real>=1) return new Complex(Math.log(a.real+Math.sqrt(a.real*a.real-1)), 0);
		else{
			Complex i=new Complex(1, 0);
			return clog(cplus(a, csqrt(cminus(cmultiply(a, a), i))));
		}
	}
	public static Complex cartanh(Complex a){
		if(a.imagin==0&&a.real>=-1&&a.real<=1) return new Complex(Math.log((1+a.real)/(1-a.real))/2, 0);
		else{
			Complex i=new Complex(1, 0);
			return cdivide(clog(cdivide(cplus(a, i), cminus(i,a))), new Complex(2,0));
		}
	}
	public static Complex cabs(Complex a){
		if(a.imagin==0) return new Complex(Math.abs(a.real),0);
		else{
			return new Complex(Math.sqrt(a.real*a.real+a.imagin*a.imagin),0);
		}
	}
	public static Complex ctheta(Complex a){
		return new Complex(a.real>=0?1:0,0);
	}
}
