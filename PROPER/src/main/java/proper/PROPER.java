//==============================================================================
//	
//	Copyright (c) 2021-
//	Authors:
//	* Ioannis Stefanakos (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PROPER.
//	
//==============================================================================
package proper;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Main PROPER class
 * @author istefanakos
 *
 */
public class PROPER {

	/** state and condition counters */
	private static int stateCtr=0, condCtr=1;

	/** dictionary that maps a reward name to a list of Property (stateNo, rewardValue) pairs,
	 *  e.g "cost" -> ( (1,5), (3,2), (12,1); "time" -> ((7, 9)) )*/
	private static Multimap<String, Property> rewards = ArrayListMultimap.create();

	/** name of the parsed method */
	private static String methodName = "";

	/** line number for each of the commands*/
	private static int line;

	/**
	 * Main 
	 * @param args
	 * @throws IOException 
	 */	
	public static void main(String[] args) throws IOException {
		// locating the root
		SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(PROPER.class).resolve("src/examples/seaa2021"));
		// name of Java class
		String nameOfJavaClass="Distance";
		// creating the abstract syntax tree
		CompilationUnit cu = sourceRoot.parse("", nameOfJavaClass+".java");
		
		FileWriter fw = new FileWriter("./models/"+nameOfJavaClass+".pm");
		fw.write(buildModel(cu));
		fw.close();
	}

	/** 
	 * Returns the name of the parsed method
	 * @return 
	 */
	private static String getMethodName(CompilationUnit cu) {
		cu.accept(new VoidVisitorAdapter<Object>() {
			@Override
			public void visit(MethodDeclaration n, Object arg) {
				super.visit(n, arg);
				if (methodName=="") {
					methodName = n.getName().clone().toString();
				}
			}
		}, null);
		return methodName;
	}

	/** 
	 * Returns the constructed model
	 * @return 
	 */
	private static String buildModel(CompilationUnit cu) {
		//ast parse method

		// model commands
		State start = new State(null);
		State end = new State(null);
		String model_commands = synthesis(cu.findFirst(BlockStmt.class).get(), start, end);
		// model preamble
		String model_preamble = "dtmc\n\n" + addVariables(condCtr) + "\nconst int end_state="+stateCtr+";\n\n";
		model_preamble += "module " + getMethodName(cu) + "\n   s : [0..end_state] init 0;\n\n";
		String model_ending = "   [] s=" + stateCtr + " -> true;\nendmodule\n\n" + addRewardStructures();
		return model_preamble + model_commands + model_ending;
	}

	/** 
	 * Returns the model's reward structures
	 * @return
	 */
	private static String addRewardStructures() {
		String addRewards = "";
		Set<String> keys = rewards.keySet();
	    for (String keyprint : keys) {
	        addRewards += "rewards \""+keyprint+"\"\n";
	        Collection<Property> properties = rewards.get(keyprint);
	        for(Property property : properties){
	            addRewards += "   s="+property.getState()+" : " + property.getValue()+";\n";
	        }
	        addRewards += "endrewards\n\n";
	    }
		return addRewards;
	}

	/** 
	 * Returns the model's variables
	 * @return
	 */
	private static String addVariables(int condCtr) {
		String variables = "";
		for (int i=1; i<condCtr; i++) {
			variables += "const double p" + i + ";\n"; 
		}
		return variables;
	}

	/** 
	 * Prints the line numbers from the parsed method
	 * */
	private static void printLineNumbers(Node node) {
		node.getRange().ifPresent(r -> {
			line = r.begin.line;
		});
	}

	static class State {
		final Statement statement;
		// the states that follow this state:
		final List<State> nextStates = new ArrayList<>();

		State(Statement statement) {
			this.statement = statement;
		}
	}

	/**
	 * Creates a state graph of the statements  in block. beforeState will get a next-state to the first statement in the block.
	 * afterState will be the next-state of the last statement(s) in the block.
	 * @return 
	 */
	private static String synthesis(BlockStmt block, State beforeState, State afterState) {
		// initialise model
		String model = "";
		Node printNode;

		// create list of states
		List<State> states = new ArrayList<>();

		// create the sequence in this block
		for (Statement statement : block.getStatements()) {
			states.add(new State(statement));
		}

		beforeState.nextStates.add(states.get(0));

		// attach to next states
		for (int i = 0; i < states.size(); i++) {
			State currentState = states.get(i);

			State nextState;
			if (i == states.size() - 1) {
				nextState = afterState;
			} else {
				nextState = states.get(i + 1);
			}

			// check if currentState is an assignment or a method
			if (currentState.statement.isExpressionStmt() || currentState.statement.isLocalClassDeclarationStmt()) {
				// adding the next state
				currentState.nextStates.add(nextState);
				// finding line number
				if (currentState.statement.isExpressionStmt()) {
					printNode = currentState.statement.asExpressionStmt().getExpression();
				}
				else {
					printNode = currentState.statement.asLocalClassDeclarationStmt().getClassDeclaration() ;
				}
				printLineNumbers(printNode);
				// adding PRISM command 
				model += "   [] s=" + (stateCtr++) + " -> 1:(s'=" + (stateCtr) + ");                 "
						+ "  \\\\line:" + line +"\n";
			}
			// check if currentState is a return statement or an exception
			else if (currentState.statement.isReturnStmt() || currentState.statement.isThrowStmt()) {
				// adding next state
				currentState.nextStates.add(nextState);
				// finding line number
				if (currentState.statement.isReturnStmt()) {
					printNode = currentState.statement.getParentNodeForChildren();
				}
				else {
					printNode = currentState.statement.asThrowStmt().getExpression();
				}
				printLineNumbers(printNode);
				// adding PRISM command 
				model += "   [] s=" + (stateCtr++) + " -> 1:(s'=end_state);           "
						+ "\\\\line:" + line +"\n";
			}
			// check if currentState is an if statement
			else if (currentState.statement.isIfStmt()) {
				// adding first part of PRISM command
				model += "   [] s=" + (stateCtr++) + " -> p" + condCtr + ":(s'=" + (stateCtr) + 
						") + (1-p" + (condCtr++) + "):(s'=";
				// searching inside the THEN branch
				String if_branch_model = synthesis(((IfStmt) currentState.statement).getThenStmt().asBlockStmt(), currentState, nextState);
				// finding line number
				printNode = currentState.statement.asIfStmt().getCondition();
				printLineNumbers(printNode);				
				// searching inside the ELSE branch if it exists
				if (((IfStmt) currentState.statement).getElseStmt().isPresent()) {
					model += (++stateCtr) +");  \\\\line:" + line + "\n" + if_branch_model + "   [] s=" + (stateCtr-1) + " -> 1:(s'="; 
					String else_branch_model = synthesis(((IfStmt) currentState.statement).getElseStmt().get().asBlockStmt(), currentState, nextState);
					model += (stateCtr) + ");\n" + else_branch_model;
				}
				else {
					// adding the second part of the PRISM command
					model += (stateCtr) + ");  \\\\line:" + line +"\n" + if_branch_model;
				}
			}
			// check if currentState is a while loop
			else if (currentState.statement.isWhileStmt()) {
				// storing the loop's starting state's number
				int loopStartingState = stateCtr;
				// adding the first part of the PRISM command
				model += "   [] s=" + (stateCtr++) + " -> p" + condCtr + ":(s'=" + (stateCtr) + 
						") + (1-p" + (condCtr++) + "):(s'=";
				// searching inside the loop's body
				String loop_body_model = synthesis(((WhileStmt) currentState.statement).getBody().asBlockStmt(), currentState, nextState);
				// finding line number 
				printNode = currentState.statement.asWhileStmt().getCondition();
				printLineNumbers(printNode);
				// adding the second part of the PRISM command
				model += (++stateCtr) + ");  \\\\line:" + line +"\n" + loop_body_model + "   [] s=" + (stateCtr-1) + " -> 1:(s'=" +loopStartingState + ");\n";
			}
			// adding rewards if exist
			if (currentState.statement.isExpressionStmt() || currentState.statement.isLocalClassDeclarationStmt() || 
					currentState.statement.isReturnStmt() || currentState.statement.isThrowStmt() || 
					currentState.statement.isIfStmt() || currentState.statement.isWhileStmt()) {
				if (currentState.statement.getComment().isPresent()) {
					String comment = currentState.statement.getComment().get().toString().replace("//", "");
					if (comment.contains("=") && comment.contains("@")) {
						comment = comment.replace("@", "");
						String[] values = comment.split(",");
						for (int j = 0; j < values.length; j++) { 
							String value = values[j];
							String[] parts = value.split("=");
							String part1 = parts[0];
							double part2 = Double.parseDouble(parts[1]);
							part1 = part1.trim();
							Property p = new Property((stateCtr-1), part2);
							rewards.put(part1,p);
						}
					}
				}
			}
		}
		return model;
	}
}

