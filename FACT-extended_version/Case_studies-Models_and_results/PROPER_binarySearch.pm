dtmc

const double n0 = 1;
param double n1 = "./op_logs/binarysearch/n1.csv";
param double n2 = "./op_logs/binarysearch/n2.csv";
param double n3 = "./op_logs/binarysearch/n3.csv";

const double p1 = n11/n0;
const double p2 = n21/n0;
const double p3 = n31/n0;

const int end_state = 8;

module binarySearch
  s : [0..end_state] init 0;

  [] s=0 -> p1:(s'=1)+(1-p1):(s'=2);
  [] s=1 -> 1:(s'=end_state);
  [] s=2 -> 1:(s'=3);
  [] s=3 -> p2:(s'=4)+(1-p2):(s'=5);
  [] s=4 -> 1:(s'=end_state);
  [] s=5 -> p3:(s'=6)+(1-p3):(s'=7);
  [] s=6 -> 1:(s'=0);
  [] s=7 -> 1:(s'=0);
  [] s=8 -> true;
endmodule

rewards "time"
  s=3 : 0.0238;
  s=5 : 0.0238;
endrewards
