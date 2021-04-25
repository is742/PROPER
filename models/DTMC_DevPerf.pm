dtmc

const double p1=1;
const double p2=0.62519;
const double p3=0.8065153010858835;
const double p4=0.5081356867071153;

const int end_state = 17;

module DevPerf
   s : [0..end_state] init 0;

   [] s=0   ->  p1:(s'=1) + (1-p1):(s'=16);  //line:12
   [] s=1   ->  p2:(s'=2) + (1-p2):(s'=5);   //line:15
   [] s=2   ->  1:(s'=3);                    //line:16
   [] s=3   ->  1:(s'=4);                    //line:17
   [] s=4   ->  1:(s'=16);                   //line:18
   [] s=5   ->  p3:(s'=6) + (1-p3):(s'=9);   //line:20
   [] s=6   ->  1:(s'=7);                    //line:21
   [] s=7   ->  1:(s'=8);                    //line:22
   [] s=8   ->  1:(s'=16);                   //line:23
   [] s=9   ->  p4:(s'=10) + (1-p4):(s'=13); //line:25
   [] s=10  ->  1:(s'=11);                   //line:26
   [] s=11  ->  1:(s'=12);                   //line:27
   [] s=12  ->  1:(s'=16);                   //line:28
   [] s=13  ->  1:(s'=14);                   //line:30
   [] s=14  ->  1:(s'=15);                   //line:31
   [] s=15  ->  1:(s'=16);                   //line:32
   [] s=16  ->  1:(s'=end_state);            //line:38
   [] s=17  ->  true;
endmodule

rewards "energy"
   s=4  : 28;
   s=8  : 34;
   s=12 : 40;
   s=15 : 48;
endrewards