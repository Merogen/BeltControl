


// map of vibrators

// group 0
// 00 -> 2
// 01 -> 3
// 02 -> 4
// 03 -> 5
// 04 -> 6
// 05 -> 7
// 06 -> 22
// 07 -> 24

// group 1
// 10 -> 21
// 11 -> 20
// 12 -> 19
// 13 -> 18
// 14 -> 17
// 15 -> 16
// 16 -> 15
// 17 -> 14

// group 2
// 20 -> 37
// 21 -> 35
// 22 -> 33
// 23 -> 31
// 24 -> 29
// 25 -> 27
// 26 -> 25
// 27 -> 23

// group 3
// 30 -> 26
// 31 -> 28
// 32 -> 30
// 33 -> 32
// 34 -> 34
// 35 -> 36
// 36 -> 38
// 37 -> 40

// group 4
// 40 -> A15 (69)
// 41 -> A14 (68)
// 42 -> A13 (67)
// 43 -> A12 (66)
// 44 -> A11 (65)
// 45 -> A10 (64)
// 46 -> A9 (63)
// 47 -> A8 (62)

// group 5
// 50 -> A7 (61)
// 51 -> A6 (60)
// 52 -> A5 (59)
// 53 -> A4 (58)
// 54 -> A3 (57)
// 55 -> A2 (56)
// 56 -> A1 (55)
// 57 -> A0 (54)

int pins0[8];
int pins1[8];
int pins2[8];
int pins3[8];
int pins4[8];
int pins5[8];

int code=0;
int id_buzz=-1;
int id_buzz1=-1;
int id_buzz2=-1;
int pwm=0;
int tempo1=0;
int tempo2=0;
int trame=0;

int pwms[60];
int tempos1[60];
int tempos2[60];
int counters[60];
int activate[60];
int trames[60];

int counter_tempo=0;

int counter_pwm=0;

unsigned long time_current=0;
unsigned long time_previous=0;

void setup() {
  // group 0
  pins0[0]=2;
  pins0[1]=3;
  pins0[2]=4;
  pins0[3]=5;
  pins0[4]=6;
  pins0[5]=7;
  pins0[6]=22;
  pins0[7]=24;
  pinMode(2,OUTPUT);
  pinMode(3,OUTPUT);
  pinMode(4,OUTPUT);
  pinMode(5,OUTPUT);
  pinMode(6,OUTPUT);
  pinMode(7,OUTPUT);
  pinMode(22,OUTPUT);
  pinMode(24,OUTPUT);

  // group 1
  pins1[0]=21;
  pins1[1]=20;
  pins1[2]=19;
  pins1[3]=18;
  pins1[4]=17;
  pins1[5]=16;
  pins1[6]=15;
  pins1[7]=14;
  pinMode(21,OUTPUT);
  pinMode(20,OUTPUT);
  pinMode(19,OUTPUT);
  pinMode(18,OUTPUT);
  pinMode(17,OUTPUT);
  pinMode(16,OUTPUT);
  pinMode(15,OUTPUT);
  pinMode(14,OUTPUT);

  // group 2
  pins2[0]=37;
  pins2[1]=35;
  pins2[2]=33;
  pins2[3]=31;
  pins2[4]=29;
  pins2[5]=27;
  pins2[6]=25;
  pins2[7]=23;
  pinMode(37,OUTPUT);
  pinMode(35,OUTPUT);
  pinMode(33,OUTPUT);
  pinMode(31,OUTPUT);
  pinMode(29,OUTPUT);
  pinMode(27,OUTPUT);
  pinMode(25,OUTPUT);
  pinMode(23,OUTPUT);

  // group 3
  pins3[0]=26;
  pins3[1]=28;
  pins3[2]=30;
  pins3[3]=32;
  pins3[4]=34;
  pins3[5]=36;
  pins3[6]=38;
  pins3[7]=40;
  pinMode(26,OUTPUT);
  pinMode(28,OUTPUT);
  pinMode(30,OUTPUT);
  pinMode(32,OUTPUT);
  pinMode(34,OUTPUT);
  pinMode(36,OUTPUT);
  pinMode(38,OUTPUT);
  pinMode(40,OUTPUT);

  // group 4
  pins4[0]=69;
  pins4[1]=68;
  pins4[2]=67;
  pins4[3]=66;
  pins4[4]=65;
  pins4[5]=64;
  pins4[6]=63;
  pins4[7]=62;
  pinMode(A15,OUTPUT);
  pinMode(A14,OUTPUT);
  pinMode(A13,OUTPUT);
  pinMode(A12,OUTPUT);
  pinMode(A11,OUTPUT);
  pinMode(A10,OUTPUT);
  pinMode(A9,OUTPUT);
  pinMode(A8,OUTPUT);

  // group 5
  pins5[0]=61;
  pins5[1]=60;
  pins5[2]=59;
  pins5[3]=58;
  pins5[4]=57;
  pins5[5]=56;
  pins5[6]=55;
  pins5[7]=54;
  pinMode(A7,OUTPUT);
  pinMode(A6,OUTPUT);
  pinMode(A5,OUTPUT);
  pinMode(A4,OUTPUT);
  pinMode(A3,OUTPUT);
  pinMode(A2,OUTPUT);
  pinMode(A1,OUTPUT);
  pinMode(A0,OUTPUT);


  for (int v=0;v<60;v++){
    pwms[v]=0;
    tempos1[v]=0;
    tempos2[v]=0;
    counters[v]=0;
    activate[v]=1;
    trames[v]=10;
  }
  Serial.begin(9600);
}

void loop() {

  char character='a';

  int donneesALire = Serial.available(); //lecture du nombre de caractères disponibles dans le buffer
  
  if(donneesALire > 0){ //si le buffer n'est pas vide
    
    //Serial.println(donneesALire);

    while (donneesALire>0){

      character = Serial.read();
      donneesALire = Serial.available();

      // if character is '+', start a new code
      if (character=='+'){
        code=1;
      }
      else if (character=='-'){
        code=7;
      }
      else{
        if (code==0){
          // just read the character
        }
        else if (code==1){
          id_buzz=(character-48)*10; // set id of the buzzer
          id_buzz1=(character-48);
          code=2;
        }
        else if (code==2){
          id_buzz+=(character-48); // set id of the buzzer
          id_buzz2=(character-48);
          code=3;
        }
        else if (code==3){
          pwm=character;    // set pwm
          code=4;
        }
        else if (code==4){
          tempo1=character;    // set temporization
          code=5;
        }
        else if (code==5){
          tempo2=character;    // set temporization
          code=6;
        }
        else if (code==6){
          trame=character;    // set temporization
          code=10;
        }
        
        
        else if (code==7){
          id_buzz=(character-48)*10; // set id of the buzzer
          id_buzz1=(character-48);
          code=8;
        }
        else if (code==8){
          id_buzz+=(character-48); // set id of the buzzer
          id_buzz2=(character-48);
          code=9;
        }
        else if (code==9){
          pwm=character;    // set pwm
          code=11;
        }
        

        if (code==10){ // set parameters to buzzer
          if (id_buzz>=0 && id_buzz1<6 && id_buzz2<8){
           setBuzzer(id_buzz,(pwm-48)*10,(tempo1-48)*10, (tempo2-48)*10, (trame-48));
           code=0;
          }
        }
        else if (code==11){ // set parameters to buzzer
          if (id_buzz>=0 && id_buzz1<6 && id_buzz2<8){
           setBuzzer(id_buzz,(pwm-48)*10,0, 0, 0);
           code=0;
          }
        }
      }
    }
  }

  // generate tempo
  /*for (int v=0;v<60;v++){
    if (tempos[v]==0) activate[v]=1;
    else{
      if (counters[v]>tempos[v]){
        counters[v]=0;
        if (activate[v]==0) activate[v]=1;
        else activate[v]=0;
      }
      counters[v]++;
    }
  }*/

  vibrations();
}

void vibrations(){

  // pwm pulse
  for (int v=0;v<8;v++){
      if (counter_pwm<pwms[ 0+v] && activate[ 0+v]==1 && trames[ 0+v]!=0) digitalWrite(pins0[v],HIGH);
      else digitalWrite(pins0[v],LOW);
      if (counter_pwm<pwms[10+v] && activate[10+v]==1 && trames[10+v]!=0) digitalWrite(pins1[v],HIGH);
      else digitalWrite(pins1[v],LOW);
      if (counter_pwm<pwms[20+v] && activate[20+v]==1 && trames[20+v]!=0) digitalWrite(pins2[v],HIGH);
      else digitalWrite(pins2[v],LOW);
      
      if (counter_pwm<pwms[30+v] && activate[30+v]==1 && trames[30+v]!=0) digitalWrite(pins3[v],HIGH);
      else digitalWrite(pins3[v],LOW);
      if (counter_pwm<pwms[40+v] && activate[40+v]==1 && trames[40+v]!=0) digitalWrite(pins4[v],HIGH);
      else digitalWrite(pins4[v],LOW);
      if (counter_pwm<pwms[50+v] && activate[50+v]==1 && trames[50+v]!=0) digitalWrite(pins5[v],HIGH);
      else digitalWrite(pins5[v],LOW);
   }
   counter_pwm++;
   if (counter_pwm>=15) counter_pwm=0;
   
   
   // trame pulsions
   for (int g=0;g<60;g+=10){
     for (int v=0;v<8;v++){
        if (tempos1[ g+v]==0 || tempos2[ g+v]==0 ){
          activate[ g+v]=1;
        }
        else{
          if (activate[ g+v]==1){
            if (counters[ g+v]>=tempos1[ g+v]){
              counters[ g+v]=0;
              activate[ g+v]=0;
              if (trames[g+v]>0) trames[g+v]--;
            }
            else{
              counters[ g+v]++;
            }
          }
          else{
            if (counters[ g+v]>=tempos2[ g+v]){
              counters[ g+v]=0;
              activate[ g+v]=1;
              
              //time_current=millis();
              //Serial.println(time_current-time_previous);
              //time_previous=time_current;
            }
            else{
              counters[ g+v]++;
            }
          }
        }
     }
   }
   delayMicroseconds(100);
  
  
  // groups 0, 1 and 2
  
  /*int counter_pwm=0;

  for (int v=0;v<8;v++){
      if (pwms[0+v]==0 || activate[0+v]==0) digitalWrite(pins0[v],LOW);
      else{
        digitalWrite(pins0[v],HIGH);
        Serial.print(v);
        Serial.print(" : ");
        Serial.print(pins0[v]);
        Serial.print(" : ");
        Serial.print(pwms[v]);
        if (activate[v]) Serial.println(" ; true");
        else Serial.println(" ; false");
      }

      if (pwms[10+v]==0 || activate[10+v]==0) digitalWrite(pins1[v],LOW);
      else digitalWrite(pins1[v],HIGH);

      if (pwms[20+v]==0 || activate[20+v]==0) digitalWrite(pins2[v],LOW);
      else digitalWrite(pins2[v],HIGH);
  }
  while (counter_pwm<100){
    for (int v=0;v<8;v++){
      if (counter_pwm>pwms[ 0+v]) digitalWrite(pins0[v],LOW);
      else delayMicroseconds(5);
      if (counter_pwm>pwms[10+v]) digitalWrite(pins1[v],LOW);
      else delayMicroseconds(5);
      if (counter_pwm>pwms[20+v]) digitalWrite(pins2[v],LOW);
      else delayMicroseconds(5);
    }
    //delayMicroseconds(100);
    counter_pwm++;
  }
  
  // groups 3, 4 and 5
  
  counter_pwm=0;

  for (int v=0;v<8;v++){
      if (pwms[30+v]==0 || activate[30+v]==0) digitalWrite(pins3[v],LOW);
      else{
        digitalWrite(pins3[v],HIGH);
        Serial.print(v);
        Serial.print(" : ");
        Serial.print(pins0[v]);
        Serial.print(" : ");
        Serial.print(pwms[v]);
        if (activate[v]) Serial.println(" ; true");
        else Serial.println(" ; false");
      }
      if (pwms[40+v]==0 || activate[40+v]==0) digitalWrite(pins4[v],LOW);
      else digitalWrite(pins4[v],HIGH);

      if (pwms[50+v]==0 || activate[50+v]==0) digitalWrite(pins5[v],LOW);
      else digitalWrite(pins5[v],HIGH);
  }
  while (counter_pwm<100){
    for (int v=0;v<8;v++){
      if (counter_pwm>pwms[30+v]) digitalWrite(pins3[v],LOW);
      else delayMicroseconds(5);
      if (counter_pwm>pwms[40+v]) digitalWrite(pins4[v],LOW);
      else delayMicroseconds(5);
      if (counter_pwm>pwms[50+v]) digitalWrite(pins5[v],LOW);
      else delayMicroseconds(5);
    }
    //delayMicroseconds(100);
    counter_pwm++;
  }*/
}

void setBuzzer(int id, int pwm, int tempo1, int tempo2, int trame){

  if (id>=0 && id<58){
    pwms[id]=pwm;
    tempos1[id]=tempo1*5; //5
    tempos2[id]=tempo2*5;
    
    if (trame==0) trames[id]=-1;
    else{
      counters[id]=0;
      trames[id]=trame;
    }
    //counters[id]=0;
    activate[id]=1;
  }
  
}



