all: minimal
	pdflatex -interaction=nonstopmode minimal.tex
	pdflatex -interaction=nonstopmode minimal.tex
	pdflatex -interaction=nonstopmode minimal.tex
	xpdf minimal.pdf
	make clean
	
minimal: 
	xsltproc xsl.xsl minimal.< l > minimal.tex
minimaltidy:
	xmllint --format minimal.xml > minimal.new.xml

clean:
	rm -rf  *.log *.out *.brf *.aux 
