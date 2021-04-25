dtmc

const double p1=0.31464;
const double p2=0.10098342476946422;
const double p3=0.07590684086667207;
const double p4=0.571978410235593;
const double p5=0.571978410235593;

const int end_state=30;

module FastSineTransformer
   s : [0..end_state] init 0;

   [] s=0   ->  1:(s'=1);                    //line:15
   [] s=1   ->  p1:(s'=2) + (1-p1):(s'=3);   //line:17
   [] s=2   ->  1:(s'=end_state);            //line:18
   [] s=3   ->  p2:(s'=4) + (1-p2):(s'=5);   //line:23
   [] s=4   ->  1:(s'=end_state);            //line:24
   [] s=5   ->  1:(s'=6);                    //line:29
   [] s=6   ->  p3:(s'=7) + (1-p3):(s'=9);   //line:30
   [] s=7   ->  1:(s'=8);                    //line:31
   [] s=8   ->  1:(s'=end_state);            //line:32
   [] s=9   ->  1:(s'=10);                   //line:35
   [] s=10  ->  1:(s'=11);                   //line:36
   [] s=11  ->  1:(s'=12);                   //line:37
   [] s=12  ->  1:(s'=13);                   //line:38
   [] s=13  ->  p4:(s'=14) + (1-p4):(s'=19); //line:39
   [] s=14  ->  1:(s'=15);                   //line:40
   [] s=15  ->  1:(s'=16);                   //line:41
   [] s=16  ->  1:(s'=17);                   //line:42
   [] s=17  ->  1:(s'=18);                   //line:43
   [] s=18  ->  1:(s'=13);                   //line:44
   [] s=19  ->  1:(s'=20);                   //line:46
   [] s=20  ->  1:(s'=21);                   //line:47
   [] s=21  ->  1:(s'=22);                   //line:48
   [] s=22  ->  1:(s'=23);                   //line:50
   [] s=23  ->  1:(s'=24);                   //line:51
   [] s=24  ->  1:(s'=25);                   //line:52
   [] s=25  ->  p5:(s'=26) + (1-p5):(s'=29); //line:53
   [] s=26  ->  1:(s'=27);                   //line:54
   [] s=27  ->  1:(s'=28);                   //line:55
   [] s=28  ->  1:(s'=25);                   //line:56
   [] s=29  ->  1:(s'=end_state);            //line:59
   [] s=30  ->  true;
endmodule

rewards "time"
   s=14 : 2.6;
endrewards

rewards "cost"
   s=2 : 5;
   s=4 : 5;
endrewards
