#include <cgRender.h>

void init()
{
	glClearColor (0.0, 0.0, 0.0, 0.0);
	cout << "init" << endl;

	/*
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
	//	exit(0);
	break;
	}
}

void messingAround() {


	char *c;
	char d;
	//float numbers[30];

	/* make sure it is large enough to hold all the data! */
	int i,j;



	//fscanf(file,"%s", &c);
	//printf("%c\n", c);
	//fscanf(file,"%*s %*s %*s %*s %*s %*s %*s %*s %*s %*s %*s %*s %s", &c);

/*fseek (file, 80, SEEK_SET);
	i = 0;
	while(i<30){
		fscanf(file, "%f %f", y,z);
		printf("%f %f",y,z);

	i++;
    }*/

	//printf("%s\n", *c);
	//while(!feof(file)) {

		//fscanf(file, "%f %f %f", &x, &y, &z);
		//printf("%f %f %f\n", &x, &y, &z);


    //}

	//printf("%f\n", number);
}

int main(int argc, char** argv)
{
	//messingAround();
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

	glScalef(10,10,10);
}
