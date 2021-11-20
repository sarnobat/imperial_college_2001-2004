#include <cgRender.h>
#include <GL/glut.h>

void init()
{
	glClearColor(0,0,0,0);
	glColor3f(0,1,1);

	/*glClearColor (0.0, 0.0, 0.0, 0.0);
	cout << "init" << endl;


	glShadeModel (GL_SMOOTH);

	// Enable lighting
	glEnable (GL_LIGHTING);
	glEnable (GL_LIGHT0);
	glLightfv(GL_LIGHT0, GL_POSITION, LightPosition);
	glLightfv(GL_LIGHT0, GL_AMBIENT,	LightAmbient);
	glLightfv(GL_LIGHT0, GL_DIFFUSE,	LightDiffuse);
	glLightfv(GL_LIGHT0, GL_SPECULAR, LightSpecular);

	// Set material parameters
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR,	MaterialSpecular);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, MaterialShininess);

	// Enable Z-buffering
	glEnable(GL_DEPTH_TEST);
	*/
}


void display(void)
{
	FILE *file;
	int i,pointCount;
	float x;
	float y;
	float z;

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	cout << "display" << endl;

	file = fopen("face.vtk", "r");
	fseek (file, 73, SEEK_SET);
	fscanf(file, "%d ",&pointCount);
	printf("Number of points: %d\n",pointCount);

	i = 0;
	while(i<pointCount){
		fscanf(file, "%*s %f %f",&x,&y,&z);
		//printf("%f %f %f\t%d\n",x,y,z,i);
		glBegin(GL_POINTS);
			glVertex3f(x,y,z);
		glEnd();
		i++;
	}
	fclose(file);



	/*glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	//glTranslatef(1,1,1);
	glScalef(2,2,2);*/
	/*gluLookAt(1,1,1,0,0,0,0,1,0);
	glutSolidCube(1);
	glutSwapBuffers();*/
 	glFlush ();

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
	}
	glFlush ();
	*/
}

void reshape (int w, int h)
{
	glViewport(0,0,w,h);
	/*glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-4,4,-4,4,-4,4);*/
	cout << "reshape" << endl;

	glViewport (0, 0, (GLsizei) w, (GLsizei) h);

	glMatrixMode (GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fovy, aspect, near, far);
	glMatrixMode (GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);

}

void keyboard(unsigned char key, int x, int y)
{
	switch (key) {
	case 27:
	//	exit(0);
	break;
	}
}
int main(int argc, char** argv)
{

	// Initialize graphics window
	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (256, 256);
	glutInitWindowPosition (0, 0);
	glutCreateWindow (argv[0]);

	// Initialize OpenGL
	init();

	// Initialize callback functions
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);

	// Start rendering
	glutMainLoop();
}
