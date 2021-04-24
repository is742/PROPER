dtmc

const double p1=0.09008;
const double p2=0.8664325850943274;
const double p3=0.8461811536751568;
const double p4=0.8458232589083584;
const double p5=0.5999390410213141;

const int end_state=18;

module Knapsack
   s : [0..end_state] init 0;

   [] s=0   ->  p1:(s'=1) + (1-p1):(s'=2);   //line:5
   [] s=1   ->  1:(s'=end_state);            //line:6
   [] s=2   ->  1:(s'=3);                    //line:9
   [] s=3   ->  1:(s'=4);                    //line:11
   [] s=4   ->  p2:(s'=5) + (1-p2):(s'=7);   //line:12
   [] s=5   ->  1:(s'=6);                    //line:13
   [] s=6   ->  1:(s'=4);                    //line:14
   [] s=7   ->  1:(s'=8);                    //line:17
   [] s=8   ->  p3:(s'=9) + (1-p3):(s'=17);  //line:18
   [] s=9   ->  1:(s'=10);                   //line:19
   [] s=10  ->  p4:(s'=11) + (1-p4):(s'=16); //line:20
   [] s=11  ->  p5:(s'=12) + (1-p5):(s'=13); //line:21
   [] s=12  ->  1:(s'=15);                   //line:22
   [] s=13  ->  1:(s'=14);                   //line:24
   [] s=14  ->  1:(s'=15);                   //line:25
   [] s=15  ->  1:(s'=10);                   //line:27
   [] s=16  ->  1:(s'=8);                    //line:29
   [] s=17  ->  1:(s'=end_state);            //line:31
   [] s=18  ->  true;
endmodule

rewards "time"
   s=13 : 2.0;
endrewards

rewards "energy"
   s=14 : 67;
endrewards

