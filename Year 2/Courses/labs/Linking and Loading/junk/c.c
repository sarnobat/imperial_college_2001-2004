extern void write(int, char [], int );
extern void exit(int);

char message [] = "Hello World!\n";

#define MESSAGELENGTH 13
int main(void){
  write(1,message,MESSAGELENGTH);
  exit(1);
}
