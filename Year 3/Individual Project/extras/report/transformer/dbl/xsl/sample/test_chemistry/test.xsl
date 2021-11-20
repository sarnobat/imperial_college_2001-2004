<?xml version='1.0'?>
<!--############################################################################# 
 |	$Id: test.xsl,v 1.2 2004/01/13 08:33:43 j-devenish Exp $
 |- #############################################################################
 |	$Author: j-devenish $												
 + ############################################################################## -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

	<xsl:import href="../docbook.xsl"/>

	<xsl:param name="latex.document.font">default</xsl:param>
	<xsl:param name="latex.use.parskip">1</xsl:param>

	<!-- we will supply our own header style -->
	<xsl:param name="latex.use.fancyhdr">0</xsl:param>
	<xsl:template name="generate.latex.pagestyle">
		<!-- 'custom' is defined in this XSL file -->
		<xsl:text>\pagestyle{custom}</xsl:text>
	</xsl:template>

	<!-- override DB2LaTeX's mapping of certain DocBook elements -->
	<xsl:variable name="latex.mapping.xml" select="document('test.map')"/>

	<!-- note: 'gold' in the line below is defined in latex.book.preamble.pre -->
	<xsl:param name="my.hyperref.param.pdftex.extra">,pdftitle={test-chemistry},linkcolor=gold,anchorcolor=blue,pagecolor=blue</xsl:param>

	<!-- typeset remarks as margin notes -->
	<xsl:template match="remark">
		<xsl:text>\marginnote{</xsl:text>
		<xsl:apply-templates/>
		<xsl:text>}&#10;</xsl:text>
	</xsl:template>

	<!-- no delimiter between a formalpara's title and its contents -->
	<xsl:template name="generate.formalpara.title.delimiter"/>

	<!-- use the 'custom' mode with the <procedure> template -->
	<xsl:template match="procedure">
		<xsl:call-template name="procedure">
			<xsl:with-param name="mode">custom</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- make a LaTeX frame around any <highlights> -->
	<xsl:template match="highlights">
		<xsl:text>
			<![CDATA[
			\par\noindent
			\begin{lrbox}{\frameboxbox}\begin{minipage}{\linewidth}
			]]>
		</xsl:text>
		<xsl:apply-templates/>
		<xsl:text>
			<![CDATA[
			\end{minipage}\end{lrbox}\noindent\framebox{\usebox{\frameboxbox}}
			\par
			]]>
		</xsl:text>
	</xsl:template>

	<!-- use a double line under <thead> and a single line under <row> -->
	<xsl:template name="table.row.separator">
		<xsl:choose>
			<xsl:when test="local-name(..)='thead'">
				<xsl:text> \hline\hline&#10;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> \hline&#10;</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- use our shaded 'cminipage' environment for <examples> and <sidebars> -->
	<xsl:template match="example|sidebar">
		<xsl:text>&#10;\vspace{0.5em}\par\noindent\begin{cminipage}</xsl:text>
		<xsl:text>{\sf\textbf{</xsl:text>
		<xsl:if test="local-name(.)='example'">
			<xsl:text>Example:&#10;</xsl:text>
		</xsl:if>
		<xsl:apply-templates select="title"/>
		<xsl:text>}}\\*</xsl:text>
		<xsl:call-template name="content-templates"/>
		<xsl:text>\end{cminipage}\vspace{0.5em}\par&#10;</xsl:text>
		<xsl:if test="$latex.use.noindent=1">
			<xsl:text>\noindent </xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- could really use a style file for this kind of stuff! -->
	<xsl:variable name="latex.book.preamble.pre">
		\makeatletter

		% define a gold colour for hyperlinks (see above)
		\usepackage{color}
		\definecolor{gold}{rgb}{0.75,0.5,0}
		\definecolor{lightgold}{rgb}{1,.9,.4}

		% Give headers a touch of class
		\newlength{\headerwidth}
		\setlength{\headerwidth}{\textwidth}
		\addtolength{\headerwidth}{\marginparwidth}
		\addtolength{\headerwidth}{2pt}
		\def\ps@custom{
			\ps@headings
			\if@twoside
			  \renewcommand{\@evenhead}{%
				\hspace{\textwidth}%
				\hspace{-\headerwidth}%
				\color{gold}
				\rule[-4pt]{\headerwidth}{0.5pt}%
				\hspace{-\headerwidth}%
				\rule[-6pt]{\headerwidth}{0.5pt}%
				\color{black}
				\hspace{-\headerwidth}%
				\parbox{\headerwidth}{\upshape\bfseries\thepage\hfill\slshape\mdseries\leftmark}%
				}
			  \renewcommand{\@oddhead}{%
				\color{gold}
				\rule[-4pt]{\headerwidth}{0.5pt}%
				\hspace{-\headerwidth}%
				\rule[-6pt]{\headerwidth}{0.5pt}%
				\color{black}
				\hspace{-\headerwidth}%
				\parbox{\headerwidth}{\slshape\rightmark\hfill\upshape\bfseries\thepage\mdseries}
				\hspace{-\textwidth}% arbitrary
				\hfil %to prevent 'overfull hbox' errors
				}
			\fi
		}

		% custom page margins
		\usepackage{anysize}
		\setlength{\headsep}{3mm}
		\addtolength{\marginparsep}{0.5cm}
		\setlength{\marginparwidth}{4.5cm}
		\newlength{\marginparinnerwidth}
		\setlength{\marginparinnerwidth}{\marginparwidth}
		\addtolength{\marginparinnerwidth}{-0.5cm}
		\marginsize{1.5cm}{6cm}{1cm}{1.5cm}

		% custom chapter headings
		\def\@makeschapterhead#1{%
		  \vspace*{50\p@}%
		  {\parindent \z@ \raggedright \normalfont
			\Huge \sffamily \bfseries #1\par\nobreak
			\interlinepenalty\@M
			\textcolor{lightgold}{\rule{5cm}{2pt}}
			\par\nobreak
			\vskip 40\p@
		  }}
		\def\@makechapterhead#1{%
		  \vspace*{50\p@}%
		  {\parindent \z@ \raggedright \normalfont
			\Huge \sffamily \bfseries #1\par\nobreak
			\interlinepenalty\@M
			\ifnum \c@secnumdepth >\m@ne
				\colorbox{lightgold}{\parbox{5cm}{\vspace{2pt}\small\sffamily\bfseries \@chapapp\space \thechapter}}
				\par\nobreak
			\fi
			\vskip 40\p@
		}}

		% custom section headings
		\renewcommand\section{\@startsection {section}{1}{\z@}%
			{-3.5ex \@plus -1ex \@minus -.2ex}%
			{2.3ex \@plus.2ex}%
			{%
				\color{gold}
				\rule[-0.3cm]{\textwidth}{1pt}
				\hspace{-\textwidth}
				\hspace{\motifadjustment}
				\rule[-0.3cm]{1pt}{0.85cm}
				\color{black}
				\hspace{0.2ex}
				\normalfont\Large\bfseries}
			}

		\makeatother
	</xsl:variable>

	<!-- the following variable is supplanted by the preceding variable -->
	<xsl:variable name="latex.book.varsets"/>

	<xsl:variable name="latex.book.preamble.post">
		\makeatletter

		% provide 'cminipage' background-shaded environment
		\def\motifadjustment{-1em}
		% Grey backgrounds for some verbatim text.
		\IfFileExists{framed.sty}{
			\newenvironment{cminipage}{\begin{shaded}}{\end{shaded}}
		  \usepackage{color,framed}
		  \definecolor{shadecolor}{gray}{0.8}
		}{
			\newlength{\cminiwidth}
			\setlength{\cminiwidth}{\textwidth}
			\addtolength{\cminiwidth}{-2\marginparsep}
			\newenvironment{cminipage}{\begin{lrbox}{\@tempboxa}\begin{minipage}{\cminiwidth}}{\end{minipage}\end{lrbox}\hspace{\marginparsep}\colorbox[gray]{0.8}{\usebox{\@tempboxa}}}
		}

		% grey background for margin notes
		\newcommand{\marginnote}[2]{%
		\marginpar{%
		\colorbox{lightgold}{\begin{tabular}{l}\hline%
		\begin{parbox}{\marginparinnerwidth}{#1}\end{parbox}%
		\\ \hline%
		\end{tabular}}%
		}%
		}

		% avoid using ampersand for LaTeX alignments -- use pipe instead
		\catcode`\|=4

		% mathematics support
		\newenvironment{rcases}{%
			\let\@ifnextchar\new@ifnextchar%
			\left.%
			\def\arraystretch{1.2}%
			\array{@{}l@{\quad}l@{}}%
		}{%
			\endarray\right\}%
		}
		\newcommand{\degr}{\:\!^{\circ}\text{C}}
		\renewcommand{\~}{\;\;\;}

		% for our "highlights" template
		\newbox\frameboxbox

		\makeatother
	</xsl:variable>

</xsl:stylesheet>
