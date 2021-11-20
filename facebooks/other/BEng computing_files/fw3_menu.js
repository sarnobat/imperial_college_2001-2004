<!--
function mmLoadMenus() {

    // This file has been coded by hand.
    // It should NOT be edited using Dreamweavers drop down
    // menu editor.

    // Just copy and paste an existing  addMenuItem line if you wish
    // to add a new menu item.
    
    // All links must be absolute, and include the domain name
    // e.g. http://www.doc.ic.ac.uk/about/
    // This is because these javascript menus are used on the
    // google search page, and therefore must specifically 
    // state the domain name.

    if (window.MainMenu_0) return;

    // About
    window.MainMenu_0 = new Menu("Menu0",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,false,false);
    MainMenu_0.addMenuItem("Overview","location='http://www.doc.ic.ac.uk/about/'");
    MainMenu_0.addMenuItem("News","location='http://www.doc.ic.ac.uk/about/news/'");
    MainMenu_0.addMenuItem("Situations&nbsp;Vacant","location='http://www.doc.ic.ac.uk/about/situationsvacant/'");
    MainMenu_0.addMenuItem("Calendar","location='http://www.doc.ic.ac.uk/about/calendar/'");
    MainMenu_0.addMenuItem("World&nbsp;of&nbsp;Computing","location='http://www.doc.ic.ac.uk/about/worldofcomputing/'");
    MainMenu_0.addMenuItem("Map&nbsp;&amp;&nbsp;Travel&nbsp;Information","location='http://www.doc.ic.ac.uk/about/mapandtravel/'");

    MainMenu_0.hideOnMouseOut=true;
    MainMenu_0.childMenuIcon="http://www.doc.ic.ac.uk/images/arrows.gif";
    MainMenu_0.bgColor='#555555';
    MainMenu_0.menuBorder=1;
    MainMenu_0.menuLiteBgColor='#ffffff';
    MainMenu_0.menuBorderBgColor='#777777';


    // Teaching
    window.MainMenu_1 = new Menu("Menu1",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,false,false);
    MainMenu_1.addMenuItem("Overview","location='http://www.doc.ic.ac.uk/teaching/'");
    MainMenu_1.addMenuItem("Undergraduate&nbsp;Degrees","location='http://www.doc.ic.ac.uk/teaching/undergraduate/'");
    MainMenu_1.addMenuItem("&nbsp;Computing","location='http://www.doc.ic.ac.uk/teaching/undergraduate/computing/'");
    MainMenu_1.addMenuItem("&nbsp;Joint&nbsp;Maths&nbsp;and&nbsp;Computing","location='http://www.doc.ic.ac.uk/teaching/undergraduate/jmc/'");
    MainMenu_1.addMenuItem("Postgraduate&nbsp;Degrees","location='http://www.doc.ic.ac.uk/teaching/postgraduate/'");
    MainMenu_1.addMenuItem("&nbsp;MSc&nbsp;Conversion","location='http://www.doc.ic.ac.uk/teaching/postgraduate/conversion/'");
    MainMenu_1.addMenuItem("&nbsp;MSc&nbsp;Advanced&nbsp;Computing","location='http://www.doc.ic.ac.uk/teaching/postgraduate/mac/'");
    MainMenu_1.addMenuItem("&nbsp;MSc&nbsp;Computing&nbsp;for&nbsp;Industry","location='http://www.doc.ic.ac.uk/teaching/postgraduate/ind/'");
    
    MainMenu_1.hideOnMouseOut=true;
    MainMenu_1.childMenuIcon="http://www.doc.ic.ac.uk/images/arrows.gif";
    MainMenu_1.bgColor='#555555';
    MainMenu_1.menuBorder=1;
    MainMenu_1.menuLiteBgColor='#ffffff';
    MainMenu_1.menuBorderBgColor='#777777';


    // Research
    window.MainMenu_2 = new Menu("Menu2",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,false,false);
    MainMenu_2.addMenuItem("Overview","location='http://www.doc.ic.ac.uk/research/'");
    // Need spaces around the '/' so that Mac IE5 does not break the menu text onto two lines
    MainMenu_2.addMenuItem("PhD&nbsp;/&nbsp;MPhil","location='http://www.doc.ic.ac.uk/research/phd/degrees.html'");
    MainMenu_2.addMenuItem("Research&nbsp;Areas","location='http://www.doc.ic.ac.uk/research/areas/'");
    MainMenu_2.addMenuItem("Seminars","location='http://www.doc.ic.ac.uk/research/seminars/'");
    MainMenu_2.addMenuItem("Technical&nbsp;Reports","location='http://www.doc.ic.ac.uk/research/technicalreports/'");
    
    MainMenu_2.hideOnMouseOut=true;
    MainMenu_2.childMenuIcon="http://www.doc.ic.ac.uk/images/arrows.gif";
    MainMenu_2.bgColor='#555555';
    MainMenu_2.menuBorder=1;
    MainMenu_2.menuLiteBgColor='#ffffff';
    MainMenu_2.menuBorderBgColor='#777777';


    // People
    window.MainMenu_3 = new Menu("Menu3",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,false,false);
    MainMenu_3.addMenuItem("Overview","location='http://www.doc.ic.ac.uk/people/'");
    MainMenu_3.addMenuItem("People&nbsp;Search","location='http://www.doc.ic.ac.uk/people/search/'");
    MainMenu_3.addMenuItem("Staff&nbsp;Photos","location='http://www.doc.ic.ac.uk/people/staffphotos/'");
    MainMenu_3.addMenuItem("Staff&nbsp;and&nbsp;Research&nbsp;Student&nbsp;List","location='http://www.doc.ic.ac.uk/people/stafflist/'");
    
    MainMenu_3.hideOnMouseOut=true;
    MainMenu_3.childMenuIcon="http://www.doc.ic.ac.uk/images/arrows.gif";
    MainMenu_3.bgColor='#555555';
    MainMenu_3.menuBorder=1;
    MainMenu_3.menuLiteBgColor='#ffffff';
    MainMenu_3.menuBorderBgColor='#777777';


    // About this site
    window.MainMenu_4 = new Menu("Menu4",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,false,false);
    MainMenu_4.addMenuItem("About&nbsp;this&nbsp;Site","location='http://www.doc.ic.ac.uk/site/'");
    MainMenu_4.addMenuItem("Search","location='http://www.doc.ic.ac.uk/site/search/'");
    MainMenu_4.addMenuItem("Site&nbsp;Index","location='http://www.doc.ic.ac.uk/site/index/'");
    MainMenu_4.addMenuItem("Feedback","location='http://www.doc.ic.ac.uk/site/feedback/'");
    
    MainMenu_4.hideOnMouseOut=true;
    MainMenu_4.childMenuIcon="/images/arrows.gif";
    MainMenu_4.bgColor='#555555';
    MainMenu_4.menuBorder=1;
    MainMenu_4.menuLiteBgColor='#ffffff';
    MainMenu_4.menuBorderBgColor='#777777';


    // Internal Links
    window.MainMenu_5 = new Menu("Menu5",200,17,"Verdana, Arial, Helvetica, sans-serif",10,"#333333","#ffffff","#ffffff","#CC3300","left","middle",3,0,1000,-5,7,true,true,true,0,true,true);
    MainMenu_5.addMenuItem("Internal&nbsp;Links","location='http://www.doc.ic.ac.uk/internal/'");
    MainMenu_5.addMenuItem("Personal&nbsp;Homepage","location='https://www.doc.ic.ac.uk/internal/personal/'");
    MainMenu_5.addMenuItem("Old&nbsp;Homepage","location='http://www.doc.ic.ac.uk/old/'");
    
    MainMenu_5.hideOnMouseOut=true;
    MainMenu_5.childMenuIcon="http://www.doc.ic.ac.uk/images/arrows.gif";
    MainMenu_5.bgColor='#555555';
    MainMenu_5.menuBorder=1;
    MainMenu_5.menuLiteBgColor='#FFFFFF';
    MainMenu_5.menuBorderBgColor='#777777';
    
    
    
    MainMenu_0.writeMenus();


} // mmLoadMenus()

//-->
