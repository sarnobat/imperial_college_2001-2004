package parser;

import java.util.TreeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NumericOperandType implements OperandType
{
  TreeMap rangeMap;
  Errors errorHandler;
  public NumericOperandType(Errors e)
  {
    rangeMap = new TreeMap();
    errorHandler = e;
  }

  public boolean isValidOperand(String o,MIPSInstruction m)
  {
    return true;
  }
  public void addValidInstance(Object i)
  {
    Node thisNode = (Node)i;
    NodeList childNodes = thisNode.getChildNodes();
    for (int j = 0; j<childNodes.getLength(); j++)
    {
      if (childNodes.item(j).getNodeName().equals("Identifier"))
      {
        rangeMap.put(childNodes.item(j).getFirstChild().getNodeValue(),thisNode);
      }
    }
  }
}
