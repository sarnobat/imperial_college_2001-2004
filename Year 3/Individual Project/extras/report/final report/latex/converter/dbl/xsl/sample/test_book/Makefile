all: test
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	xpdf test.pdf
	make clean
pdf:	
	pdflatex -interaction=nonstopmode test.tex
	pd> atex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
test: 
	xsltproc test.xsl test.xml > test.tex
testtidy:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
