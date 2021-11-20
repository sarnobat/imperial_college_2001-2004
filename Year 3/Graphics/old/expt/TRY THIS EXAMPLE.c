

Limit search to current directory
>> Advanced search

Home > Common libs documentation > OpenGL > OpenGL Tutorial en > Examples > Polygons List.c

// OpenGL Tutorial
// Polygons_List.c

/*************************************************************************
This example is a modification Polygons.c to use display lists.
*************************************************************************/

// gcc -o Polygons_List  Polygons_List.c -lX11 -lMesaGL -lMesaGLU -lMesatk -lm

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <gltk.h>

#define PYRAMID 1
#define CUBE 2

void reshape(int width, int height) {

	// Set the new viewport size
	glViewport(0, 0, (GLint)width, (GLint)height);

	// Choose the projection matrix to be the matrix 
	// manipulated by the following calls
	glMatrixMode(GL_PROJECTION);

	// Set the projection matrix to be the identity matrix
	glLoadIdentity();

	// Define the dimensions of the Orthographic Viewing Volume
	glOrtho(-8.0, 8.0, -8.0, 8.0, -8.0, 8.0);

	// Choose the modelview matrix to be the matrix
	// manipulated by further calls
	glMatrixMode(GL_MODELVIEW);
}

GLenum key_down(int key, GLenum state) {

	if ((key == TK_ESCAPE) || (key == TK_q) || (key == TK_Q))
		tkQuit();
}

void draw(void) {

	// Clear the RGB buffer and the depth buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Set the modelview matrix to be the identity matrix
	glLoadIdentity();
	// Translate and rotate the object
	glTranslatef(-2.5, 0.0, 0.0);
	glRotatef(-30, 1.0, 0.0, 0.0);
	glRotatef(30, 0.0, 1.0, 0.0);
	glRotatef(30, 0.0, 0.0, 1.0);

	// Draw pyramid
	glCallList(PYRAMID);

	glLoadIdentity();
	glTranslatef(2.5, 0.0, 0.0);
	glRotatef(45, 1.0, 0.0, 0.0);
	glRotatef(45, 0.0, 1.0, 0.0);
	glRotatef(45, 0.0, 0.0, 1.0);

	// Draw cube
	glCallList(CUBE);

	// Flush the buffer to force drawing of all objects thus far
	glFlush();
}

void make_pyramid() {

	glNewList(PYRAMID, GL_COMPILE);

		glColor3f(1.0, 0.0, 1.0);
	
		// Draw the sides of the three-sided pyramid
		glBegin(GL_TRIANGLE_FAN);
			glVertex3d(0, 4, 0);
			glVertex3d(0, -4, -4);
			glVertex3d(-4, -4, 4);
			glVertex3d(4, -4, 4);
			glVertex3d(0, -4, -4);
		glEnd();
	
		glColor3f(0.0, 1.0, 1.0);
	
		// Draw the base of the pyramid
		glBegin(GL_TRIANGLES);
			glVertex3d(0, -4, -4);
			glVertex3d(-4, -4, 4);
			glVertex3d(4, -4, 4);
		glEnd();

	glEndList();
}


void make_cube() {

	glNewList(CUBE, GL_COMPILE);

		glColor3f(0.0, 1.0, 0.0);
	
		// Draw the sides of the cube
		glBegin(GL_QUAD_STRIP);
			glVertex3d(3, 3, -3);
			glVertex3d(3, -3, -3);
			glVertex3d(-3, 3, -3);
			glVertex3d(-3, -3, -3);
			glVertex3d(-3, 3, 3);
			glVertex3d(-3, -3, 3);
			glVertex3d(3, 3, 3);
			glVertex3d(3, -3, 3);
			glVertex3d(3, 3, -3);
			glVertex3d(3, -3, -3);
		glEnd();
	
		glColor3f(0.0, 0.0, 1.0);
	
		// Draw the top and bottom of the cube
		glBegin(GL_QUADS);
			glVertex3d(-3, -3, -3);
			glVertex3d(3, -3, -3);
			glVertex3d(3, -3, 3);
			glVertex3d(-3, -3, 3);
	
			glVertex3d(-3, 3, -3);
			glVertex3d(3, 3, -3);
			glVertex3d(3, 3, 3);
			glVertex3d(-3, 3, 3);
		glEnd();

	glEndList();
}

void main(int argc, char **argv) {

	// Set top left corner of window to be at location (0, 0)
	// Set the window size to be 500x500 pixels
	tkInitPosition(0, 0, 500, 500);

	// Initialize the RGB and Depth buffers
	tkInitDisplayMode(TK_RGB | TK_DEPTH);

	// Open a window, name it "Polygons_List"
	if (tkInitWindow("Polygons_List") == GL_FALSE) {
		tkQuit();
	}

	// Set the clear color to black
	glClearColor(0.0, 0.0, 0.0, 0.0);

	// Set the shading model
	glShadeModel(GL_FLAT);

	// Set the polygon mode to fill
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	// Enable depth testing for hidden line removal
	glEnable(GL_DEPTH_TEST);

	// Create the display lists
	make_pyramid();
	make_cube();

	// Assign reshape() to be the function called whenever
	// an expose event occurs
	tkExposeFunc(reshape);

	// Assign reshape() to be the function called whenever 
	// a reshape event occurs
	tkReshapeFunc(reshape);

	// Assign key_down() to be the function called whenever
	// a key is pressed
	tkKeyDownFunc(key_down);

	// Assign draw() to be the function called whenever a display
	// event occurs, generally after a resize or expose event
	tkDisplayFunc(draw);

	// Pass program control to tk's event handling code
	// In other words, loop forever
	tkExec();
}

Docs.mandragor.org - Version 1.0

Limit search to current directory
>> Advanced search

Home > Common libs documentation > OpenGL > OpenGL Tutorial en > Examples > Polygons List.c

// OpenGL Tutorial
// Polygons_List.c

/*************************************************************************
This example is a modification Polygons.c to use display lists.
*************************************************************************/

// gcc -o Polygons_List  Polygons_List.c -lX11 -lMesaGL -lMesaGLU -lMesatk -lm

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <gltk.h>

#define PYRAMID 1
#define CUBE 2

void reshape(int width, int height) {

	// Set the new viewport size
	glViewport(0, 0, (GLint)width, (GLint)height);

	// Choose the projection matrix to be the matrix 
	// manipulated by the following calls
	glMatrixMode(GL_PROJECTION);

	// Set the projection matrix to be the identity matrix
	glLoadIdentity();

	// Define the dimensions of the Orthographic Viewing Volume
	glOrtho(-8.0, 8.0, -8.0, 8.0, -8.0, 8.0);

	// Choose the modelview matrix to be the matrix
	// manipulated by further calls
	glMatrixMode(GL_MODELVIEW);
}

GLenum key_down(int key, GLenum state) {

	if ((key == TK_ESCAPE) || (key == TK_q) || (key == TK_Q))
		tkQuit();
}

void draw(void) {

	// Clear the RGB buffer and the depth buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Set the modelview matrix to be the identity matrix
	glLoadIdentity();
	// Translate and rotate the object
	glTranslatef(-2.5, 0.0, 0.0);
	glRotatef(-30, 1.0, 0.0, 0.0);
	glRotatef(30, 0.0, 1.0, 0.0);
	glRotatef(30, 0.0, 0.0, 1.0);

	// Draw pyramid
	glCallList(PYRAMID);

	glLoadIdentity();
	glTranslatef(2.5, 0.0, 0.0);
	glRotatef(45, 1.0, 0.0, 0.0);
	glRotatef(45, 0.0, 1.0, 0.0);
	glRotatef(45, 0.0, 0.0, 1.0);

	// Draw cube
	glCallList(CUBE);

	// Flush the buffer to force drawing of all objects thus far
	glFlush();
}

void make_pyramid() {

	glNewList(PYRAMID, GL_COMPILE);

		glColor3f(1.0, 0.0, 1.0);
	
		// Draw the sides of the three-sided pyramid
		glBegin(GL_TRIANGLE_FAN);
			glVertex3d(0, 4, 0);
			glVertex3d(0, -4, -4);
			glVertex3d(-4, -4, 4);
			glVertex3d(4, -4, 4);
			glVertex3d(0, -4, -4);
		glEnd();
	
		glColor3f(0.0, 1.0, 1.0);
	
		// Draw the base of the pyramid
		glBegin(GL_TRIANGLES);
			glVertex3d(0, -4, -4);
			glVertex3d(-4, -4, 4);
			glVertex3d(4, -4, 4);
		glEnd();

	glEndList();
}


void make_cube() {

	glNewList(CUBE, GL_COMPILE);

		glColor3f(0.0, 1.0, 0.0);
	
		// Draw the sides of the cube
		glBegin(GL_QUAD_STRIP);
			glVertex3d(3, 3, -3);
			glVertex3d(3, -3, -3);
			glVertex3d(-3, 3, -3);
			glVertex3d(-3, -3, -3);
			glVertex3d(-3, 3, 3);
			glVertex3d(-3, -3, 3);
			glVertex3d(3, 3, 3);
			glVertex3d(3, -3, 3);
			glVertex3d(3, 3, -3);
			glVertex3d(3, -3, -3);
		glEnd();
	
		glColor3f(0.0, 0.0, 1.0);
	
		// Draw the top and bottom of the cube
		glBegin(GL_QUADS);
			glVertex3d(-3, -3, -3);
			glVertex3d(3, -3, -3);
			glVertex3d(3, -3, 3);
			glVertex3d(-3, -3, 3);
	
			glVertex3d(-3, 3, -3);
			glVertex3d(3, 3, -3);
			glVertex3d(3, 3, 3);
			glVertex3d(-3, 3, 3);
		glEnd();

	glEndList();
}

void main(int argc, char **argv) {

	// Set top left corner of window to be at location (0, 0)
	// Set the window size to be 500x500 pixels
	tkInitPosition(0, 0, 500, 500);

	// Initialize the RGB and Depth buffers
	tkInitDisplayMode(TK_RGB | TK_DEPTH);

	// Open a window, name it "Polygons_List"
	if (tkInitWindow("Polygons_List") == GL_FALSE) {
		tkQuit();
	}

	// Set the clear color to black
	glClearColor(0.0, 0.0, 0.0, 0.0);

	// Set the shading model
	glShadeModel(GL_FLAT);

	// Set the polygon mode to fill
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	// Enable depth testing for hidden line removal
	glEnable(GL_DEPTH_TEST);

	// Create the display lists
	make_pyramid();
	make_cube();

	// Assign reshape() to be the function called whenever
	// an expose event occurs
	tkExposeFunc(reshape);

	// Assign reshape() to be the function called whenever 
	// a reshape event occurs
	tkReshapeFunc(reshape);

	// Assign key_down() to be the function called whenever
	// a key is pressed
	tkKeyDownFunc(key_down);

	// Assign draw() to be the function called whenever a display
	// event occurs, generally after a resize or expose event
	tkDisplayFunc(draw);

	// Pass program control to tk's event handling code
	// In other words, loop forever
	tkExec();
}

Docs.mandragor.org - Version 1.0