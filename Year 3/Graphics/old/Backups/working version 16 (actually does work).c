
#include <cgRender.h>

float toRadians = 0.01745329251;
int pointCount;
float xPoints[30000];
float yPoints[30000];
float zPoints[30000];
float xTexPoints[30000];
float yTexPoints[30000];
float maxX,maxY,maxZ;
float minX,minY,minZ;
float centreX,centreY,centreZ;
float cameraX,cameraY,cameraZ;
float cameraXoriginal,cameraYoriginal,cameraZoriginal;
float cameraUpX,cameraUpY,cameraUpZ;
float cameraUpXOriginal,cameraUpYOriginal,cameraUpZOriginal;
char string[30];
int dbn = 0; //Debug number
int times = 0;
GLubyte myImage[512][512][3];
int textureApplied = 0;
float viewportUnit = 10;//0.1;
float thetaAzimuth = 0;//310
float thetaElevation = 0;
float thetaRoll = 0;
float faceAngleYaw = 0;
float faceAngleRoll = 0;
float faceAnglePitch = 0;
float distance = 0.05;
int polygonA[60000];
int polygonB[60000];
int polygonC[60000];
int polygonCount;
float radiusX = 0;
float radiusY = 0;
float radiusZ = -1;

void debug(){
	printf("Debug point %d\n",dbn);
	dbn++;
}

void getPoints(){
	FILE *file;
	int i;
	float x;
	float y;
	float z;


	file = fopen("face.vtk", "r");
	fseek (file, 73, SEEK_SET);
	fscanf(file, "%d ",&pointCount);
	fscanf(file, "%s",string);

	i = 0;
	while(i<pointCount)
	{
		//store points in file
		fscanf(file, "%f %f %f",&x,&y,&z);
		xPoints[i] = x;
		yPoints[i] = y;
		zPoints[i] = z;

		//determine Bounding Box
		if(x>maxX){maxX = x;}
		if(x<minX){minX = x;}
		if(y>maxY){maxY = y;}
		if(y<minY){minY = y;}
		if(z>maxZ){maxZ = z;}
		if(z<minZ){minZ = z;}

		//find mean point
 		/*centreX = (centreX*i + x)/(i+1);
		centreY = (centreY*i + y)/(i+1);
		centreZ = (centreZ*i + z)/(i+1);*/

  		i++;

	}
	centreX = (minX + maxX)/2;
	centreY = (minY + maxY)/2;
	centreZ = (minZ + maxZ)/2;

}
void phongShading(void){
	textureApplied = 0;
	GLfloat LightPosition[] = {0,2,0,0};
	GLfloat LightAmbient[] = {1,0,0,1};
	GLfloat LightDiffuse[] = {1,1,1,1};
	GLfloat LightSpecular[] = {0.1,0.1,0.1,1};

	GLfloat MaterialSpecular[] = {0.99,0.91,0.8,1};
	GLfloat MaterialShininess[] = {57.8};

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
 	glEnable(GL_NORMALIZE);

}

void readTextureValues(void){
	int row,column,color = 0;
	FILE *textureFile;
	unsigned char number;

	//the "rb" means reading a binary file
	textureFile = fopen("face.ppm", "rb");
	int i = 0;
	//Skip to correct place in file
	while(i<15){
		fread(&number,1,1,textureFile);
		i++;
	}

	// Go through the file and store the RGB triplets
	for(row = 0;row<512;row++) {
		for(column=0;column<512;column++) {
			for(color=0;color<3;color++) {
				fread(&number,1,1,textureFile);
				myImage[row][column][color] = number;
			}
		}
	}
}

void applyTexture(void){
	textureApplied = 1;
	int row,column,color;

	glutSwapBuffers();

}
void readTexturePoints(){
	FILE *fileTexPts;
	float texX1,texY1,texX2,texY2,texX3,texY3;
	int texturePointCount;
	char string[30];
	fileTexPts = fopen("face.vtk", "r");;

	// Skip to correct place in file
	while(strcmp(string,"POINT_DATA")!=0){
		fscanf(fileTexPts,"%s",&string);
	}
	//Read the number of texture points
	fscanf(fileTexPts,"%d",&texturePointCount);

	//Skip to start of texture point data
	while(strcmp(string,"float")!=0){
		fscanf(fileTexPts,"%s",&string);
 	}

	//Copy the data into memory
	int i = 0;
	while(i<texturePointCount) {
		fscanf(fileTexPts,"%f %f\n",&texX1,&texY1);
		xTexPoints[i] = texX1;
		yTexPoints[i] = texY1;
		i++;
	}
}
void textureMapped() {
	readTextureValues();
	readTexturePoints();
	applyTexture();

	glEnable(GL_TEXTURE_2D);
	//glTexImage2D(GL_TEXTURE_2D,0,3,512,512,0,GL_RGB,GL_UNSIGNED_BYTE,image);
	glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,512,512,0,GL_RGB,GL_UNSIGNED_BYTE,myImage);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_CLAMP);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_CLAMP);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_NEAREST);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
}

void getPolygons() {

	int p1,p2,p3;

	char string[30];
	FILE *file;

	file = fopen("face.vtk", "r");

	// Skip to correct place in file
	fscanf(file,"%s",&string);
	while(strcmp(string,"POLYGONS")!=0){
		fscanf(file,"%s",&string);

	}
	// Record the number of polygons
	fscanf(file,"%d %s",&polygonCount,&string);

	int i = 0;
	while(i<polygonCount)
	{
		fscanf(file,"%*d %d %d %d",&p1,&p2,&p3);
		polygonA[i] = p1;
		polygonB[i] = p2;
		polygonC[i] = p3;
		i++;
	}

}

void init()
{
 glClearColor (0.0, 0.0, 0.0, 0.0);
 cout << "init" << endl;

	getPoints();
	getPolygons();
	cameraX = distance*sin(thetaAzimuth*toRadians);
	cameraY = 0;
	cameraZ = -distance*cos(thetaAzimuth*toRadians);
	printf("Original position: ((%f,%f,%f)\n",cameraX,cameraY,cameraZ);

	cameraXoriginal = cameraX;
	cameraYoriginal = cameraY;
	cameraZoriginal = cameraZ;
	cameraUpX = 0;
	cameraUpY = 1;
	cameraUpZ = 0;
	cameraUpXOriginal = cameraUpX;
	cameraUpYOriginal = cameraUpY;
	cameraUpZOriginal = cameraUpZ;

 	phongShading();
	//textureMapped();

	viewportUnit = (maxX-minX);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
 	glTranslatef(centreX,centreY,centreZ);
	gluLookAt(cameraX,cameraY,cameraZ,0,0,0,cameraUpX,cameraUpY,cameraUpZ);
	glOrtho(-viewportUnit,viewportUnit,-viewportUnit,viewportUnit,-viewportUnit,viewportUnit);
	glTranslatef(-centreX,-centreY,-centreZ);
	glutPostRedisplay();
}

void display(void)
{
 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 //cout << "display" << endl;

 	viewportUnit = (maxX-minX);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(centreX,centreY,centreZ);
 	gluLookAt(cameraX,cameraY,cameraZ,0,0,0,cameraUpX,cameraUpY,cameraUpZ);
	glOrtho(-viewportUnit,viewportUnit,-viewportUnit,viewportUnit,-viewportUnit,viewportUnit);
	glRotatef(thetaElevation,1,0,0);
	glRotatef(thetaAzimuth,0,1,0);
	glRotatef(thetaRoll,radiusX,radiusY,radiusZ);
	glTranslatef(-centreX,-centreY,-centreZ);
	/*
	printf("Camera position: (%f,%f,%f)\n",cameraX,cameraY,cameraZ);
	printf("Angle of Azimuth: %f\n",thetaAzimuth);
	printf("Angle of Elevation: %f\n",thetaElevation);
	printf("Camera orientation: (%f,%f,%f)\n\n",cameraUpX,cameraUpY,cameraUpZ);
	*/
	float x1,y1,z1;
	float xx1,yy1,zz1;
	float x2,y2,z2;
	float xx2,yy2,zz2;
	float x3,y3,z3;
	float xx3,yy3,zz3;
 	float texX1,texY1,texX2,texY2,texX3,texY3;

	char string[30];

	int i = 0;

	//plot the polygons
	while(i<polygonCount)
	{
  		x1 = xPoints[polygonA[i]];
		y1 = yPoints[polygonA[i]];
		z1 = zPoints[polygonA[i]];
		//yy1 = y1*cos(faceAngleYaw*toRadians) - z1*sin(faceAngleYaw*toRadians);
		//zz1 = y1*sin(faceAngleYaw*toRadians) + z1*cos(faceAngleYaw*toRadians);

		x2 = xPoints[polygonB[i]];
		y2 = yPoints[polygonB[i]];
		z2 = zPoints[polygonB[i]];
		//xx2 = x2;
		//yy2 = y2*cos(faceAngleYaw*toRadians) - z2*sin(faceAngleYaw*toRadians);
		//zz2 = y2*sin(faceAngleYaw*toRadians) + z2*cos(faceAngleYaw*toRadians);

		x3 = xPoints[polygonC[i]];
		y3 = yPoints[polygonC[i]];
		z3 = zPoints[polygonC[i]];
		//xx3 = x3;
		//yy3 = y3*cos(faceAngleYaw*toRadians) - z3*sin(faceAngleYaw*toRadians);
		//zz3 = y3*sin(faceAngleYaw*toRadians) + z3*cos(faceAngleYaw*toRadians);

		texX1 = xTexPoints[polygonA[i]];
		texY1 = yTexPoints[polygonA[i]];
		texX2 = xTexPoints[polygonB[i]];
		texY2 = yTexPoints[polygonB[i]];
		texX3 = xTexPoints[polygonC[i]];
		texY3 = yTexPoints[polygonC[i]];

		//glTranslatef(centreX,centreY,centreZ);
		glBegin(GL_TRIANGLES);
			glTexCoord2f(texX1,texY1);
			glVertex3f(x1,y1,z1);
			glTexCoord2f(texX2,texY2);
			glVertex3f(x2,y2,z2);
			glTexCoord2f(texX3,texY3);
			glVertex3f(x3,y3,z3);
		glEnd();
		//glTranslatef(-centreX,-centreY,-centreZ);

		//The normal for phong shading
		glNormal3f(	((x2-x1)*(y3-y1)) - ((x3-x1)*(y2-y1)) ,
				-( ((x2-x1)*(z3-z1)) - ((x3-x1)*(z2-z1)) ) ,
				((y2-y1)*(z3-z1)) - ((y3-y1)*(z2-z1))
				);
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
 	//cout << "keyboard" << endl;
	//printf("Key pressed: %c (%d)\n",key,key);

	double thetaIncremenet = 10;

	switch (key) {
  		case 32: {//SPACE
			thetaAzimuth = 0;
			thetaElevation = 0;
			thetaRoll = 0;
			faceAngleYaw = 0;
			faceAngleRoll = 0;
			faceAnglePitch = 0;
			printf("\n--------------------------------------\n");
			break;
		}
		case 97: {//A - Azimuth
			thetaAzimuth += thetaIncremenet;
			break;
		}
		case 115: {//S - Azimuth
			thetaAzimuth -= thetaIncremenet;
			break;
		}
		case 101: {//E - Elevation
			thetaElevation += thetaIncremenet;
			break;
		}
		case 119: {//W - Elevation
			thetaElevation -= thetaIncremenet;
			break;
		}
		case 114: {//R - Roll (Camera)
			thetaRoll += thetaIncremenet;
			break;
		}
		case 116: {//T - Roll (Camera)
   			thetaRoll -= thetaIncremenet;
			break;
		}
		case 121: {//Y - Yaw
			faceAngleYaw += thetaIncremenet;
			break;
		}
		case 117: {//U - Yaw
			faceAngleYaw -= thetaIncremenet;
			break;
		}
		case 102: {//F - Roll (Face)
			faceAngleRoll += thetaIncremenet;
			break;
		}
		case 100: {//D - Roll (Face)
			faceAngleRoll -= thetaIncremenet;
			break;
		}
		case 112: {//P - Pitch
			faceAnglePitch += thetaIncremenet;
			break;
		}
		case 111: {//O - Pitch
			faceAnglePitch -= thetaIncremenet;
			break;
		}
 	}
	radiusX = distance*sin(thetaAzimuth*toRadians)*cos(thetaElevation*toRadians);
	radiusY = distance*sin(thetaElevation*toRadians);
	radiusZ = -distance*cos(thetaAzimuth*toRadians)*cos(thetaElevation*toRadians);

	if(thetaAzimuth >360){
		thetaAzimuth -= 360;
	}
	if(thetaAzimuth < 0){
		thetaAzimuth += 360;
	}
	if(thetaElevation >360){
		thetaElevation -= 360;
	}
	if(thetaElevation < 0){
		thetaElevation += 360;
	}

 	glutPostRedisplay();
}

void idle(){
	//cout << "idle" << endl;

	//glutPostRedisplay();
}

int main(int argc, char** argv)
{
	readTextureValues();
 // Initialize graphics window
 glutInit(&argc, argv);
 glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
 glutInitWindowSize (500, 500);
 glutInitWindowPosition (500, 500);
 //glutCreateWindow (argv[0]);
 glutCreateWindow ("Face");

 // Initialize OpenGL
 init();

 // Initialize callback functions
 glutDisplayFunc(display);
 glutReshapeFunc(reshape);
 glutKeyboardFunc(keyboard);
 glutIdleFunc(idle);



 // Start rendering
 glutMainLoop();

}

