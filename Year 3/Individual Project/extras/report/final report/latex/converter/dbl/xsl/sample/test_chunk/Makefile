all: test
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	xpdf test.pdf
	make clean
	
test: 
	xsltproc test.xsl test.xml > test.tex
testt> y:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
