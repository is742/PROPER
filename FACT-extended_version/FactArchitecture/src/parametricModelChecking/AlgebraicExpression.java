package parametricModelChecking;

public class AlgebraicExpression {
	public String poly;
	//------------------------------------------------------------------------------------------------------------
	private  boolean isLetter(char ch){
		return ((ch >= 'a')&&(ch <= 'z'))||((ch >='A')&&(ch <= 'Z'));			
	}
	//------------------------------------------------------------------------------------------------------------
	private  String translate(String input){
		StringBuilder inputBuild = new StringBuilder(input);

		for(int index=0;index<inputBuild.length();index++){

			char currentChar = inputBuild.charAt(index);


			if((currentChar == ' ')&&(index-1 >= 0)&&(index+1<input.length())){
				char prevChar = inputBuild.charAt(index-1);
				char nextChar = inputBuild.charAt(index+1);

				if(((prevChar >= '0')&&(prevChar <= '9')||(prevChar == ')'))&&isLetter(nextChar)){
					inputBuild.setCharAt(index,'*');
				}

			}
		}

		int divideCharIndex = inputBuild.lastIndexOf("|");
		if (divideCharIndex > -1){
			//inputBuild.setCharAt(divideCharIndex,(char) 92 );//change | to /	
			String left = "("+inputBuild.substring(0, divideCharIndex-1)+")";			
			String right = "("+inputBuild.substring(divideCharIndex+1, inputBuild.length())+")";
			return left+"/"+right;			
		}
		return inputBuild.toString();
	}
	//------------------------------------------------------------------------------------------------------------
	public String toString(){
		return poly;
	}
	//------------------------------------------------------------------------------------------------------------
	public AlgebraicExpression(String poly) {
		this.poly = this.translate(poly);	
	}
	//------------------------------------------------------------------------------------------------------------
	public String getPolynomial(){
		return this.poly;
	}
	//------------------------------------------------------------------------------------------------------------
}
