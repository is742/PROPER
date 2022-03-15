probabilistic

param double x = 100 100;

module Module1
  s : [0..2] init 0;
  [] s=0 -> x1:(s'=1) + (1-x1):(s'=2);
  [] s>0 -> true;
endmodule
