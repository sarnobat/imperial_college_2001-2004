all: test
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	pdflatex -interaction=nonstopmode test.tex
	xpdf test.pdf
	make clean
	
test: 
	xsltproc xsl.xsl test.xml > test.tex
testti< :
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux 
