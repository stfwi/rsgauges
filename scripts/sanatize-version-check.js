#!/usr/bin/djs

if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir("src"))) throw new Error("Failed to switch to mod base directory.");

var modrsgauges_java = fs.readfile("src/main/java/wile/rsgauges/ModRsGauges.java", function(line){
  return line.search(/String[\s]+MODVERSION[\s]*=/i) >= 0;
}).replace(/^.*?"/,"").replace(/".*/,"").trim();

var build_gradle = fs.readfile("build.gradle", function(line){
  return line.search(/^version[\s]*=[\s]*"/i) >= 0;
}).replace(/^.*?"/,"").replace(/".*/,"").trim();

var mcmod_info = JSON.parse(fs.readfile("src/main/resources/mcmod.info"));
mcmod_info = "mc" + (mcmod_info[0].mcversion.trim()) + "-" + (mcmod_info[0].version.trim());

if((modrsgauges_java != mcmod_info) || (build_gradle != mcmod_info)) {
  alert("Version data differ:");
  alert(" - mcmod.info      : '" + mcmod_info + "'");
  alert(" - build.gradle    : '" + build_gradle + "'");
  alert(" - ModRsGauges.java: '" + modrsgauges_java + "'");
  exit(1);
}
