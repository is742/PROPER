dtmc

const double n0=1;
param double n1 = "./op_logs/knapsack/n1.csv";
param double n2 = "./op_logs/knapsack/n2.csv";
param double n3 = "./op_logs/knapsack/n3.csv";
param double n4 = "./op_logs/knapsack/n4.csv";
param double n5 = "./op_logs/knapsack/n5.csv";

const double p1 = n11;
const double p2 = n21/(n0+n21);
const double p3 = n31/(n0+n31);
const double p4 = n41/(n31+n41);
const double p5 = n51/n41;

const int end_state=18;

module Knapsack
   s : [0..end_state] init 0;

   [] s=0   ->  p1:(s'=1) + (1-p1):(s'=2);
   [] s=1   ->  1:(s'=end_state);
   [] s=2   ->  1:(s'=3);
   [] s=3   ->  1:(s'=4);
   [] s=4   ->  p2:(s'=5) + (1-p2):(s'=7);
   [] s=5   ->  1:(s'=6);
   [] s=6   ->  1:(s'=4);
   [] s=7   ->  1:(s'=8);
   [] s=8   ->  p3:(s'=9) + (1-p3):(s'=17);
   [] s=9   ->  1:(s'=10);
   [] s=10  ->  p4:(s'=11) + (1-p4):(s'=16);
   [] s=11  ->  p5:(s'=12) + (1-p5):(s'=13);
   [] s=12  ->  1:(s'=15);
   [] s=13  ->  1:(s'=14);
   [] s=14  ->  1:(s'=15);
   [] s=15  ->  1:(s'=10);
   [] s=16  ->  1:(s'=8);
   [] s=17  ->  1:(s'=end_state);
   [] s=18  ->  true;
endmodule

rewards "time"
   s=13 : 2;
endrewards

rewards "energy"
   s=14 : 67;
endrewards
