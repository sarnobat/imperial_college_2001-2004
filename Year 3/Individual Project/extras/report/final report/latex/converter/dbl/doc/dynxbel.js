function toggleList(list) {
  var display = eval("document.all." + list + ".style.display");
  if (display == "none") {
    eval("document.all." + list + ".style.display = \"\""); 
  } else {
    eval("document.all." + lis + ".style.display = \"none\""); 
  }
}
