#include <curses.h>
#include "curspriv.h"

/****************************************************************/
/* Wclear() fills all lines of window 'win' with blanks, and	*/
/* Marks the window to be cleared at next refres operation.	*/
/****************************************************************/

void wclear(win)
WINDOW *win;
{
  werase(win);
  win->_clear = TRUE;
}				/* wclear */
