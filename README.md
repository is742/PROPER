# PROPER

PROPER is a tool-supported method for the formal analysis of timing, resource use, cost and other quality aspects of computer programs. PROPER synthesises a Markov-chain model of the analysed code, computes this quantitative model's transition probabilities using information from program logs, and employs probabilistic model checking to evaluate the performance properties of interest. Importantly, the probabilistic model can be reused to accurately predict how the program performance would change if the code ran on a different hardware platform, used a new function library, or had a different usage profile.

### Update
PROPER now supports the calculation of confidence-intervals for the properties of interest using an extended version of the confidence-interval probabilistic model checker [FACT](https://www-users.cs.york.ac.uk/~cap/FACT/) that enables its use to analyse PROPER-generated pDTMCs.

## Instructions

PROPER is a Java-based tool that uses Maven for managing the project and its dependencies, and uses [JavaParser](https://javaparser.org/) 3.20.2 for parsing Java code. Probabilistic model checkers such as [PRISM](https://www.prismmodelchecker.org/) and [Storm](https://www.stormchecker.org/) can then be used, to receieve as input the generated probabilistic model, and verify the performance properties of interest.

To use PROPER:
1. Import the project in your IDE of preference
2. Add the name and path of the .java file you want to parse in "PROPER.java"
3. Annotate the code with information related to performance properties of interest, e.g., *//@time=1.5*
4. Run "PROPER.java" as Java Application
3. The generated model will be inside the [/models/](../models/) folder
4. Use any prefered transition probability calculation method to obtain the probabilities and add them to the generated model
5. Perform probabilistic model checking to evaluate the identified properties using model checkers such as the ones mentioned above

Examples of models, property files and annotated code are provided in the project files. Additionally, we provide examples of how to extract the transition probabilities by simulating the execution of the selected Java code, using program [logs](../logs/).

To use the extended version of FACT:
1. Information on installing FACT and its dependencies can be found [here](https://www-users.cs.york.ac.uk/~cap/FACT/gettingstarted.html).
2. The extended source code along with the case studies used in the evaluation are located at [/FACT-extended_version/](../FACT-extended_version/).  
