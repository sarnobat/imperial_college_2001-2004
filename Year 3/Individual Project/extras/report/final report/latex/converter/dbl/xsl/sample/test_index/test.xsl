<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: test.xsl,v 1.1 2003/03/16 15:38:19 rcasellas Exp $
 |- #############################################################################
 |	$Author: rcasellas $												
 |														
 + ############################################################################## -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
<xsl:import href="../../docbook.xsl"/>
<xsl:output method="text" encoding="ISO-8859-1" indent="yes"/>


<xsl:template match="mathelement/mathremark">
<xsl:text>\begin{rem}[</xsl:text>
<xsl:value-of select="title"/>
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{rem}&#10;</xsl:text>
</xsl:template>

<xsl:template match="mathelement/mathexample">
<xsl:text>\begin{exm}[</xsl:text>
<xsl:value-of select="title"/>
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{exm}&#10;</xsl:text>
</xsl:template>


<xsl:template match="mathelement/mathproposition">
<xsl:text>\begin{prop}[</xsl:text>
<xsl:value-of select="title"/>
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{prop}&#10;</xsl:text>
</xsl:template>


<!--
##########################################
#
#  \begin{thm}[title]
#
#  \end{thm}
#
##########################################
-->
<xsl:template match="mathelement/maththeorem">
<xsl:text>\begin{thm}[</xsl:text>
<xsl:value-of select="title"/>
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{thm}&#10;</xsl:text>
</xsl:template>



<!--
##########################################
#
#  \begin{defn}[definition title]
#
#  \end{defn}
#
##########################################
-->
<xsl:template match="mathelement/mathdefinition">
<xsl:text>\begin{defn}[</xsl:text>
<xsl:value-of select="title"/>
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{defn}&#10;</xsl:text>
</xsl:template>

<!--
##########################################
#
#  \begin{lem}[lemma title]
#
#  \end{lem}
#
##########################################
-->
<xsl:template match="mathelement/mathlemma">
    <xsl:text>\begin{lem}[</xsl:text>
    <xsl:value-of select="title"/>
<!--
<xsl:call-template name="normalize-scape">
	<xsl:with-param name="string" select="title"/> 
    </xsl:call-template>
    -->
<xsl:text>]&#10;</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{lem}&#10;</xsl:text>
</xsl:template>


<!--
##########################################
#
#  \begin{proof}
#
#  \end{proof}
#
##########################################
-->
<xsl:template match="mathproof">
<xsl:text>\begin{proof}</xsl:text>
<xsl:variable name="id"> <xsl:call-template name="label.id"/> </xsl:variable>
<xsl:apply-templates/>
<xsl:text>\end{proof}&#10;</xsl:text>
</xsl:template>




<xsl:variable name="latex.override">
\documentclass[pdftex,french,english,a4paper,10pt,twoside,openright,]{report}
\label{book}
\usepackage[pdftex]{graphicx}
\pdfcompresslevel=9
\usepackage{anysize}
\marginsize{3cm}{2cm}{1.25cm}{1.25cm}
% ---------------------- 
% Most Common Packages   
% ---------------------- 
\usepackage{makeidx} 
\usepackage{varioref}         
\usepackage{latexsym}         
\usepackage{enumerate}         
\usepackage{fancybox}      
\usepackage{float}       
\usepackage{ragged2e}       
\usepackage[french]{babel} 
\usepackage{isolatin1}         
\usepackage{rotating}         
\usepackage{tabularx}         
\usepackage{url}         
\usepackage{fancyhdr}         
% ---------------
% Document Font  
% ---------------
\usepackage{palatino}
% --------------------------------------------
% Math support                                
% --------------------------------------------
\usepackage{amsmath,amsthm, amsfonts, amssymb, amsxtra,amsopn}
 \theoremstyle{plain}
 \newtheorem{thm}{Thérème}[section]
 \newtheorem{cor}{Corollaire}[section]
 \newtheorem{lem}{Lemme}[section]
 \newtheorem{prop}{Proposition}[section]
 \newtheorem{ax}{Axiome}[section]
 \theoremstyle{remark}
 \newtheorem{rem}{Remarque}[section]
 \newtheorem{exm}{Exemple}[section]
 \newtheorem{notation}{Notation}[section]
 \theoremstyle{definition}
 \newtheorem{defn}{Définition}[section]

 \newcommand{\ntt}{\normalfont\ttfamily}
 \newcommand{\thmref}[1]{Théoème~\ref{#1}}
 \newcommand{\secref}[1]{\S\ref{#1}}
 \newcommand{\lemref}[1]{Lemme~\ref{#1}}
 \newcommand{\bysame}{\mbox{\rule{3em}{.4pt}}\,}
 \newcommand{\A}{\mathcal{A}}
 \newcommand{\B}{\mathcal{B}}
 \newcommand{\XcY}{{(X,Y)}}
 \newcommand{\SX}{{S_X}}
 \newcommand{\SY}{{S_Y}}
 \newcommand{\SXY}{{S_{X,Y}}}
 \newcommand{\SXgYy}{{S_{X|Y}(y)}}
 \newcommand{\Cw}[1]{{\hat C_#1(X|Y)}}
 \newcommand{\G}{{G(X|Y)}}
 \newcommand{\PY}{{P_{\mathcal{Y}}}}
 \newcommand{\X}{\mathcal{X}}
 \newcommand{\wt}{\widetilde}
 \newcommand{\wh}{\widehat}
 \DeclareMathOperator{\per}{per}
 \DeclareMathOperator{\cov}{cov}
 \DeclareMathOperator{\cf}{cf}
 \DeclareMathOperator{\add}{add}
 \DeclareMathOperator{\Cham}{Cham}
 \DeclareMathOperator{\IM}{Im}
 \DeclareMathOperator{\esssup}{ess\,sup}
 \DeclareMathOperator{\essinf}{ess\,inf}
 \DeclareMathOperator{\meas}{meas}
 \DeclareMathOperator{\seg}{seg}
 \DeclareMathOperator{\avg}{avg}
 \DeclareMathOperator{\non}{non}

\newcommand{\href}[1]{{}}
\newcommand{\hyperlink}[1]{{}}
\newcommand{\hypertarget}[2]{#2}
% --------------------------------------------
% Commands to manage/style/create floats      
% figures, tables, algorithms, examples, eqn  
% --------------------------------------------
 \floatstyle{ruled}
 \restylefloat{figure}
 \floatstyle{ruled}
 \restylefloat{table}
 \floatstyle{ruled}
 \newfloat{program}{ht}{lop}[section]
 \floatstyle{ruled}
 \newfloat{example}{ht}{loe}[section]
 \floatname{example}{Example}
 \floatstyle{ruled}
 \newfloat{dbequation}{ht}{loe}[section]
 \floatname{dbequation}{Equation}
 \floatstyle{boxed}
 \newfloat{algorithm}{ht}{loa}[section]
 \floatname{algorithm}{Algorithm}
\DeclareGraphicsExtensions{.pdf,.png,.jpg}
\newcommand{\docbooktolatexalignrl}{\protect\ifvmode\raggedleft\else\hfill\fi}
\newcommand{\docbooktolatexalignrr}{\protect}
\newcommand{\docbooktolatexalignll}{\protect\ifvmode\raggedright\else\fi}
\newcommand{\docbooktolatexalignlr}{\protect\ifvmode\else\hspace*\fill\fi}
\newcommand{\docbooktolatexaligncl}{\protect\ifvmode\centering\else\hfill\fi}
\newcommand{\docbooktolatexaligncr}{\protect\ifvmode\else\hspace*\fill\fi}
</xsl:variable>
</xsl:stylesheet>
