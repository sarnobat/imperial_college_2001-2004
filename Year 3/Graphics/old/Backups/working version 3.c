#include <cgRender.h>
//float *xsPtr;
int pointCount;
float *x2;
float *y2;
float *z2;
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
	int i;
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
	float ys[pointCount];
	float zs[pointCount];
	fscanf(file, "%s",string);
	/*x2 = (float *) malloc(pointCount*4);
	y2 = (float *) malloc(pointCount*4);
	z2 = (float *) malloc(pointCount*4);*/

	i = 0;
	while(i<pointCount)
	{
  		//fscanf(file, "%f %f %f",x2,y2,z2);
		//printf("%f %f %f %i\n",*x2,*y2,*z2,i);
		fscanf(file, "%f %f %f",&x,&y,&z);
		xs[i] = x;
		ys[i] = y;
		zs[i] = z;
		//printf("%f %f %f\t%d\n",xs[i],ys[i],zs[i],i);
		i++;
		/*x2++;
		y2++;
		z2++;*/

	}
	x2 = &(xs[0]);
	y2 = &(ys[0]);
	z2 = &(zs[0]);




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
 	debug();
	int i = 0;
	while(i<pointCount)
	{
		//printf("%f %f %f %i\n",*x2,*y2,*z2,i);
		glBegin(GL_POINTS);
			//glVertex3f(xs[i],ys[i],zs[i]);
			glVertex3f(*x2,*y2,*z2);
		glEnd();
		x2++;
		y2++;
		z2++;
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

