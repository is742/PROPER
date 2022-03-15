probabilistic

const double n0 = 1;
param double n1 = "./op_logs/distance/n1.csv";
param double n2 = "./op_logs/distance/n2.csv";

const double p1 = n11/n0;
const double p2 = n21/(n0+n21);
//const double p2 = n21/((1-n11)+n21);

const int end_state = 8;

module distance
   s : [0..end_state] init 0;

   [] s=0   ->  p1:(s'=1)+(1-p1):(s'=2);
   [] s=1   ->  1:(s'=end_state);
   [] s=2   ->  1:(s'=3);
   [] s=3   ->  1:(s'=4);
   [] s=4   ->  p2:(s'=5)+(1-p2):(s'=7);
   [] s=5   ->  1:(s'=6);
   [] s=6   ->  1:(s'=4);
   [] s=7   ->  1:(s'=end_state);
   [] s=8   ->  true;
endmodule

rewards "time"
   s=5 : 1.8; //2.5
endrewards

rewards "cost"
   s=1 : 7;
endrewards
