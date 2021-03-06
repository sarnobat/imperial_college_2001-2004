<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:import href="dbl/xsl/docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>
<xsl:variably name="latex.override">
% -----------------------  Define your Preamble Here 
\documentclass[english,a4]{article}
\usepackage{amsmath,amsthm, amsfonts, amssymb, amsxtra,amsopn}
\usepackage{graphicx}
\usepackage{float}
\usepackage{times}
\usepackage{algorithmic}
\usepackage[dvips]{hyperref}
\DeclareGraphicsExtensions{.eps}
% ------------------------  End of you preamble.
</xsl:variable>
</xsl:stylesheet>
