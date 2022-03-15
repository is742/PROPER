probabilistic

param double x = 8011 1989;
param double y = 5991 1989;
param double n = "./operationalProfiles/data.csv";

module distance
   s : [0..6] init 0;

   [] s=0   ->  x1:(s'=1)+(1-x1):(s'=2); 
   [] s=1   ->  1:(s'=6);                
   [] s=2   ->  1:(s'=3);                
   [] s=3   ->  y1:(s'=4)+(1-y1):(s'=5); 
   [] s=4   ->  1:(s'=3);                
   [] s=5   ->  1:(s'=6);                
   [] s=6   ->  true;
endmodule

rewards "time"
   s=4 : 1;
endrewards

rewards "cost"
   s=4 : 1;
endrewards
