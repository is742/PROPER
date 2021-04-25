dtmc

const double p1=0.66602;
const double p2=0.7498464534491799;

const int end_state=8;

module distance1
   s : [0..end_state] init 0;

   [] s=0   ->  p1:(s'=1)+(1-p1):(s'=2); //line: 17
   [] s=1   ->  1:(s'=end_state);        //line: 18
   [] s=2   ->  1:(s'=3);                //line: 21
   [] s=3   ->  1:(s'=4);                //line: 22
   [] s=4   ->  p2:(s'=5)+(1-p2):(s'=7); //line: 23
   [] s=5   ->  1:(s'=6);                //line: 24
   [] s=6   ->  1:(s'=4);                //line: 25
   [] s=7   ->  1:(s'=end_state);        //line: 27
   [] s=8   ->  true;
endmodule

rewards "time"
   s=5 : 3.2;
endrewards

rewards "cost"
   s=1 : 7;
endrewards
