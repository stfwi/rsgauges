# @file Makefile
# @author Stefan Wilhelm (wile)
# @license MIT
#
# GNU Make makefile for, well, speeding
# up the development a bit.
# You very likely need some tools installed
# to use all build targets, so this file is
# not "official". If you work on windows and
# install GIT with complete shell PATH (the
# red marked option in the GIT installer) you
# should have the needed unix tools available.
# For image stripping install imagemagick and
# also put the "magick" executable in the PATH.
#
MOD_JAR=$(filter-out %-sources.jar,$(wildcard build/libs/*.jar))

ifeq ($(OS),Windows_NT)
GRADLE=gradlew.bat
INSTALL_DIR=$(realpath ${APPDATA}/.minecraft)
SERVER_INSTALL_DIR=$(realpath ${APPDATA}/minecraft-server-forge-1.12.2-14.23.3.2655)
DJS=djs
else
GRADLE=./gradle
INSTALL_DIR=~/.minecraft
DJS=djs
endif

wildcardr=$(foreach d,$(wildcard $1*),$(call wildcardr,$d/,$2) $(filter $(subst *,%,$2),$d))

#
# Targets
#
.PHONY: mod init clean clean-all all install texture-infos start-server

all: clean mod install

mod:
	@echo "Building mod using gradle ..."
	@$(GRADLE) build

clean:
	@echo "Cleaning ..."
	@rm -f build/libs/*

clean-all: clean
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
	@echo "Installing '$(MOD_JAR)' to '$(SERVER_INSTALL_DIR)/mods' ..."
	@[ -d "$(SERVER_INSTALL_DIR)/mods" ] && cp -f "$(MOD_JAR)" "$(SERVER_INSTALL_DIR)/mods/"

start-server: install
	@echo "Starting local dedicated server ..."
	@cd "$(SERVER_INSTALL_DIR)" && java -jar forge-1.12.2-14.23.3.2655-universal.jar &

texture-infos:
	djs scripts/texture-processing.js src/main/resources/assets/rsgauges/textures
