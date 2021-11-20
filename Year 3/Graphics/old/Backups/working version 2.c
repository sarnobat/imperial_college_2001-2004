#include <cgRender.h>
//float *xsPtr;
int pointCount;
//10];
float *x2;
float ys[26460];
float zs[26460];
char string[30];
int dbn = 0; //Debug number

void debug(){
	printf("Debug point %d\n",dbn);
	dbn++;
}

void init()
{
 glClearColor (0.0, 0.0, 0.0, 0.0);
 cout << "init" << endl;

	FILE *file;
	int i;//,pointCount;

	float x;
	float y;
	float z;
	float xCenter = 0;
	float yCenter = 0;
	float zCenter = 0;


	file = fopen("face.vtk", "r");
	fseek (file, 73, SEEK_SET);
	fscanf(file, "%d ",&pointCount);
	float xs[pointCount];
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




 /*
 glShadeModel (GL_SMOOTH);

 // Enable lighting
 glEnable (GL_LIGHTING);
 glEnable (GL_LIGHT0);
 glLightfv(GL_LIGHT0, GL_POSITION, LightPosition);
 glLightfv(GL_LIGHT0, GL_AMBIENT, LightAmbient);
 glLightfv(GL_LIGHT0, GL_DIFFUSE, LightDiffuse);
 glLightfv(GL_LIGHT0, GL_SPECULAR, LightSpecular);

 // Set material parameters
 glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, MaterialSpecular);
 glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, MaterialShininess);

 // Enable Z-buffering
 glEnable(GL_DEPTH_TEST);
 */
}

void display(void)
{
 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 cout << "display" << endl;


/*
 for (all polygons)
 glBegin(GL_POLYGON);
 for (all vertices of polygon)
 // Define texture coordinates of vertex
 glTexCoord2f(...);
 // Define normal of vertex
 glNormal3f(...);
 // Define coordinates of vertex
 glVertex3f(...);
 }
 glEnd();
 }*/
	int i = 0;
	while(i<pointCount)
	{
		glBegin(GL_POINTS);
			//glVertex3f(xs[i],ys[i],zs[i]);
			glVertex3f(*x2,ys[i],zs[i]);
		glEnd();
		x2++;
		i++;
	}




glFlush ();


}

void reshape (int w, int h)
{
 cout << "reshape" << endl;

 glViewport (0, 0, (GLsizei) w, (GLsizei) h);
/*
 glMatrixMode (GL_PROJECTION);
 glLoadIdentity();
 gluPerspective(fovy, aspect, near, far);
 glMatrixMode (GL_MODELVIEW);
 glLoadIdentity();
 gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
 */
}

void keyboard(unsigned char key, int x, int y)
{
 switch (key) {
 case 27:
 // exit(0);
 break;
 }
}

void idle(){
}

int main(int argc, char** argv)
{
 // Initialize graphics window
 glutInit(&argc, argv);
 glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
 glutInitWindowSize (256, 256);
 glutInitWindowPosition (0, 0);
 //glutCreateWindow (argv[0]);
 glutCreateWindow ("Face");

 // Initialize OpenGL
 init();

 // Initialize callback functions
 glutDisplayFunc(display);
 glutReshapeFunc(reshape);
 glutKeyboardFunc(keyboard);
 //glutIdleFunc(idle);

 // Start rendering
 glutMainLoop();
}
