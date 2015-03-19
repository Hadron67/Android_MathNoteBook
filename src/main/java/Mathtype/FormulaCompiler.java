package Mathtype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;

public class FormulaCompiler implements Cloneable{
	private HashMap<String, String> operatorlist=new HashMap<String, String>();
	private HashMap<String,Complex> varibles=new HashMap<String, Complex>();
	private Object[] poland;
	public FormulaCompiler(){
		this(new HashMap<String,Complex>());
	}
	public FormulaCompiler(HashMap<String,Complex> varibles) {
		this.varibles=varibles;
		this.operatorlist.put("+", "1");//
		this.operatorlist.put("-", "1");//
		this.operatorlist.put("*", "2");//
		this.operatorlist.put("/", "2");//
		this.operatorlist.put("^", "3");//
		this.operatorlist.put("!","4");
		this.operatorlist.put("sqrt","4");//
		this.operatorlist.put("sin", "4");//
		this.operatorlist.put("cos", "4");//
		this.operatorlist.put("tan", "4");//
		this.operatorlist.put("cot", "4");//
		this.operatorlist.put("sec", "4");//
		this.operatorlist.put("csc", "4");//
		this.operatorlist.put("sinh", "4");//
		this.operatorlist.put("cosh", "4");//
		this.operatorlist.put("tanh", "4");//
		this.operatorlist.put("arsinh", "4");//
		this.operatorlist.put("arcosh", "4");//
		this.operatorlist.put("artanh", "4");//
		this.operatorlist.put("arcsin", "4");//
		this.operatorlist.put("arccos", "4");//
		this.operatorlist.put("arctan2", "4");
		this.operatorlist.put("arctan", "4");//
		this.operatorlist.put("exp", "4");//
		this.operatorlist.put("ln", "4");//
		this.operatorlist.put("abs", "4");//
		this.operatorlist.put("re", "4");//
		this.operatorlist.put("im", "4");//
		this.operatorlist.put("the","4");//
	}
	public void compile(String formula) throws Thouway_away{
		Queue<String> input=init(formula);
		Queue<String> output=new LinkedList<String>();
		Stack<String> operators=new Stack<String>();
		while(!input.isEmpty()){
			String currents=input.poll();
			if(currents.equals("(")){
				operators.push(currents);
			}
			else if(currents.equals(")")){
				while(!operators.peek().equals("(")){
					output.offer(operators.pop());
				}
				operators.pop();
			}
			else if(isOperator(currents)){
				if(!operators.isEmpty()) if(priority(operators.peek())==4) output.offer(operators.pop());
				if(operators.isEmpty()){
					operators.push(currents);
				}
				else{
					if(priority(currents)>priority(operators.peek())){
						operators.push(currents);
					}
					else{
						output.offer(operators.pop());
						operators.push(currents);
					}
				}
			}
			else{
				output.offer(currents);
			}
		}
		if(!operators.isEmpty()){
			while(!operators.isEmpty()){
				output.offer(operators.pop());
			}
		}
		this.poland=output.toArray();
	}
	public Queue<String> init(String input){
		//Object[] operators=this.operatorlist.keySet().toArray();
		Queue<String> output=new LinkedList<String>();
		input=input.replaceAll("[ ]+", "%");
		input=input.replace("+", "%+%");
		input=input.replace("-", "%-%");
		input=input.replace("*", "%*%");
		input=input.replace("/", "%/%");
		input=input.replace("^", "%^%");
		input=input.replace("(", "%(%");
		input=input.replace(")", "%)%");
		input=input.replace(",", "),(");
		input=input.replaceAll("[%]+","%");
		String[] temp=input.split("%");
		if(temp.length>1) if(temp[1].equals("-")) output.offer("0");
		for(int i=0;i<temp.length;i++){
			if(i>1) if(temp[i-1].equals("(")&&temp[i].equals("-")) output.offer("0");
			if(!temp[i].equals("")) output.offer(temp[i]);
		}
		return output;
	}
	private boolean isOperator(String s){
		return this.operatorlist.containsKey(s);
	}
	private int priority(String s){
		String k=this.operatorlist.get(s);
		return (k==null)?0:Integer.parseInt(k);
	}
	private Complex eval(String operator,Stack<Complex> result){
		if(operator.equals("+")){
			Complex p1=result.pop();
			Complex p2=result.pop();
			return Complex.cplus(p1, p2);
		}
		if(operator.equals("-")){
			Complex p1=result.pop();
			Complex p2=result.pop();
			return Complex.cminus(p2, p1);
		}
		if(operator.equals("*")){
			Complex p1=result.pop();
			Complex p2=result.pop();
			return Complex.cmultiply(p2, p1);
		}
		if(operator.equals("/")){
			Complex p1=result.pop();
			Complex p2=result.pop();
			return Complex.cdivide(p2, p1);
		}
		if(operator.equals("^")){
			Complex p1=result.pop();
			Complex p2=result.pop();
			return Complex.cpow(p2, p1);
		}
		if(operator.equals("the")){
			Complex p1=result.pop();
			return Complex.ctheta(p1);
		}
		if(operator.equals("sin")){
			Complex p1=result.pop();
			return Complex.csin(p1);
		}
		if(operator.equals("cos")){
			Complex p1=result.pop();
			return Complex.ccos(p1);
		}
		if(operator.equals("tan")){
			Complex p1=result.pop();
			return Complex.ctan(p1);
		}
		if(operator.equals("cot")){
			Complex p1=result.pop();
			return Complex.ccot(p1);
		}
		if(operator.equals("sec")){
			Complex p1=result.pop();
			return Complex.csec(p1);
		}
		if(operator.equals("csc")){
			Complex p1=result.pop();
			return Complex.ccsc(p1);
		}
		if(operator.equals("arcsin")){
			Complex p1=result.pop();
			return Complex.carcsin(p1);
		}
		if(operator.equals("arccos")){
			Complex p1=result.pop();
			return Complex.carccos(p1);
		}
		if(operator.equals("arctan")){
			Complex p1=result.pop();
			return Complex.carctan(p1);
		}
		if(operator.equals("sinh")){
			Complex p1=result.pop();
			return Complex.csinh(p1);
		}
		if(operator.equals("cosh")){
			Complex p1=result.pop();
			return Complex.ccosh(p1);
		}
		if(operator.equals("tanh")){
			Complex p1=result.pop();
			return Complex.ctanh(p1);
		}
		if(operator.equals("arsinh")){
			Complex p1=result.pop();
			return Complex.carsinh(p1);
		}
		if(operator.equals("arcosh")){
			Complex p1=result.pop();
			return Complex.carcosh(p1);
		}
		if(operator.equals("artanh")){
			Complex p1=result.pop();
			return Complex.cartanh(p1);
		}
		if(operator.equals("sqrt")){
			Complex p1=result.pop();
			return Complex.csqrt(p1);
		}
		if(operator.equals("exp")){
			Complex p1=result.pop();
			return Complex.cexp(p1);
		}
		if(operator.equals("ln")){
			Complex p1=result.pop();
			return Complex.clog(p1);
		}
		if(operator.equals("abs")){
			Complex p1=result.pop();
			return Complex.cabs(p1);
		}
		if(operator.equals("re")){
			Complex p1=result.pop();
			return new Complex(p1.Re(),0);
		}
		if(operator.equals("im")){
			Complex p1=result.pop();
			return new Complex(p1.Im(),0);
		}
		return Complex.NaN;
	}
	private Complex getValue(String s) throws Thouway_away{
		if(isnumber(s)) return new Complex(Double.parseDouble(s),0);
		else if(s.equals("i")) return new Complex(0,1);
		else if(this.varibles.containsKey(s)) return this.varibles.get(s);
		else {
			throw new Thouway_away();
		}
	}
	public Complex calculate(){
		Object[] input=this.poland;
		Stack<Complex> result=new Stack<Complex>();
		try {
//			while (!input.isEmpty()) {
//				if (isOperator(input.peek())) {
//					result.push(eval(input.poll(), result));
//				} 
//				else {
//					result.push(getValue(input.poll()));
//				}
//			}
			for(int i=0;i<input.length;i++){
				if(isOperator(input[i].toString())){
					result.push(eval(input[i].toString(),result));
				}
				else{
					result.push(getValue(input[i].toString()));
				}
			}
			return result.pop();
		} catch (Exception e) {
			e.toString();
			return Complex.NaN;
		}
	}
	private boolean isnumber(String s){
		return Pattern.compile("[0-9\\.]*").matcher(s).matches();
	}
	public void setVarible(String name,Complex value){
		if(this.varibles.containsKey(name)) this.varibles.remove(name);
		this.varibles.put(name, value);
	}

    @Override
    public Object clone(){
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e){
            return null;
        }
    }
}
