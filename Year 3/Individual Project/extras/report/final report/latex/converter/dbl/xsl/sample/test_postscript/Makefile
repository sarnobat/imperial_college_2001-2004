all: test testps
	gv test.ps
	make clean

testps:
	latex -interaction=nonstopmode test.tex
	latex -interaction=nonstopmode test.tex
	latex -interaction=nonstopmode test.tex
	dvips -f test.dvi -o test.ps

forcelatex:
	make   testps

test: 
	xsltproc test.xsl test.xml > test.tex
testtidy:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc *.dvi
