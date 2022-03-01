JFLAGS = -g -cp . -d .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/main/java/constants/GrammarConstants.java \
	src/main/java/constants/ParserConstants.java \
	src/main/java/constants/ScannarConstants.java \
	src/main/java/exceptions/WinzigIOException.java \
	src/main/java/exceptions/WinzigParserException.java \
	src/main/java/exceptions/WinzigScannarException.java \
	src/main/java/helper/FileHelper.java \
	src/main/java/analyzer/GrammarNode.java \
	src/main/java/analyzer/GrammarRule.java \
	src/main/java/analyzer/GrammarReader.java \
	src/main/java/analyzer/Resolver.java \
	src/main/java/analyzer/Selector.java \
	src/main/java/scannar/Token.java \
	src/main/java/parser/TreeNode.java \
	src/main/java/parser/TreeStack.java \
	src/main/java/executor/TreeGenerator.java \
	src/main/java/scannar/Scanner.java \
	src/main/java/scannar/Screener.java \
	src/main/java/scannar/LexicalAnalyzer.java \
	src/main/java/parser/Parser.java \
	src/main/java/winzigc.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	find . -name "*.class" -type f -delete