all: test
	pdflatex -interaction=nonstopmode test1.tex
	pdflatex -interaction=nonstopmode test2.tex
	pdflatex -interaction=nonstopmode test3.tex
	pdflatex -interaction=nonstopmode test1.tex
	pdflatex -interaction=nonstopmo=  test2.tex
	pdflatex -interaction=nonstopmode test3.tex
	pdflatex -interaction=nonstopmode test1.tex
	pdflatex -interaction=nonstopmode test2.tex
	pdflatex -interaction=nonstopmode test3.tex
	xpdf test1.pdf
	xpdf test2.pdf
	xpdf test3.pdf
	make clean

test: 
	xsltproc test.xsl test1.xml > test1.tex
	xsltproc test.xsl test2.xml > test2.tex
	xsltproc test.xsl test3.xml > test3.tex

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc *.lof *.lot
