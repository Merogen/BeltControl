// Sweep
// by BARRAGAN <http://barraganstudio.com> 
// This example code is in the public domain.


#include <Servo.h> 
 
Servo myservo[2];  // create servo object to control a servo 
                // a maximum of eight servo objects can be created 
               
int code=0;

int values[2];

int servo;

int refX=0;
int refY=0;
long stickX=0;
long stickY=0;

int fluid_friction=0;
long solid_friction=0;

int offsetX=0;
int offsetY=0;

int gx=0;
int gy=0;

int val1=0;
int val2=0;
int val3=0;

int counter=0;
 
void setup() 
{ 
  myservo[0].attach(9);  // attaches the servo on pin 9 to the servo object 
  myservo[1].attach(10);
  
  values[0]=90;
  values[1]=90;
  
  code=0;        // 0 -> waiting, 1 -> solid, 2 -> fluid
  
  // read reference joystick position
  delay(200);
  refX=analogRead(A0);
  refY=analogRead(A1);
  
  Serial.begin(9600);
} 
 
 
void loop(){ 
  
  // read parameters
  char character='a';
  int donneesALire = Serial.available();
  
  if(donneesALire > 0){ //si le buffer n'est pas vide
     while (donneesALire>0){

       character = Serial.read();
       donneesALire = Serial.available();
       
       if (character=='s'){
         code=1;
       }
       else if (character=='f'){
         code=11;
       }
       else if (character=='g'){
         code=21;
       }
       else if (character=='h'){
         code=31;
       }
       else if (character=='o'){
         code=41;
       }
       else if (character=='p'){
         code=51;
       }
       
       else{
         if (code==0){
          // just read the character
         }
         else if (code==1){
           val1=(character-48)*100; // set id of the buzzer
           code=2;
        }
        else if (code==2){
           val1+=(character-48)*10; // set id of the buzzer
           code=3;
        }
        else if (code==3){
           val1+=(character-48); // set id of the buzzer
           solid_friction=val1;
           code=0;
        }
        
        else if (code==11){
           val1=(character-48)*100; // set id of the buzzer
           code=12;
        }
        else if (code==12){
           val1+=(character-48)*10; // set id of the buzzer
           code=13;
        }
        else if (code==13){
           val1+=(character-48); // set id of the buzzer
           fluid_friction=val1;
           code=0;
        }
        
        else if (code==21){
           val1=(character-48)*100; // set id of the buzzer
           code=22;
        }
        else if (code==22){
           val1+=(character-48)*10; // set id of the buzzer
           code=23;
        }
        else if (code==23){
           val1+=(character-48); // set id of the buzzer
           gx=val1-500;
           code=0;
        }
        
        else if (code==31){
           val1=(character-48)*100; // set id of the buzzer
           code=32;
        }
        else if (code==32){
           val1+=(character-48)*10; // set id of the buzzer
           code=33;
        }
        else if (code==33){
           val1+=(character-48); // set id of the buzzer
           gy=val1-500;
           code=0;
        }
        
        else if (code==41){
           val1=(character-48)*100; // set id of the buzzer
           code=42;
        }
        else if (code==42){
           val1+=(character-48)*10; // set id of the buzzer
           code=43;
        }
        else if (code==43){
           val1+=(character-48); // set id of the buzzer
           offsetX=(val1-500);
           code=0;
        }
        
        else if (code==51){
           val1=(character-48)*100; // set id of the buzzer
           code=52;
        }
        else if (code==52){
           val1+=(character-48)*10; // set id of the buzzer
           code=53;
        }
        else if (code==53){
           val1+=(character-48); // set id of the buzzer
           offsetY=(val1-500);
           code=0;
        }
        
         
       }
     }
  }
  
  
  // read joystick values
  stickX=analogRead(A0);
  stickY=analogRead(A1);
  
  stickX-=refX;
  stickY-=refY;
  
  stickX=stickX*1.6;
  stickY=stickY*1.6;
  
  
  Serial.print('a');
  Serial.print(String(stickX));
  Serial.print('b');
  Serial.print(String(stickY));
  Serial.print('c');
  
  
  
  // edges
  stickX-=gx;
  stickY-=gy;
  
  // solid friction
  long vect=stickX*stickX+stickY*stickY;
  //Serial.println(vect);
  if (solid_friction>0){
    if (vect<solid_friction*solid_friction){
      stickX=0;
      stickY=0;
    }
    else{
      stickX=stickX*(500-solid_friction)/500;
      stickY=stickY*(500-solid_friction)/500;
    }
  }
  
  // fluid friction
  if (fluid_friction>0){
    stickX=stickX*(500-fluid_friction)/500;
    stickY=stickY*(500-fluid_friction)/500;
  }
  
  //Serial.println(offsetX);
  
  // offset
  stickX+=offsetX;
  stickY+=offsetY;
  
  
  
  // servo 0
  //if (stickX<-5 || stickX>5){
    servo=map(stickX, -360,360, 180, 0);
    servo=1410+servo;
    myservo[1].writeMicroseconds(servo);
  //}
  
  // servo 1
  //if (stickY<-5 || stickY>5){
    servo=map(stickY, -360,360, 180, 0);
    servo=1410+servo;
    myservo[0].writeMicroseconds(servo);
  //}
  
  delay(20);
  
  counter++;
  /*if (counter==100){
    counter=0;
    solid_friction+=20;
    if (solid_friction>=320) solid_friction=0;
  }*/
  
  /*if (counter==100){
    counter=0;
    fluid_friction+=2;
    if (fluid_friction>=70) fluid_friction=0;
  }*/
}
