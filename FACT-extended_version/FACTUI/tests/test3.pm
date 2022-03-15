// one service invoked
// 0.5 probability of retry if invocation fails

probabilistic

// numbers of successful and failed invocations
param double x = 20000 100;

module workflow
  s : [0..3] init 0; 
  [] s=0 -> x1:(s'=1) + (1-x1):(s'=2);
  [] s=1 -> true; // success
  [] s=2 -> 0.5:(s'=0) + 0.5:(s'=3); // retry
  [] s=3 -> true;
endmodule
