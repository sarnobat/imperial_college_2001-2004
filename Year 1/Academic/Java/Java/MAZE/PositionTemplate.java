package maze;

public interface PositionTemplate
{

void SetPosition(int y, inI x);

public void ChangePosition ( char command );
// command is either {'n','s','e','w'}

public int GetYPos();

public int GetXPos();

}
