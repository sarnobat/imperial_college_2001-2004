#include <cgRender.h>
int pointCount;
float *x2;
float *y2;
float *z2;

void display(void)
{
	int i = 0;
	while(i<pointCount)
	{
		glBegin(GL_POINTS);
			glVertex3f(*x2,*y2,*z2);
		glEnd();

		//	printf("%f %f %f\n",x2,y2,z2);

		x2++;
		y2++;
		z2++;
		i++;
	}



	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
 	gluLookAt(0.1,0.2,0.1,0,-0.2048,-0.09060,0,1,0);
	glutWireCube(0.5);
	glutSwapBuffers();
}

void reshape(int w,int h){
	glViewport(0,0,w,h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-4,4,-4,4,-4,4);

}

void init()
{
	glClearColor(0,0,0,0);
	glColor3f(1,1,1);

	FILE *file;
	int i;
	float x;
	float y;
	float z;
	float xCenter = 0;
	float yCenter = 0;
	float zCenter = 0;
	char string[30];


	file = fopen("face.vtk", "r");
	fseek (file, 73, SEEK_SET);
	fscanf(file, "%d ",&pointCount);
	float xs[pointCount];
	float ys[pointCount];
	float zs[pointCount];
	fscanf(file, "%s",string);

	i = 0;
	while(i<pointCount)
	{
		fscanf(file, "%f %f %f",&x,&y,&z);
		xs[i] = x;
		ys[i] = y;
		zs[i] = z;
		//printf("%f %f %f\t%d\n",xs[i],ys[i],zs[i],i);
		i++;
	}
	x2 = &(xs[0]);
	y2 = &(ys[0]);
	z2 = &(zs[0]);


}

int main(int argc, char** argv)
{
  // Initialize graphics window
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
  glutInitWindowSize(500,500);
  glutInitWindowPosition (0,0);
  glutCreateWindow("cube");
  glutReshapeFunc(reshape);
  glutDisplayFunc(display);
  init();
  glutMainLoop();
}
