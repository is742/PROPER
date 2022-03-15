probabilistic

param double y = 40530 59460 10;
param double x = 57900 50 43050;
param double w = 99980 20;
param double z = 25065 125 74810;
param double k = 99960 40;


module M1

q : [0..9] init 0;

[] q=0 -> y1:(q'=1) + y2:(q'=3) + (1-y1-y2):(q'=7);
[] q=1 -> 0.2:(q'=1) + 0.55:(q'=2) + 0.25:(q'=8);
[] q=2 -> 0.7:(q'=5) + 0.3:(q'=8);
[] q=3 -> x1:(q'=8) + x2:(q'=9) + (1-x1-x2):(q'=4);
[] q=4 -> w1:(q'=8) + (1-w1):(q'=9);
[] q=5 -> z1:(q'=6) + z2:(q'=9) + (1-z1-z2):(q'=8);
[] q=6 -> k1:(q'=8) + (1-k1):(q'=9);
[] q=7 -> 1:(q'=7);
[] q=8 -> 1:(q'=8);
[] q=9 -> 1:(q'=9);

endmodule


rewards "cost"
	q=1 : 1;
        q=2 : 2;
        q=3 : 1;
        q=4 : 1;
        q=5 : 1;
        q=6 : 4;
endrewards

rewards "time"
	q=4 : 4;
        q=6 : 7;
endrewards
