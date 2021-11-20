all: fullblown
	pdflatex -interaction=nonstopmode fullblown.tex
	pdflatex -interaction=nonstopmode fullblown.tex
	pdflatex -interaction=nonstopmode fullblown.tex
	xpdf fullblown.pdf
	make clean

fullblown.pdf: fullblown.tex
	pdflatex -interaction=nonstopmode fullblown.tex && pdflatex -interaction=nonstopmode fullblown.tex && pdflatex -interaction=nonstopmode fullblown.tex

pdf:
	pdflatex -interaction=nonstopmode fullblown.tex
	pdflatex -interaction=nonstopmode fullblown.tex
	pdflatex -interaction=nonstopmode fullblown.tex
	xpdf fullblown.pdf

fullblown: 
	xsltproc xsl.xsl fullblown.xml > fullblown.tex
fullblowntidy:
	xmllint --format fullblown.xml > fullblown.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux *.idx *.glo *.toc
	
