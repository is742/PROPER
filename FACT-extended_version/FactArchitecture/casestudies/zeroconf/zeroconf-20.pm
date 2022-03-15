// Model taken from Daws04
// This version by Ernst Moritz Hahn (emh@cs.uni-sb.de)

probabilistic

const double p1;
const double q1;
const int n = 20;

module main
  s: [-2..n+1] init 0;

  [b] (s=-1) -> (s'=-2);
  [a] (s=0) -> 1-q1 : (s'=-1) + q1 : (s'=1);
  [a] (s>0) & (s<n+1) -> 1-p1 : (s'=0) + p1 : (s'=s+1);

endmodule

rewards
 [a] true : 1;
 [b] true : n-1;
endrewards
