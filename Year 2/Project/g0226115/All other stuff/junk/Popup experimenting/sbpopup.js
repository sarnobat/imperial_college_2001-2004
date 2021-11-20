/*
Javascript popup hint window
Replaces missing "Title" tag rendering in Netscape
Copyright (c) 2000 Soccerbot
www.soccerbot.com
*/

var FontFace =   "\"Arial,Helvetica\"";
var FontSize = "\"-2\"";
var PopupWidth = "150";
var Margin = "1";
var BorderColour = "#333399";
var PopupColour = "#FFFFE0";
var FontColour = "#000000";
var CursorOffsetX = 8;
var CursorOffsetY = 12;
var PageX = 0;
var PageY = 0;
var LeftOffset=0;
var PopupVisible = false;
var ns4,ie4,ie5,ns6 = false;
var sbw;
ns4 = (document.layers)? true:false;
ie4 = (document.all)? true:false;
ns6 = (document.getElementById)? true:false;

if (ie4) {
  if((navigator.userAgent.indexOf('MSIE 5')> 0)||(navigator.userAgent.indexOf('MSIE 6')> 0)){
    ie5 = true;
  }
  if (ns6) ns6 = false;
}

if ( (ns4) || (ie4) || (ns6) ) {
  if (ns4) sbw = document.sbWin;
  if (ns6) sbw = document.getElementById("sbWin");
  if (ie4) sbw = sbWin.style;
  document.onmousemove = mouseMove;
  if (ns4) document.captureEvents(Event.MOUSEMOVE);
}

// Popup to right of cursor
function wr(text,twidth) {
  PopupWidth = twidth;
  LeftOffset = CursorOffsetX;
  popup(text);
}

// Popup to left of cursor
function wl(text,twidth) {
  PopupWidth = twidth;
  LeftOffset = 0-CursorOffsetX-PopupWidth;
  popup(text);
}

// Popup to centre of cursor
function wc(text,twidth) {
  PopupWidth = twidth;
  LeftOffset=CursorOffsetX-(PopupWidth/2)
  popup(text);
}

function popup(text) {	
  var ltext = "<table width="+PopupWidth+" border=0 cellpadding="+Margin+" cellspacing=0 bgcolor=\""+BorderColour+"\">
			<tr><td>
				<table width=100% border=0 cellpadding=2 cellspacing=0 bgcolor=\""+PopupColour+"\">
					<tr><td>
						<font face="+FontFace+" color=\""+FontColour+"\" size=" +FontSize + ">"
							+text+"
						</font>
					</td></tr>
				</table>
			</td></tr>
		</table>"
  LayerWrite(ltext);
  pdisplay();
}

//hide the popup 
function h() {
  if ( (ns4) || (ie4) || (ns6) ) {
    PopupVisible = false;
    ObjectHide(sbw);
  }
}

//display the popup
function pdisplay() {
var cw,ch,thisX,thisY;
  if ((ns4) || (ie4) || (ns6)) {
    if (!PopupVisible) {
      if (ie4) {
        cw=document.body.clientWidth;
	  ch=document.body.clientHeight;
      }
      if (ns4) {
        cw=innerWidth;
        ch=innerHeight;
      }
      if (ns6) {
        cw=outerWidth;
        ch=outerHeight;
      }
      thisX=PageX+LeftOffset;
      thisY=PageY+CursorOffsetY;
      if ((PopupWidth+thisX)>cw) {
        thisX=thisX-((thisX+PopupWidth)-cw);
        if (thisX<0) thisX=1;
      }
      moveTo(sbw,thisX,thisY);
      ObjectShow(sbw);
      PopupVisible = true;
    }
  }
}

function mouseMove(e) {
  if ((ns4) || (ns6)) {PageX=e.pageX; PageY=e.pageY;}
  if (ie4) {PageX=event.x; PageY=event.y;}
  if (ie5) {PageX=event.x+document.body.scrollLeft; PageY=event.y+document.body.scrollTop;}
  if (PopupVisible) {
    //moveTo(sbw,PageX+LeftOffset,PageY+CursorOffsetY);
  }
}

//function LayerWrite(lText) {
//  if ((ns4) || (ns6)) {
//    var lyr = document.sbWin.document;
//    lyr.write(lText);
//    lyr.close();
//  }
//  else if (ie4) document.all["sbWin"].innerHTML = lText;
//}

function LayerWrite(txt){
//thanks Erik Bosrup for help with this code
txt +="\n";
if(ns4){
  var lyr=document.sbWin.document
  lyr.write(txt)
  lyr.close()
} 
else if(ie4){
  document.all["sbWin"].innerHTML=txt
}
else if(ns6){
  range=document.createRange();
  range.setStartBefore(sbWin);
  domfrag=range.createContextualFragment(txt);
  while(sbWin.hasChildNodes()){
    sbWin.removeChild(sbWin.lastChild);
  }
  sbWin.appendChild(domfrag);
}
}


function ObjectShow(obj) {
  if ((ns4) || (ns6)) obj.visibility = "show";
  else if (ie4) obj.visibility = "visible";
}

function ObjectHide(obj) {
  if ((ns4) || (ns6)) obj.visibility = "hide";
  else if (ie4) obj.visibility = "hidden";
}

function moveTo(obj,l,t) {
  obj.left = l;
  obj.top = t;
}


