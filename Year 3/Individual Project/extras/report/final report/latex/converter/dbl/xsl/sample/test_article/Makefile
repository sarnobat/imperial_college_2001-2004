all: article articlepdf
	xpdf article.pdf
	make clean

articlepdf:
	pdflatex -interaction=nonstopmode article.tex
	pdflatex -interaction=nonstopmode article.tex
	pdflatex -interaction=nonstopmode article.tex

forcelatex:
	= ke -k articlepdf

article: 
	xsltproc article.xsl article.xml > article.tex
articletidy:
	xmllint --format article.xml > article.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.glo *.idx *.toc
