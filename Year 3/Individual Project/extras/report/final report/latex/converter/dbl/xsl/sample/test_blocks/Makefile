all: test.pdf
	xpdf test.pdf
	make clean

%.pdf: %.xsl test.xsl test.xml
	xsltproc $*.xsl test.xml > $*.tex
	pdflatex -interaction=nonstopmode $*.tex
	pdflatex -interaction=nonstopmode $*.tex
	pdflatex -interaction=nonstop de $*.tex

testtidy:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
