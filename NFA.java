package regExpToNfa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

public class NFA {

	public static String findFinalState(String nfa){

		String final_state ="";
		String str="";
		//extract the index of the final state
		for(int i=nfa.length()-1; i>= 0; i--){
			char ch = nfa.charAt(i);
			if(ch!='a' && ch!='b' && ch!='c' && ch!='d' && ch!='E' && ch!=')' && ch !='q'){
				str += ch;
			}
			else
				break;
		}
		// reverse the order of character to get the proper string
		for(int i=str.length()-1; i>=0; i--)
			final_state += str.charAt(i);

		return final_state;
	}

	public static String findStartState(String nfa){

		String start_state ="";
		// extract the index of the start state
		for(int i=0; i< nfa.length(); i++){
			char ch = nfa.charAt(i);
			if(ch =='q')
				;
			else if(ch!='a' && ch!='b' && ch!='c' && ch!='d' && ch!='E' && ch!='(')
				start_state += ch;
			else
				break;
		}
		return start_state;
	}

	public static String concatinate(String nfa1, String nfa2){

		String nfa = nfa1;
		nfa += " "+findFinalState(nfa1);
		nfa += "E";	
		nfa += ""+findStartState(nfa2); 

		nfa += " "+nfa2;

		return nfa;
	}

	public static String union(String nfa1, String nfa2){
		// add new start state
		int newStartState= Integer.parseInt(findFinalState(nfa2));
		newStartState +=1;

		String nfa = newStartState + "E";
		nfa += findStartState(nfa1);
		nfa +=" "+newStartState +"E";
		nfa += findStartState(nfa2);

		nfa += " "+ nfa1;
		nfa +=" "+ nfa2;

		// add new final state
		int newfinalState = newStartState +1;

		nfa +=" " + findFinalState(nfa1);
		nfa +="E";
		nfa +=""+newfinalState;

		nfa +=" " + findFinalState(nfa2);
		nfa +="E";
		nfa +=""+newfinalState;

		return nfa;
	}

	public static String star(String nfa1){
		int newStartState = Integer.parseInt(findFinalState(nfa1));
		newStartState+=1;

		String nfa =""+ newStartState+"E";
		nfa += findStartState(nfa1);
		nfa += " "+nfa1;

		nfa += " "+findFinalState(nfa1);
		nfa += "E";
		nfa += newStartState;

		return nfa;
	}

	public static final  ArrayList<String> getFileInputStream(String filePath) throws Exception{
		ArrayList<String> regExp = new ArrayList<>();
		String line;
		try{
			FileReader fileToRead = new FileReader(filePath);
			BufferedReader bf = new BufferedReader(fileToRead);

			while((line = bf.readLine())!= null){
				regExp.add(line);
			}
			bf.close();
		}
		catch(FileNotFoundException e){
			throw new Exception("Unable to open the file -- " + e.getMessage());
		}
		System.out.println("File containt: ");
		System.out.println(regExp+"\n");
		return regExp;
	}

	public static String createNFA(int state, String transition){
		int nextstate = state+1;
		String nfa1 = ""+state;
		nfa1 += transition;
		nfa1 += nextstate;

		return nfa1;
	}

	private static String findSymbole(String nfa){
		char ch='\0';
		for(int i=0; i<nfa.length(); i++){
			ch = nfa.charAt(i);
			if(ch=='a' || ch == 'b' || ch=='c' || ch=='d' || ch=='E')
				return ""+ch;
		}
		return "";
	}
	public static void printNFA(String nfa1){
		String[] nfa = nfa1.split(" ");
		System.out.println("Sart state: q" + findStartState(nfa1));
		System.out.println("Final state: q" + findFinalState(nfa1));
		for(int i=0; i<nfa.length; i++){
			printAux(nfa[i]);
		}
		System.out.println();
	}

	// Extract state and print it out in the form (q1, a) -> q2
	private static void printAux(String trans){
		System.out.println("(q"+findStartState(trans)+", "+findSymbole(trans) + ")-> q" + findFinalState(trans));
	}

	static boolean isCorrectRegExp(String nfa){
		Stack<String> NFA = new Stack<>();
		NFA.push("incorrect");
		char ch = '\0';

		if(nfa.length() ==0 || nfa.charAt(0)=='&' || nfa.charAt(0)=='*' || nfa.charAt(0)=='+')
			return false;

		for(int i=0; i<nfa.length(); i++){
			ch = nfa.charAt(i);
			
			if(NFA.isEmpty())
				return false;
			if(ch !='a' && ch!='b' && ch!='c' && ch!='d' && ch!='E' && ch!='&' && ch!='*' && ch!='+')
				return false;
			if(ch =='a' || ch=='b' || ch=='c' || ch=='d' || ch=='E')
				NFA.push(""+ch);
			else if(ch=='+' || ch=='&' )
				NFA.pop();
			else if(ch=='*');
		}
		
		if(NFA.isEmpty())
			return false;
		else if(NFA.peek().contains("incorrect"))
			return false;
		else
			return true;
	}

	public static void main(String[] args) {

		try{
			ArrayList<String> regExpInputStream = getFileInputStream(args[0]);

			for(int i=0; i<regExpInputStream.size(); i++){
				int nbr_state =0;
				String regExp ="";
				char ch='\0';
				Stack<String> NFA = new Stack<>();
				regExp = regExpInputStream.get(i);

				if(isCorrectRegExp(regExp)){
					
					for(int j=0; j<regExp.length(); j++){

						ch = regExp.charAt(j);

						if(ch == '&'){
							String nfa2 = NFA.pop();
							String nfa1 = NFA.pop();
							String nfa = concatinate(nfa1, nfa2);
							NFA.push(nfa);
						}
						else if(ch == '+')
						{
							String nfa2 = NFA.pop();
							String nfa1 = NFA.pop();
							String nfa = union(nfa1, nfa2);
							NFA.push(nfa);
							nbr_state+=2;
						}
						else if(ch == '*'){
							String nfa1 = NFA.pop();
							String nfa = star(nfa1);
							NFA.push(nfa);
							nbr_state++;
						} 
						else{
							NFA.push(createNFA(nbr_state+1, ""+ch));
							nbr_state +=2;
						}
					}
					String result = NFA.pop();
					System.out.println(regExp);
					System.out.println("--------------");
					printNFA(result);


				}
				else{
					System.out.println((regExp) + ": is in incorrect form");
					System.out.println("------------------------------------");
				}
			}


		}
		catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}

	}

}
