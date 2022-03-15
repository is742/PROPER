// two services invoked one after the other

probabilistic

// numbers of successful and failed invocations
param double x = 20000 1000;
param double y = 30000 5000;

module workflow
  s : [0..3] init 0; 
  [] s=0 -> x1:(s'=1) + (1-x1):(s'=3);
  [] s=1 -> y1:(s'=2) + (1-y1):(s'=3);
  [] s=2 -> true;
  [] s=3 -> true;
endmodule
