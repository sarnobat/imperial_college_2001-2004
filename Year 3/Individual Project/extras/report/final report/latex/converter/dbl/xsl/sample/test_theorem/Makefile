all: test testpdf
	xpdf test.pdf
	make clean

testpdf:
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex

forcelatex:
	make -k testpdf

test 
	xsltproc test.xsl test.xml > test.tex
testtidy:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
