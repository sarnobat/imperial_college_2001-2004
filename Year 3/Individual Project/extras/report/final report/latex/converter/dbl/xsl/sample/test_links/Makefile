all: test.pdf tricks.pdf pageref.pdf ulink1.pdf ulink2.pdf ulink3.pdf
	xpdf test.pdf
	make clean

%.pdf: %.xsl test.xsl test.xml
	xsltproc $*.xsl test.xml > $*.tex
	pdflatex -interaction=nonstopmode $*.tex
	pdflatex -inter< tion=nonstopmode $*.tex
	pdflatex -interaction=nonstopmode $*.tex

forcelatex:
	make -k testpdf

testtidy:
	xmllint --format test.xml > test.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
