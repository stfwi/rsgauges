# @file Makefile
# @author Stefan Wilhelm (wile)
# @license MIT
#
# GNU Make makefile for, well, speeding
# up the development a bit.
# You very likely need some tools installed 
# to use all build targets, so this file is  
# not "official".
#
.PHONY: mod init clean all install
MOD_JAR=$(filter-out %-sources.jar,$(wildcard build/libs/*.jar))

ifeq ($(OS),Windows_NT)
GRADLE=gradlew.bat
INSTALL_DIR=$(realpath ${APPDATA}/.minecraft)
else
GRADLE=./gradle
INSTALL_DIR=~/.minecraft
endif

all: mod

mod:
	@echo "Building mod using gradle ..."
	@$(GRADLE) build
  
clean:  
	@echo "Cleaning using gradle ..."
	@$(GRADLE) clean

init:
	@echo "Initialising eclipse workspace using gradle ..."
	@$(GRADLE) setupDecompWorkspace
	@$(GRADLE) eclipse

install: $(MOD_JAR)
	@if [ ! -d "$(INSTALL_DIR)" ]; then echo "Cannot find installation minecraft directory."; false; fi
	@echo "Installing '$(MOD_JAR)' to '$(INSTALL_DIR)/mods' ..."
	@[ -d "$(INSTALL_DIR)/mods" ] || mkdir "$(INSTALL_DIR)/mods"
	@cp -f "$(MOD_JAR)" "$(INSTALL_DIR)/mods/"

