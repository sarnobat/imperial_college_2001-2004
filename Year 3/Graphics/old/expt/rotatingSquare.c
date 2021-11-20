#define DEG_TO_RAD 57.29
#include <GL/glut.h>
#include <math.h>
GLfloat theta = 0;

void display(void)
{

 	glClear(GL_COLOR_BUFFER_BIT);
	glBegin(GL_POLYGON);
		glVertex2f(cos(DEG_TO_RAD * theta),sin(DEG_TO_RAD * theta));
		glVertex2f(-sin(DEG_TO_RAD * theta),cos(DEG_TO_RAD * theta));
		glVertex2f(-cos(DEG_TO_RAD * theta),-sin(DEG_TO_RAD * theta));
		glVertex2f(sin(DEG_TO_RAD * theta),-cos(DEG_TO_RAD * theta));
	glEnd();

	glFlush();
}




void idle(){
	//cout << "idle" << endl;
	/*theta += 0.0002;
	if(theta >= 360) {
		theta -= 360;
	}*/
	//glutPostRedisplay();

}
void keyboard(unsigned char c,int a, int b) {
	theta += 0.001;
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glRotatef(theta,0,1,0);
	glutPostRedisplay();
}

int main(int argc, char** argv)
{

 // Initialize graphics window
 //glutInit(&argc, argv);
 //glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
 glutInitWindowSize (500, 500);
 glutInitWindowPosition (500, 500);
 //glutCreateWindow (argv[0]);
 glutCreateWindow ("Face");


 // Initialize callback functions
 glutDisplayFunc(display);
 glutIdleFunc(idle);
 glutKeyboardFunc(keyboard);



 // Start rendering
 glutMainLoop();

}

