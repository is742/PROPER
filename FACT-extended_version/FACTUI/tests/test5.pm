probabilistic

param double x = 100 50 50;

module Module1
  s : [0..3] init 0;
  [] s=0 -> x1:(s'=1) + x2:(s'=2) + (1-x1-x2):(s'=3);
  [] s>0 -> true;
endmodule
