# Developer note

## Development tools

Markdown plugin is developed with Gradle build tool and IntelliJ IDEA. 
It depends on  Java8.


## Library

This project use [PegDown](https://github.com/sirthias/pegdown) Markdown parser.


## Hacking plugin

### Abstract Syntax Tree(AST)

PegDown genearete AST when parsing Markdown file.
You can observe it with break point at

OmegatMarkdownFilter.java:236 in main source or
MarkdownSerializerTest.java:70 in test
and watch `RootNode astRoot`


### Source text

Source text is stored in `char[] articleBuf`. Each node of AST has a index in articleBuf.
Indexes can be retrieved using `getStartIndex()` and `getEndIndex()` for node.
