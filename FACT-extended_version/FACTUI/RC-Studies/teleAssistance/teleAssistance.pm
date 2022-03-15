probabilistic

// probabilities of different outcomes 
const double p_analyzeData_sendAlarm = 0.004; 
const double p_analyzeData_changeDrug = 0.3; 
const double p_analyzeData_doNothing = 0.696; 
const double p_request_sendAlarm = 0.1;
const double p_request_analyzeData = 0.9;

// Probabilities of successful service invocations
param double pAlarm = 96800 3200;
param double pDrug = 95000 5000;
param double pAnalysis = 96500 3500;

const double N = 1000.0; // Expected number of workflow executions before STOP
const double pStop = 1.0/(N + 1);  

module TeleAssistance
a : [0..10] init 0;
  
[initial] (a=0) -> 1.0:(a'=2); //request?
[final] (a=1) -> true; //FINAL

[request] (a=2) -> p_request_sendAlarm:(a'=5) + (p_request_analyzeData):(a'=3);

[analyseVitalParams] (a=3) -> pAnalysis1:(a'=4) + (1-pAnalysis1):(a'=9);

[result] (a=4) -> p_analyzeData_sendAlarm:(a'=5) + 
                  p_analyzeData_changeDrug:(a'=6)+
                  p_analyzeData_doNothing:(a'=10);

[sendAlarm] (a=5) -> pAlarm1:(a'=10) + (1-pAlarm1):(a'=7);
[changeDrug] (a=6) -> pDrug1:(a'=10) + (1-pDrug1):(a'=8);

[failedSendAlarm] (a=7) -> 1.0:(a'=10);//failed send alarm

[failedChangeDrug] (a=8) -> 1.0:(a'=10);//failed changed drug
[failedAnalysis] (a=9) -> 1.0:(a'=10);//failed analysis

[stop] (a=10) -> pStop:(a'=1) + (1-pStop):(a'=0);//stop?
endmodule

// labels
label "stop" = a=10;
label "FailedSendAlarm" = a=7; 
label "WF_fail" = a=7|a=8|a=9; 
label "analyseVitalParams" = a=3;  

//rewards structure that associates costs to the various service invocations
rewards
(a=5) :  1;//%sendAlarm_COST; //cost of invoking alarm 
(a=3) :  1;//%analyzeData_COST; //cost of invoking analysis 
(a=6) :  1;//%changeDrug_COST;//cost of invoking drug 
endrewards
