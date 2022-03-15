dtmc

param double n1 = "./op_logs/minPathSum/n1.csv";
param double n2 = "./op_logs/minPathSum/n2.csv";
param double n3 = "./op_logs/minPathSum/n3.csv";
param double n4 = "./op_logs/minPathSum/n4.csv";
param double n5 = "./op_logs/minPathSum/n5.csv";

const double n0=1;

const double p1 = n11/n0;
const double p2 = n21/(n0+n21);
const double p3 = n31/(n0+n31);
const double p4 = n41/(n0+n41);
const double p5 = n51/(n51+n41);

const int end_state = 21;

module minPathSum
  s : [0..end_state] init 0;

  [] s=0  -> 1:(s'=1);
  [] s=1  -> p1:(s'=2)+(1-p1):(s'=3);
  [] s=2  -> 1:(s'=end_state);
  [] s=3  -> 1:(s'=4);
  [] s=4  -> 1:(s'=5);
  [] s=5  -> 1:(s'=6);
  [] s=6  -> p2:(s'=7)+(1-p2):(s'=9);
  [] s=7  -> 1:(s'=8);
  [] s=8  -> 1:(s'=6);
  [] s=9  -> 1:(s'=10);
  [] s=10 -> p3:(s'=11)+(1-p3):(s'=13);
  [] s=11 -> 1:(s'=12);
  [] s=12 -> 1:(s'=10);
  [] s=13 -> 1:(s'=14);
  [] s=14 -> p4:(s'=15)+(1-p4):(s'=20);
  [] s=15 -> 1:(s'=16);
  [] s=16 -> p5:(s'=17)+(1-p5):(s'=19);
  [] s=17 -> 1:(s'=18);
  [] s=18 -> 1:(s'=16);
  [] s=19 -> 1:(s'=14);
  [] s=20 -> 1:(s'=end_state);
  [] s=21 -> true;
endmodule

rewards "time"
  s=7  : 0.01;
  s=11 : 0.01;
  s=18 : 0.03;
endrewards

rewards "cost"
  s=18 : 0.25;
endrewards
