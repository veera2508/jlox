# Define variables
SRC_DIR = src
BIN_DIR = bin
PACKAGE = com/jlox/lox
TOOLS_PACKAGE = com/jlox/tool
MAIN_CLASS = com.jlox.lox.Main
GENERATE_AST_CLASS = com.jlox.tool.GenerateAst


SRC_FILES := $(wildcard $(SRC_DIR)/$(PACKAGE)/*.java)
TOOLS_SRC_FILES := $(wildcard $(SRC_DIR)/$(TOOLS_PACKAGE)/*.java)


CLASS_FILES := $(SRC_FILES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)
TOOLS_CLASS_FILES := $(TOOLS_SRC_FILES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)


.PHONY: all
all: $(BIN_DIR) $(CLASS_FILES)


$(BIN_DIR):
	mkdir -p $(BIN_DIR)


$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	javac -d $(BIN_DIR) -sourcepath $(SRC_DIR) $<


tools: $(BIN_DIR) $(TOOLS_CLASS_FILES)

# Run the main class
.PHONY: run
run: all
	java -cp $(BIN_DIR) $(MAIN_CLASS)


.PHONY: generate
generate: tools
	java -cp $(BIN_DIR) $(GENERATE_AST_CLASS) $(ARGS)


.PHONY: clean
clean:
	rm -rf $(BIN_DIR)/$(PACKAGE)/*.class $(BIN_DIR)/$(TOOLS_PACKAGE)/*.class
