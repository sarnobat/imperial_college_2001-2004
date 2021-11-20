// OpenGL Tutorial
// Hello_World.c

/*************************************************************************
This program essentially opens a window, clears it, sets the drawing
color, draws the object, and flushes the drawing buffer.  It then goes
into an infinite loop accepting events and calling the appropriate
functions.
*************************************************************************/

// gcc -o Hello_World  Hello_World.c -lX11 -lMesaGL -lMesaGLU -lMesatk -lm

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <gltk.h>

void expose(int width, int height) {

	// Clear the window
	glClear(GL_COLOR_BUFFER_BIT);
}

void reshape(int width, int height) {

	// Set the new viewport size
	glViewport(0, 0, (GLint)width, (GLint)height);

	// Clear the window
	glClear(GL_COLOR_BUFFER_BIT);
}

void draw(void) {

	// Set the drawing color
	glColor3f(1.0, 1.0, 1.0);

	// Specify which primitive type is to be drawn
	glBegin(GL_POLYGON);
		// Specify verticies in quad
		glVertex2f(-0.5, -0.5);
		glVertex2f(-0.5, 0.5);
		glVertex2f(0.5, 0.5);
		glVertex2f(0.5, -0.5);
	glEnd();

	// Flush the buffer to force drawing of all objects thus far
	glFlush();
}

void main(int argc, char **argv) {

	// Open a window, name it "Hello World"
	if (tkInitWindow("Hello World") == GL_FALSE) {
		tkQuit();
	}

	// Set the clear color to black
	glClearColor(0.0, 0.0, 0.0, 0.0);

	// Assign expose() to be the function called whenever
	// an expose event occurs
	tkExposeFunc(expose);

	// Assign reshape() to be the function called whenever 
	// a reshape event occurs
	tkReshapeFunc(reshape);

	// Assign draw() to be the function called whenever a display
	// event occurs, generally after a resize or expose event
	tkDisplayFunc(draw);

	// Pass program control to tk's event handling code
	// In other words, loop forever
	tkExec();
}
