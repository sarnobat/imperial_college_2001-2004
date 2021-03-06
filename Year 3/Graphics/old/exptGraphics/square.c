#include <cgRender.h>

void init()
{
}

void display(void)
{
	glClearColor (1.0, 0.0, 0.0, 0.0);
	glClear(GL_COLOR_BUFFER_BIT);
	glBegin(GL_POLYGON);
		glVertex2f(-0.5,-0.5);
		glVertex2f(-0.5,0.5);
		glVertex2f(0.5,0.5);
 glVertex2f(0.5,-0.5);
	glEnd();

	glFlush();
}



int main(int argc, char** argv)
{
  // Initialize graphics window
  glutInit(&argc, argv);
  glutCreateWindow (argv[0]);
  glutDisplayFunc(display);

	init();
  // Start rendering
  glutMainLoop();
}
