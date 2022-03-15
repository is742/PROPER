// one service invoked

probabilistic

// numbers of successful and failed invocations
param double x = 20000 1000;

module workflow
  s : [0..2] init 0; 
  [] s=0 -> x1:(s'=1) + (1-x1):(s'=2);
  [] s=1 -> true; // success
  [] s=2 -> true; // failure
endmodule
