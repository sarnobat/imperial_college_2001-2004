
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
float cameraUpX,cameraUpY,cameraUpZ;
float cameraAzimuth,cameraElevation,cameraRoll,faceYaw,faceRoll,facePitch;
float zoom;
float near,far;
char string[30];
int dbn = 0; //Debug number
float xWidth,yWidth,zWidth;
int polygonA[60000];
int polygonB[60000];
int polygonC[60000];
int polygonCount;
float distance;
GLubyte myImage[512][512][3]; // Texture file must be resized
bool myFace = false;
bool textured = false;

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
	float xTotal = 0;
	float yTotal = 0;
	float zTotal = 0;

	if(myFace){
		file = fopen("ss401_1083.vtk","r");
	}
	else {
		file = fopen("face.vtk","r");
	}

	while(strcmp(string,"POINTS")!=0){
		fscanf(file,"%s",&string);
	}
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

		//printf("%f\t%f\t%f\n",xPoints[i],yPoints[i],zPoints[i]);

		//determine Bounding Box
		if(x>maxX){maxX = x;}
		if(x<minX){minX = x;}
		if(y>maxY){maxY = y;}
		if(y<minY){minY = y;}
		if(z>maxZ){maxZ = z;}
		if(z<minZ){minZ = z;}
		xTotal += x;
		yTotal += y;
		zTotal += z;

		i++;
	}

	//find centre of face using interval halving
	centreX = (minX + maxX)/2;
	centreY = (minY + maxY)/2;
	centreZ = (minZ + maxZ)/2;

	xWidth = maxX-minX;
	yWidth = maxY-minY;
	zWidth = maxZ-minZ;

	distance = zWidth*40;

	printf("Centre of face: (%f,%f,%f)\n",centreX,centreY,centreZ);
	printf("BOUNDING BOX\n");
	printf("Top far corner: (%f %f %f)\n",maxX,maxY,maxZ);
	printf("Bottom near corner: (%f %f %f)\n",minX,minY,minZ);
	printf("Size: %f %f %f\n",xWidth,yWidth,zWidth);

}

void readTextureValues(void){
	int row,column,color = 0;
	int rowCount = 512;
	int columnCount = 512;
	FILE *textureFile;
	unsigned char number;

	//the "rb" means reading a binary file
	if(myFace) {
		textureFile = fopen("ss401_1083.ppm", "rb");
  	}
	else {
		textureFile = fopen("face.ppm", "rb");
	}
	int i = 0;
	//Skip to correct place in file
	while(i<15){
		fread(&number,1,1,textureFile);
		i++;
	}

	for(row = 0;row<rowCount;row++) {
		for(column=0;column<columnCount;column++) {
			for(color=0;color<3;color++) {
				fread(&number,1,1,textureFile);
				//printf("%d ",number);
				myImage[row][column][color] = number;
			}
			//printf("\n");
		}
	}
}

void applyTexture(void){

	int row,column,color;

	glutSwapBuffers();
}

void readTexturePoints(){
	FILE *fileTexPts;
	float texX1,texY1,texX2,texY2,texX3,texY3;
	int texturePointCount;
	char string[30];

	if(myFace){
		fileTexPts = fopen("ss401_1083.vtk","r");
	}
	else {
		fileTexPts = fopen("face.vtk","r");
	}

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
	printf("%d\n",texturePointCount);
	while(i<texturePointCount) {
		fscanf(fileTexPts,"%f %f\n",&texX1,&texY1);
		xTexPoints[i] = texX1;
		yTexPoints[i] = texY1;
		//printf("%f\t%f\n",xTexPoints[i],yTexPoints[i]);
		i++;
	}
}

void phongShading(void){

	//textureApplied = 0;
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

	glutSwapBuffers();

}

void textureMapped() {
	readTextureValues();
	readTexturePoints();

	glEnable(GL_TEXTURE_2D);

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

	if(myFace){
		file = fopen("ss401_1083.vtk","r");
	}
	else {
		file = fopen("face.vtk","r");
	}
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
		//printf("%d\t%d\t%d\n",polygonA[i],polygonB[i],polygonC[i]);
		i++;
	}
}

void init()
{
 	glClearColor (0.0, 0.0, 0.0, 0.0);
 	//cout << "init" << endl;

	cameraAzimuth = 230;
	cameraElevation = 0;
	cameraRoll = 0;
	faceYaw = 0;
	faceRoll = 0;
	facePitch = 0;

	zoom = 1;

	if(textured) {
		textureMapped();
	}
	else {
		phongShading();
	}

	// Create a convenient camera position
	if(myFace){
		near = -100;
		far = 500;
		cameraX = centreX;
		cameraY = centreY;
		cameraZ = distance;
	}
	else {
		near = 0.1;
		far = 50;
	}
}

void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMatrixMode (GL_PROJECTION);
	glLoadIdentity();

	gluPerspective(180*toRadians, 1, near, far);


	glMatrixMode (GL_MODELVIEW);
	glLoadIdentity();
 	glTranslatef(cameraX,cameraY,cameraZ);
	glRotatef(faceRoll,0,0,1);
	glRotatef(facePitch,1,0,0);
 	glRotatef(faceYaw,0,1,0);
	glTranslatef(-cameraX,-cameraY,-cameraZ);
	gluLookAt(0,0,distance, centreX, centreY, centreZ, 0, 1, 0);

	glTranslatef(centreX,centreY,centreZ);
	glRotatef(cameraRoll,0,0,1);
	glRotatef(cameraElevation,1,0,0);
	glRotatef(cameraAzimuth,0,1,0);
	glScalef(zoom,zoom,zoom);
	glTranslatef(-centreX,-centreY,-centreZ);

	printf("Camera\tAzimuth:\t%f\n",cameraAzimuth);
	printf("Camera\tElevation:\t%f\n",cameraElevation);
	printf("Camera\tRoll:\t\t%f\n",cameraRoll);
 	printf("Face\tYaw:\t\t%f\n",faceYaw);
	printf("Face\tRoll:\t\t%f\n",faceRoll);
	printf("Face\tPitch:\t\t%f\n",facePitch);
	printf("\n");

	float x1,y1,z1;
	float x2,y2,z2;
	float x3,y3,z3;
 	float texX1,texY1,texX2,texY2,texX3,texY3;

	//plot the polygons
	int i = 0;
	while(i<polygonCount)
	{
  		x1 = xPoints[polygonA[i]];
		y1 = yPoints[polygonA[i]];
		z1 = zPoints[polygonA[i]];

		x2 = xPoints[polygonB[i]];
		y2 = yPoints[polygonB[i]];
		z2 = zPoints[polygonB[i]];

		x3 = xPoints[polygonC[i]];
		y3 = yPoints[polygonC[i]];
		z3 = zPoints[polygonC[i]];

		texX1 = xTexPoints[polygonA[i]];
		texY1 = yTexPoints[polygonA[i]];
		texX2 = xTexPoints[polygonB[i]];
		texY2 = yTexPoints[polygonB[i]];
		texX3 = xTexPoints[polygonC[i]];
		texY3 = yTexPoints[polygonC[i]];

		//printf("(%f,%f,%f) (%f,%f,%f) (%f,%f,%f)\n",x1,y1,z1,x2,y2,z2,x3,y3,z3);
		glBegin(GL_TRIANGLES);
			glTexCoord2f(texX1,texY1);
			glVertex3f(x1,y1,z1);
			glTexCoord2f(texX2,texY2);
			glVertex3f(x2,y2,z2);
			glTexCoord2f(texX3,texY3);
			glVertex3f(x3,y3,z3);
		glEnd();

		//The normal for phong shading
		glNormal3f(	((x2-x1)*(y3-y1)) - ((x3-x1)*(y2-y1)) ,
				-( ((x2-x1)*(z3-z1)) - ((x3-x1)*(z2-z1)) ) ,
				((y2-y1)*(z3-z1)) - ((y3-y1)*(z2-z1))
				);

		i++;

	}
	glutSwapBuffers();
}


void reshape (int w, int h)
{
 	cout << "reshape" << endl;
	glViewport (0, 0, (GLsizei) w, (GLsizei) h);
}

void keyboard(unsigned char key, int x, int y)
{
 	//cout << "keyboard" << endl;

	double focusMaintainedIncrement = 10;
	double focusLostIncrement = 1;
	double scaleFactor = 1.1;

	switch (key) {
  		case 32: {//SPACE
			init();
			printf("--------------------------------------\n");
			break;
		}
		case 97: {//A - Azimuth
			cameraAzimuth += focusMaintainedIncrement;
			break;
		}
		case 115: {//S - Azimuth
			cameraAzimuth -= focusMaintainedIncrement;
			break;
		}
		case 101: {//E - Elevation
			cameraElevation += focusMaintainedIncrement;
			break;
		}
		case 119: {//W - Elevation
			cameraElevation -= focusMaintainedIncrement;
			break;
		}
		case 114: {//R - Roll (Camera)
			cameraRoll += focusMaintainedIncrement;
			break;
		}
		case 116: {//T - Roll (Camera)
   			cameraRoll -= focusMaintainedIncrement;
			break;
		}
		case 121: {//Y - Yaw
			faceYaw += focusLostIncrement;
   			break;
		}
		case 117: {//U - Yaw
			faceYaw -= focusLostIncrement;
			break;
		}
		case 102: {//F - Roll (Face)
			faceRoll += focusMaintainedIncrement;
			break;
		}
		case 100: {//D - Roll (Face)
			faceRoll -= focusMaintainedIncrement;
			break;
		}
		case 112: {//P - Pitch
			facePitch += focusLostIncrement;
			break;
		}
		case 111: {//O - Pitch
			facePitch -= focusLostIncrement;
			break;
		}
		case 122: {//Z - Zoom
			zoom = zoom*scaleFactor;
			break;
		}
		case 120: {//X - Zoom
			zoom = zoom*(1/scaleFactor);
			break;
		}
 	}

	glutPostRedisplay();
}

int main(int argc, char** argv)
{
	// Read in all points; determine bounding box; determine centre of face
	getPoints();
	// Read in the index of each point which makes up a triangle
	getPolygons();


	// Initialize graphics window
	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (500, 500);
	glutInitWindowPosition (500, 500);
	glutCreateWindow ("Face");

	// Initialize OpenGL
	init();

	// Initialize callback functions
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);

	// Start rendering
	glutMainLoop();
}
